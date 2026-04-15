# Poll Service (Spring Boot)

## Overview

This project is a simple Poll API built with Spring Boot. It supports creating polls, retrieving polls with options, and voting on options. The design focuses on simplicity, clear separation of concerns, and an API structure that is easy to extend.

---

## 1. Requirements

The system supports the following core features:

* Create a poll with multiple options
* Retrieve a poll with its options
* Vote on a poll option
* Return vote counts per option

The API is intentionally minimal, focusing on correctness and clean structure rather than premature optimization.

---

## 2. Design Decisions

## Domain Model

The core domain consists of:

### Poll

Represents the question being asked.

```
Poll
- id
- question
```

### PollOption

Represents a selectable answer for a poll.

```
PollOption
- id
- text
- voteCount
- pollId
```

### Relationship

```
Poll (1) ---> (many) PollOption
```

---

## DTO Layer

The API uses DTOs instead of exposing entities directly.

Example DTOs:

```
PollDTO
PollOptionDTO
```

### Why DTOs are used

* Decouple API from persistence model
* Avoid lazy loading / serialization issues
* Keep response shapes stable even if schema changes
* Allow computed fields in responses

Example PollOptionDTO:

```
- id
- text
- voteCount
```

This can later be extended without changing the database model.

---

## Query Strategy

The project uses some explicit queries.

Examples:

* fetch poll options by poll id
* increment vote counts directly in repository layer
* use projection interfaces for read models

This ensures:

* predictable SQL behavior
* no hidden joins or lazy loading surprises
* easier performance reasoning

---

## Breadcrumbs for Future Extension

### Optional Vote entity evolution

If the system evolves to require more detailed read models, a dedicated Vote entity can be introduced:

```
Vote
- id
- userId
- pollId
- pollOptionId
```

This would enable more flexible querying patterns for analytics-style responses.

---

## 3. Tech Stack

* Java 17
* Spring Boot
* Spring Web
* Spring Data JPA
* H2 Database
* Maven

---

## 4. Running the Project

## Start the application

```bash
./mvnw spring-boot:run
```

or

```bash
mvn spring-boot:run
```

App runs at:

```
http://localhost:8080
```

---


## Example API Endpoints

```
POST /polls
GET /polls/{id}
POST /poll-options/{id}/vote
```
