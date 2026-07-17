import { useState } from "react";
import api from "../services/api";
import { useNavigate } from "react-router-dom";

function Register() {
  const navigate = useNavigate();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");

  const register = async (e) => {
    e.preventDefault();

    try {
      await api.post("/auth/register", {
        username,
        password,
      });

      const response = await api.post("/auth/login", {
        username,
        password,
      });

      localStorage.setItem(
        "token", 
        response.data.token
      );

      navigate("/"); 

    } catch (err) {
      setMessage("Registration failed");
    }
  };

  return (
    <div style={{ padding: 20 }}>
      <h1>Register</h1>

      <form onSubmit={register}>

        <input
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />

        <br /><br />

        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />

        <br /><br />

        <button type="submit">Register</button>

      </form>

      {message && <p>{message}</p>}
    </div>
  );
}

export default Register;