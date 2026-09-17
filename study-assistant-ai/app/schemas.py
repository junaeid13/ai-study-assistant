from pydantic import BaseModel


class EvaluationQuestion(BaseModel):
    document_id: int
    question: str
    expected_chunk_index: int


class ChatRequest(BaseModel):
    document_id: int
    question: str
    top_k: int = 5


class ChunkRequest(BaseModel):
    document_id: int
    chunks: list[str]


class SearchRequest(BaseModel):
    document_id: int
    query: str
    top_k: int = 5


class KeyConceptRequest(BaseModel):
    text: str


class KeyConceptResponse(BaseModel):
    concept: str
    explanation: str


class StudyNoteRequest(BaseModel):
    text: str


class StudyNoteResponse(BaseModel):
    title: str
    content: str


class QuizRequest(BaseModel):
    text: str


class QuizResponse(BaseModel):
    question: str
    optionA: str
    optionB: str
    optionC: str
    optionD: str
    correctAnswer: str


class FlashcardRequest(BaseModel):
    text: str


class FlashcardResponse(BaseModel):
    question: str
    answer: str