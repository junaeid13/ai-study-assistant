from fastapi import FastAPI, UploadFile, File
import PyPDF2

from pydantic import BaseModel
from sumy.summarizers.lsa import LsaSummarizer
from sumy.parsers.plaintext import PlaintextParser
from sumy.nlp.tokenizers import Tokenizer
from vector_store import VectorStore
from llm_service import llm_service





app = FastAPI()

vectore_store = VectorStore()




#====================================================
# Request model for Evaluation
#====================================================

class EvaluationQuestion(BaseModel):
    document_id: int
    question: str
    expected_chunk_index: int

#====================================================
# Request model for Chat
#====================================================

class ChatRequest(BaseModel):
    document_id: int 
    question: str
    top_k: int = 5


#====================================================
# Request and Search model for Vector Store
#====================================================
class ChunkRequest(BaseModel):
    document_id: int
    chunks: list[str]


class SearchRequest(BaseModel):
    document_id: int
    query: str
    top_k: int = 5

# =====================================================
#  Key Concept: Request and Response model
# =====================================================

class KeyConceptRequest(BaseModel):
    text:str

class KeyConceptResponse(BaseModel):
    concept: str
    explanation: str

# =====================================================
# Utility: Extract text from PDF
# =====================================================
def extract_text(file):
    reader = PyPDF2.PdfReader(file)
    text = ""

    for page in reader.pages:
        extracted = page.extract_text()
        if extracted:
            text += extracted

    return text

#====================================================
# Study Note request and Response model
#====================================================

class StudyNoteRequest(BaseModel):
    text: str

class StudyNoteResponse(BaseModel):
    title: str
    content: str
#====================================================
# Quiz request and Response model
#====================================================

class QuizRequest(BaseModel):
    text: str

class QuizResponse(BaseModel):
    question: str
    optionA: str
    optionB: str
    optionC: str
    optionD: str
    correctAnswer: str

#====================================================
# Flashcard request and Response model
#====================================================
class FlashcardRequest(BaseModel):
    text: str

class FlashcardResponse(BaseModel):
    question: str
    answer: str


# =====================================================
# Utility: Summarize text
# =====================================================
def summarize_text(text):

    if not text:
        return "No text found in document."

    # limit size for performance
    text = text[:5000]

    prompt = f"""
                You are an AI study assistant.

                Create a clear and concise summary of the following document.

                Rules:
                - Use only information from the document.
                - Do not add outside information.
                - Focus on the main ideas and important facts.
                - Make the summary easy for a student to understand.
                - Do not mention these instructions.

                Document:
                {text}

                Summary:
        """

    return llm_service.generate(prompt)


# ===================================================
# Key concept generation definition
# ===================================================

def generate_key_concepts(text):
    sentences = [
        s.strip()
        for s in text.split(".")
        if s.strip()
    ]

    concepts = []

    for sentence in sentences[:10]:
        words = sentence.split()

        if len(words) <5:
            continue

        concepts.append(
            KeyConceptResponse(
                concept = words[0],
                explanation = sentence
            )
        )

    return concepts

#====================================================
# Study Note generation definition
#====================================================

def generate_study_notes(text: str):
    if not text:
        return []
    text = text[:5000]

    parser = PlaintextParser.from_string(
        text,
        Tokenizer("english")
    )
    summarizer = LsaSummarizer()
    summary_sentences = summarizer(parser.document, 8)

    return [
        StudyNoteResponse(
            title=f"Study Notes",
            content=" ".join(str(sentence) for sentence in summary_sentences)
        )
    ]

#====================================================
# Quiz generation endpoint
#====================================================

def generate_quiz(text: str):
    sentences = [
        s.strip()
        for s in text.split(".")
        if s.strip()
    ]

    quiz_questions = []

    for sentence in sentences:
        words = sentence.split()
        if len(words) < 5:
            continue

        keyword = words[0]  

        question = f"What is the meaning of: '{keyword}'?"
        options = [
            sentence, 
            "None of the above",
            "Not mentioned in the document", 
            "Cannot be determined from the context"
            ]
        correct_answer = sentence

        quiz_questions.append(
            QuizResponse(
                question=question,
                optionA=options[0],
                optionB=options[1],
                optionC=options[2],
                optionD=options[3],
                correctAnswer=correct_answer
            )
        )

    return quiz_questions[:10]

