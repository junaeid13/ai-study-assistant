import {useState} from "react";
import QuizOption from "./QuizOption";

function QuizCard({quiz}) {
    const [selected, setSelected] = useState(null);
    const [submitted, setSubmitted] = useState(false);

    const correct = selected === quiz.correctAnswer;

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
                options={quiz.optionA}
                selected={selected === quiz.optionA}
                onClick={()=> setSelected(quiz.optionA)}
            />
            <QuizOption
                options={quiz.optionB}
                selected={selected === quiz.optionB}
                onClick={()=> setSelected(quiz.optionB)}
            />
            <QuizOption
                options={quiz.optionC}
                selected={selected === quiz.optionC}
                onClick={()=> setSelected(quiz.optionC)}
            />
            <QuizOption
                options={quiz.optionD}
                selected={selected === quiz.optionD}
                onClick={()=> setSelected(quiz.optionD)}
            />
            <button
                style={{marginTop: "20px"}}
                disabled= {!selected}
                onClick={() => setSubmitted(true)}
            >
                Check Answer
            </button>

            {submitted && (
                <div style={{marginTop: "15px"}}>
                    {correct ? (
                        <span style={{color: "green"}}>Correct!</span>
                    ) : (
                        <span style={{color: "red"}}>
                            Incorrect! The correct answer is: {quiz.correctAnswer}
                        </span>
                    )}
                </div>
            )}      
        </div>
    );
}

export default QuizCard;