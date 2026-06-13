from fastapi import FastAPI, UploadFile, File
import PyPDF2

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