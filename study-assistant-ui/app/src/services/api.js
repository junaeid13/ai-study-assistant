import axios from "axios";

const api = axios.create({
baseURL: "http://localhost:8080/api"
});

export const chatWithDocument = async(
  documentId,
  question,
  topK = 5
)=> {
  const response = await api.post(
    `/documents/${documentId}/chat`,
    {
      question,
      topK
    }
  );
  return response.data;
};

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

export const chatWithDocument = async (documentId, question, topK = 5) => {
  const response = await api.post(`/documents/${documentId}/chat`, {
    question,
    topK,
  });

  return response.data;
};

export const getCurrentUser = async () => {
    const response = await api.get("/users/me");
    return response.data;

};

export const generateKeyConcepts = async (documentId) => {
  const response = await api.post(
    `/key-concepts/${documentId}`
  );
  return response.data;
};

export const getKeyConcepts = async (documentId) => {
  const response = await api.get(`/key-concepts/${documentId}`);
  return response.data;
};

export const generateStudyNotes = async (documentId) => {
  const response = await api.post(`/documents/${documentId}/study-notes`);
  return response.data;
};

export const getStudyNotes = async (documentId) => {
  const response = await api.get(`/documents/${documentId}/notes`);
  return response.data;
};

export const getDocumentById = async (id) => {
    const response = await api.get(`/documents/${id}`);
    return response.data;

};

export const generateOrGetFlashcards = async (documentId) => {
    const response = await api.get(`/flashcards/${documentId}`);
    return response.data;
};

export const generateOrGetQuiz = async (documentId) => {
    const response = await api.get(`/quizzes/${documentId}`);
    return response.data;
};

export const submitQuiz = async (payload) => {
    const response = await api.post(`/quizzes/submit`, payload);
    return response.data;

};


export default api;