import React from "react";
import AppNavbar from "../components/headers/AppNavbar";
import SentMailTable from "../components/tables/SentMailTable"; 
import AsideMailActions from "../components/asides/AsideMailActions";
import { Trans, t } from "@lingui/macro";

const SentMailPage = () => {
   return (
      <>
         <AppNavbar />
         <AsideMailActions />
         <div className="ilm-pageb-with-aside">
            <h1 className="page-title">
               <span className="app-slogan-1"><Trans id="os-teus-emails-enviados">Your</Trans> </span>
               <span className="app-slogan-2"><Trans>Sent e-Mails</Trans></span>
            </h1>
            <div className="table-margin-top">
               <SentMailTable />
            </div>
         </div>
      </>
   );
};

export default SentMailPage;
