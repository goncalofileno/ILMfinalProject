import "./LoginProjectsCards.css";
import { useMediaQuery } from "react-responsive";

export default function LoginProjectsCards({ cardBkgColor, title, description, scrollToRef }) {
   const isTablet = useMediaQuery({ maxWidth: 1170 });

   return (
      <div className={`ilm-card ${cardBkgColor}  `}>
         <div className="flex-column">
            <h3 className="cards-title" style={{ fontSize: isTablet && "20px" }}>
               {title}
            </h3>
            <div className="description-cards">{description}</div>
            <div className="more-details-cards" onClick={scrollToRef}>
               Login to see more details <i className="fas fa-arrow-up margin-left-arrow"></i>
            </div>
         </div>
      </div>
   );
}
