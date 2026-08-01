package com.ai.studyassistant.repository;

import com.ai.studyassistant.entity.KeyConcept;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeyConceptRepository extends JpaRepository<KeyConcept, Long> {
}
