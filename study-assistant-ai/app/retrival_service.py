from vector_store import vector_store


class RetrievalService:

    def __init__(self, vector_store):
        self.vector_store = vector_store


    def create_embeddings(
            self,
            document_id: int,
            chunks: list[str]
    ):

        self.vector_store.add_chunks(
            document_id=document_id,
            chunks=chunks
        )

        return {
            "message": "Embeddings created successfully.",
            "document_id": document_id,
            "chunks_count": len(chunks)
        }

    def search(
            self,
            document_id: int,
            query: str,
            top_k: int = 5
    ):
        return self.vector_store.search(
            document_id=document_id,
            query=query,
            top_k=top_k
        )

    def evaluate(
            self,
            document_id: int,
            question: str,
            expected_chunk_index: int,
            top_k: int = 5
    ):

        results = self.search(
            document_id=document_id,
            query=question,
            top_k=top_k
        )

        retrieved_chunk_indices = [
            result["metadata"]["chunkIndex"]
            for result in results
        ]

        is_expected_chunk_retrieved = (
            expected_chunk_index in retrieved_chunk_indices
        )

        expected_rank = None

        if is_expected_chunk_retrieved:
            expected_rank = (
                retrieved_chunk_indices.index(
                    expected_chunk_index
                ) + 1
            )

        return {
            "message": "Evaluation completed.",
            "expected_chunk_index": expected_chunk_index,
            "retrieved_chunk_indices": retrieved_chunk_indices,
            "is_expected_chunk_retrieved": is_expected_chunk_retrieved,
            "expected_chunk_rank": expected_rank
        }


retrieval_service = RetrievalService(vector_store)