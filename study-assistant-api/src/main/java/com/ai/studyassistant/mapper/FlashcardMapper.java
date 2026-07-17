package com.ai.studyassistant.mapper;

import com.ai.studyassistant.dto.FlashcardResponse;
import com.ai.studyassistant.entity.Document;
import com.ai.studyassistant.entity.Flashcard;
import org.springframework.stereotype.Component;

@Component
public class FlashcardMapper {
    public FlashcardResponse mapToResponse(Flashcard flashcard) {
        return new FlashcardResponse(
                flashcard.getQuestion(),
                flashcard.getAnswer()
        );
    }

    public Flashcard toEntity(
            FlashcardResponse dto,
            Document document
    ) {
        Flashcard flashcard = new Flashcard();

        flashcard.setDocument(document);
        flashcard.setAnswer(dto.answer());
        flashcard.setQuestion(dto.question());

        return flashcard;
    }
}