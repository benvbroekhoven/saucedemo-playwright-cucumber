import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    vus: 5,
    duration: '20s',
    thresholds: {
        http_req_duration: ['p(95)<700'],
        http_req_failed: ['rate<0.01'],
    },
};

export default function () {
    let res = http.get('https://www.saucedemo.com/');
    check(res, { 'homepage loaded': (r) => r.status === 200 });

    sleep(1);

    // Product page
    res = http.get('https://www.saucedemo.com/inventory.html');
    check(res, { 'inventory loaded': (r) => r.status === 200 });

    sleep(1);
}
