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
        self.model = SentenceTransformer(
            'all-MiniLM-L6-v2'
            )

    def add_chunks(
            self, 
            document_id: int, 
            chunks: list[str]
        ):
        
        if not chunks:
            return

        embeddings = self.model.encode(chunks).tolist()
        ids = [
            f"{document_id}-{index}" 
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
            top_k:int=5,
            distance_threshold:float=0.8
            ):
        query_embedding = self.embedding_model.encode(
            [query]
        ).tolist()

        results = self.collection.query(
            query_embeddings=query_embedding,
            n_results=top_k,
            where={"document_id": document_id}
        )

        documents = results['documents',[[]]][0]
        distances = results['distances',[[]]][0]
        metadatas = results['metadatas',[[]]][0]

        search_results = []

        for i in range(len(documents)):
            if distances[i] > distance_threshold:
                continue
            metadata = metadatas[i]
            search_results.append({
                "context": documents[i],
                "distance": distances[i],
                "metadata": {
                    "documentId": metadata["document_id"],
                    "chunkIndex": metadata["chunk_index"]
                }
            })

        return search_results

    def expand_context(
            self,
            document_id:int,
            search_results:list[dict],
            neighbor_window:int=1
    ):
        if not search_results:
            return []
        chunk_indexes = set()

        for result in search_results:
            chunk_index = result["metadata"]["chunkIndex"]

            for offset in range(-neighbor_window, neighbor_window + 1):
                neighbor_index = chunk_index + offset
                if neighbor_index >= 0:
                    chunk_indexes.add(neighbor_index)

        ids = [
            f"{document_id}-{index}" 
            for index in chunk_indexes
        ]

        results = self.collection.get(
            ids=ids 
        )
        documents = results.get('documents', [])
        metadatas = results.get('metadatas', [])

        expanded_results = []

        for document, metadata in zip(documents, metadatas):
            expanded_results.append({
                "context": document,
                "metadata": {
                    "documentId": metadata["document_id"],
                    "chunkIndex": metadata["chunk_index"]
                }
            })  

        expanded_results.sort(
            key=lambda result: result["metadata"]["chunkIndex"]
            )
        return expanded_results

vector_store = VectorStore()