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
}