# =====================================================
# Health check
# =====================================================
@app.get("/")
def home():
    return {"message": "Python backend is running"}


# =====================================================
# Upload PDF (debug endpoint)
# =====================================================
@app.post("/upload-pdf")
async def upload_pdf(file: UploadFile = File(...)):

    text = extract_text(file.file)

    return {
        "filename": file.filename,
        "text_preview": text[:500]
    }


# =====================================================
# Summarize PDF (MAIN endpoint used by Spring)
# =====================================================
@app.post("/summarize-pdf")
async def summarize_pdf(file: UploadFile = File(...)):

    text = extract_text(file.file)
    summary = summarize_text(text)

    return {
        "text": text,
        "summary": summary
    }


#======================================================
# Flashcard generation endpoint
#=====================================================
@app.post(
        "/generate-flashcards",
        response_model=list[FlashcardResponse]
        )
def generate_flashcards(request: FlashcardRequest):
    sentences = [
        s.strip()
        for s in request.text.split(".")
        if s.strip()
    ]

    flashcards = []

    for sentence in sentences[:10]:
        words = sentence.split()
        if len(words) <4:
            continue
        
        flashcards.append(
            FlashcardResponse(
                question=f"What is the meaning of: '{sentence}'?",
                answer=sentence
            )   
        )
    
    return flashcards
 
#=====================================================
# Quiz generation endpoint
#=====================================================

@app.post(
    "/generate-quiz",
    response_model=list[QuizResponse]
)
def generate_quiz_endpoint(request: QuizRequest):
    quiz_questions = generate_quiz(request.text)
    return quiz_questions   


#=====================================================
# Study Note generation endpoint
#=====================================================
@app.post(
    "/generate-study-notes",
    response_model=list[StudyNoteResponse]
)
def generate_study_notes_endpoint(request: StudyNoteRequest):
    study_notes = generate_study_notes(request.text)
    return study_notes


#=====================================================
# Key Concept generation endpoint
#=====================================================

@app.post(
    "/generate-key-concepts",
    response_model=list[KeyConceptResponse]
)
def generate_key_concepts_endpoint(request: KeyConceptRequest):
    key_concepts = generate_key_concepts(request.text)
    return key_concepts 

#=====================================================
# Vector Store endpoints
#=====================================================

@app.post("/create-embeddings")
def create_embeddings(request: ChunkRequest):
    vector_store.add_chunks(
        document_id=request.document_id,
        chunks=request.chunks
    )
    return {
        "message": "Embeddings created successfully.",
        "document_id": request.document_id,
        "chunks_count": len(request.chunks)
        }  

@app.post("/semantic-search")
def semantic_search(request: SearchRequest):
    results = vector_store.search(
        document_id=request.document_id,
        query=request.query,
        top_k=request.top_k
    )
    return results

#====================================================
# Chat with Document endpoint
#====================================================

@app.post("/chat-with-document")
def chat_with_document(request: ChatRequest):
    results = vector_store.search(
        document_id=request.document_id,
        query=request.question,
        top_k=request.top_k
    )

    if not results:
        return {
            "answer": "No relevant information found in the document.",
            "sources": []
        }

    expanded_results = vector_store.expand_context(
        document_id=request.document_id,
        search_results=results,
        neighbor_count=1
    )

    context = "\n\n".join(
            f"[Chunk {result['metadata']['chunkIndex']}]\n"
            f"{result['content']}"
        for result in expanded_results
        )

    answer = llm_service.generate_answer(
        question=request.question,
        context=context
    )
    return {
        "answer": answer,
        "sources": results
    }

@app.post("/evaluate-retrieval")
def evaluate_retrieval(request: EvaluationQuestion):
    results = vector_store.search(
        document_id=request.document_id,
        query=request.question,
        top_k=5
    )

    if not results:
        return {
            "message": "No relevant information found in the document.",
            "expected_chunk_index": request.expected_chunk_index,
            "retrieved_chunk_indices": []
        }

    retrieved_chunk_indices = [
        result['metadata']['chunkIndex'] for result in results
    ]

    is_expected_chunk_retrieved = request.expected_chunk_index in retrieved_chunk_indices

    return {
        "message": "Evaluation completed.",
        "expected_chunk_index": request.expected_chunk_index,
        "retrieved_chunk_indices": retrieved_chunk_indices,
        "is_expected_chunk_retrieved": is_expected_chunk_retrieved
    }