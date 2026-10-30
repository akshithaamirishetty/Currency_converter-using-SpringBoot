# Currency Converter Using Spring Boot

## Project Description

The Currency Converter is a Java-based web application developed using Spring Boot. It allows users to convert amounts between different currencies using exchange rates retrieved from an external API.

## Features

* Convert one currency into another.
* View supported currencies.
* Get the latest available exchange rates.
* View conversion history.
* Delete conversion history records.
* Simple and user-friendly web interface.

## Technologies Used

* Java
* Spring Boot
* Spring Data JPA
* HTML
* CSS
* JavaScript
* H2 Database
* Maven
* Frankfurter Exchange Rate API

## API Endpoints

| Method | Endpoint              | Description              |
| ------ | --------------------- | ------------------------ |
| GET    | `/api/currencies`     | Get supported currencies |
| GET    | `/api/rates?base=USD` | Get exchange rates       |
| POST   | `/api/convert`        | Convert currency         |
| GET    | `/api/history`        | View conversion history  |
| GET    | `/api/history/{id}`   | Get a particular record  |
| DELETE | `/api/history/{id}`   | Delete a record          |

## How to Run

1. Clone the repository.
2. Open the project in an IDE or GitHub Codespaces.
3. Run the Spring Boot application using:

```bash
mvn spring-boot:run
```

4. Open the application in your browser at `http://localhost:8080`.

## Author

Amirishetty Akshitha
