package com.ai.studyassistant.service;

import com.ai.studyassistant.dto.EmbeddingRequest;
import com.ai.studyassistant.dto.EmbeddingResponse;
import com.ai.studyassistant.entity.Document;
import com.ai.studyassistant.entity.DocumentChunk;
import com.ai.studyassistant.repository.DocumentChunkRepository;
import com.ai.studyassistant.repository.DocumentRepository;
import com.ai.studyassistant.utility.DocumentChunker;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentChunkService {
    private final DocumentRepository documentRepository;
    private final DocumentChunkRepository documentChunkRepository;
    private final DocumentChunker documentChunker;
    private final PythonApiClient pythonApiClient;

    public DocumentChunkService(
            DocumentChunkRepository documentChunkRepository,
            DocumentRepository documentRepository,
            DocumentChunker documentChunker,
            PythonApiClient pythonApiClient
    ) {
        this.documentChunkRepository = documentChunkRepository;
        this.documentRepository = documentRepository;
        this.documentChunker = documentChunker;
        this.pythonApiClient = pythonApiClient;
    }

    public List<DocumentChunk> createChunk(Long documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document Not Found"));

        if (document.getExtractedText() == null || document.getExtractedText().isBlank()) {
            throw new RuntimeException("Extracted Text Not Found");
        }

        // Avoid creating duplicate chunks
        List<DocumentChunk> existingChunks =
                documentChunkRepository
                        .findByDocumentIdOrderByChunkIndex(documentId);

        if (!existingChunks.isEmpty()) {
            return existingChunks;
        }

        List<String> chunks =
                documentChunker.chunk(document.getExtractedText());
        if (chunks.isEmpty()) {
            throw new RuntimeException("Extracted Text Not Found");
        }

        // create DocumentChunk entities
        List<DocumentChunk> documentChunks = new ArrayList<>();

        for (int i = 0; i < chunks.size(); i++) {
            documentChunks.add(
                    new DocumentChunk(
                            null,
                            chunks.get(i),
                            i,
                            document
                    )
            );


        }
        // save all chunks
        List<DocumentChunk> savedChunks = documentChunkRepository.saveAll(documentChunks);
        createEmbeddings(documentId, chunks);
        return savedChunks;
    }

    private void createEmbeddings(
            Long documentId,
            List<String> chunks
    ) {
        EmbeddingRequest request = new EmbeddingRequest(
                documentId,
                chunks
        );

        EmbeddingResponse response = pythonApiClient.postForObject(
                "/create-embeddings",
                request,
                EmbeddingResponse.class
        );

        if (response == null) {
            throw new RuntimeException(
                    "Failed to create embeddings for documentId: " + documentId
            );
        }
    }
}
