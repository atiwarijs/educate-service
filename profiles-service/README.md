# Profile Service

A comprehensive Spring Boot microservice for managing user profiles, including personal details, education, experience, family, medical, and vehicle information.

## Overview

The Profile Service is a RESTful API built with Spring Boot that provides complete profile management capabilities. It handles various aspects of user profiles with secure data storage and retrieval.

## Technology Stack

- **Framework**: Spring Boot 3.x
- **Language**: Java 17+
- **Database**: MySQL 8.0
- **Security**: OAuth2 Resource Server with JWT
- **Messaging**: Apache Kafka
- **Documentation**: OpenAPI 3 (Swagger)
- **Build Tool**: Maven
- **Mapping**: MapStruct 1.5.5
- **Lombok**: For boilerplate code reduction

## Features

### Core Profile Management
- **Personal Details**: User's basic information with encryption for sensitive data
- **Address Management**: Complete address details with hierarchical structure
- **Education Records**: Academic qualifications and certifications
- **Work Experience**: Professional experience tracking
- **Family Information**: Family member details
- **Medical Records**: Health-related information
- **Vehicle Details**: Vehicle ownership and registration

### Technical Features
- **Data Encryption**: Sensitive fields are encrypted using AES encryption
- **Event-Driven Architecture**: Kafka integration for asynchronous processing
- **Generic CRUD Operations**: Base repository pattern for common operations
- **RESTful API Design**: Standardized endpoints with OpenAPI documentation
- **Security**: OAuth2 with JWT token validation
- **Validation**: Comprehensive input validation with custom error messages

## API Endpoints

### Personal Details
```
GET    /personal              - Get all personal details
GET    /personal/{id}         - Get personal details by ID
POST   /personal              - Create new personal details
PUT    /personal/{id}         - Update personal details
DELETE /personal/{id}         - Delete personal details
```

### Address Management
```
GET    /address               - Get all addresses
GET    /address/{id}          - Get address by ID
POST   /address               - Create new address
PUT    /address/{id}          - Update address
DELETE /address/{id}          - Delete address
```

### Education Records
```
GET    /education             - Get all education records
GET    /education/{id}        - Get education record by ID
POST   /education             - Create new education record
PUT    /education/{id}        - Update education record
DELETE /education/{id}        - Delete education record
```

### Work Experience
```
GET    /experience            - Get all experience records
GET    /experience/{id}       - Get experience record by ID
POST   /experience            - Create new experience record
PUT    /experience/{id}       - Update experience record
DELETE /experience/{id}       - Delete experience record
```

### Family Information
```
GET    /family                - Get all family members
GET    /family/{id}           - Get family member by ID
POST   /family                - Create new family member
PUT    /family/{id}           - Update family member
DELETE /family/{id}           - Delete family member
```

### Medical Records
```
GET    /medical               - Get all medical records
GET    /medical/{id}          - Get medical record by ID
POST   /medical               - Create new medical record
PUT    /medical/{id}          - Update medical record
DELETE /medical/{id}          - Delete medical record
```

### Vehicle Details
```
GET    /vehicle               - Get all vehicle records
GET    /vehicle/{id}          - Get vehicle record by ID
POST   /vehicle               - Create new vehicle record
PUT    /vehicle/{id}          - Update vehicle record
DELETE /vehicle/{id}          - Delete vehicle record
```

### Location Data
```
GET    /states                - Get all states
GET    /states/{id}           - Get state by ID
POST   /states                - Create new state
PUT    /states/{id}           - Update state
DELETE /states/{id}           - Delete state
```

## Database Schema

### Core Entities

#### PersonalDetails
- `id` (Primary Key)
- `firstName`, `lastName`, `middleName`
- `email`, `phone`, `mobile`
- `dateOfBirth`, `gender`
- `nationalId`, `passportNumber` (Encrypted)
- `maritalStatus`, `bloodGroup`
- `createdBy`, `updatedAt`

#### AddressDetails
- `id` (Primary Key)
- `addressLine1`, `addressLine2` (Encrypted)
- `city`, `district`, `state`, `postalCode`
- `countryCode`, `countryName`
- `latitude`, `longitude`
- `landmark`, `addressType`, `status`

#### EducationDetails
- `id` (Primary Key)
- `institutionName`, `degree`
- `fieldOfStudy`, `grade`
- `startDate`, `endDate`
- `isCurrent`, `description`

