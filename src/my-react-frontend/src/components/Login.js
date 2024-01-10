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
            alert(response.data);
            if (response.status === 200) {
                if (response.data)
                {
                    onLogin(username, password); // Update App state if authentication is successful
                    navigate('/home'); // Navigate to HomePage
                }
                else
                {
                    setPassword('');
                    alert('Wrong Password');
                }

            } else {
                throw new Error('Authentication failed'); // Rzuć wyjątek, jeśli autoryzacja nie powiedzie się
            }
        } catch (error) {
            console.error('Login error:', error);
            if (error.response && error.response.status === 500) {
                alert('Wrong Username');
                setLoginError('Internal Server Error'); // Ustaw błąd dla błędów serwera
            } else {
                setLoginError('An error occurred during login'); // Ustaw błąd dla innych błędów
            }
            alert(loginError); // Teraz alert wyświetli aktualny błąd
        };
    }

    return (
        <div className="flex justify-center items-center h-screen">
            <div className="w-full max-w-xs flex flex-col items-center">
                <form onSubmit={handleLoginSubmit} className="w-full flex flex-col">
                    <input
                        className="px-3 py-2 mb-3 border rounded shadow appearance-none text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                        type="text"
                        placeholder="Username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                    <input
                        className="px-3 py-2 mb-3 border rounded shadow appearance-none text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                    <button
                        type="submit"
                        className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline mb-4"
                    >
                        Login
                    </button>

                </form>
                <button
                    className="w-full bg-gray-300 hover:bg-gray-400 text-gray-800 font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
                    onClick={handleRegisterClick}
                >
                    Register
                </button>
            </div>
        </div>
    );
}

export default Login;
