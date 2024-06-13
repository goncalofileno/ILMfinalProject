import create from 'zustand';
import { getUnreadNotificationsCount, fetchNotifications } from '../utilities/services';
import Cookies from 'js-cookie';

const useNotificationStore = create((set) => ({
  unreadNotificationsCount: 0,
  notifications: [],
  setUnreadNotificationsCount: (count) => set({ unreadNotificationsCount: count }),
  resetUnreadNotificationsCount: () => set({ unreadNotificationsCount: 0 }),
  setNotifications: (notifications) => set({ notifications }),
  clearNotifications: () => set({ notifications: [] }),
  fetchUnreadNotificationsCount: async () => {
    const sessionId = Cookies.get("session-id");
    if (sessionId) {
      const result = await getUnreadNotificationsCount(sessionId);
      console.log("UNREAD NOTIFICATIONS COUNT", result)
      if (typeof result === 'number') { // Check if result is a number
        set({ unreadNotificationsCount: result });
      } else {
        console.error("Failed to fetch unread notifications count: Invalid response format", result);
      }
    } else {
      console.error("Session ID not found in cookies");
    }
  },
  fetchNotifications: async (page = 1) => {
    const sessionId = Cookies.get("session-id");
    console.log("SESSION ID", sessionId)
    if (sessionId) {
        console.log("FETCHING NOTIFICATIONS")
      const result = await fetchNotifications(sessionId, page);
      set((state) => ({
        notifications: page === 1 ? result : [...state.notifications, ...result],
      }));
    } else {
      console.error("Session ID not found in cookies");
    }
  },
}));

export default useNotificationStore;
