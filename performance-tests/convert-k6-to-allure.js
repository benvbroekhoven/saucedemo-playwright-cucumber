import fs from 'fs';

const raw = JSON.parse(fs.readFileSync('results.json', 'utf8'));

const allureDir = 'allure-results';
if (!fs.existsSync(allureDir)) {
    fs.mkdirSync(allureDir);
}

const result = {
    name: "k6 performance test",
    status: "passed",
    steps: [],
    attachments: [],
    parameters: [],
};

for (const metric of raw.metrics.http_req_duration.values) {
    result.steps.push({
        name: "http_req_duration",
        status: "passed",
        start: Date.now(),
        stop: Date.now(),
        attachments: [],
    });
}

fs.writeFileSync(`${allureDir}/k6-result.json`, JSON.stringify(result, null, 2));
console.log("Converted k6 results to Allure format");
