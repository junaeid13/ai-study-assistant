import { useEffect, useState } from "react";
import api, {getCurrentUser} from "../services/api";
import { useNavigate } from "react-router-dom";
import { Link } from "react-router-dom";
import Header from "../components/Header";
import LoadingSpinner from "../components/LoadingSprinner";
import UploadForm from "../components/UploadForm";
import SummaryCard from "../components/SummaryCard";
import DocumentList from "../components/DocumentList";

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

    if(loading && !result) {
        return <LoadingSpinner />;
    }

    return (
        <div style={{ padding: "20px" }}>
            <Header 
                username={user ? user.username : ""} 
                onLogout={logout} 
            />

            <hr />

            {/* UPLOAD SECTION */}
            <UploadForm
                file={file}
                setFile={setFile}
                uploadFile={uploadFile}
                loading={loading}
            />


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
            <SummaryCard result={result} />

            {/* HISTORY */}
            <div style={{ marginTop: "30px" }}>

            <DocumentList documents={documents} />
            

            </div>

        </div>
    );
}

export default Dashboard;
