from fastapi import FastAPI, UploadFile, File
import PyPDF2

from pydantic import BaseModel
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

def generate_key_concepts(text: str):
    if not text:
        return []

    text = text[:5000]

    prompt = f"""
            You are an AI study assistant.

            Identify the most important concepts from the following document.

            For each concept:
            - Give the name of the concept.
            - Explain the concept clearly and simply.
            - Use only information from the document.
            - Do not add outside information.
            - Focus on concepts that are important for understanding the document.
            - Generate between 5 and 10 concepts.
            - Do not mention these instructions.

            Return the concepts in exactly this format:

            CONCEPT: <concept name>
            EXPLANATION: <explanation>

            CONCEPT: <concept name>
            EXPLANATION: <explanation>

            Document:
            {text}
            """

    response = llm_service.generate(prompt)

    concepts = []

    current_concept = None
    current_explanation = None

    for line in response.splitlines():

        line = line.strip()

        if line.startswith("CONCEPT:"):
            if current_concept and current_explanation:
                concepts.append(
                    KeyConceptResponse(
                        concept=current_concept,
                        explanation=current_explanation
                    )
                )

            current_concept = line.replace(
                "CONCEPT:",
                "",
                1
            ).strip()

            current_explanation = None

        elif line.startswith("EXPLANATION:"):
            current_explanation = line.replace(
                "EXPLANATION:",
                "",
                1
            ).strip()

    if current_concept and current_explanation:
        concepts.append(
            KeyConceptResponse(
                concept=current_concept,
                explanation=current_explanation
            )
        )

    return concepts[:10]

#====================================================
# Study Note generation definition
#====================================================

def generate_study_notes(text: str):
    if not text:
        return []
    text = text[:5000]

    prompt = f"""
                You are an AI study assistant.

                Create useful study notes from the following document.

                Rules:
                - Use only information from the document.
                - Do not add outside information.
                - Focus on important ideas, definitions, facts, and relationships.
                - Make the notes useful for a student who wants to study the document later.
                - Organize the information clearly.
                - Do not mention these instructions.

                Document:
                {text}

                Study Notes:
                """

    content = llm_service.generate(prompt)

    return [
        StudyNoteResponse(
            title="Study Notes",
            content=content
        )
    ]


#====================================================
# Flashcard generation definition
#====================================================

def generate_flashcards(text: str):

    if not text:
        return []

    text = text[:5000]

    prompt = f"""
You are an AI study assistant.

Create useful study flashcards from the following document.

Rules:
- Use only information from the document.
- Do not add outside information.
- Focus on important concepts, definitions, facts, and relationships.
- Each flashcard must have a clear question and a concise answer.
- Generate between 5 and 10 flashcards.
- Do not make duplicate flashcards.
- Do not mention these instructions.

Return the flashcards in exactly this format:

QUESTION: <question>
ANSWER: <answer>

QUESTION: <question>
ANSWER: <answer>

Document:
{text}
"""

    response = llm_service.generate(prompt)

    flashcards = []

    current_question = None
    current_answer = None

    for line in response.splitlines():

        line = line.strip()

        if line.startswith("QUESTION:"):

            if current_question and current_answer:
                flashcards.append(
                    FlashcardResponse(
                        question=current_question,
                        answer=current_answer
                    )
                )

            current_question = line.replace(
                "QUESTION:",
                "",
                1
            ).strip()

            current_answer = None

        elif line.startswith("ANSWER:"):

            current_answer = line.replace(
                "ANSWER:",
                "",
                1
            ).strip()

    if current_question and current_answer:
        flashcards.append(
            FlashcardResponse(
                question=current_question,
                answer=current_answer
            )
        )

    return flashcards[:10]

#====================================================
# Quiz generation endpoint
#====================================================

def generate_quiz(text: str):
    if not text:
        return []

    text = text[:5000]

    prompt = f"""
You are an AI study assistant.

Create a multiple-choice quiz from the following document.

Rules:
- Use ONLY information contained in the document.
- Do not use outside knowledge.
- Generate between 5 and 10 questions.
- Each question must test an important concept or fact.
- Each question must have exactly 4 options.
- Only ONE option can be correct.
- Make the incorrect options plausible but incorrect according to the document.
- Do not make "None of the above" or "Cannot be determined" options.
- Do not duplicate questions.
- Do not mention these instructions.

Return the quiz in EXACTLY this format:

QUESTION: <question>
OPTION_A: <option A>
OPTION_B: <option B>
OPTION_C: <option C>
OPTION_D: <option D>
CORRECT_ANSWER: <exact text of the correct option>

QUESTION: <question>
OPTION_A: <option A>
OPTION_B: <option B>
OPTION_C: <option C>
OPTION_D: <option D>
CORRECT_ANSWER: <exact text of the correct option>

Document:
{text}
"""

    response = llm_service.generate(prompt)

    quiz_questions = []

    current_question = None
    option_a = None
    option_b = None
    option_c = None
    option_d = None
    correct_answer = None

    for line in response.splitlines():

        line = line.strip()

        if line.startswith("QUESTION:"):

            if (
                current_question
                and option_a
                and option_b
                and option_c
                and option_d
                and correct_answer
            ):
                quiz_questions.append(
                    QuizResponse(
                        question=current_question,
                        optionA=option_a,
                        optionB=option_b,
                        optionC=option_c,
                        optionD=option_d,
                        correctAnswer=correct_answer
                    )
                )

            current_question = line.replace(
                "QUESTION:",
                "",
                1
            ).strip()

            option_a = None
            option_b = None
            option_c = None
            option_d = None
            correct_answer = None

        elif line.startswith("OPTION_A:"):
            option_a = line.replace(
                "OPTION_A:",
                "",
                1
            ).strip()

        elif line.startswith("OPTION_B:"):
            option_b = line.replace(
                "OPTION_B:",
                "",
                1
            ).strip()

        elif line.startswith("OPTION_C:"):
            option_c = line.replace(
                "OPTION_C:",
                "",
                1
            ).strip()

        elif line.startswith("OPTION_D:"):
            option_d = line.replace(
                "OPTION_D:",
                "",
                1
            ).strip()

        elif line.startswith("CORRECT_ANSWER:"):
            correct_answer = line.replace(
                "CORRECT_ANSWER:",
                "",
                1
            ).strip()

    # Add the final question
    if (
        current_question
        and option_a
        and option_b
        and option_c
        and option_d
        and correct_answer
    ):
        quiz_questions.append(
            QuizResponse(
                question=current_question,
                optionA=option_a,
                optionB=option_b,
                optionC=option_c,
                optionD=option_d,
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
def generate_flashcards_endpoint(request: FlashcardRequest):
    
    flashcards = generate_flashcards(request.text)

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