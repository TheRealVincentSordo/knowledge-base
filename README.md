
# Knowledge Base REST API with LLaMA3 Enhancement

This project is a **Java Spring Boot** REST API that acts as a knowledge base, allowing users to store, retrieve, and query documents. The content of each document is enhanced using the **local Ollama LLaMA3 model**. The document data is stored in a **PostgreSQL** database, and the LLaMA3 model is called via an HTTP request to a local service.

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Prerequisites](#prerequisites)
- [Setup and Installation](#setup-and-installation)
- [Project Structure](#project-structure)
- [API Endpoints](#api-endpoints)
- [How the LLaMA3 Integration Works](#how-the-llama3-integration-works)
- [Testing](#testing)
- [License](#license)

## Features

- **CRUD Operations**: Create, retrieve, update, and delete documents.
- **Content Enhancement**: The content of each document is enhanced using the local **Ollama LLaMA3** model.
- **PostgreSQL**: Persistent data storage for documents.
- **RESTful API**: Exposed via Spring Boot's REST API.

## Technologies Used

- **Java 17**
- **Spring Boot 3.x**
    - Spring Web
    - Spring Data JPA
    - Spring Boot DevTools (optional)
    - RestTemplate
- **PostgreSQL**: For storing documents.
- **Ollama LLaMA3 Model**: Local language model used for content enhancement.
- **Jackson**: For JSON serialization/deserialization.

## Prerequisites

1. **Java 17 or higher**: Ensure that Java is installed on your machine.
2. **PostgreSQL**: Install and configure a PostgreSQL instance.
3. **Ollama**: Install the Ollama CLI and the LLaMA3 model locally.
    - You can install Ollama via the instructions from [Ollama's website](https://ollama.com/).
    - Download the LLaMA3 model:
      ```bash
      ollama pull llama3
      ```
4. **Maven**: Ensure Maven is installed to build and manage the project.

## Setup and Installation

### Step 1: Clone the Repository
```bash
git clone https://github.com/your-username/knowledge-base-llama3.git
cd knowledge-base-llama3
```

### Step 2: Set Up PostgreSQL
1. Create a PostgreSQL database called `knowledge_base`.
   ```bash
   psql -U postgres -c "CREATE DATABASE knowledge_base;"
   ```
2. Edit the `application.properties` file to include your PostgreSQL credentials:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/knowledge_base
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

### Step 3: Install Dependencies
Run Maven to download and install dependencies:
```bash
./mvnw clean install
```

### Step 4: Run the Application
Start the Spring Boot application:
```bash
./mvnw spring-boot:run
```

The application should now be running on `http://localhost:8080`.

### Step 5: Ensure Ollama LLaMA3 is Running Locally
Make sure the local LLaMA3 model is accessible via the following URL: `http://localhost:11434/api/generate`.

## Project Structure

```bash
src/
├── main/
│   ├── java/
│   │   └── com/example/knowledgebase/
│   │       ├── controller/           # REST controllers
│   │       ├── dto/                  # Data Transfer Objects
│   │       ├── entity/               # JPA entities (Document entity)
│   │       ├── repository/           # Repositories for database interactions
│   │       └── service/              # Services including LlamaService for LLaMA3 integration
│   └── resources/
│       ├── application.properties    # Application configuration
└── test/                             # Unit and integration tests
```

## API Endpoints

| Method | Endpoint                  | Description                                |
|--------|---------------------------|--------------------------------------------|
| GET    | `/api/documents`           | Retrieve all documents                     |
| GET    | `/api/documents/{id}`      | Retrieve a specific document by ID         |
| POST   | `/api/documents`           | Create a new document                      |
| PUT    | `/api/documents/{id}`      | Update an existing document by ID          |
| DELETE | `/api/documents/{id}`      | Delete a document by ID                    |

### Example API Calls:

#### Create a Document

```bash
POST /api/documents
Content-Type: application/json

{
  "title": "My Document",
  "content": "This is the content that needs enhancement."
}
```

#### Get a Document

```bash
GET /api/documents/{id}
```

The response will include the **enhanced content** from LLaMA3:
```json
{
  "id": 1,
  "title": "My Document",
  "content": "This is the enhanced version of your document content."
}
```

## How the LLaMA3 Integration Works

The LLaMA3 model is run locally, and the application communicates with it using an HTTP request.

1. **API Request**: When a document is retrieved, the service makes a POST request to the local LLaMA3 service at `http://localhost:11434/api/generate`.
2. **Payload**: The document's content is sent to the LLaMA3 model as a prompt.
3. **Response**: The LLaMA3 service returns the enhanced content.
4. **API Response**: The enhanced content is included in the response sent back to the user.

Here’s how the `LlamaService` calls the local API:

```java
public String enhanceDocumentContent(String content) {
    String url = "http://localhost:11434/api/generate";
    Map<String, String> requestPayload = new HashMap<>();
    requestPayload.put("prompt", "Enhance this document: " + content);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<Map<String, String>> request = new HttpEntity<>(requestPayload, headers);
    ResponseEntity<LlamaResponse> response = restTemplate.postForEntity(url, request, LlamaResponse.class);
    
    return response.getBody().getGeneratedText();
}
```

## Testing

To test the application, use **Postman**, **cURL**, or a similar API testing tool.

### Example: Create a Document with Postman

1. Open Postman.
2. Create a POST request to `http://localhost:8080/api/documents`.
3. Set the body to `raw` JSON:
   ```json
   {
     "title": "Test Document",
     "content": "This is a test document content."
   }
   ```
4. Send the request, and the document will be created. When you retrieve the document, the content will be enhanced using the LLaMA3 model.

## License

This project is licensed under the MIT License.

---
