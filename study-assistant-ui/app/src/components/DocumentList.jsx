import DocumentCard from "./DocumentCard";

function DocumentList({ documents }) {
    if(!documents || documents.length === 0) {
        return (
            <p>No documents uploaded yet.</p>
        );
    }
    return (
        <div>
            <h2>Document History</h2>
            {documents.map((doc) => (
                <DocumentCard 
                    key={doc.id} 
                    document={doc} 
                    />
                ))
            }
        </div>
    );
}

export default DocumentList;