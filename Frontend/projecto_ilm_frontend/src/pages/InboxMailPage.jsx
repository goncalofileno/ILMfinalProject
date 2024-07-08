import React from "react";
import AppNavbar from "../components/headers/AppNavbar";
import MailTable from "../components/tables/MailTable"; // Componente para exibir emails recebidos
import AsideMailActions from "../components/asides/AsideMailActions"; // Componente para ações de email
import { useMediaQuery } from "react-responsive";
import { useState } from "react";

const InboxMailPage = () => {
  const isMobile = useMediaQuery({ query: "(max-width: 768px)" });
  const [isAsideVisible, setIsAsideVisible] = useState(false);
  return (
    <>
      <AppNavbar setIsAsideVisible={setIsAsideVisible} pageWithAside={true} />
      <AsideMailActions isVisible={isAsideVisible} />
      <div
        className={isMobile ? "ilm-page-mobile" : "ilm-pageb-with-aside"}
        onClick={() => isAsideVisible && setIsAsideVisible((prev) => !prev)}
      >
        <h1 className="page-title">
          <span className="app-slogan-1">Your </span>
          <span className="app-slogan-2">Inbox</span>
        </h1>
        <div className="table-margin-top">
          <MailTable />
        </div>
      </div>
    </>
  );
};

export default InboxMailPage;
