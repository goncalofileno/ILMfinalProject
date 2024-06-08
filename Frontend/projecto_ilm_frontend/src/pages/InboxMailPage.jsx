import React from "react";
import AppNavbar from "../components/headers/AppNavbar";
import MailTable from "../components/tables/MailTable"; // Componente para exibir emails recebidos
import AsideMailActions from "../components/asides/AsideMailActions"; // Componente para ações de email

const InboxMailPage = () => {
   return (
      <>
         <AppNavbar />
         <AsideMailActions />
         <div className="ilm-pageb-with-aside">
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

