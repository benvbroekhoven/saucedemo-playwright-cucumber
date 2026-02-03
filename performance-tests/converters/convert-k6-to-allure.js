import fs from 'fs';
import { ChartJSNodeCanvas } from 'chartjs-node-canvas';

// 1. NDJSON inlezen
const lines = fs.readFileSync('performance-tests/results/results.json', 'utf8')
    .trim()
    .split('\n')
    .map(line => JSON.parse(line));

// 2. Metrics verzamelen
const durations = [];
const timestamps = [];
let totalRequests = 0;
let failedRequests = 0;

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

// 3. Statistieken berekenen
const avg = durations.reduce((a, b) => a + b, 0) / durations.length;
const sorted = durations.slice().sort((a, b) => a - b);
const p95 = sorted[Math.floor(sorted.length * 0.95)] || 0;

// 4. Allure directory
const allureDir = 'allure-results';
if (!fs.existsSync(allureDir)) {
    fs.mkdirSync(allureDir);
}

// 5. Grafieken genereren
const width = 800;
const height = 400;
const chartJSNodeCanvas = new ChartJSNodeCanvas({ width, height });

// Histogram
const histogramData = {
    labels: sorted.map((_, i) => i),
    datasets: [{
        label: 'Response time (ms)',
        data: sorted,
        borderColor: 'blue',
        backgroundColor: 'lightblue',
    }]
};

const histogramImage = await chartJSNodeCanvas.renderToBuffer({
    type: 'line',
    data: histogramData
});

fs.writeFileSync(`${allureDir}/duration-histogram.png`, histogramImage);

// Line chart (trend)
const trendData = {
    labels: timestamps.map(t => t.toISOString()),
    datasets: [{
        label: 'Response time over time',
        data: durations,
        borderColor: 'green',
        backgroundColor: 'lightgreen',
    }]
};

const trendImage = await chartJSNodeCanvas.renderToBuffer({
    type: 'line',
    data: trendData
});

fs.writeFileSync(`${allureDir}/duration-trend.png`, trendImage);

// 6. Allure testresultaat opbouwen
const result = {
    name: "k6 performance test",
    status: failedRequests > 0 ? "failed" : "passed",
    steps: [
        { name: `Total requests: ${totalRequests}`, status: "passed" },
        { name: `Failed requests: ${failedRequests}`, status: failedRequests > 0 ? "failed" : "passed" },
        { name: `Average duration: ${avg.toFixed(2)} ms`, status: "passed" },
        { name: `p95 duration: ${p95.toFixed(2)} ms`, status: "passed" },
    ],
    attachments: [
        {
            name: "Response time histogram",
            type: "image/png",
            source: "duration-histogram.png"
        },
        {
            name: "Response time trend",
            type: "image/png",
            source: "duration-trend.png"
        }
    ],
    parameters: [],
};

// 7. Wegschrijven
fs.writeFileSync(`${allureDir}/k6-result.json`, JSON.stringify(result, null, 2));
console.log("Converted k6 NDJSON to Allure with charts");
