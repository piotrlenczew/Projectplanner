import React, {useState, useEffect} from "react";
import axios from "axios";

const ProjectManagementComponent = () => {
    const [projects, setProjects] = useState([]);
    const [newProjectName, setNewProjectName] = useState("");
    const [newProjectOwnerId, setNewProjectOwnerId] = useState("");

    useEffect(() => {
        axios.get("/api/projects/all")
            .then(response => {
                setProjects(response.data);
            })
            .catch(error => console.error("API call error:", error));
    }, []);

    const handleAddProject = (e) => {
        e.preventDefault();
        const newProject = {name: newProjectName, owner: {id: newProjectOwnerId}};
        axios.post("/api/projects/add", newProject)
            .then(response => {
                setProjects([...projects, response.data]);
                setNewProjectName("");    // Clear the input field
                setNewProjectOwnerId(""); // Clear the input field
            })
            .catch(error => console.error("Error adding project:", error));
    };

    const handleDeleteProject = (projectId) => {
        axios.delete(`/api/projects/delete/${projectId}`)
            .then(() => {
                setProjects(projects.filter(project => project.id !== projectId));
            })
            .catch(error => console.error("Error deleting project:", error));
    };

    return (
        <div className="flex flex-col items-center justify-center overflow-hidden">
            <h2 className="text-3xl mb-2">Projects</h2>
            {/* Scrollable container for the project list */}
            <div className="mylist-container">
                {projects.map(project => (
                    <div key={project.id} className="mylist-entry" >
                        <span className="text-gray-800">
                            <span className="font-bold">ID: {project.id}</span>, owner: {project.owner.name}
                        </span>
                        <span className="text-gray-800 ml-2 mr-2">{project.name}</span>
                        <button
                            onClick={() => handleDeleteProject(project.id)}
                            className="mybutton"
                            >
                            Delete
                        </button>
                    </div>
                ))}
            </div>
            {/* Form for adding a new project */}
            <form onSubmit={handleAddProject} className="mb-4">
                <input
                    type="text"
                    value={newProjectName}
                    onChange={(e) => setNewProjectName(e.target.value)}
                    placeholder="Name"
                    className="myinput"
                />
                <input
                    type="text"
                    value={newProjectOwnerId}
                    onChange={(e) => setNewProjectOwnerId(e.target.value)}
                    placeholder="Owner ID"
                    className="myinput"
                />
                <button
                    type="submit"
                    className="mybutton"
                    >
                    Add project
                </button>
            </form>
        </div>
    );
};

export default ProjectManagementComponent;

