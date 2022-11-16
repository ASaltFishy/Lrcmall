import axios from 'axios';
const instance = axios.create(
    {
        baseURL: 'https://localhost:8443',
        timeout: 5000,
        headers: {
            'Content-type': 'application/json'
        },
        withCredentials: "true",
    });
const microInstance = axios.create(
    {
        baseURL: 'http://localhost:8060',
        timeout: 5000,
        headers: {
            'Content-type': 'application/json'
        },
        withCredentials: "true",
    });
export { instance,microInstance};