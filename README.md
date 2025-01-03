# Reactive Error Handling with RFC 9457 and Internationalization (i18n)
**Author:** Cem Aktas

---
This project demonstrates how to implement a modern, standardized error handling approach based on **RFC 9457** in a reactive Spring WebFlux application. It also showcases **internationalization (i18n)**, allowing error messages to be localized based on the `Accept-Language` HTTP header.

## Key Features

- **Reactive Spring WebFlux**: A non-blocking, asynchronous web framework.
- **RFC 9457-Compliant Error Responses**: Implements structured error fields like `type`, `title`, `status`, `detail`, and `instance`. You can access details about RFC 9457 from [here](https://www.rfc-editor.org/rfc/rfc9457.html).
- **Internationalized Error Messages**: Supports multiple languages using resource bundles.
- **Spring Validation**: Utilizes Spring's validation framework to automatically handle validation errors.
- **Automated Error Responses**: Provides consistent and standardized error responses for validation and business exceptions.

## Additional Features

- **Custom Exception Handling**: Demonstrates how to create and handle custom exceptions (`LocalizedException`) with localized messages.
- **OpenAPI Documentation with Swagger**: Provides comprehensive API documentation and testing capabilities via Swagger UI. You can access Swagger UI [here](http://localhost:8080/webjars/swagger-ui/index.html).
- **Support for Multiple Locales**: Easily extendable to support additional languages.

## Starting the Application
You can start the application using Docker Compose. This will build the Docker image and run the container.
```bash
docker-compose up --build -d
```
## Accessing the Application
Once the application is up and running, you can access the Swagger UI to test the API endpoints:

> http://localhost:8080/webjars/swagger-ui/index.html

The Swagger UI provides a user-friendly interface to interact with the API, view documentation, and send test requests.
