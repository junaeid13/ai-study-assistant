from fastapi import FastAPI, UploadFile, File
import PyPDF2

from vector_store import VectorStore
from llm_service import llm_service
from reg_service import rag_service
from content_service import content_service
from schemas import (
    EvaluationQuestion,
    ChatRequest,
    ChunkRequest,
    SearchRequest,
    KeyConceptRequest,
    KeyConceptResponse,
    StudyNoteRequest,
    StudyNoteResponse,
    QuizRequest,
    QuizResponse,
    FlashcardRequest,
    FlashcardResponse
)




app = FastAPI()

vector_store = VectorStore()



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
    summary = content_service.summarize(text)

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
def generate_flashcards_endpoint(request: FlashcardRequest):
    
    flashcards = content_service.generate_flashcards(request.text)

    return flashcards
 
#=====================================================
# Quiz generation endpoint
#=====================================================

@app.post(
    "/generate-quiz",
    response_model=list[QuizResponse]
)
def generate_quiz_endpoint(request: QuizRequest):
    quiz_questions = content_service.generate_quiz(request.text)
    return quiz_questions   


#=====================================================
# Study Note generation endpoint
#=====================================================
@app.post(
    "/generate-study-notes",
    response_model=list[StudyNoteResponse]
)
def generate_study_notes_endpoint(request: StudyNoteRequest):
    study_notes = content_service.generate_study_notes(request.text)
    return study_notes


#=====================================================
# Key Concept generation endpoint
#=====================================================

@app.post(
    "/generate-key-concepts",
    response_model=list[KeyConceptResponse]
)
def generate_key_concepts_endpoint(request: KeyConceptRequest):
    key_concepts = content_service.generate_key_concepts(request.text)
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
    response = rag_service.chat(
        document_id=request.document_id,
        question=request.question,
        top_k=request.top_k
    )
    return response

@app.post("/evaluate-retrieval")
def evaluate_retrieval(request: EvaluationQuestion):
    results = vector_store.search(
        document_id=request.document_id,
        query=request.question,
        top_k=request.top_k
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