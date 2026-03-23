# Technical Overview: AI Decision Platform

## Project Architecture & Core Technologies
The "AI Decision Platform" is a centralized intelligence hub designed to provide actionable insights, automate diagnostics, and serve as an intelligent assistant for different user personas (Risk Managers, Data Engineers, Product Owners) within a financial or trading domain.

### Tech Stack
*   **Framework:** Spring Boot 3.5.10 (Java 17)
*   **Database:** PostgreSQL (Aiven Cloud, JPA/Hibernate for ORM)
*   **AI Integration:** Gemini API (Custom `GeminiClient` with multiple fallback API keys)
*   **Documentation:** Springdoc OpenAPI (Swagger UI) for API documentation.
*   **Build Tool:** Gradle

## Core Modules

The application is structured into several distinct feature domains under `com.ashirvad.platform`:

### 1. `ai` (Artificial Intelligence Engine)
*   Provides a centralized `AiService` wrapper around the `GeminiClient`.
*   Uses a prompt template system via `PromptTemplates` and `SystemPromptType` enum.
*   **System Prompts Managed:**
    *   `GENAI_WRAPPER_CHAT` (General Chat / Telegram)
    *   `PNL_EXPLANATION` (Financial Anomaly Explanation)
    *   `ETL_DIAGNOSIS` (Data Pipeline Troubleshooting)
    *   `ROLE_ASSISTANT_*` (Persona-based assistants for Risk Managers, Data Engineers, Product Owners)
    *   `RAG_TENANT_CHAT` / `RAG_TENANT_CHAT_BALANCED` (Retrieval-Augmented Generation context)

### 2. `pnl` (Profit & Loss / Anomaly Detection)
*   Handles ingestion, tracking, and analysis of daily PnL records per desk.
*   **Ingestion:** `PnlIngestionService` processes CSV uploads (date, desk, product, pnl_amount).
*   **Anomaly Calculation:** `AnomalyCalculationService` analyzes records, calculating deviations from a 7-day rolling average to flag anomalies.
*   **AI Explanation:** `PnlExplanationService` leverages the AI engine to explain why a specific anomaly occurred based on deviation severity and historical context, returning a suggested action.

### 3. `etl` (ETL Diagnosis)
*   Aimed at data engineers, this module helps diagnose failing Directed Acyclic Graphs (DAGs) and tasks.
*   `EtlDiagnosisService` takes DAG ID, Task ID, Error messages, and Log URLs, sending them to the AI engine to determine root causes, severity, suggested actions, and whether a retry is safe.

### 4. `assistant` (Persona-Based AI Assistant)
*   Provides tailored insights based on user roles (`Persona`: RISK_MANAGER, DATA_ENGINEER, PRODUCT_OWNER).
*   `AssistantService` queries recent PnL anomalies from the database and feeds them as context to the AI, allowing users to ask natural language questions about the recent state of the system and receive persona-specific advice.

### 5. `chat` (External Chat Integrations)
*   Features a `TelegramService` acting as a Telegram Bot.
*   Integrates directly with the `AiService` to provide a conversational AI interface over Telegram.
*   Handles message chunking (Telegram's 4096 char limit) and maintains a local `chat_log.txt` for auditing.

### 6. `rag` (Retrieval-Augmented Generation)
*   Contains services for document seeding, embedding generation, and vector store interaction (`SeedingService`, `EmbeddingService`, `RagStoreService`).
*   `RagChatService` handles user queries by first retrieving relevant context from the vector store before querying the AI, ensuring responses are grounded in tenant-specific documentation.

### 7. `util`
*   Includes utilities like `JsonParsingService` to reliably extract structured JSON objects (like DTOs) from raw AI Markdown/Text responses.

## Deployment & Infrastructure
*   The project includes a `Dockerfile` for containerization.
*   Requires external environment variables for API keys (`GEMINI_API_KEY1`, `GEMINI_API_KEY2`, `GEMINI_API_KEY3`) and Telegram bot configuration (`TELEGRAM_BOT_TOKEN`).
*   Connects to a managed PostgreSQL database instance.

## Data Models (JPA Entities)
*   **PnlRecord:** Stores daily PnL amounts mapped to specific desks and products.
*   **AnomalyRecord:** Stores flagged deviations, linking a desk, date, calculated deviation, and severity level.