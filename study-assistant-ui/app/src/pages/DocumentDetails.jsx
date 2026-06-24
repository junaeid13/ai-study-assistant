import { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import { getDocumentById } from "../services/api";

function DocumentDetails() {

  const { id } = useParams();

  const [document, setDocument] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {

    const fetchDocument = async () => {

      try {

        const data = await getDocumentById(id);

        setDocument(data);

      } catch (err) {

        console.error(err);
        setError("Failed to load document");

      } finally {

        setLoading(false);
      }
    };

    fetchDocument();

  }, [id]);

  if (loading) {
    return <p>Loading document...</p>;
  }

  if (error) {
    return <p>{error}</p>;
  }

  if (!document) {
    return <p>Document not found.</p>;
  }

  return (
    <div style={{ padding: "20px" }}>

      <Link to="/">
        ← Back to Dashboard
      </Link>

      <h1>{document.filename}</h1>

      <p>
        <strong>ID:</strong> {document.id}
      </p>

      <p>
        <strong>Uploaded:</strong>{" "}
        {new Date(document.uploadedAt).toLocaleString()}
      </p>

      <hr />

      <h2>Summary</h2>

      <p>{document.summary}</p>

    </div>
  );
}

export default DocumentDetails;