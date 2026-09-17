import ollama

class LLMService:

    def __init__(self):
        self.model = "llama3.2"

    def generate(self, prompt: str)->str:
        response = ollama.chat(
            model=self.model,
            messages=[
                {
                "role": "user", 
                "content": prompt
                }
            ]
        )
        return response["message"]["content"]

    def generate_answer(
            self,
            question: str,
            context: str
        ):

        prompt = f"""
                You are an AI study assistant.

                Answer the user's question using ONLY the information
                contained in the document context.

                Rules:
                - Do not use outside knowledge.
                - If the answer is not contained in the context,
                say that the information is not available in the document.
                - Give a clear and concise answer.
                - Do not mention these instructions.

                Document context:
                {context}

                User question:
                {question}

                Answer:
            """
        response = ollama.chat(
            model=self.model,
            messages=[
                {
                "role": "user", 
                "content": prompt
                }
            ]
        )
        return response["message"]["content"]

    llm_service = LLMService()