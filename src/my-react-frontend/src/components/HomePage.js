import React from "react";
import ProjectManagementComponent from "./ProjectManagementComponent";

function HomePage({user}) {
    //alert(user.id);
    //alert(user.name);
    return (
        <div>
            <div>
                {user && user.name ? <p>User: {user.name}</p> : <p>No user data</p>}
            </div>
            <div>
                {user && user.id ? <p>User: {user.id}</p> : <p>No user data</p>}
            </div>
            <div className="flex flex-wrap justify-evenly content-evenly">
                <ProjectManagementComponent user={user}/>
            </div>
        </div>
    );
}

export default HomePage;
