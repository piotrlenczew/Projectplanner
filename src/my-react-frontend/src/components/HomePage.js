import React from 'react';
import ProjectManagementComponent from "./ProjectManagementComponent";
import { useNavigate } from "react-router-dom";
import { googleLogout } from '@react-oauth/google';


function HomePage({ user, onLogout }) {
    const navigate = useNavigate();

    const handleLogoutClick = (e) => {
        onLogout(); // Call the handleLogout function from props
        googleLogout();
        navigate('/login'); // Navigate to LoginPage
        alert("User logged out");
    }

    //alert(user.id);
    //alert(user.name);
    return (
        <div>
            <div className="min-h-screen bg-custom-background">

                <div>
                    {user && user.name ? <p>User: {user.name}</p> : <p>No user data</p>}
                </div>
                <div>
                    {user && user.id ? <p>User: {user.id}</p> : <p>No user data</p>}
                </div>
                {user ? (
                    <div>
                        <h1>Home Page</h1>
                        <h3>{user.name}</h3>
                        <h4>{user.email}</h4>
                        <img src={user.picture} alt="User Avatar" />
                    </div>
                ) : (
                    <p>No user data</p>
                )}
                <div className="flex flex-wrap justify-evenly content-evenly">
                    <ProjectManagementComponent user={user} />
                </div>
                <button onClick={(e) => handleLogoutClick(e)}>Sign Out</button>
            </div>
        </div>
    );
}

export default HomePage;
