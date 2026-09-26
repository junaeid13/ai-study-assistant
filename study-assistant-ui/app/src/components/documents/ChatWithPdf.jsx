import {useState} from "react";
import {chatWithDocument} from "../../services/api";

function ChatWithPdf({documentId}) {
    const [question, setQuestion] = useState("");
    const [messages, setMessage] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    const handleSubmit = async (event) => {
        event.preventDefault();
        
        if(!question.trim()) {
            return;
        }

        const userQuestion = question.trim();

        setMessage((previousMessages) =>[ 
            ...previousMessages,
            {
                role: "user",
                content: userQuestion,
            },
        ]);

        setQuestion("");
        setError("");
        setLoading(true);

        try {
            const response = await chatWithDocument(
                documentId, 
                userQuestion
            );
            setMessage((previousMessages) => [
                ...previousMessages, 
                {
                role: "assistant",
                content: response.answer,
                sources: response.sources || [],
                },
            ]);
        } catch (err) {
            console.error("Error during chat:", err);
            setError("An error occurred while processing your request.");
        } finally {
            setLoading(false);
        }
    };

      return (
        <div
            style={{
                maxWidth: "800px",
                margin: "40px auto",
                padding: "20px",
            }}
        >
            <h2>Chat With PDF</h2>

            <div
                style={{
                    border: "1px solid #ccc",
                    borderRadius: "8px",
                    padding: "20px",
                    minHeight: "300px",
                    marginBottom: "20px",
                }}
            >
                {messages.length === 0 && (
                    <p>
                        Ask a question about this document.
                    </p>
                )}

                {messages.map((message, index) => (
                    <div
                        key={index}
                        style={{
                            marginBottom: "20px",
                            padding: "12px",
                            borderRadius: "8px",
                            backgroundColor:
                                message.role === "user"
                                    ? "#f0f0f0"
                                    : "#ffffff",
                        }}
                    >
                        <strong>
                            {message.role === "user"
                                ? "You"
                                : "AI"}
                        </strong>

                        <p
                            style={{
                                whiteSpace: "pre-wrap",
                                lineHeight: "1.6",
                            }}
                        >
                            {message.content}
                        </p>

                        {message.sources &&
                            message.sources.length > 0 && (
                                <div>
                                    <strong>
                                        Sources
                                    </strong>

                                    {message.sources.map(
                                        (source, sourceIndex) => (
                                            <p
                                                key={sourceIndex}
                                                style={{
                                                    fontSize: "14px",
                                                    color: "#666",
                                                }}
                                            >
                                                Chunk{" "}
                                                {source.metadata
                                                    ?.chunkIndex}
                                            </p>
                                        )
                                    )}
                                </div>
                            )}
                    </div>
                ))}

                {loading && (
                    <p>
                        AI is thinking...
                    </p>
                )}
            </div>

            {error && (
                <p style={{ color: "red" }}>
                    {error}
                </p>
            )}

            <form onSubmit={handleSubmit}>
                <div
                    style={{
                        display: "flex",
                        gap: "10px",
                    }}
                >
                    <input
                        type="text"
                        value={question}
                        onChange={(event) =>
                            setQuestion(event.target.value)
                        }
                        placeholder="Ask something about this PDF..."
                        disabled={loading}
                        style={{
                            flex: 1,
                            padding: "12px",
                            borderRadius: "6px",
                            border: "1px solid #ccc",
                        }}
                    />

                    <button
                        type="submit"
                        disabled={
                            loading ||
                            !question.trim()
                        }
                    >
                        {loading
                            ? "Thinking..."
                            : "Ask"}
                    </button>
                </div>
            </form>
        </div>
    );
}
export default ChatWithPdf;
