import { useState } from "react";
import axios from "axios";

function App() {
  const [file, setFile] = useState(null);
  const [result, setResult] = useState("");

  const uploadFile = async () => {
    const formData = new FormData();
    formData.append("file", file);

    const response = await axios.post(
      "http://localhost:8080/api/summarize",
      formData
    );

    setResult(response.data);
  };

  return (
    <div style={{ padding: 20 }}>
      <h1>AI Study Assistant</h1>

      <input
        type="file"
        onChange={(e) => setFile(e.target.files[0])}
      />

      <button onClick={uploadFile}>
        Upload & Summarize
      </button>

      <pre>{JSON.stringify(result, null, 2)}</pre>
    </div>
  );
}

export default App;