package com.ai.studyassistant.repository;

import com.ai.studyassistant.entity.StudyNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyNoteRepository extends JpaRepository<StudyNote, Long> {
}
