function StudyNote({studyNote}) {
    return (
        <div 
            style={{
                border: "1px solid #ccc",
                borderRadius: "8px",
                padding: "20px",
                width: "100%",
                widthMax: "600px",
                boxShadow: "0 2px 4px rgba(0,0,0,0.1)",
                marginBottom: "10px",
                backgroundColor: "#fff"
            }}
        >
            <h3>{studyNote.title}</h3>
            <p
                style={{
                    whiteSpace: "pre-wrap",
                    wordWrap: "break-word",
                }}  
            >
                {studyNote.content}
            </p>
        </div>
    );
}

export default StudyNote;