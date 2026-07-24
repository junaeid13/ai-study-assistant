from fastapi import FastAPI, UploadFile, File
import PyPDF2

from pydantic import BaseModel
from sumy.summarizers.lsa import LsaSummarizer
from sumy.parsers.plaintext import PlaintextParser
from sumy.nlp.tokenizers import Tokenizer



app = FastAPI()

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
    text = text[:3000]

    parser = PlaintextParser.from_string(text, Tokenizer("english"))
    summarizer = LsaSummarizer()

    summary_sentences = summarizer(parser.document, 5)

    return " ".join(str(sentence) for sentence in summary_sentences)


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