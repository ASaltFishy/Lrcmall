import axios from 'axios';
const instance = axios.create(
    {
        baseURL:'http://localhost:8080',
        timeout: 5000,
        headers:{
            'Content-type':'application/json'
        },
        withCredentials:"true",
    });
export {instance};