# PetScan 🐾

![image](https://github.com/user-attachments/assets/2889161d-764d-432c-b99c-348faa72a6ea)

PetScan is a mobile application designed to streamline health tracking for domestic animals. It enables pet owners to monitor vaccinations, schedule reminders, and manage overall healthcare effortlessly. The platform provides a comprehensive system for storing and managing pet health information through an intuitive interface tailored for ease of use.

Our capstone project enhances PetScan by integrating AI-powered features capable of analyzing photos of animals to accurately detect health issues, such as pink eye, and providing users with real-time information. By facilitating veterinary care management and offering advanced diagnostic support, PetScan contributes to improved animal well-being and simplifies the responsibilities of pet owners.

## Table of Contents
- [Software Architecture](#software-architecture)
- [Docker Image](#docker-image)
- [Frontend](#frontend)
- [Backend](#backend)
- [Getting Started](#getting-started)
- [Video Demonstration](#video-demonstration)
- [Contributing](#contributing)

## Software Architecture
![WhatsApp Image 2024-12-29 at 20 18 57_d5b9bed4](https://github.com/user-attachments/assets/e80d2bb6-2e1a-40b2-b2b0-89ccacfd2c8f)

![mobile_architecture 1](https://github.com/user-attachments/assets/418d5f2e-4991-4fc5-9ad2-6ce2222087f8)

The application architecture uses HTML and CSS for the frontend of the web and Spring Boot and Django for the backend, with communication via an HTTP client.

## Docker Image

### Backend Docker Configuration
```yaml
# docker-compose.yml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    container_name: pet-mysql
    environment:
      MYSQL_DATABASE: pets
      MYSQL_ROOT_PASSWORD: rootpassword
    ports:
      - "3307:3306"
    volumes:
      - mysql_data:/var/lib/mysql
    networks:
      - pet-network

  app:
    build: .
    container_name: pet-app
    depends_on:
      - mysql
    ports:
      - "8010:8010"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/pets?createDatabaseIfNotExist=true
      - SPRING_DATASOURCE_USERNAME=root
      - SPRING_DATASOURCE_PASSWORD=rootpassword
      - SPRING_JPA_HIBERNATE_DDL_AUTO=update
      - SPRING_JPA_SHOW_SQL=true
      - SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT=org.hibernate.dialect.MySQL8Dialect
      - SPRING_JPA_PROPERTIES_HIBERNATE_FORMAT_SQL=true
      - APP_JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
      - APP_JWT_EXPIRATION_MS=86400000
      - APP_UPLOAD_DIR=/app/uploads/products
      - APP_QRCODE_STORAGE_LOCATION=/app/uploads/qrcodes/
      - APP_QRCODE_BASE_URL=http://localhost:8000/api/pets/track/
      - SPRING_SECURITY_USER_NAME=admin
      - SPRING_SECURITY_USER_PASSWORD=admin
    volumes:
      - app_uploads:/app/uploads
    networks:
      - pet-network

volumes:
  mysql_data:
  app_uploads:

networks:
  pet-network:
    driver: bridge
```

### Frontend Docker Configuration
```yaml
version: '3.8'

services:
  web:
    build: .
    container_name: django_app
    command: >
      sh -c "python manage.py migrate &&
             python manage.py runserver 0.0.0.0:8000"
    volumes:
      - .:/app
      - static_volume:/app/static
      - media_volume:/app/media
    ports:
      - "8000:8000"
    environment:
      - DEBUG=1
      - DJANGO_ALLOWED_HOSTS=localhost,127.0.0.1
      - SECRET_KEY=django-insecure-oe9!^5%r*mis@8s))wz0(y^qq#^re+#hgmdxx*0m4g9q_ytyfp

volumes:
  static_volume:
  media_volume:
```

## Technologies Used

### Web Frontend
- HTML
- CSS
- Python
- Django

### Mobile Frontend
- Kotlin
- Retrofit

### Backend
- Spring Boot
- MySQL

## Backend Project Structure

### 1. `ma.ensa.pet.config`
- Purpose: Contains configuration files and setup classes for the Spring Boot application.

### 2. `ma.ensa.pet.controller`
- Purpose: Handles incoming HTTP requests and responses
- Key Components:
  - Controller Classes for RESTful APIs
  - Request parsing and response handling
  - Service layer interaction

### 3. `ma.ensa.pet.model`
- Purpose: Data entity representation
- Key Components:
  - JPA annotated entity classes
  - Database table mappings
  - Relationship definitions

### 4. `ma.ensa.pet.repository`
- Purpose: Database operation abstraction
- Key Components:
  - JPA repository interfaces
  - CRUD operation methods
  - Custom query definitions

### 5. `ma.ensa.pet.dto`
- Purpose: Data Transfer Objects
- Key Components:
  - API request/response structures
  - Data mapping classes

### 6. `ma.ensa.pet.exception`
- Purpose: Exception handling
- Key Components:
  - Custom exception classes
  - Global exception handler

### 7. `ma.ensa.pet.security`
- Purpose: Application security management
- Key Components:
  - JWT authentication
  - Endpoint security configuration
  - Role-based access control

### 8. `ma.ensa.pet.service`
- Purpose: Business logic implementation
- Key Components:
  - Service layer classes
  - Business rule implementation
  - Repository interaction

## Dependencies

### Core Dependencies
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
</dependency>
```

## Getting Started

### Prerequisites
1. Git (Download from [git-scm.com](https://git-scm.com))
2. Docker and Docker Compose (Download from [docker.com](https://docker.com))
3. Django (Install via `npm install Django`)

### Project Setup

#### Step 1: Clone the Repository
```bash
git clone <repository_url>
cd <project_folder>
```

#### Step 2: Backend Setup (Spring Boot and MySQL)
1. Configure Docker Compose file
2. Start backend services:
```bash
docker-compose up --build
```
3. Verify backend at `http://localhost:8010`

#### Step 3: Web Frontend Setup (Django)
1. Configure environment variables
2. Start frontend services:
```bash
docker-compose up --build
```
3. Verify frontend at `http://localhost:8000`

### Troubleshooting Tips
- Use `docker-compose logs -f` for monitoring
- Check Docker permissions if encountering access issues
- Verify all ports are available and not in use

## Video Demonstration
[[Link to demonstration video]](https://github.com/user-attachments/assets/07c335b0-8436-4bcf-9384-5b7bb36a77b8)

## Contributing
We welcome contributions from everyone! Please help us make this project better.

### Contributors
- Younes Amerga ([GitHub](https://github.com/Younessamg))
- Mohammed Aziz ([GitHub](https://github.com/aziz-laravel))
- Mohamed Lachgar ([ResearchGate](https://github.com/lachgar))
