function SummaryCard({ result }) {
    if (!result) {
        return null; 
    }
    return (
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
    );
}

export default SummaryCard;