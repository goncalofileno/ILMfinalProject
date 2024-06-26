import create from 'zustand';

const useChatStore = create((set) => ({
  messages: [],
  onlineMembers: [],
  setMessages: (messages) => set({ messages }),
  addMessage: (message) => set((state) => ({ messages: [...state.messages, message] })),
  setOnlineMembers: (members) => set({ onlineMembers: members }),
}));

export default useChatStore;

