/**
 * K6 → Allure converter
 *
 * This script processes ALL JSON files inside performance-tests/results/
 * and generates:
 *  - One Allure test result per K6 test file
 *  - A response time histogram (PNG)
 *  - A response time trend chart (PNG)
 *
 * Output is written to: allure-results/
 *
 * This script is designed to run both locally and inside GitHub Actions.
 */

import fs from 'fs';
import path from 'path';
import { ChartJSNodeCanvas } from 'chartjs-node-canvas';

// -----------------------------
// CONFIGURATION
// -----------------------------
const resultsDir = 'performance-tests/results';
const allureDir = 'allure-results';

// Create allure-results directory if missing
if (!fs.existsSync(allureDir)) {
    fs.mkdirSync(allureDir);
    console.log("Created allure-results/ directory");
}

// -----------------------------
// READ ALL JSON FILES
// -----------------------------
const files = fs.readdirSync(resultsDir).filter(f => f.endsWith('.json'));

if (files.length === 0) {
    console.error("❌ No JSON files found in performance-tests/results/. Nothing to convert.");
    process.exit(1);
}

console.log(`Found ${files.length} K6 result files:`)
files.forEach(f => console.log(" - " + f));

// Chart generator
const chart = new ChartJSNodeCanvas({ width: 800, height: 400 });

// -----------------------------
// PROCESS EACH TEST FILE
// -----------------------------
for (const file of files) {
    const scriptName = file.replace('.json', '');
    const fullPath = path.join(resultsDir, file);

    console.log(`\nProcessing: ${file}`);

    // Read NDJSON (one JSON object per line)
    const lines = fs.readFileSync(fullPath, 'utf8')
        .trim()
        .split('\n')
        .map(line => JSON.parse(line));

    const durations = [];
    const timestamps = [];
    let totalRequests = 0;
    let failedRequests = 0;

    // Extract metrics from K6 output
    for (const entry of lines) {
        if (entry.metric === "http_req_duration" && entry.type === "Point") {
            durations.push(entry.data.value);
            timestamps.push(new Date(entry.data.time));
        }

        if (entry.metric === "http_reqs" && entry.type === "Point") {
            totalRequests += entry.data.value;
        }

        if (entry.metric === "http_req_failed" && entry.type === "Point") {
            failedRequests += entry.data.value;
        }
    }

    // Basic statistics
    const avg = durations.reduce((a, b) => a + b, 0) / durations.length;
    const sorted = durations.slice().sort((a, b) => a - b);
    const p95 = sorted[Math.floor(sorted.length * 0.95)] || 0;

    // -----------------------------
    // GENERATE HISTOGRAM CHART
    // -----------------------------
    const histogramImage = await chart.renderToBuffer({
        type: 'line',
        data: {
            labels: sorted.map((_, i) => i),
            datasets: [{
                label: 'Response time (ms)',
                data: sorted,
                borderColor: 'blue',
                backgroundColor: 'lightblue'
            }]
        }
    });

    const histogramFile = `${scriptName}-histogram.png`;
    fs.writeFileSync(`${allureDir}/${histogramFile}`, histogramImage);

    // -----------------------------
    // GENERATE TREND CHART
    // -----------------------------
    const trendImage = await chart.renderToBuffer({
        type: 'line',
        data: {
            labels: timestamps.map(t => t.toISOString()),
            datasets: [{
                label: 'Response time over time',
                data: durations,
                borderColor: 'green',
                backgroundColor: 'lightgreen'
            }]
        }
    });

    const trendFile = `${scriptName}-trend.png`;
    fs.writeFileSync(`${allureDir}/${trendFile}`, trendImage);

    // -----------------------------
    // CREATE ALLURE TEST RESULT
    // -----------------------------
    const allureResult = {
        name: `${scriptName} performance test`,
        status: failedRequests > 0 ? "failed" : "passed",
        steps: [
            { name: `Total requests: ${totalRequests}`, status: "passed" },
            { name: `Failed requests: ${failedRequests}`, status: failedRequests > 0 ? "failed" : "passed" },
            { name: `Average duration: ${avg.toFixed(2)} ms`, status: "passed" },
            { name: `p95 duration: ${p95.toFixed(2)} ms`, status: "passed" }
        ],
        attachments: [
            {
                name: "Response time histogram",
                type: "image/png",
                source: histogramFile
            },
            {
                name: "Response time trend",
                type: "image/png",
                source: trendFile
            }
        ],
        parameters: []
    };

    const resultFile = `${scriptName}-result.json`;
    fs.writeFileSync(`${allureDir}/${resultFile}`, JSON.stringify(allureResult, null, 2));

    console.log(`✔ Created Allure test: ${resultFile}`);
}

console.log("\n🎉 All K6 tests successfully converted to Allure format!");
