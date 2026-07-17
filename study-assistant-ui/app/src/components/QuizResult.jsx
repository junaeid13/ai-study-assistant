function QuizResult({ 
    total,
    correct
}) {
    return (
        <div style={{marginTop: "20px"}}>
            <h2>Quiz Result</h2>
            <p>Total Questions: {total}</p>
            <p>Correct Answers: {correct}</p>
            <p>Score: {(correct / total) * 100}%</p>
        </div>
    );
}

export default QuizResult;