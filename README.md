# Ticketo24

This project is used to showcase a demo about how to decouple changes between backend and UI for my talk "Unlocking Continuous Delivery"

The project consists of three components:

1. ticketo-app - Frontend (React, Vite, Tailwind, [NextUI](https://nextui.org))
2. concert-service - Backend (Kotlin, Ktor, Exposed, Postgres)
3. ticket-service - Backend (Kotlin, Spring Boot, Postgres)

## Running the project

To run the project you will need:

- Docker
- React
- JVM 17+

1. Run the `compose.yaml` from the concert-service and ticket-service to bootstrap Postgres.
2. Run both backend projects.
3. Run the ticketo-app (Frontend) `npm run dev` and open the app in a browser window.
