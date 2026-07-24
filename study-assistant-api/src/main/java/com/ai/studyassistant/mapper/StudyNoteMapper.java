package com.ai.studyassistant.mapper;

import com.ai.studyassistant.dto.StudyNoteResponse;
import com.ai.studyassistant.entity.Document;
import com.ai.studyassistant.entity.StudyNote;
import org.springframework.stereotype.Component;

@Component
public class StudyNoteMapper {

    public StudyNote toEntity(
            StudyNoteResponse response,
            Document document
    ) {
        StudyNote studyNote = new StudyNote();
        studyNote.setTitle(response.title());
        studyNote.setContent(response.content());
        studyNote.setDocument(document);

        return studyNote;
    }

    public StudyNoteResponse toResponse(StudyNote studyNote) {
        return new StudyNoteResponse(
                studyNote.getId(),
                studyNote.getTitle(),
                studyNote.getContent()
        );
    }
}
