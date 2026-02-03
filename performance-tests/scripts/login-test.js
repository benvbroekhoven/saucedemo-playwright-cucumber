import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    vus: 10,
    duration: '30s',
    thresholds: {
        http_req_duration: ['p(95)<500'],   // 95% van requests < 500ms
        http_req_failed: ['rate<0.01'],     // minder dan 1% failures
    },
};


export default function () {
    // 1. Open de loginpagina
    const res = http.get('https://www.saucedemo.com/');

    // 2. Basiscontrole: pagina moet 200 OK geven
    check(res, {
        'login page loaded': (r) => r.status === 200,
    });

    // 3. Wacht even (simuleert realistisch gedrag)
    sleep(1);
}
