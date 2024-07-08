import React from "react";
import AppNavbar from "../components/headers/AppNavbar";
import SentMailTable from "../components/tables/SentMailTable"; // Componente para exibir emails enviados
import AsideMailActions from "../components/asides/AsideMailActions"; // Componente para ações de email
import { useMediaQuery } from "react-responsive";
import { useState } from "react";

const SentMailPage = () => {
  const isMobile = useMediaQuery({ query: "(max-width: 768px)" });
  const [isAsideVisible, setIsAsideVisible] = useState(false);
  return (
    <>
      <AppNavbar setIsAsideVisible={setIsAsideVisible} />
      <AsideMailActions isVisible={isAsideVisible} />
      <div
        className={isMobile ? "ilm-page-mobile" : "ilm-pageb-with-aside"}
        onClick={() => isAsideVisible && setIsAsideVisible((prev) => !prev)}
      >
        <h1 className="page-title">
          <span className="app-slogan-1">Your </span>
          <span className="app-slogan-2">Sent e-Mails</span>
        </h1>
        <div className="table-margin-top">
          <SentMailTable />
        </div>
      </div>
    </>
  );
};

export default SentMailPage;
