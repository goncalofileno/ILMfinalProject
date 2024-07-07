import React from "react";
import AppNavbar from "../components/headers/AppNavbar";
import MailTable from "../components/tables/MailTable"; 
import AsideMailActions from "../components/asides/AsideMailActions"; 
import { Trans, t } from "@lingui/macro";

const InboxMailPage = () => {
   return (
      <>
         <AppNavbar />
         <AsideMailActions />
         <div className="ilm-pageb-with-aside">
            <h1 className="page-title">
               <span className="app-slogan-1"><Trans id="you-caixa-de-entrada">Your</Trans> </span>
               <span className="app-slogan-2"><Trans id="inbox-caixa-de-entrada">Inbox</Trans></span>
            </h1>
            <div className="table-margin-top">
               <MailTable />
            </div>
         </div>
      </>
   );
};

export default InboxMailPage;

