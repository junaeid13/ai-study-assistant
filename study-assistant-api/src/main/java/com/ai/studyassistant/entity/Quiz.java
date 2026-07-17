package com.ai.studyassistant.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "quizzes")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String question;

    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctAnswer;

    @ManyToOne
    @JoinColumn(name = "document_id")
    private Document document;
}
