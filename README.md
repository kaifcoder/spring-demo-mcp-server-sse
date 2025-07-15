# MCP Spring Boot Example with User Service

This project demonstrates how to build a simple Spring Boot API and expose it as both REST endpoints and Model Context Protocol (MCP) tools using Spring Boot and Spring AI MCP Server. The app now includes a `UserService` that is exposed as an MCP tool.

---

## Table of Contents
- [Features](#features)
- [Requirements](#requirements)
- [Setup](#setup)
- [Running the Application](#running-the-application)
- [REST API Usage](#rest-api-usage)
- [MCP Server Usage (SSE + JSON-RPC)](#mcp-server-usage-sse--json-rpc)
- [Testing](#testing)
- [Development Notes](#development-notes)
- [Troubleshooting](#troubleshooting)

---

## Features
- UserService tool: get user details by user ID (REST and MCP)
- REST API endpoint for user details
- MCP server exposes user service as AI tool (usable by VS Code Copilot, etc.)
- Full test coverage with integration tests

---

## Requirements
- Java 17+
- Maven 3.8+
- VS Code (optional, for MCP client testing)
- [REST Client extension](https://marketplace.visualstudio.com/items?itemName=humao.rest-client) (optional, for `.http` files)

---

## Setup
1. **Clone the repository:**
   ```sh
   git clone <your-repo-url>
   cd mcpdemo
   ```
2. **Configure Git (optional):**
   ```sh
   git config user.name "<your-username>"
   git config user.email "<your-email>"
   ```
3. **Build the project:**
   ```sh
   ./mvnw clean install
   ```

---

## Running the Application
Start the Spring Boot application:
```sh
./mvnw spring-boot:run
```
The server will start on [http://localhost:8080](http://localhost:8080).

---

## REST API Usage
You can test the user endpoint using curl, Postman, or the provided `.http` file.

### UserService Example request:
- **Get user details:**
  ```http
  GET http://localhost:8080/api/users/123
  ```
  Response:
  ```json
  "User: 123, Name: John Doe"
  ```

---

## MCP Server Usage (SSE + JSON-RPC)
The MCP server uses Server-Sent Events (SSE) for session-based communication and JSON-RPC over HTTP for sending messages. This allows you to receive asynchronous responses in a browser or SSE client while sending requests via POST.

### 1. Create a session using the SSE endpoint
Open your browser and navigate to:
```
http://localhost:8080/mcp/sse
```
A new session will be created and you will receive a response like:
```
id:8a09a074-ad2d-434d-a768-8e30f25a186b
event:endpoint
data:/mcp/messages?sessionId=8a09a074-ad2d-434d-a768-8e30f25a186b
```
- The `id` is your session ID.
- The `data` field gives you the correct messages endpoint for this session.

### 2. Send JSON-RPC messages using the messages endpoint
Use a tool like Bruno, Postman, or curl to send POST requests to:
```
http://localhost:8080/mcp/messages?sessionId=<your-session-id>
```
with a JSON body. For example, to initialize:
```json
{
  "jsonrpc": "2.0",
  "id": 0,
  "method": "initialize",
  "params": {
    "protocolVersion": "2024-11-05",
    "capabilities": {},
    "clientInfo": {
      "name": "warp",
      "version": "1.0.0"
    }
  }
}
```

Then send:
```json
{
  "jsonrpc": "2.0",
  "method": "notifications/initialized"
}
```

You can now send tool/list or tool/invoke requests as shown below. All responses will appear in the browser window (SSE session) you opened in step 1.

#### Example: List available tools
```json
{
  "jsonrpc": "2.0",
  "id": "2",
  "method": "tools/list",
  "params": {}
}
```

#### Example: Invoke a tool
```json
{
  "jsonrpc": "2.0",
  "id": "3",
  "method": "tools/call",
  "params": {
    "name": "getUserDetailsMcp",
    "arguments": {
      "userId": "123"
    }
  }
}
```

#### Example response (will appear in SSE session)
```json
{
  "jsonrpc": "2.0",
  "id": "3",
  "result": "User: 123, Name: John Doe"
}
```

---

## VS Code Integration
1. Open VS Code and install an MCP-compatible extension (e.g., Copilot Chat).
2. Set the MCP server URL to `http://localhost:8080/mcp/sse` in the extension settings.
3. Use the extension's chat or tool features to call your user tool.

---

## Testing
Run all tests with:
```sh
./mvnw test
```
All endpoints and MCP tools are covered by integration tests.

---

## Development Notes
- User logic is in `UserService`.
- REST endpoint is in `UserController`.
- MCP tool registration is in `McpConfig`.
- MCP server configuration is in `src/main/resources/application.yaml`.

---

## Troubleshooting
- **SessionId error:** Always include a `sessionId` in MCP tool/completion requests.
- **Tool not visible in VS Code:** Ensure the MCP server is running and the extension is pointed to the correct URL.
- **Port conflicts:** Change the port in `application.yaml` if needed.
- **Logs:** Enable TRACE logging in `application.yaml` for detailed MCP server logs.

---

## License
[Apache 2.0](LICENSE)
