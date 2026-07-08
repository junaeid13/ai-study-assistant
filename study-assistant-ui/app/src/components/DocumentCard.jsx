import {Link} from "react-router-dom";
function DocumentCard({ document }) {
    return (
        <div 
            style={{
                border: "1px solid #ddd",
                padding: "15px",
                borderRadius: "5px",
                marginBottom: "10px"
            }}
        >
            <h3>
                <Link to={`/documents/${document.id}`}>
                    {document.filename}
                </Link>
            </h3>
            <p>{document.summary}</p>
            <small>
                Uploaded: {" "}
                {new Date(document.uploadedAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
            </small>
        </div>
    );
}

export default DocumentCard;