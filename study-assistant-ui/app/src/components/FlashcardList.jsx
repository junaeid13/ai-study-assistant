import Flashcard from "./Flashcard";

function FlashcardList({flashcards}) {
    if(!flashcards || flashcards.length === 0) {
        return <p>No flashcards available.</p>;
    }

    return (
       <div style={{ padding: "20px" }}>
            <h2 style={{ textAlign: "center", marginBottom: "20px" }}>
                Generated Flashcards
            </h2>

            <div
                style={{
                    display: "grid",
                    gridTemplateColumns: "repeat(auto-fit, minmax(280px, 1fr))",
                    gap: "20px",
                    justifyItems: "center",
                }}
            >
                {flashcards.map((flashcard, index) => (
                    <Flashcard key={index} flashcard={flashcard} />
                ))}
            </div>
        </div>
    );
}

export default FlashcardList;