# Autonomous Content Factory

Every product launch requires Marketing to rewrite the same content for
blog, LinkedIn, and newsletter separately. Repetitive rewriting causes
burnout, factual errors, and tone inconsistencies across channels.

Autonomous Content Factory is an AI-agent system where a single source document is first verified for facts, then transformed into platform-specific content — automatically
and consistently.

### Feature 1: Fact-Check & Research Agent

→ Extract features, specs, and audience from a text file or URL

→ Produce a structured Fact-Sheet (JSON/Markdown) as single source of truth

→ Flag ambiguous statements that could cause inconsistencies

### Feature 2: Creative Copywriter Agent

→ Produce 500-word Blog, 5-post Social Thread, 1-paragraph Email Teaser

→ Switch tone per format: Professional for blog, Punchy for social

→ Core Value Proposition must be central — no invented claims

## Requirements

Java

Spring Boot

Node.js

npm

React

## Backend Setup

cd backend

*Add openrouter API key* in application.properties

mvn clean install

mvn spring-boot:run

Open http://localhost:8080

## Frontend Setup

cd frontend

npm install

npm run dev

Open http://localhost:5173
