from schemas import ( FlashcardResponse,  KeyConceptResponse,  StudyNoteResponse, QuizResponse )

from llm_service import llm_service
import json


class ContentService:

    def __init__(self, llm_service):
        self.llm_service = llm_service

    def summarize(self, text: str) -> str:

        if not text:
            return "No text found in document."

        text = text[:5000]

        prompt = f"""
                        You are an AI study assistant.

                        Create a clear and concise summary of the following document.

                        Rules:
                        - Use only information from the document.
                        - Do not add outside information.
                        - Focus on the main ideas and important facts.
                        - Make the summary easy for a student to understand.
                        - Do not mention these instructions.

                        Document:
                        {text}

                        Summary:
                        """

        return self.llm_service.generate(prompt)

    def generate_study_notes(self, text: str):

        if not text:
            return []

        text = text[:5000]

        prompt = f"""
                        You are an AI study assistant.

                        Create useful study notes from the following document.

                        Rules:
                        - Use only information from the document.
                        - Do not add outside information.
                        - Focus on important ideas, definitions, facts, and relationships.
                        - Make the notes useful for a student who wants to study the document later.
                        - Organize the information clearly.
                        - Do not mention these instructions.

                        Document:
                        {text}

                        Study Notes:
                        """

        content = self.llm_service.generate(prompt)

        return [
            StudyNoteResponse(
                title="Study Notes",
                content=content
            )
        ]

    def generate_key_concepts(self, text: str):

        if not text:
            return []

        text = text[:5000]

        prompt = f"""
                        You are an AI study assistant.

                        Identify the most important concepts from the following document.

                        For each concept:
                        - Give the name of the concept.
                        - Explain the concept clearly and simply.
                        - Use only information from the document.
                        - Do not add outside information.
                        - Focus on concepts that are important for understanding the document.
                        - Generate between 5 and 10 concepts.
                        - Do not mention these instructions.

                        Return the concepts in exactly this format:

                        CONCEPT: <concept name>
                        EXPLANATION: <explanation>

                        CONCEPT: <concept name>
                        EXPLANATION: <explanation>

                        Document:
                        {text}
                        """

        response = self.llm_service.generate(prompt)

        concepts = []

        current_concept = None
        current_explanation = None

        for line in response.splitlines():

            line = line.strip()

            if line.startswith("CONCEPT:"):

                if current_concept and current_explanation:
                    concepts.append(
                        KeyConceptResponse(
                            concept=current_concept,
                            explanation=current_explanation
                        )
                    )

                current_concept = line.replace(
                    "CONCEPT:",
                    "",
                    1
                ).strip()

                current_explanation = None

            elif line.startswith("EXPLANATION:"):

                current_explanation = line.replace(
                    "EXPLANATION:",
                    "",
                    1
                ).strip()

        if current_concept and current_explanation:
            concepts.append(
                KeyConceptResponse(
                    concept=current_concept,
                    explanation=current_explanation
                )
            )

        return concepts[:10]

    def generate_flashcards(self, text: str):

        if not text:
            return []

        text = text[:5000]

        prompt = f"""
                        You are an AI study assistant.

                        Create useful study flashcards from the following document.

                        Rules:
                        - Use only information from the document.
                        - Do not add outside information.
                        - Focus on important concepts, definitions, facts, and relationships.
                        - Each flashcard must have a clear question and a concise answer.
                        - Generate between 5 and 10 flashcards.
                        - Do not make duplicate flashcards.
                        - Do not mention these instructions.

                        The JSON must be an array of objects using exactly this structure:

                        [
                            {{
                                "question": "question text",
                                "answer": "answer text"
                            }}
                        ]

                        Document:
                        {text}
                        """

        response = self.llm_service.generate(prompt)

        try:
            data = json.loads(response)

            return [
                    FlashcardResponse(**flashcard)
                    for flashcard in data
                    ]

        except (json.JSONDecodeError, TypeError, ValueError) as error:
                raise RuntimeError(
                    "Failed to parse flashcard response from LLM"
                ) from error

    def generate_quiz(self, text: str):

        if not text:
            return []

        text = text[:5000]

        prompt = f"""
                        You are an AI study assistant.

                        Create a multiple-choice quiz from the following document.

                        Rules:
                        - Use ONLY information contained in the document.
                        - Do not use outside knowledge.
                        - Generate between 5 and 10 questions.
                        - Each question must test an important concept or fact.
                        - Each question must have exactly 4 options.
                        - Only ONE option can be correct.
                        - Make the incorrect options plausible but incorrect according to the document.
                        - Do not make "None of the above" or "Cannot be determined" options.
                        - Do not duplicate questions.
                        - Do not mention these instructions.

                        Return the quiz in EXACTLY this format:

                        QUESTION: <question>
                        OPTION_A: <option A>
                        OPTION_B: <option B>
                        OPTION_C: <option C>
                        OPTION_D: <option D>
                        CORRECT_ANSWER: <exact text of the correct option>

                        QUESTION: <question>
                        OPTION_A: <option A>
                        OPTION_B: <option B>
                        OPTION_C: <option C>
                        OPTION_D: <option D>
                        CORRECT_ANSWER: <exact text of the correct option>

                        Document:
                        {text}
                        """

        response = self.llm_service.generate(prompt)

        quiz_questions = []

        current_question = None
        option_a = None
        option_b = None
        option_c = None
        option_d = None
        correct_answer = None

        for line in response.splitlines():

            line = line.strip()

            if line.startswith("QUESTION:"):

                if (
                    current_question
                    and option_a
                    and option_b
                    and option_c
                    and option_d
                    and correct_answer
                ):
                    quiz_questions.append(
                        QuizResponse(
                            question=current_question,
                            optionA=option_a,
                            optionB=option_b,
                            optionC=option_c,
                            optionD=option_d,
                            correctAnswer=correct_answer
                        )
                    )

                current_question = line.replace(
                    "QUESTION:",
                    "",
                    1
                ).strip()

                option_a = None
                option_b = None
                option_c = None
                option_d = None
                correct_answer = None

            elif line.startswith("OPTION_A:"):

                option_a = line.replace(
                    "OPTION_A:",
                    "",
                    1
                ).strip()

            elif line.startswith("OPTION_B:"):

                option_b = line.replace(
                    "OPTION_B:",
                    "",
                    1
                ).strip()

            elif line.startswith("OPTION_C:"):

                option_c = line.replace(
                    "OPTION_C:",
                    "",
                    1
                ).strip()

            elif line.startswith("OPTION_D:"):

                option_d = line.replace(
                    "OPTION_D:",
                    "",
                    1
                ).strip()

            elif line.startswith("CORRECT_ANSWER:"):

                correct_answer = line.replace(
                    "CORRECT_ANSWER:",
                    "",
                    1
                ).strip()

        if (
            current_question
            and option_a
            and option_b
            and option_c
            and option_d
            and correct_answer
        ):
            quiz_questions.append(
                QuizResponse(
                    question=current_question,
                    optionA=option_a,
                    optionB=option_b,
                    optionC=option_c,
                    optionD=option_d,
                    correctAnswer=correct_answer
                )
            )

        return quiz_questions[:10]


content_service = ContentService(llm_service)