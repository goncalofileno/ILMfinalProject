import create from 'zustand';
import { getReceivedMessages } from '../utilities/services';
import Cookies from 'js-cookie';

const useMailStore = create((set) => ({
  unreadCount: 0,
  receivedMails: [],
  totalMails: 0,
  setUnreadCount: (count) => set({ unreadCount: count }),
  decrementUnreadCount: () => set((state) => ({ unreadCount: state.unreadCount - 1 })),
  incrementUnreadCount: () => set((state) => ({ unreadCount: state.unreadCount + 1 })),
  setReceivedMails: (mails) => set({ receivedMails: mails }),
  setTotalMails: (total) => set({ totalMails: total }),
  fetchMailsInInbox: async () => {
    const sessionId = Cookies.get("session-id");
    if (sessionId) {

      const result = await getReceivedMessages(sessionId, 1, 8);
      const { mails, totalMails } = result;
      mails.sort((a, b) => new Date(b.date) - new Date(a.date));
      set({ receivedMails: mails, totalMails: totalMails });
    }
  },
}));

export default useMailStore;
