import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom'; // Import useNavigate


const Register = ({ onRegister }) => {
    const [newUserName, setNewUserName] = useState("");
    const [newUserPassword, setNewUserPassword] = useState("");
    const navigate = useNavigate(); // Initialize useNavigate
    const handleRegisterUser = (e) => {
        e.preventDefault();
        const newUser = { name: newUserName, password: newUserPassword };
        axios.post("/api/users/register", newUser)
            .then(response => {
                alert('User added successfully!'); // Notify user
                alert(response.data.id)
                onRegister(newUserName, newUserPassword);
                navigate('/home'); // Navigate to the HomePage
            })
            .catch(error => {
                // Check if the error is due to the username being taken
                if (error.response && error.response.status === 500) { // Assuming 409 status code for conflict
                    alert("Username taken");
                } else {
                    console.error("Error adding user:", error);
                }
            });
    };

    return (
        <div className="flex justify-center items-center h-screen">
            <form onSubmit={handleRegisterUser} className="flex flex-col items-center mb-4">
                <input
                    type="text"
                    value={newUserName}
                    onChange={(e) => setNewUserName(e.target.value)}
                    placeholder="Username"
                    className="myinput mb-2" // Add margin-bottom for spacing
                />
                <input
                    type="password"
                    value={newUserPassword}
                    onChange={(e) => setNewUserPassword(e.target.value)}
                    placeholder="Password"
                    className="myinput mb-2" // Add margin-bottom for spacing
                />
                <button
                    type="submit"
                    className="mybutton"
                >
                    Register
                </button>
            </form>
        </div>
    );
};

export default Register;