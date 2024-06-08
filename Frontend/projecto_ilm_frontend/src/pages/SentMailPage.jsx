import React from "react";
import AppNavbar from "../components/headers/AppNavbar";
import SentMailTable from "../components/tables/SentMailTable"; // Componente para exibir emails enviados
import AsideMailActions from "../components/asides/AsideMailActions"; // Componente para ações de email

const SentMailPage = () => {
   return (
      <>
         <AppNavbar />
         <AsideMailActions />
         <div className="ilm-pageb-with-aside">
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