#### ExperienceDetails
- `id` (Primary Key)
- `companyName`, `position`
- `department`, `employmentType`
- `startDate`, `endDate`
- `responsibilities`, `achievements`
- `isCurrent`

#### FamilyMember
- `id` (Primary Key)
- `relationship`, `fullName` (Encrypted)
- `dateOfBirth`, `gender`
- `contactNumber` (Encrypted)
- `occupation`, `notes`

#### MedicalDetails
- `id` (Primary Key)
- `bloodType`, `allergies` (Encrypted)
- `medications` (Encrypted)
- `conditions` (Encrypted)
- `emergencyContact`, `doctorName`

#### VehicleDetails
- `id` (Primary Key)
- `make`, `model`, `year`
- `licensePlate` (Encrypted)
- `vehicleType`, `color`
- `registrationDate`, `expiryDate`

## Security

### Authentication & Authorization
- OAuth2 Resource Server configuration
- JWT token validation
- Role-based access control (RBAC)
- API endpoint security

### Data Protection
- AES encryption for sensitive personal information
- Secure password handling
- Input validation and sanitization
- SQL injection prevention

## Configuration

### Application Properties
```yaml
spring:
  application:
    name: profile-service
  datasource:
    url: jdbc:mysql://localhost:3306/profile_db
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
  kafka:
    bootstrap-servers: ${KAFKA_SERVERS:localhost:9092}
    consumer:
      group-id: profile-service-group
```

### Environment Variables
- `DB_USERNAME`: Database username
- `DB_PASSWORD`: Database password
- `KAFKA_SERVERS`: Kafka bootstrap servers
- `JWT_ISSUER_URI`: JWT token issuer URI

## Kafka Integration

### Topics
- `user-created`: New user creation events
- `profile-updated`: Profile modification events
- `profile-deleted`: Profile deletion events

### Event Listeners
- `UserCreatedEventListener`: Processes user creation events
- Automatic profile initialization for new users

## API Documentation

The service provides comprehensive API documentation through OpenAPI 3.0:

- **Swagger UI**: `/swagger-ui.html`
- **OpenAPI JSON**: `/v3/api-docs`

## Error Handling

### Global Exception Handler
- Standardized error response format
- Validation error handling
- Resource not found handling
- Security exception handling

### Response Format
```json
{
  "timestamp": "2024-01-01T12:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "details": {
    "field": "email",
    "reason": "Invalid email format"
  }
}
```

## Development

### Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 8.0+
- Apache Kafka 2.8+

### Running Locally
```bash
# Clone the repository
git clone <repository-url>
cd profile-service

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

### Docker Support
```bash
# Build Docker image
docker build -t profile-service .

# Run with Docker
docker run -p 8080:8080 profile-service
```

## Testing

### Unit Tests
- Repository layer testing
- Service layer testing
- Controller layer testing

### Integration Tests
- Database integration
- Kafka integration
- Security testing

### Test Coverage
- Target: >80% code coverage
- Includes edge cases and error scenarios

## Monitoring & Logging

### Actuator Endpoints
- `/actuator/health`: Health check
- `/actuator/metrics`: Application metrics
- `/actuator/info`: Application information

### Logging
- Structured logging with Logback
- Log levels: ERROR, WARN, INFO, DEBUG
- Request/response logging for debugging

## Deployment

### Production Considerations
- Environment-specific configuration
- Database connection pooling
- Caching strategies
- Load balancing
- Health checks

### Kubernetes Deployment
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: profile-service
spec:
  replicas: 3
  selector:
    matchLabels:
      app: profile-service
  template:
    metadata:
      labels:
        app: profile-service
    spec:
      containers:
      - name: profile-service
        image: profile-service:latest
        ports:
        - containerPort: 8080
```

## Contributing

### Code Standards
- Follow Java coding conventions
- Use meaningful variable names
- Add comprehensive comments
- Write unit tests for new features

### Git Workflow
- Feature branches for new development
- Pull requests for code review
- Automated CI/CD pipeline

## Version History

- **v1.0.0**: Initial release with basic profile management
- **v1.1.0**: Added encryption for sensitive data
- **v1.2.0**: Enhanced security with OAuth2
- **v1.3.0**: Kafka integration for event processing

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Contact

For support and inquiries:
- **Project Lead**: Ashish Kumar
- **Email**: ashish@example.com
- **Repository**: [GitHub Repository URL]

---

**Note**: This service is part of a microservices architecture and should be deployed alongside other services for complete functionality.
