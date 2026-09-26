
import { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import {
  getDocumentById,
  generateOrGetFlashcards,
  generateOrGetQuiz,
  generateStudyNotes,
  generateKeyConcepts,
} from "../services/api";
import FlashcardList from "../components/FlashcardList";
import QuizList from "../components/quizzes/QuizList";
import StudyNoteList from "../components/notes/StudyNoteList";
import KeyConceptList from "../components/KeyConceptList";
import ChatWithPdf from "../components/ChatWithPdf";

function DocumentDetails() {

  const { id } = useParams();


  const [document, setDocument] = useState(null);
  const [loading, setLoading] = useState(true);
  const [documentError, setDocumentError] = useState("");

  const [keyConcepts, setKeyConcepts] = useState([]);
  const [loadingKeyConcepts, setLoadingKeyConcepts] = useState(false);
  const [keyConceptsError, setKeyConceptsError] = useState("");
  
  const [studyNotes, setStudyNotes] = useState([]);
  const [loadingStudyNotes, setLoadingStudyNotes] = useState(false);
  const [studyNotesError, setStudyNotesError] = useState("");
  
  const [flashcards, setFlashcards] = useState([]);
  const [loadingFlashcards, setLoadingFlashcards] = useState(false);
  const [flashcardsError, setFlashcardsError] = useState("");

  const [quizzes, setQuizzes] = useState([]);
  const [loadingQuizzes, setLoadingQuizzes] = useState(false);
  const [quizzesError, setQuizzesError] = useState("");

  const loadKeyConcepts = async () => {
    try {
      setKeyConceptsError("");
      setLoadingKeyConcepts(true);

      const response = await generateKeyConcepts(id);
      setKeyConcepts(response);
    } catch (err) {
      console.error(err);
      setKeyConceptsError("Failed to generate key concepts");
    } finally {
      setLoadingKeyConcepts(false);
    }
  };

  const loadStudyNotes = async () => {
    try {
      setStudyNotesError("");
      setLoadingStudyNotes(true);

      const response = await generateStudyNotes(id);
      setStudyNotes(response);
    } catch (err) {
      console.error(err);
      setStudyNotesError("Failed to load study notes");
    } finally {
      setLoadingStudyNotes(false);
    }
  };


  const loadQuiz = async () => {

    try {
        setQuizzesError("");
        setLoadingQuizzes(true);

        const response = await generateOrGetQuiz(id);
        setQuizzes(response);
      } catch (error) {
          console.error(error);
          setQuizzesError("Failed to load quizzes");
      } finally {
          setLoadingQuizzes(false);
      }
    };

  const loadFlashcards = async () => {
    try {
      setLoadingFlashcards(true);
      
      const response = await generateOrGetFlashcards(id);
      setFlashcards(response);
    } catch (err) {
      console.error(err);
      setFlashcardsError("Failed to load flashcards");
    } finally {
      setLoadingFlashcards(false);
    }
  };

  useEffect(() => {

    const fetchDocument = async () => {

      try {

        setLoading(true);
        setDocumentError("");

        const data = await getDocumentById(id);
        setDocument(data);
      } catch (err) {
        console.error(err);
        setDocumentError("Failed to load document");

      } finally {
        setLoading(false);
      }
    };

    fetchDocument();

  }, [id]);

  if (loading) {
    return <p>Loading document...</p>;
  }
  if (documentError) {
    return <p>{documentError}</p>;
  }
  if (!document) {
    return <p>Document not found.</p>;
  }

  return (
    <div style={{ padding: "20px" }}>

      <Link to="/">
        ← Back to Dashboard
      </Link>

      <h1>{document.filename}</h1>

      <p>
        <strong>ID:</strong> {document.id}
      </p>

      <p>
        <strong>Uploaded:</strong>{" "}
        {new Date(document.uploadedAt).toLocaleString()}
      </p>

      <hr />

      <h2>Summary</h2>

      <p>{document.summary}</p>

      {/* Study Notes */}

      <h2>Study Notes</h2>

      <button
        onClick={loadStudyNotes}
        disabled={loadingStudyNotes}
        style={{ marginBottom: "20px" }}
      >
        {loadingStudyNotes ? "Generating..." : "Generate Study Notes"}
      </button>
      {studyNotesError && (
        <p style={{ color: "red" }}>{studyNotesError}</p>
      )}
      <StudyNoteList 
            studyNotes={studyNotes} 
      />
      {/* Key Concepts */}
      <h2>Key Concepts</h2>
      <button 
        onClick={loadKeyConcepts}
        disabled={loadingKeyConcepts}
        style={{ marginBottom: "20px" }}
      >
        {loadingKeyConcepts ? "Generating..." : "Generate Key Concepts"}
      </button>
      {keyConceptsError && (
        <p style={{ color: "red" }}>{keyConceptsError}</p>
      )}
      <KeyConceptList keyConcepts={keyConcepts} />
      {/* Flashcards */}
      <h2>Flashcards</h2>

      <button
        onClick={loadFlashcards}
        disabled={loadingFlashcards}
        style={{ marginBottom: "20px" }}
      >
        {loadingFlashcards ? "Generating..." : "Generate Flashcards"}
      </button>
      {flashcardsError && (
        <p style={{ color: "red" }}>{flashcardsError}</p>
      )}
      <FlashcardList flashcards={flashcards} /> 
      {/* Quizzes */}
      <h2>Quizzes</h2>
      <button
        onClick={loadQuiz}
        disabled={loadingQuizzes}
        >
        {loadingQuizzes ? "Generating..." : "Generate Quiz"}
      </button>
      {quizzesError && (
        <p style={{ color: "red" }}>{quizzesError}</p>
      )}
      <QuizList 
          quizzes={quizzes} 
          documentId={id}
      />

      <hr/>
      <ChatWithPdf documentId={id} />
      
        
    </div>
  );
}

export default DocumentDetails;