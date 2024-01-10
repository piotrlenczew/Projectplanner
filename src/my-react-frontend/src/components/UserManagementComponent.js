import React, {useState, useEffect} from "react";
import axios from "axios";

const UserManagementComponent = () => {
    const [users, setUsers] = useState([]);
    const [newUserName, setNewUserName] = useState("");
    const [newUserPassword, setNewUserPassword] = useState("");

    useEffect(() => {
        axios.get("/api/users/all")
            .then(response => {
                setUsers(response.data);
            })
            .catch(error => console.error("API call error:", error));
    }, []);

    const handleAddUser = (e) => {
        e.preventDefault();
        const newUser = {name: newUserName, password: newUserPassword};
        axios.post("/api/users/add", newUser)
            .then(response => {
                setUsers([...users, response.data]);
                setNewUserName(""); // Clear the input field
                setNewUserPassword(""); // Clear the input field
            })
            .catch(error => console.error("Error adding user:", error));
    };

    const handleDeleteUser = (userId) => {
        axios.delete(`/api/users/delete/${userId}`)
            .then(() => {
                setUsers(users.filter(user => user.id !== userId));
            })
            .catch(error => console.error("Error deleting user:", error));
    };

    return (
        <div className="flex flex-col items-center justify-evenly overflow-hidden">
            <h2 className="text-3xl mb-2">Users</h2>
            {/* Scrollable container for the user list */}
            <div className="mylist-container">
                {users.map(user => (
                    <div key={user.id} className="mylist-entry" >
                        <span className="text-gray-800 font-bold">ID: {user.id}</span>
                        <span className="text-gray-800 ml-2 mr-2">{user.name}</span>
                        <button
                            onClick={() => handleDeleteUser(user.id)}
                            className="mybutton"
                            >
                            Delete
                        </button>
                    </div>
                ))}
            </div>
            {/* Form for adding a new user */}
            <form onSubmit={handleAddUser} className="mb-4">
                <input
                    type="text"
                    value={newUserName}
                    onChange={(e) => setNewUserName(e.target.value)}
                    placeholder="Name"
                    className="myinput"
                />
                <input
                    type="text"
                    value={newUserPassword}
                    onChange={(e) => setNewUserPassword(e.target.value)}
                    placeholder="Password"
                    className="myinput"
                />
                <button
                    type="submit"
                    className="mybutton"
                    >
                    Add user
                </button>
            </form>
        </div>
    );
};

export default UserManagementComponent;
