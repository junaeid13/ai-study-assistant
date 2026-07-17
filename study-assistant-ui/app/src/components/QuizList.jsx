import QuizCard from "./QuizCard";


function QuizList({quizzes}) {
    if(!quizzes || quizzes.length === 0) {
        return <p>No quizzes available.</p>;
    }

    return (
        <div>
            {quizzes.map((quiz, index) => (
            <QuizCard
                key={quiz.id ?? `${index}-${quiz.question}`}
                quiz={quiz}
            />
        ))}
        </div>
    );
}

export default QuizList;