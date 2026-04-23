from fastapi import FastAPI, UploadFile, File
import PyPDF2

# Summarization (simple version first)
from sumy.summarizers.lsa import LsaSummarizer
from sumy.parsers.plaintext import PlaintextParser
from sumy.nlp.tokenizers import Tokenizer

import nltk

nltk.download('punkt')
nltk.download('punkt_tab')

app = FastAPI()

'''
Utility: Extract text from PDF
'''
def extract_text(file):
    reader = PyPDF2.PdfReader(file)
    text = ""

    for page in reader.pages:
        extracted = page.extract_text()
        if extracted:
            text += extracted

    return text

'''
Utility: Summarize text
'''
def summarize_text(text):
    # limit text size for performance
    text = text[:2000]

    parser = PlaintextParser.from_string(text, Tokenizer("english"))
    summarizer = LsaSummarizer()

    summary = summarizer(parser.document, 5)

    return " ".join(str(sentence) for sentence in summary)



'''
Endpoint: Health Check
'''
@app.get("/")
def home():
    return {"message": "Python backend is running"}


'''
Endpoint: Upload a file 
'''
@app.post("/upload-pdf")
async def upload_pdf(file: UploadFile = File(...)):
    text = extract_text(file.file)

    return {
        "filename": file.filename,
        "text_preview": text[:500]
    }


'''
Endpoint: Text Summarizer
'''
@app.post("/summarize-pdf")
async def summarize_pdf(file: UploadFile = File(...)):
    text = extract_text(file.file)
    summary = summarize_text(text)

    return {
        "filename": file.filename,
        "summary": summary
    }