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
                answer: answers[quiz.id] || null
            }))
        };

        console.log("Submitting quiz with payload:", payload);
        try {
            const response = await submitQuiz(payload);
            setResult(response);
        } catch (error) {
            console.error("Error submitting quiz:", error);
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
        <button
            onClick={handleSubmit}
            style={{marginTop: "20px"}}
        >
            Submit Quiz
        </button>
        {result && <QuizResult result={result} />}  
        </div>
    );
}

export default QuizList;