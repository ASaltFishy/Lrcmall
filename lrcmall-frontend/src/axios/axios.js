import axios from 'axios';
const instance = axios.create(
    {
        baseURL:'https://localhost:8443',
        timeout: 5000,
        headers:{
            'Content-type':'application/json'
        },
        withCredentials:"true",
    });
export {instance};