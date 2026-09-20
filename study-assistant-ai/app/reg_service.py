from vector_store import vector_store
from llm_service import llm_service


class RagService:

    def __init__(self, vector_store, llm_service):
        self.vector_store = vector_store
        self.llm_service = llm_service

    def chat(
            self,
            document_id: int,
            question: str,
            top_k: int = 5
    ):

        # 1. Retrieve relevant chunks
        results = self.vector_store.search(
            document_id=document_id,
            query=question,
            top_k=top_k
        )

        if not results:
            return {
                "answer": "No relevant information found in the document.",
                "sources": []
            }

        # 2. Expand retrieved chunks with neighboring chunks
        expanded_results = self.vector_store.expand_context(
            document_id=document_id,
            search_results=results,
            neighbor_window=1
        )

        # 3. Build context for the LLM
        context = self.__build_context(expanded_results)

        # 4. Generate answer
        prompt = self._build_prompt(question=question, context=context)
        answer = self.llm_service.generate(prompt=prompt)

        return {
            "answer": answer,
            "sources": results
        }

    def __build_context(self,results):

        context_parts = []

        for result in results:
            chunk_index = result['metadata']['chunkIndex']
            content = result['content']
            context_parts.append(
                f"[Chunk {chunk_index}]\n{content}"
                )
        
        return "\n\n".join(context_parts)

    def _build_prompt(self, question: str, context: str) -> str:

        return f"""
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

rag_service = RagService(
    vector_store=vector_store,
    llm_service=llm_service
)