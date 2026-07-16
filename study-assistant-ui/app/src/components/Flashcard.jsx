import {useState} from "react";

function Flashcard({flashcard}) {
    const [showAnswer, setShowAnswer] = useState(false);

    return (
        <div 
        className="flashcard"
        style={{
            border: "1px solid #ccc",
            borderRadius: "8px",
            padding: "20px",
            margin: "10px",
            width: "300px",
            textAlign: "center",
            boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)",
        }}
        
        >
           <h3>Question</h3>
           <p>{flashcard.question}</p>
           {showAnswer && (
               <>
                   <h3>Answer</h3>
                   <p>{flashcard.answer}</p>
               </>
           )}

            <button 
                onClick={() => setShowAnswer(!showAnswer)}
                style={{
                    marginTop: "10px"}}
            >
                {showAnswer ? "Show Question" : "Show Answer"}
            </button>
        </div>
    );
}

export default Flashcard;