import React from "react";
import AppNavbar from "../components/headers/AppNavbar";
import SentMailTable from "../components/tables/SentMailTable";
import AsideMailActions from "../components/asides/AsideMailActions";
import { Trans, t } from "@lingui/macro";
import { useMediaQuery } from "react-responsive";
import { useState } from "react";
import Cookies from "js-cookie";

const SentMailPage = () => {
  const isMobile = useMediaQuery({ query: "(max-width: 768px)" });
  const [isAsideVisible, setIsAsideVisible] = useState(false);
  const [currentLanguage, setCurrentLanguage] = useState(
    Cookies.get("user-language") || "ENGLISH"
  );
  return (
    <>
      <AppNavbar
        setIsAsideVisible={setIsAsideVisible}
        setCurrentLanguage={setCurrentLanguage}
        pageWithAside={true}
      />
      <AsideMailActions isVisible={isAsideVisible} />
      <div
        className={isMobile ? "ilm-page-mobile" : "ilm-pageb-with-aside"}
        onClick={() => isAsideVisible && setIsAsideVisible((prev) => !prev)}
      >
        <h1 className="page-title">
          <span className="app-slogan-1">
            <Trans id="os-teus-emails-enviados">Your</Trans>{" "}
          </span>
          <span className="app-slogan-2">
            <Trans>Sent e-Mails</Trans>
          </span>
        </h1>
        <div className="table-margin-top">
          <SentMailTable />
        </div>
      </div>
    </>
  );
};

export default SentMailPage;
