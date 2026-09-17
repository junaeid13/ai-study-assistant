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
                You are an AI study assistant answering questions about a document.

                Use ONLY the information provided in the document context.

                Rules:
                - Do not use outside knowledge.
                - Answer the question directly.
                - If the answer cannot be found in the context, say:
                "The information is not available in the document."
                - Do not invent facts.
                - Do not invent citations.
                - When making a factual statement, cite the relevant chunk.
                - Use citations exactly like: [Chunk X]
                - Only cite chunk numbers that appear in the provided context.
                - Keep the answer clear and concise.
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