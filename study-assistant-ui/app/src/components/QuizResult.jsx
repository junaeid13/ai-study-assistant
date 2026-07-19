function QuizResult({ 
    result
}) {
    return (
        <div 
            style={{
                 border: "2px solid green",
                 marginTop: "30px",
                 padding: "20px",
                }}
        >
            <h2>Quiz Result</h2>
            <p>Total Questions: {" "}{result.total}</p>
            <p>Correct Answers: {" "}{result.correct}</p>
            <p>Score: {result.score}%</p>
        </div>
    );
}

export default QuizResult;