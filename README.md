# Deck - Flashcard Learning App

## A Java Spring-based web app for effective memorization through spaced repetition using flashcards.

Deck is a web application written in Java using the Spring Framework.
It helps users learn through flashcards in either quiz or study mode.
The app supports user authentication, public/private cards, and category-based filtering.


## Tech stack

- Java 21
- Spring Boot
- Spring Security
- Thymeleaf
- PostgreSQL (Docker)
- Maven

## Local Setup

1. Clone the repo:

```bash
git clone https://github.com/nenp/deck.git

cd Deck
```

2. Create a `.env` file using the provided example file:
```bash
cp .env.example .env
```
    Update your `.env` file to match your database setup.

&nbsp;<br>
3. Start PostgreSQL with Docker:
```bash
docker compose up -d
```

4. Run the application:
```bash
./mvnw spring-boot:run

5. Access the app at:
`http://localhost:8080`

```
## Database Schema

![Database Schema](https://github.com/Nenp/Deck/blob/master/docs/schema.png?raw=true)
