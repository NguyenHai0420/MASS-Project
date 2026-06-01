import { createRoot } from "react-dom/client";
import App from "./app/App.jsx";
import "bootstrap/dist/css/bootstrap.min.css";
import {AuthProvider} from "./app/providers/AuthProvider.jsx";

createRoot(document.getElementById("root")).render(
  <AuthProvider>
    <App />
  </AuthProvider>
);