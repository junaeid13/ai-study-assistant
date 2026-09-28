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

                        OUTPUT REQUIREMENTS:
                        - Return ONLY a valid JSON array.
                        - Do not include Markdown code fences.
                        - Do not include explanations outside the JSON.
                        - Use double quotes for all JSON keys and string values.
                        - Escape special characters inside strings correctly.
                        - Do not include trailing commas.
                        - Each object must contain exactly "concept" and "explanation".

                        Required structure:
                        [
                            {{
                                "concept": "concept name",
                                "explanation": "clear explanation of the concept"
                            }}
                        ]

                        Document:
                        {text}
                        """

        response = self.llm_service.generate(prompt)

        try:
            data = json.loads(response)
            if not isinstance(data, list):
                raise ValueError(
                    "LLM response must be a JSON array"
                )

            for concept in data:
                if not isinstance(concept, dict):
                    raise ValueError(
                        "Each concept must be a JSON object"
                    )
                if "concept" not in concept or "explanation" not in concept:
                    raise ValueError(
                        "Each concept must contain"
                        "'concept' and 'explanation'"
                    )

            return [
                KeyConceptResponse(**concept)
                for concept in data
            ]

        except (json.JSONDecodeError, TypeError, ValueError) as error:
            raise RuntimeError(
                "Failed to parse key concept response from LLM"
                f"Invalid JSON at line {error.lineno}"
                f"colum {error.colno}"
            ) from error
        except(TypeError,ValueError) as error:
            raise RuntimeError(
                f"Invalid key concept response from LLM: {error}"
            ) from error

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

                        Return ONLY valid JSON.

                        The JSON must be an array of objects using exactly this structure:

                        [
                            {{
                                "question": "question text",
                                "options": [
                                    "option 1",
                                    "option 2",
                                    "option 3",
                                    "option 4"
                                ],
                                "correctAnswer": "the exact correct option"
                            }}
                        ]

                        Document:
                        {text}
                        """

        response = self.llm_service.generate(prompt)

        #print("Quiz response from LLM:", response)  # Debugging line

        try:
            data = json.loads(response)

            return [
                QuizResponse(
                    question=quiz["question"],
                    optionA=quiz["options"][0],
                    optionB=quiz["options"][1],
                    optionC=quiz["options"][2],
                    optionD=quiz["options"][3],
                    correctAnswer=quiz["correctAnswer"]
                )
                for quiz in data
]

        except (json.JSONDecodeError, TypeError, ValueError) as error:
            raise RuntimeError(
                "Failed to parse quiz response from LLM"
            ) from error


content_service = ContentService(llm_service)