package com.ai.studyassistant.mapper;

import com.ai.studyassistant.dto.KeyConceptResponse;
import com.ai.studyassistant.entity.Document;
import com.ai.studyassistant.entity.KeyConcept;
import org.springframework.stereotype.Component;

@Component
public class KeyConceptMapper {

    public KeyConcept toEntity(
            KeyConceptResponse response,
            Document document
    ) {
        return new KeyConcept(
                response.concept(),
                response.explanation(),
                document
        );
    }

    public KeyConceptResponse toResponse(
            KeyConcept keyConcept
    ) {
        return new KeyConceptResponse(
                keyConcept.getId(),
                keyConcept.getConcept(),
                keyConcept.getExplanation()
        );
    }
}
