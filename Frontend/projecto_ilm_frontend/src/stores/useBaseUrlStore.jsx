import create from 'zustand';

const useBaseUrlStore = create((set) => ({
  baseUrl: 'https://localhost:8443/my_activities_backend/rest/',
  setBaseUrl: (newUrl) => set({ baseUrl: newUrl }),
}));

export default useBaseUrlStore;