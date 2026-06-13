import { useEffect, useState } from "react";
import axios from "axios";

function App() {

  const [file, setFile] = useState(null);
  const [result, setResult] = useState(null);
  const [documents, setDocuments] = useState([]);

  const [loading, setLoading] = useState(false);   
  const [error, setError] = useState("");           

  const loadDocuments = async () => {
    try {
      const response = await axios.get(
        "http://localhost:8080/api/documents"
      );

      setDocuments(Array.isArray(response.data) ? response.data : []);

    } catch (err) {
      console.error(err);
      setDocuments([]);
      setError("Failed to load documents");
    }
  };

  const uploadFile = async () => {

    if (!file) {
      alert("Please select a file");
      return;
    }

    setLoading(true);
    setError("");

    try {
      const formData = new FormData();
      formData.append("file", file);

      const response = await axios.post(
        "http://localhost:8080/api/summarize",
        formData
      );

      setResult(response.data);

      await loadDocuments();

    } catch (err) {
      console.error(err);
      setError("Upload failed. Please try again.");

    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadDocuments();
  }, []);

  return (
    <div style={{ padding: "20px" }}>

      <h1>AI Study Assistant</h1>

      <input
        type="file"
        accept=".pdf"
        onChange={(e) => setFile(e.target.files[0])}
      />

      <button
        onClick={uploadFile}
        disabled={loading}
        style={{ marginLeft: "10px" }}
      >
        {loading ? "Uploading..." : "Upload & Summarize"}
      </button>

      {error && (
        <p style={{ color: "red" }}>{error}</p>
      )}

      {loading && (
        <p style={{ color: "blue" }}>
          Processing file, please wait...
        </p>
      )}

      {result && (
        <div style={{ marginTop: "30px" }}>

          <h2>Latest Summary</h2>

          <div
            style={{
              border: "1px solid #ccc",
              padding: "15px",
              borderRadius: "5px"
            }}
          >
            <p>
              <strong>File:</strong> {result.filename}
            </p>

            <p>
              <strong>Summary:</strong>
            </p>

            <p>{result.summary}</p>

          </div>
        </div>
      )}

      <div style={{ marginTop: "30px" }}>

        <h2>Document History</h2>

        {documents.length === 0 ? (
          <p>No documents uploaded yet.</p>
        ) : (
          documents.map((doc) => (
            <div
              key={doc.id}
              style={{
                border: "1px solid #ddd",
                padding: "15px",
                marginBottom: "10px",
                borderRadius: "5px"
              }}
            >
              <h3>{doc.filename}</h3>
              <p>{doc.summary}</p>
            </div>
          ))
        )}

      </div>

    </div>
  );
}

export default App;