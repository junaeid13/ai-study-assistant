package com.ai.studyassistant.utility;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DocumentChunker {

    private static final int CHUNK_SIZE = 1000;
    private static final int CHUNK_OVERLAP = 200;

    public List<String> chunk(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        List<String> paragraphs = splitIntoParagraphs(text);
        List<String> chunks = new ArrayList<>();
        StringBuilder currentChunk = new StringBuilder();

        for (String paragraph : paragraphs) {
            if (currentChunk.length() + paragraph.length() <= CHUNK_SIZE) {
                if (!currentChunk.isEmpty()) {
                    currentChunk.append("\n\n");
                }
                currentChunk.append(paragraph);
            } else {
                if (!currentChunk.isEmpty()) {
                    chunks.add(currentChunk.toString().trim());
                }

                String overlap = getOverlap(currentChunk.toString());
                currentChunk = new StringBuilder();
                if (!overlap.isBlank()) {
                    currentChunk.append(overlap);
                    currentChunk.append("\n\n");
                }

                currentChunk.append(paragraph);

            }
        }

        if (!currentChunk.isEmpty()) {
            chunks.add(currentChunk.toString().trim());
        }
        return chunks;
    }

    private List<String> splitIntoParagraphs(String text) {
        String[] parts = text.split("\\n\\s*\\n");
        List<String> paragraphs = new ArrayList<>();

        for (String part : parts) {
            String paragraph = part.trim();
            if (!paragraph.isBlank()) {
                paragraphs.add(paragraph);
            }
        }

        return paragraphs;
    }

    private String getOverlap(String chunk) {
        if (chunk.length() < CHUNK_OVERLAP) {
            return chunk;
        }
        return chunk.substring(chunk.length() - CHUNK_OVERLAP);
    }
}
