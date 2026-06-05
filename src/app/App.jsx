import { useState } from 'react'
import reactLogo from '../assets/react.svg'
import viteLogo from '../assets/vite.svg'
import heroImg from '../assets/hero.png'
import '../styles/App.css'
import Doctors from "../features/admin/pages/Doctors.jsx";
import DoctorDetail from "../features/admin/pages/DoctorDetail.jsx";
function App() {
    return <DoctorDetail />;
}

export default App
