import axios from "axios";

const api = axios.create({
baseURL: "http://localhost:8080/api"
});


/**
* Automatically attach JWT token to every request
*/
api.interceptors.request.use(
  (config) => {
        const token = localStorage.getItem("token");

        if (token) {
          config.headers.Authorization = `Bearer ${token}`;
        }

        return config;
      },
      (error) => {
        return Promise.reject(error);
      }
);

/**
 * Handle authentication errors globally
 */

api.interceptors.response.use(
  (response) => response,
    (error) => {
      if (error.response?.status === 401) {
        console.log("Unauthorized - token invalid or expired");

        // Optional future improvement:
        // localStorage.removeItem("token");
        // window.location.href = "/login";
        }

    return Promise.reject(error);
  }
);


/**
 * Chat with a document.
 */
export const chatWithDocument = async (
  documentId,
  question,
  topK = 5
) => {
  const response = await api.post(
    `/documents/${documentId}/chat`,
    {
      question,
      topK,
    }
  );

  return response.data;
};

/**
 * get the current logged in user
 */
export const getCurrentUser = async () => {
    const response = await api.get("/users/me");
    return response.data;

};

/**
 * Generate or get key concepts for a document
 */
export const generateKeyConcepts = async (documentId) => {
  const response = await api.post(
    `/key-concepts/${documentId}`
  );
  return response.data;
};


/**
 * get key concepts for a document
 */
export const getKeyConcepts = async (documentId) => {
  const response = await api.get(`/key-concepts/${documentId}`);
  return response.data;
};

/**
 * Generate study notes for a document
 */
export const generateStudyNotes = async (documentId) => {
  const response = await api.post(`/documents/${documentId}/study-notes`);
  return response.data;
};

/**
 * get study notes for a document
 */
export const getStudyNotes = async (documentId) => {
  const response = await api.get(`/documents/${documentId}/notes`);
  return response.data;
};

/**
 * get a document by its ID
 */
export const getDocumentById = async (id) => {
    const response = await api.get(`/documents/${id}`);
    return response.data;

};

/**
 * Generate or get flashcards
 */
export const generateOrGetFlashcards = async (documentId) => {
    const response = await api.get(`/flashcards/${documentId}`);
    return response.data;
};

/**
 * Generate or get quiz 
 */
export const generateOrGetQuiz = async (documentId) => {
    const response = await api.get(`/quizzes/${documentId}`);
    return response.data;
};

/**
 * submit quiz
 */

export const submitQuiz = async (payload) => {
    const response = await api.post(`/quizzes/submit`, payload);
    return response.data;

};


export default api;