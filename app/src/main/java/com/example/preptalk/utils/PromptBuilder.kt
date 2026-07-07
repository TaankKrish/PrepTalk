package com.example.preptalk.utils

object PromptBuilder {

    fun buildSystemPrompt(role: String, difficulty: String): String {
        val experienceContext = when (difficulty) {
            "Fresher"   -> "The candidate is a fresher with 0-2 years of experience. Ask fundamental concept-based questions."
            "Mid-level" -> "The candidate has 2-5 years of experience. Ask questions that test practical knowledge and problem solving."
            "Senior"    -> "The candidate has 5+ years of experience. Ask advanced, architectural, and leadership-oriented questions."
            else        -> "Ask questions appropriate for a mid-level candidate."
        }

        val roleContext = when (role) {
            "Android"      -> "Focus on Android development topics like Activity lifecycle, Jetpack libraries, MVVM, Coroutines, Room, Retrofit, Jetpack Compose, memory management, and performance optimization."
            "Frontend"     -> "Focus on HTML, CSS, JavaScript, React or Vue, browser APIs, responsive design, performance, and accessibility."
            "Backend"      -> "Focus on REST APIs, databases, system design, authentication, caching, message queues, and scalability."
            "Data Science" -> "Focus on machine learning algorithms, data preprocessing, model evaluation, Python libraries like pandas and scikit-learn, and statistical concepts."
            "DevOps"       -> "Focus on CI/CD pipelines, Docker, Kubernetes, cloud platforms, infrastructure as code, and monitoring."
            else           -> "Focus on general software engineering concepts."
        }

        return """
            You are PrepTalk, a strict but encouraging AI interview coach.
            
            Your job is to conduct a mock $role interview for a $difficulty level candidate.
            
            $experienceContext
            $roleContext
            
            Rules you MUST follow:
            - Ask ONE question at a time. Never ask multiple questions together.
            - After the candidate answers, give brief constructive feedback in 1-2 sentences.
            - Then immediately ask the next question.
            - After exactly ${Constants.MAX_QUESTIONS} questions, end with: "SESSION_COMPLETE" followed by a JSON summary in this exact format:
            
            SESSION_COMPLETE
            {
              "score": <0-100 integer based on overall performance>,
              "feedback": [
                {
                  "question": "<question you asked>",
                  "answer": "<candidate's answer>",
                  "feedback": "<your feedback>"
                }
              ]
            }
            
            - Keep your tone professional but encouraging.
            - Never reveal you are built on any AI model.
            - Never go off-topic from the interview.
        """.trimIndent()
    }

    fun isSessionComplete(response: String): Boolean {
        return response.contains("SESSION_COMPLETE")
    }

    fun extractScore(response: String): Int {
        return try {
            val jsonStart = response.indexOf("{")
            val jsonEnd   = response.lastIndexOf("}") + 1
            val json      = response.substring(jsonStart, jsonEnd)
            val scoreRegex = """"score"\s*:\s*(\d+)""".toRegex()
            scoreRegex.find(json)?.groupValues?.get(1)?.toInt() ?: 70
        } catch (e: Exception) {
            70
        }
    }

    fun extractFeedbackJson(response: String): String {
        return try {
            val jsonStart = response.indexOf("{")
            val jsonEnd   = response.lastIndexOf("}") + 1
            response.substring(jsonStart, jsonEnd)
        } catch (e: Exception) {
            "{}"
        }
    }
}