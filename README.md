#  AI Study Assistant

> An AI-powered study platform that helps students learn from PDF documents by generating summaries, flashcards, quizzes, and personalized study materials.

---

##  Overview

AI Study Assistant is a full-stack microservice application designed to improve the learning experience from academic documents.

Users can upload PDF files and automatically generate:

-  Generate documents summaries
-  Extract key concepts
-  Generate flashcards
-  Generate multiple-choice quizzes
-  Generate study notes
-  Chat with their document using Retrieval-Augmented Generation (RAG)

The project follows a **production-oriented microservice architecture** with separate frontend, Java backend, and Python AI services.

---

#  Architecture

```text
                React (Frontend)
                        │
                        │ REST API
                        ▼
          Spring Boot Backend (Java)
                        │
        ┌───────────────┴───────────────┐
        │                               │
        ▼                               ▼
   H2 / PostgreSQL              FastAPI AI Service
                                    │
                         ┌──────────┼──────────┐
                         │          │          │
                         ▼          ▼          ▼
                    PDF Processing  AI       RAG
                                    │          │
                                    │          ▼
                                    │       ChromaDB
                                    │          │
                                    ▼          │
                                  Ollama ◄─────┘
```
# Document Chat/RAG Flow
```text
User Question
      │
      ▼
React
      │
      ▼
Spring Boot
      │
      ▼
FastAPI
      │
      ▼
Question Embedding
      │
      ▼
ChromaDB
      │
      ▼
Relevant Document Chunks
      │
      ▼
Context + Question
      │
      ▼
Ollama LLM
      │
      ▼
AI Answer + Sources
```
---



#  Tech Stack
## Frontend
- React
- Vite
- React Router
- Axios
- JavaScript
## Backend
- Java
- Spring Boot 3
- Spring Security
- Spring Data JPA
- JWT Authentication
- Maven
## AI Service
- Python
- FastAPI
- PyPDF2
- Sumy
- NLTK
- Sentence Transformers
- Ollama
## RAG / Vector Search
- ChromaDB
- Sentence Transformers
- Retrieval-Augmented Generation (RAG)
## LLM
- Ollama
- Llama 3.2
## Database 
### Development
- H2 Database
### Production
- PostgreSQL

---

#  Project Structure

```text
ai-study-assistant/

│
├── study-assistant-ui/          # React Frontend
│
├── study-assistant-backend/     # Spring Boot
│
├── study-assistant-python/      # FastAPI AI Service
│
└── README.md
```
# Backend Responsibilities
The Spring Boot service is responsible for:
- REST API
- Authentication and authorization
- JWT-based security
- User management
- Document management
- Study material management
- Database persistence
- Document ownership validation
- Communication with the AI service
# AI Service Responsibilities
The FastAPI service is responsible for:
- PDF text extraction
- Text summarization
- Document chunking
- Embedding generation
- Vector search
- RAG context retrieval
- LLM interaction
- AI-generated study content
# RAG Architecture
The document chat functionality uses a Retrieval-Augmented Generation approach.
```text
PDF
 │
 ▼
Extract Text
 │
 ▼
Split into Chunks
 │
 ▼
Generate Embeddings
 │
 ▼
ChromaDB
 │
 │
 │       User Question
 │             │
 │             ▼
 │       Generate Embedding
 │             │
 └─────────────┤
               ▼
        Similarity Search
               │
               ▼
        Relevant Chunks
               │
               ▼
          Build Context
               │
               ▼
            Ollama
               │
               ▼
          Final Answer
```
The LLM is instructed to use only information retrieved from the document context. When the required information cannot be found, the system responds that the information is not available in the document.
# API Communication
The application uses REST communication between services:
```text
React
  │
  ▼
Spring Boot REST API
  │
  ▼
FastAPI REST API
  │
  ▼
AI / RAG Processing
```
The Spring Boot backend acts as the main backend/API layer for the frontend while the Python service handles AI-specific processing.

# Current Status
### Completed
- [x] React frontend
- [x] Spring Boot backend
- [x] JWT authentication
- [x] PDF upload
- [x] PDF text extraction
- [x] AI document summarization
- [x] Document chunking
- [x] Embedding generation
- [x] Vector storage with ChromaDB
- [x] Semantic document search
- [x] Flashcard generation
- [x] Quiz generation
- [x] Study note generation
- [x] Key concept generation
- [x] Chat with PDF
- [x] RAG-based document question answering
- [x] Document source references
- [x] Backend exception handling
- [x] API consistency
- [x] Document ownership/security checks

### Remaining
- [ ] Improve local LLM response performance
- [ ] Automated backend tests
- [ ] Automated AI-service tests
- [ ] Docker integration
- [ ] PostgreSQL production configuration
- [ ] Production deployment
- [ ] Production configuration and environment management

# Development
The application currently consists of three services:
```text
React Frontend
Spring Boot Backend
FastAPI AI Service
```
The AI service additionally requires:
- Ollama
- Llama 3.2
- ChromaDB

# Future Improvements
Potential future improvements include:
- Streaming AI responses
- Improved RAG retrieval evaluation
- Better chunking strategies
- Retrieval quality evaluation
- Conversation history for document chat
- Improved AI response performance
- PostgreSQL production setup
- Docker Compose
- Cloud deployment
- CI/CD pipeline
- Automated integration tests

# License

This project is currently intended as a personal portfolio and learning project.