
import QuizOption from "./QuizOption";

function QuizCard({
    quiz,
    selectedAnswer,
    onAnswerSelected,
}) {


    return (
        <div
            style={{
                border: "1px solid gray",
                padding: "10px",
                marginBottom: "20px",
            }}
        >
            <h3>{quiz.question}</h3>
            <QuizOption
                option={quiz.optionA}
                selected={selectedAnswer === quiz.optionA}
                onClick={()=> onAnswerSelected(
                    quiz.id,
                    quiz.optionA
                )}
            />
            <QuizOption
                option={quiz.optionB}
                selected={selectedAnswer === quiz.optionB}
                onClick={()=> onAnswerSelected(
                    quiz.id,
                    quiz.optionB
                )}
            />
            <QuizOption
                option={quiz.optionC}
                selected={selectedAnswer === quiz.optionC}
                onClick={()=> onAnswerSelected(
                    quiz.id,
                    quiz.optionC
                )}
            />
            <QuizOption
                option={quiz.optionD}
                selected={selectedAnswer === quiz.optionD}
                onClick={()=> onAnswerSelected(
                    quiz.id,
                    quiz.optionD
                )}
            />    
        </div>
    );
}

export default QuizCard;