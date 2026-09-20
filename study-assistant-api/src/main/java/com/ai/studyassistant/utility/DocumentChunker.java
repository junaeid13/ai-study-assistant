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

        List<String> chunks = new ArrayList<>();
        int start = 0;
        while (start < text.length()) {
            int end = Math.min(
                    start + CHUNK_SIZE,
                    text.length()
            );
            String chunk = text
                    .substring(start, end)
                    .trim();

            if (!chunk.isBlank()) {
                chunks.add(chunk);
            }
            if (end == text.length()) {
                break;
            }
            start = end - CHUNK_OVERLAP;
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
