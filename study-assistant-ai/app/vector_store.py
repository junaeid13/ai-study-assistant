import chromadb
from sentence_transformers import SentenceTransformer

class VectorStore:
    def __init__(self):
        self.client = chromadb.PersistentClient(
            path="./chroma_db"
            )
        self.collection = self.client.get_or_create_collection(
            name="document_chunks"
            )
        self.model = SentenceTransformer('all-MiniLM-L6-v2')

    def add_chunks(
            self, 
            document_id: int, 
            chunks: list[str]
            ):
        if not chunks:
            return

        embeddings = self.model.encode(chunks).tolist()
        ids = [
            f"{document_id}_chunk_{index}" 
            for index in range(len(chunks))
        ]

        metadata = [
            {
                "document_id": document_id,
                "chunk_index": index
            }
            for index in range(len(chunks))
        ]
        self.collection.upsert(
            ids=ids,
            embeddings=embeddings,
            metadatas=metadata,
            documents=chunks
        )

    def search(
            self, 
            document_id:int,
            query:str,
            top_k:int=5
            ):
        query_embedding = self.embedding_model.encode(
            [query]
        ).tolist()
        results = self.collection.query(
            query_embeddings=[query_embedding],
            n_results=top_k,
            where={"document_id": document_id}
        )
        return results

    vector_store = VectorStore()