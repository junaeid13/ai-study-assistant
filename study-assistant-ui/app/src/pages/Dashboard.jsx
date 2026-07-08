import { useEffect, useState } from "react";
import api, {getCurrentUser} from "../services/api";
import { useNavigate } from "react-router-dom";
import { Link } from "react-router-dom";
import Header from "../components/Header";

function Dashboard() {

    const [file, setFile] = useState(null);
    const [result, setResult] = useState(null);
    const [documents, setDocuments] = useState([]);

    const [loading, setLoading] = useState(false); 
    const [error, setError] = useState(""); 
    const navigate = useNavigate(); 
    const [user, setUser] = useState(null);


    const loadDocuments = async () => {
        try {
            const response = await api.get(
                "http://localhost:8080/api/documents"
            );

            setDocuments(Array.isArray(response.data) ? response.data : []);

        } catch (err) {
            console.error(err);
            setDocuments([]);
            setError("Failed to load documents");
        }
    };

    const fetchUser = async () => {
        try {
        const user = await getCurrentUser();
            setUser(user);
            console.log("Current user:", user);
        } catch (err) {
            console.error("Error fetching current user:", err);
            setError("Failed to fetch current user");
        }
    };

    const logout = () => {

        localStorage.removeItem("token");

        navigate("/login");
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

            const response = await api.post(
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
        fetchUser();
    }, []);

    return (
        <div style={{ padding: "20px" }}>
        <Header 
            username={user ? user.username : ""} 
            onLogout={logout} 
        />

        <hr />

        {/* UPLOAD SECTION */}
        <div style={{ marginTop: "20px" }}>

        <input
        type="file"
        accept=".pdf"
        onChange={(e) => setFile(e.target.files[0])}
        />

        <button
        onClick={uploadFile}
        disabled={loading || !file}
        style={{ marginLeft: "10px" }}
        >
        {loading ? "Processing..." : "Upload & Summarize"}
        </button>

        </div>

        {/* STATUS */}
        {error && (
        <p style={{ color: "red" }}>{error}</p>
        )}

        {loading && (
        <p style={{ color: "blue" }}>
        Processing file, please wait...
        </p>
        )}

        {/* RESULT */}
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

        {/* HISTORY */}
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
            <Link to={`/documents/${doc.id}`}>
                {doc.filename}
            </Link>
            <p>{doc.summary}</p>
            </div>
            ))
            )}

        </div>

        </div>
    );
}

export default Dashboard;
