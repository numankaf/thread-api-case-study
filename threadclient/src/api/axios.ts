import axios from 'axios';

const baseURL = import.meta.env.SERVER_URL;
export const axiosBase = axios.create({
  baseURL: baseURL,
});
