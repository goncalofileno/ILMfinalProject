import axios from 'axios';
import useBaseUrlStore from '../stores/useBaseUrlStore';
const useAxios = () => {

  const baseUrl = useBaseUrlStore((state) => state.baseUrl);

  const instance = axios.create({
    baseURL: baseUrl,
    headers: {
      'Content-Type': 'application/json',
    },
  });

  return instance;
};

export default useAxios;
