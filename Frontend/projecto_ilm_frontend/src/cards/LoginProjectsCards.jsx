import "./LoginProjectsCards.css";

export default function LoginProjectsCards({ cardBkgColor, title, description, scrollToRef }) {
   return (
      <div className={`ilm-card ${cardBkgColor}  `}>
         <div className="flex-column">
            <h3 className="cards-title">{title}</h3>
            <div className="description-cards">{description}</div>
            <div className="more-details-cards" onClick={scrollToRef}>
               Login to see more details <i class="fas fa-arrow-up margin-left-arrow"></i>
            </div>
         </div>
      </div>
   );
}
