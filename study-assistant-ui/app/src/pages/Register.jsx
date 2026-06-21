import { useState } from "react";
import api from "../services/api";

function Register() {

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

      setMessage("User registered successfully. You can now login.");

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