import React from "react";
import ProjectManagementComponent from "./ProjectManagementComponent";
import { useNavigate } from "react-router-dom";
import { googleLogout } from '@react-oauth/google';
import './Style/HomePage.css';

function HomePage({ user, onLogout }) {
    const navigate = useNavigate();

    const handleLogoutClick = () => {
        onLogout(); // Call the handleLogout function from props
        googleLogout();
        navigate('/login'); // Navigate to LoginPage
        alert("User logged out");
    }

    return (
        <div className="min-h-screen bg-custom-background">
            <div className="wrapper">
                <div className="text-white">
                    <h1>Welcome {user.name}</h1>
                    <h2>{user.email}</h2>
                </div>

                <div className="flex flex-wrap justify-evenly content-evenly">
                    <ProjectManagementComponent user={user} />
                </div>

            </div>


            <div className="user-section">
                <img src={user.picture || './Images/npc_face.jpg'} alt="User Avatar" />
                <button
                    className="mybutton"
                    onClick={(e) => handleLogoutClick(e)}
                >
                    Sign out
                </button>
            </div>
        </div>
    );
}

export default HomePage;
