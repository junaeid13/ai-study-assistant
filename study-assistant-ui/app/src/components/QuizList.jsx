import {useState} from "react";
import QuizCard from "./QuizCard";
import {submitQuiz} from "../services/api";
import QuizResult from "./QuizResult";


function QuizList({
    quizzes,
    documentId,
}) {

    const [answers, setAnswers] = useState({});
    const [result, setResult] = useState(null);
    const [submitting, setSubmitting] = useState(false);
    const [submitError, setSubmitError] = useState(null);

    const handleAnswer = (
        quizId, 
        answer
    ) => {
        setAnswers(prev => ({
            ...prev,
            [quizId]: answer
        }));
    };

    const handleSubmit = async () => {
        const payload = {
            documentId,
            answers:quizzes.map(quiz => ({
                quizId: quiz.id,
                answer: answers[quiz.id] ?? null
            }))
        };

        setSubmitError("");
        setSubmitting(true);

        try {
            const response = await submitQuiz(payload);
            setResult(response);
        } catch (error) {
            console.error("Error submitting quiz:", error);
            setSubmitError("Failed to submit quiz. Please try again.");
        } finally {
            setSubmitting(false);
        }
    };

    if(!quizzes || quizzes.length === 0) {
        return <p>No quizzes available.</p>;
    }

    return (
        <div>
            {quizzes.map((quiz) => (
                <QuizCard
                    key={quiz.id}
                    quiz={quiz}
                    selectedAnswer={answers[quiz.id]}
                    onAnswerSelected={handleAnswer}     
                />
        ))}

        {submitError && (
        <p style={{color: "red"}}>{submitError}</p>
        )}
      
        <button
            onClick={handleSubmit}
            disabled={submitting}
            style={{marginTop: "20px"}}
        >
            {submitting ? "Submitting..." : "Submit Quiz"}
        </button>
        {result && (
            <QuizResult result={result} />
        )}  
        </div>
    );
}

export default QuizList;