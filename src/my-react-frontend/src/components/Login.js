import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function Login({ onLogin }) {
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
                    onLogin(response.data); // Update App state if authentication is successful
                    navigate('/home'); // Navigate to HomePage
                }
            } else {
                throw new Error('Authentication failed'); // Rzuć wyjątek, jeśli autoryzacja nie powiedzie się
            }
        } catch (error) {
            console.error('Login error:', error);
            if (error.response && error.response.status === 404) {
                setLoginError("User not found"); // Ustaw błąd dla błędnej nazwy użytkownika
            } else {
                setLoginError('An error occurred during login'); // Ustaw błąd dla innych błędów
            }
            alert(loginError); // Teraz alert wyświetli aktualny błąd
        };
    }

    return (
        <div className="flex justify-center items-center h-screen">
            <div className="w-full max-w-xs flex flex-col items-center">
                <h2 className="text-3xl mb-2">Login</h2>
                <form onSubmit={handleLoginSubmit} className="w-full flex flex-col mb-8">
                    <input
                        className="myinput mb-2"
                        autoFocus
                        required
                        type="text"
                        placeholder="Username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                    <input
                        className="myinput mb-2"
                        required
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                    <button
                        type="submit"
                        className="loginbutton loginbutton-submit"
                    >
                        Login
                    </button>

                </form>
                <button
                    className="loginbutton loginbutton-other w-full"
                    onClick={handleRegisterClick}
                >
                    Register here
                </button>
            </div>
        </div>
    );
}

export default Login;
