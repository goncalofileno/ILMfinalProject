import "./App.css";
import { useNavigate } from "react-router-dom";

function App() {
   const navigate = useNavigate();

   return (
      <div className="App">
         <button className="btn-register" onClick={() => navigate("/register")}>
            Register
         </button>
      </div>
   );
}

export default App;
