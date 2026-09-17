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
        context = "\n\n".join(
            f"[Chunk {result['metadata']['chunkIndex']}]\n"
            f"{result['content']}"
            for result in expanded_results
        )

        # 4. Generate answer
        answer = self.llm_service.generate_answer(
            question=question,
            context=context
        )

        return {
            "answer": answer,
            "sources": results
        }


rag_service = RagService(
    vector_store=vector_store,
    llm_service=llm_service
)