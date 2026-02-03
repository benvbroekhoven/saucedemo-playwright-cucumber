import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    vus: 10,            // 10 gelijktijdige gebruikers
    duration: '30s',    // test duurt 30 seconden
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
