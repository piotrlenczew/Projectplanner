import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Login from './components/Login';
import Register from './components/Register';
import HomePage from './components/HomePage';

function App() {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [user, setUser] = useState();

    const handleLogin = (user) => {
        setUser(user);
        setIsLoggedIn(true);
    };

    const handleRegister = (user) => {
        setUser(user);
        setIsLoggedIn(true);
    };

    const handleLogout = () => {
        setUser(null);
        setIsLoggedIn(false);
    };

    return (
        <Router>
            <Routes>
                <Route path="/" element={<Navigate to="/login" />} />
                <Route path="/login" element={<Login onLogin={handleLogin} />} />
                <Route path="/register" element={<Register onRegister={handleRegister} />} />
                <Route path="/home" element={isLoggedIn ? <HomePage user={user} onLogout={handleLogout} /> : <Navigate to="/login" />} />
            </Routes>
        </Router>
    );
}

export default App;