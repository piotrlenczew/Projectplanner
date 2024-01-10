import React from 'react';
import UserManagementComponent from "./UserManagementComponent.js";
import ProjectManagementComponent from "./ProjectManagementComponent";

function HomePage({ userCredentials }) {
    return (
        <div>
            <div className="min-h-screen bg-custom-background">
                <p>Username: {userCredentials.username}</p>
                <p>Password: {userCredentials.password}</p> {/* Be cautious about displaying passwords */}

                <div className="flex flex-wrap justify-evenly content-evenly">
                    <UserManagementComponent/>
                    <ProjectManagementComponent/>
                </div>
            </div>
        </div>
    );
}

export default HomePage;
