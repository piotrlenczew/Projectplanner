import React, { useState} from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { jwtDecode } from "jwt-decode";
import { GoogleLogin } from '@react-oauth/google';
import { FaUser, FaLock } from "react-icons/fa";
import './Style/Login.css';
import { setAuthHeader } from '../helpers/axios_helper';

const clientId = "653829545632-s1tg9di96ernst657soqhvtdt37vssp8.apps.googleusercontent.com";

function Login({ onLogin }) {
    const [user, setUser] = useState('');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [loginError, setLoginError] = useState('');
    const navigate = useNavigate();


    const handleRegisterClick = () => {
        navigate('/register');
    };

    const handleLoginSubmit = async (e) => {
        e.preventDefault();
        setLoginError(''); // Reset any previous login errors

        try {
            // Prepare the request body and parameters for the authentication request
            const requestBody = {
                name: username,
                password: password,
            };

            const response = await axios.post("/api/users/login", requestBody);
            //alert(response.data);
            //alert(response.status)
            if (response.status === 200) {
                if (response.data)
                {
                    //alert(response.data.id)
                    setAuthHeader(response.data.token)
                    onLogin(response.data); // Update App state if authentication is successful
                    navigate('/home'); // Navigate to HomePage
                }
            } else {
                setAuthHeader(null)
                throw new Error('Authentication failed'); // Rzuć wyjątek, jeśli autoryzacja nie powiedzie się
            }
        } catch (error) {
            setAuthHeader(null)
            console.error('Login error:', error);
            if (error.response && error.response.status === 404) {
                setLoginError("User not found"); // Ustaw błąd dla błędnej nazwy użytkownika
            } else {
                setLoginError('An error occurred during login'); // Ustaw błąd dla innych błędów
            }
            alert(loginError); // Teraz alert wyświetli aktualny błąd
        }
    }

    const onGoogleSuccess = async (response) => {
        console.log("Google login Success: currentUser:", response);
        console.log("Encoded JWT ID token: " + response.credential);
        // Handle the Google Sign-In callback response if needed
        // For example, you can use the response to:
        // - Obtain the Google user ID
        // - Obtain an ID token for the user (which may be used with Google services)
        // - See the Google Sign-In documentation for more details
        //alert(response.credential)

        try {
            const userObject = jwtDecode(response.credential);
            console.log("Decoded JWT ID token:", userObject);

            const requestBody = {
                name: userObject.given_name + " " + userObject.family_name,
                email: userObject.email,
            };

            const response2 = await axios.post("/api/users/googleLogin", requestBody);
            if (response2.status === 200 && response2.data) {
                setAuthHeader(response2.data.token)
                onLogin(response2.data); // Update App state if authentication is successful
            } else {
                setAuthHeader(null)
                throw new Error("Authentication failed");
            }

            navigate("/home");
        } catch (error) {
            setAuthHeader(null)
            console.error("Error:", error);
        }
    };

    const onGoogleFailure = (response) => {
        console.log("Google login failed:", response);
    }

    return (
        <div className="flex justify-center items-center h-screen">
            <div className="w-full max-w-xs flex flex-col items-center">
                <h2 className="text-3xl text-white mb-2">Welcome to Project Planner</h2>
                <form onSubmit={handleLoginSubmit} className="w-full flex flex-col mb-4">
                    <input
                        className="myinput mb-2"
                        autoFocus
                        required
                        type="text"
                        placeholder="Username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                    <FaUser className="icon_user"/>
                    <input
                        className="myinput mb-2"
                        required
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                    <FaLock className="icon_lock"/>
                    <button
                        type="submit"
                        className="loginbutton loginbutton-submit"
                    >
                        Login
                    </button>

                </form>

                <button
                    className="loginbutton loginbutton-other w-full mb-4"
                    onClick={handleRegisterClick}
                >
                    Register here
                </button>

                <GoogleLogin clientId={clientId}
                    buttonText="Login"
                    onSuccess={onGoogleSuccess}
                    onFailure={onGoogleFailure}
                    cookiePolicy={"single_host_origin"}
                    isSignedIn={true}
                    className={"w-full"}
                />
            </div>
        </div>
    );
}

export default Login;
