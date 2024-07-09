import React from "react";
import AppNavbar from "../components/headers/AppNavbar";
import MailTable from "../components/tables/MailTable";
import AsideMailActions from "../components/asides/AsideMailActions";
import { Trans, t } from "@lingui/macro";
import { useMediaQuery } from "react-responsive";
import { useState } from "react";
import Cookies from "js-cookie";

const InboxMailPage = () => {
  const isMobile = useMediaQuery({ query: "(max-width: 768px)" });
  const [isAsideVisible, setIsAsideVisible] = useState(false);
  const [currentLanguage, setCurrentLanguage] = useState(
    Cookies.get("user-language") || "ENGLISH"
  );
  return (
    <>
      <AppNavbar
        setIsAsideVisible={setIsAsideVisible}
        pageWithAside={true}
        setCurrentLanguage={setCurrentLanguage}
      />
      <AsideMailActions isVisible={isAsideVisible} />
      <div
        className={isMobile ? "ilm-page-mobile" : "ilm-pageb-with-aside"}
        onClick={() => isAsideVisible && setIsAsideVisible((prev) => !prev)}
      >
        <h1 className="page-title">
          <span className="app-slogan-1">
            <Trans id="you-caixa-de-entrada">Your</Trans>{" "}
          </span>
          <span className="app-slogan-2">
            <Trans id="inbox-caixa-de-entrada">Inbox</Trans>
          </span>
        </h1>
        <div className="table-margin-top">
          <MailTable />
        </div>
      </div>
    </>
  );
};

export default InboxMailPage;
