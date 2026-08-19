import { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import { getDocumentById } from "../services/api";
import FlashcardList from "../components/FlashcardList";
import { getFlashcards } from "../services/api";
import { getQuiz } from "../services/api";
import QuizList from "../components/QuizList";
import StudyNoteList from "../components/StudyNoteList";
import { getStudyNotes } from "../services/api";
import keyConceptList from "../components/KeyConceptList";
import {generateKeyConcepts, getKeyConcepts} from "../services/api";


function DocumentDetails() {

  const { id } = useParams();




  const [keyConcepts, setKeyConcepts] = useState([]);
  const [loadingKeyConcepts, setLoadingKeyConcepts] = useState(false);
  const [studyNotes, setStudyNotes] = useState([]);
  const [loadingStudyNotes, setLoadingStudyNotes] = useState(false);
  const [document, setDocument] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [flashcards, setFlashcards] = useState([]);
  const [loadingFlashcards, setLoadingFlashcards] = useState(false);

  const [quizzes, setQuizzes] = useState([]);
  const [loadingQuizzes, setLoadingQuizzes] = useState(false);


  const generateConcepts = async () => {
    try {
      setLoadingKeyConcepts(true);
      const response = await generateKeyConcepts(id);
      setKeyConcepts(response);
    } catch (err) {
      console.error(err);
      setError("Failed to generate key concepts");
    } finally {
      setLoadingKeyConcepts(false);
    }
  };

  const loadKeyConcepts = async () => {
    try {
      setLoadingKeyConcepts(true);
      const response = await getKeyConcepts(id);
      setKeyConcepts(response);
    } catch (err) {
      console.error(err);
      setError("Failed to load key concepts");
    } finally {
      setLoadingKeyConcepts(false);
    }
  };
  
  useEffect(() => {
    loadKeyConcepts();
  }, [id]);


  const loadStudyNotes = async () => {
    try {
      setLoadingStudyNotes(true);
      const response = await getStudyNotes(id);
      setStudyNotes(response);
    } catch (err) {
      console.error(err);
      setError("Failed to load study notes");
    } finally {
      setLoadingStudyNotes(false);
    }
  };

  useEffect(() => {
    loadStudyNotes();
  }, [id]);

  const loadQuiz = async () => {

    try {

        setLoadingQuizzes(true);

        const response = await getQuiz(id);

        console.log("QUIZ RESPONSE:", response);

        setQuizzes(response);

      } catch (error) {

          console.error(error);

      } finally {

          setLoadingQuizzes(false);
      }
    };

  const loadFlashcards = async () => {
    try {
      setLoadingFlashcards(true);
      
      const response = await getFlashcards(id);
      setFlashcards(response);
    } catch (err) {
      console.error(err);
      setError("Failed to load flashcards");
    } finally {
      setLoadingFlashcards(false);
    }
  };

  useEffect(() => {
    loadFlashcards();
  }, [id]);

  useEffect(() => {

    const fetchDocument = async () => {

      try {

        const data = await getDocumentById(id);

        setDocument(data);

      } catch (err) {

        console.error(err);
        setError("Failed to load document");

      } finally {

        setLoading(false);
      }
    };

    fetchDocument();

  }, [id]);

  if (loading) {
    return <p>Loading document...</p>;
  }

  if (error) {
    return <p>{error}</p>;
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

      <button
        onClick={loadStudyNotes}
        disabled={loadingStudyNotes}
        style={{ marginBottom: "20px" }}
      >
        {loadingStudyNotes ? "Generating..." : "Generate Study Notes"}
      </button>
      <StudyNoteList 
            studyNotes={studyNotes} 
      />

      <button
        onClick={loadFlashcards}
        disabled={loadingFlashcards}
        style={{ marginBottom: "20px" }}
      >
        {loadingFlashcards ? "Generating..." : "Generate Flashcards"}
      </button>

      <FlashcardList flashcards={flashcards} /> 

      <button
        onClick={loadQuiz}
        disabled={loadingQuizzes}
        >
        {loadingQuizzes ? "Generating..." : "Generate Quiz"}
      </button>

      <QuizList 
          quizzes={quizzes} 
          documentId={id}
      />
        
    </div>
  );
}

export default DocumentDetails;