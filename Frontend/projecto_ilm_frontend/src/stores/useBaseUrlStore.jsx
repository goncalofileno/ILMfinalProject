import create from 'zustand';

const useBaseUrlStore = create((set) => ({
  baseUrl: 'https://localhost:8443/projeto_ilm_final/rest/',
  setBaseUrl: (newUrl) => set({ baseUrl: newUrl }),
}));

export default useBaseUrlStore;