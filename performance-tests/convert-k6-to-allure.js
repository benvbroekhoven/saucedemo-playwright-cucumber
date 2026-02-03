import fs from 'fs';

const lines = fs.readFileSync('results.json', 'utf8')
    .trim()
    .split('\n');

const metrics = lines.map(line => JSON.parse(line));

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

for (const entry of metrics) {
    if (entry.metric === "http_req_duration") {
        result.steps.push({
            name: "http_req_duration",
            status: "passed",
            start: Date.now(),
            stop: Date.now(),
            attachments: [],
        });
    }
}

fs.writeFileSync(`${allureDir}/k6-result.json`, JSON.stringify(result, null, 2));
console.log("Converted k6 NDJSON to Allure format");
