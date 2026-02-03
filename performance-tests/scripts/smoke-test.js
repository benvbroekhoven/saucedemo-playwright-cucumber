import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
    vus: 1,          // 1 gebruiker
    duration: '10s', // test duurt 10 seconden
};

export default function () {
    http.get('https://www.saucedemo.com/');
    sleep(1);
}
