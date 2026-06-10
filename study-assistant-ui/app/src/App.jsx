import { useEffect, useState } from "react"; 
import axios from "axios";

function App() {

  const [file, setFile] = useState(null);

  const [result, setResult] = useState("");

  const [documents, setDocuments] = useState([]);

  const loadDocuments = async () => {
    try {

      const response = await axios.get(
        "http://localhost:8080/api/documents"
      );

      setDocuments(response.data);

    } catch (error) {
      console.error(error);
    }
  };

  const uploadFile = async () => {

    if (!file) {
      alert("Please select a PDF file");
      return;
    }

    try {

      const formData = new FormData();

      formData.append("file", file);

      const response = await axios.post(
        "http://localhost:8080/api/summarize",
        formData
      );

      setResult(response.data);

      await loadDocuments();

    } catch (error) {

      console.error(error);

      alert("Upload failed");

    }
  };


  useEffect(() => {
    loadDocuments();
  }, []);

  return (
    <div style={{ padding: 20 }}>

      <h1>AI Study Assistant</h1>


      <input
        type="file"
        accept=".pdf"
        onChange={(e) => setFile(e.target.files[0])}
      />

      <button onClick={uploadFile}>
        Upload & Summarize
      </button>


      {result && (
        <div style={{ marginTop: 20 }}>

          <h2>Latest Summary</h2>

          <div
            style={{
              border: "1px solid #ccc",
              padding: "10px",
              borderRadius: "5px"
            }}
          >

            <p>{result.summary}</p>

          </div>

        </div>
      )}


      <div style={{ marginTop: 30 }}>

        <h2>Document History</h2>

        {documents.length === 0 ? (

          <p>No documents uploaded yet.</p>

        ) : (

          documents.map((doc) => (

            <div
              key={doc.id}
              style={{
                border: "1px solid #ccc",
                padding: "10px",
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