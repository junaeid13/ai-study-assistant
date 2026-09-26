
import { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import {
  getDocumentById,
  generateOrGetFlashcards,
  generateOrGetQuiz,
  generateStudyNotes,
  generateKeyConcepts,
} from "../services/api";
import FlashcardList from "../components/flashcards/FlashcardList";
import QuizList from "../components/quizzes/QuizList";
import StudyNoteList from "../components/notes/StudyNoteList";
import KeyConceptList from "../components/concepts/KeyConceptList";
import ChatWithPdf from "../components/documents/ChatWithPdf";
import ContentGenerationSection from "../components/documents/contentGenerationSection";

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
      setFlashcardsError("");
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
      <ContentGenerationSection
        title="Study Notes"
        buttonText="Generate Study Notes"
        loadingText="Generating study notes..."
        loading={loadingStudyNotes}
        error={studyNotesError}
        onGenerate={loadStudyNotes}
      >
        <StudyNoteList studyNotes={studyNotes} />
      </ContentGenerationSection>

      {/* Key Concepts */}
      <ContentGenerationSection
        title="Key Concepts"
        buttonText="Generate Key Concepts"
        loadingText="Generating key concepts..."
        loading={loadingKeyConcepts}
        error={keyConceptsError}
        onGenerate={loadKeyConcepts}
      >
        <KeyConceptList keyConcepts={keyConcepts} />
      </ContentGenerationSection>
            
      {/* Flashcards */}
      <ContentGenerationSection
        title="Flashcards"
        buttonText="Generate Flashcards"
        loadingText="Generating flashcards..."
        loading={loadingFlashcards}
        error={flashcardsError}
        onGenerate={loadFlashcards}
      >
        <FlashcardList flashcards={flashcards} />
      </ContentGenerationSection>

      {/* Quizzes */}
      <ContentGenerationSection
        title="Quizzes"
        buttonText="Generate Quiz"
        loadingText="Generating quiz..."
        loading={loadingQuizzes}
        error={quizzesError}
        onGenerate={loadQuiz}
      >
        <QuizList quizzes={quizzes} />
      </ContentGenerationSection>

      <hr/>
        
      <ChatWithPdf documentId={id} />
      
        
    </div>
  );
}

export default DocumentDetails;