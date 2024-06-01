import { create } from "zustand";

const alertStore = create((set) => ({
   message: "",
   setMessage: (newMessage) => set({ message: newMessage }),
   visibility: false, // visible or hidden
   setVisibility: (newVisibility) => set({ visibility: newVisibility }),
   type: false, // success or danger
   setType: (newType) => set({ type: newType }),

   confirmMessage: "",
   setConfirmMessage: (newConfirmMessage) => set({ confirmMessage: newConfirmMessage }),
   confirmVisible: false,
   setConfirmVisible: (newConfirmVisible) => set({ confirmVisible: newConfirmVisible }),
   confirmCallback: () => {},
   setConfirmCallback: (newConfirmCallback) => set({ confirmCallback: newConfirmCallback }),
}));

export default alertStore;
