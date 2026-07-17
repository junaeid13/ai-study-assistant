import axios from "axios";




const api = axios.create({
baseURL: "http://localhost:8080/api"
});

export const getDocumentById = async (id) => {
  try {
    const response = await api.get(`/documents/${id}`);
    return response.data;
  } catch (error) {
    console.error("Error fetching document:", error);
    throw error;
  }
};

export const getCurrentUser = async () => {
  try {
    const response = await api.get("/users/me");
    return response.data;
  } catch (error) {
    console.error("Error fetching current user:", error);
    throw error;
  }
};

export const getFlashcards = async (documentId) => {
  try {
    const response = await api.get(`/flashcards/${documentId}`);
    return response.data;
  } catch (error) {
    console.error("Error fetching flashcards:", error);
    throw error;
  }
};

export const getQuiz = async (documentId) => {
  try {
    const response = await api.get(`/quizzes/${documentId}`);
    return response.data;
  } catch (error) {
    console.error("Error fetching quiz:", error);
    throw error;
  }
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

export default api;