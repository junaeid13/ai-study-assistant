#  AI Study Assistant

> An AI-powered study platform that helps students learn from PDF documents by generating summaries, flashcards, quizzes, and personalized study materials.

---

##  Overview

AI Study Assistant is a full-stack microservice application designed to improve the learning experience from academic documents.

Users can upload PDF files and automatically generate:

-  AI Summaries
-  Flashcards
-  Multiple Choice Quizzes
-  Learning Progress (In Progress)
-  Chat with PDF (Planned)

The project follows a **production-oriented architecture** with separate frontend, Java backend, and Python AI services.

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
   H2 / PostgreSQL              FastAPI (Python)
        │                               │
        │                        PDF Processing
        │                        AI Generation
        │                               │
        └───────────────┬───────────────┘
                        ▼
                 JSON Responses
```

---

#  Tech Stack

## Frontend

- React (Vite)
- React Router
- Axios
- JavaScript

---

## Backend

- Spring Boot 3
- Spring Security
- Spring Data JPA
- JWT Authentication
- Maven

---

## AI Service

- FastAPI
- PyPDF2
- Sumy
- NLTK

---

## Database

Development

- H2 Database

Production (Planned)

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
