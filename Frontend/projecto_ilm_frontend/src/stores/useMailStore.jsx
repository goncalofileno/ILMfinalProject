import create from 'zustand';

const useMailStore = create((set) => ({
  unreadCount: 0,
  setUnreadCount: (count) => set({ unreadCount: count }),
  decrementUnreadCount: () => set((state) => ({ unreadCount: state.unreadCount - 1 })),
}));

export default useMailStore;