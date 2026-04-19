# EduServices Platform

A comprehensive microservices-based educational management platform built with Spring Boot and modern cloud-native technologies.

## Overview

EduServices Platform is a modular, scalable solution for educational institutions that provides comprehensive management capabilities through a set of interconnected microservices. The platform handles student management, course administration, campus operations, security, and more.

## Architecture

The platform follows a microservices architecture with the following core services:

### Core Services

- **address-service** - Address and location management
- **campus-mgmt-service** - Campus facilities and location management
- **classes-service** - Class and course management
- **configuration-service** - System configuration management
- **enrollment-service** - Student enrollment management
- **organization-service** - Organizational structure management
- **profiles-service** - User profile management
- **registrations-service** - Registration and admission management
- **security-service** - Authentication and authorization
- **student-service** - Student information management
- **teacher-service** - Teacher and faculty management

### Supporting Services

- **common-dto** - Shared data transfer objects
- **common-service** - Shared utilities and components
- **cloud-configs** - Cloud configuration services (Eureka, Config Server, API Gateway)
- **campus-mgmt-ui** - Angular frontend for campus management

## Technology Stack

- **Backend**: Spring Boot 3.4.5, Java 17
- **Security**: Spring Security with OAuth2/JWT
- **Database**: H2 (development), MySQL/PostgreSQL (production)
- **Messaging**: Apache Kafka
- **Service Discovery**: Eureka Server
- **API Gateway**: Spring Cloud Gateway
- **Configuration**: Spring Cloud Config
- **Frontend**: Angular
- **Build Tool**: Maven

## Features

- **Microservices Architecture**: Scalable, independent services
- **Service Discovery**: Automatic service registration and discovery
- **Centralized Configuration**: Dynamic configuration management
- **Security**: OAuth2-based authentication and authorization
- **Event-Driven**: Kafka-based asynchronous communication
- **API Gateway**: Single entry point with routing and security
- **Database per Service**: Independent data storage per microservice
- **Docker Ready**: Containerized deployment support

## Quick Start

### Prerequisites

- Java 17+
- Maven 3.6+
- Node.js 16+ (for UI)
- Docker (optional)
- MySQL/PostgreSQL (for production)

### Running the Platform

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/edu-services.git
   cd edu-services
   ```

2. **Build all services**
   ```bash
   mvn clean install
   ```

3. **Start Infrastructure Services**
   - Eureka Server (Port 8761)
   - Config Server (Port 8888)
   - API Gateway (Port 8080)

4. **Start Core Services**
   ```bash
   # Individual service startup
   cd security-service && mvn spring-boot:run
   cd student-service && mvn spring-boot:run
   # ... other services
   ```

5. **Start Frontend**
   ```bash
   cd campus-mgmt-ui
   npm install
   ng serve
   ```

### Service Ports

| Service | Port | Context Path |
|---------|------|--------------|
| Eureka Server | 8761 | - |
| Config Server | 8888 | - |
| API Gateway | 8080 | - |
| Security Service | 8086 | /api/v1/security |
| Student Service | 8083 | /api/v1/student |
| Teacher Service | 8084 | /api/v1/teacher |
| Campus Mgmt Service | 8080 | /api/v1/campus-mgmt |
| Profiles Service | 8082 | /api/v1/profiles |
| Classes Service | 8085 | /api/v1/classes |

## Configuration

### Database Configuration

Each service can be configured to use different database profiles:

- **H2**: In-memory database (default for development)
- **MySQL**: Production-ready relational database
- **PostgreSQL**: Alternative production database

Example configuration for MySQL:
```yaml
spring:
  profiles:
    active: mysql
  datasource:
    url: jdbc:mysql://localhost:3306/edu_services
    username: root
    password: password
```

### Security Configuration

The platform uses Keycloak for authentication and authorization:

- **Keycloak Server**: http://localhost:8085
- **Realm**: securityrealm
- **Client IDs**: Configured per service

### Kafka Configuration

Services communicate through Kafka topics:

- **Bootstrap Servers**: localhost:9092
- **Topics**: User events, profile updates, enrollment events

## Development

### Project Structure

```
edu-services/
|-- pom.xml                     # Parent POM
|-- address-service/            # Address management
|-- campus-mgmt-service/        # Campus management
|-- classes-service/            # Class management
|-- cloud-configs/              # Infrastructure services
|-- common-dto/                 # Shared DTOs
|-- common-service/             # Shared utilities
|-- configuration-service/      # Configuration management
|-- enrollment-service/         # Enrollment management
|-- organization-service/       # Organization management
|-- profiles-service/           # Profile management
|-- registrations-service/      # Registration management
|-- security-service/           # Security service
|-- services-config/            # Service configurations
|-- student-service/            # Student management
|-- teacher-service/            # Teacher management
```

### Adding New Services

1. Create new service directory
2. Add service to parent POM modules
3. Configure service discovery and dependencies
4. Implement service interfaces
5. Update API Gateway routing

### API Documentation

Each service exposes OpenAPI documentation at:
- Swagger UI: `http://localhost:{port}/swagger-ui.html`
- OpenAPI JSON: `http://localhost:{port}/v3/api-docs`

## Deployment

### Docker Deployment

Build and run services with Docker:
```bash
# Build all services
mvn clean package

# Run with Docker Compose
docker-compose up -d
```

### Production Considerations

- Use external databases instead of embedded H2
- Configure proper SSL/TLS certificates
- Set up monitoring and logging
- Configure backup strategies
- Implement CI/CD pipelines

## Monitoring

### Health Endpoints

Each service exposes health endpoints:
- Health Check: `/actuator/health`
- Metrics: `/actuator/metrics`
- Info: `/actuator/info`

### Management Ports

- Security Service: 7000
- Student Service: 7002
- Other services: 7001, 7003, etc.

## Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Code Style

- Follow Java coding conventions
- Use meaningful variable names
- Add proper documentation
- Write unit tests
- Ensure services are loosely coupled

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support

For support and questions:
- Create an issue in the GitHub repository
- Check the documentation
- Review the API specifications

## Roadmap

- [ ] Enhanced analytics and reporting
- [ ] Mobile application support
- [ ] Advanced notification system
- [ ] Integration with external educational tools
- [ ] Multi-tenant support
- [ ] Advanced security features

---

**Note**: This is an open-source project designed for educational institutions. Feel free to contribute, suggest improvements, or report issues.
