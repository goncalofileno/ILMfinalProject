import create from "zustand";
import {
  getUnreadNotificationsCount,
  fetchNotifications,
} from "../utilities/services";
import Cookies from "js-cookie";

const useNotificationStore = create((set, get) => ({
  unreadNotificationsCount: 0,
  notifications: [],
  totalNotifications: 0,
  hasMoreNotifications: true,
  setUnreadNotificationsCount: (count) =>
    set({ unreadNotificationsCount: count }),
  resetUnreadNotificationsCount: () => set({ unreadNotificationsCount: 0 }),
  incrementUnreadNotificationsCount: () =>
    set((state) => ({
      unreadNotificationsCount: state.unreadNotificationsCount + 1,
    })),
  fetchUnreadNotificationsCount: async () => {
    const sessionId = Cookies.get("session-id");
    if (sessionId) {
      const result = await getUnreadNotificationsCount(sessionId);
      if (typeof result === "number") {
        // Check if result is a number
        set({ unreadNotificationsCount: result });
      } else {
        console.error(
          "Failed to fetch unread notifications count: Invalid response format",
          result
        );
      }
    } else {
      console.error("Session ID not found in cookies");
    }
  },
  fetchNotifications: async (page) => {
    const sessionId = Cookies.get("session-id");
    if (sessionId) {
      const result = await fetchNotifications(sessionId, page);
      set((state) => ({
        notifications:
          page === 1
            ? result.notifications
            : [...state.notifications, ...result.notifications],
        totalNotifications: result.totalNotifications,
        hasMoreNotifications:
          state.notifications.length + result.notifications.length <
          result.totalNotifications,
      }));
    }
  },
  loadMoreNotifications: () => {
    const { fetchNotifications, notifications } = get();
    const nextPage = Math.floor(notifications.length / 5) + 1;
    fetchNotifications(nextPage);
  },
  clearNotifications: () =>
    set({ notifications: [], hasMoreNotifications: true }),
}));

export default useNotificationStore;
