import {useState} from 'react';
import api from '../services/api';
import { useNavigate } from "react-router-dom";

function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const login = async (e) => {
    e.preventDefault();
    setError('');
    try {
      const response = await api.post('/auth/login', {username, password});
      localStorage.setItem('token', response.data.token);
      navigate('/');   
    
    } catch (err) {
      setError('Login failed. Please check your credentials.');
      console.error(err);
    }};

    return (
    <div style={{ padding: "20px" }}>

      <h1>Login</h1>

      <form onSubmit={login}>

        <div>
          <input
            type="text"
            placeholder="Username"
            value={username}
            onChange={(e) =>
              setUsername(e.target.value)
            }
          />
        </div>

        <br />

        <div>
          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) =>
              setPassword(e.target.value)
            }
          />
        </div>

        <br />

        <button type="submit">
          Login
        </button>

      </form>

      {error && (
        <p style={{ color: "red" }}>
          {error}
        </p>
      )}

    </div>
  );
}

export default Login;