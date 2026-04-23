from fastapi import FastAPI, UploadFile, File
import PyPDF2

app = FastAPI()

def extract_text(file):
    reader = PyPDF2.PdfReader(file)
    text = ""
    for page in reader.pages:
        text += page.extract_text()
    return text


@app.post("/upload-pdf")
async def upload_pdf(file: UploadFile = File(...)):
    text = extract_text(file.file)

    return {
        "filename": file.filename,
        "text_preview": text[:500]
    }