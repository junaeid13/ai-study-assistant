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



llm_service = LLMService()