import StudyNote from './StudyNote';

function StudyNoteList({studyNotes}) {
    if(!studyNotes || studyNotes.length === 0) {
        return <p>No study notes available.</p>;
    }

    return (
        <div style={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
            <h2
                style={{
                    marginBottom: "20px",
                    fontSize: "24px",
                    fontWeight: "bold",
                }}
            >
                Study Notes
            </h2>
            <div
                style={{
                    display: "flex",
                    flexDirection: "column",
                    alignItems: "center",
                    width: "100%",
                    maxWidth: "600px",
                }}
            >
                {studyNotes.map((studyNote) => (
                    <StudyNote 
                        key={studyNote.id} 
                        studyNote={studyNote} 
                    />
                ))} 
            </div>
        </div>
    );
}

export default StudyNoteList;