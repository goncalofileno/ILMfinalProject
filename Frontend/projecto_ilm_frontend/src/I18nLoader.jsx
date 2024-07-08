import React, { useState, useEffect, createContext, useContext } from "react";
import { I18nProvider } from "@lingui/react";
import { i18n } from "@lingui/core";
import { messages as enMessages } from "./locales/en/messages";
import { messages as ptMessages } from "./locales/pt/messages";
import Cookies from "js-cookie";

// Criação do contexto de idioma
const LanguageContext = createContext();

export const useLanguage = () => useContext(LanguageContext);

const I18nLoader = ({ children }) => {
  const [language, setLanguage] = useState(Cookies.get("user-language") || "ENGLISH");
  const [loading, setLoading] = useState(true);

  const changeLanguage = (newLanguage) => {
    Cookies.set("user-language", newLanguage, { expires: 365 });
    setLanguage(newLanguage);
  };

  useEffect(() => {
    const userLanguage = Cookies.get("user-language") || "ENGLISH";
    i18n.load({
      ENGLISH: enMessages,
      PORTUGUESE: ptMessages,
    });
    i18n.activate(userLanguage);
    setLoading(false);
  }, [language]);

  if (loading) {
    return <div>Loading...</div>;
  }

  return (
    <LanguageContext.Provider value={{ language, changeLanguage }}>
      <I18nProvider i18n={i18n}>{children}</I18nProvider>
    </LanguageContext.Provider>
  );
};

export default I18nLoader;
