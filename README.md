# PetScan

PetScan is a mobile application designed to streamline health tracking for domestic animals. It enables pet owners to monitor vaccinations, schedule reminders, and manage overall healthcare effortlessly. Our platform provides a comprehensive system for storing and managing pet health information through an intuitive interface tailored for ease of use.

Our capstone project enhances PetScan by integrating **AI-powered features** capable of analyzing photos of animals to accurately detect health issues, such as pink eye, and providing users with real-time information. By facilitating veterinary care management and offering advanced diagnostic support, PetScan contributes to improved animal well-being and simplifies the responsibilities of pet owners.

---

## Table of Contents
- [Software Architecture](#software-architecture)
- [Docker Image](#docker-image)
- [Technologies Used](#technologies-used)
- [Backend Project Structure](#backend-project-structure)
- [Dependencies](#dependencies)
- [Getting Started](#getting-started)
- [Video Demonstration](#video-demonstration)
- [Contributing](#contributing)

---

## Software Architecture
The application architecture consists of:
- **Frontend:** HTML and CSS for the web interface.
- **Backend:** Spring Boot and Django for server-side logic.
- **Communication:** HTTP client for communication between frontend and backend.

---

## Docker Image
### Backend:
```yaml
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
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/pets?createDatabaseIfNotExist=true
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: rootpassword
      SPRING_SECURITY_USER_NAME: admin
      SPRING_SECURITY_USER_PASSWORD: admin
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
Frontend:
yaml
Copy code
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
      DEBUG: 1
      DJANGO_ALLOWED_HOSTS: localhost,127.0.0.1
      SECRET_KEY: django-insecure-key

volumes:
  static_volume:
  media_volume:
Technologies Used
Web Frontend:
HTML
CSS
Python
Django
Mobile:
Kotlin
Retrofit
Backend:
Spring Boot
MySQL
Backend Project Structure
ma.ensa.pet.config: Configuration files for Spring Boot.
ma.ensa.pet.controller: RESTful APIs to handle HTTP requests and responses.
ma.ensa.pet.model: Data entities with JPA annotations.
ma.ensa.pet.repository: Interfaces for database operations using Spring Data JPA.
ma.ensa.pet.dto: Data Transfer Objects for API communication.
ma.ensa.pet.exception: Custom exceptions and global error handling.
ma.ensa.pet.security: JWT-based authentication and authorization.
ma.ensa.pet.service: Business logic operations.
Dependencies
Spring Data JPA:
xml
Copy code
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
MySQL Connector/J:
xml
Copy code
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
</dependency>
Getting Started
Prerequisites
Git: Install from git-scm.com.
Docker: Install from docker.com.
Django: Install using npm install Django.
Project Setup
Step 1: Clone the Repository
bash
Copy code
git clone <repository_url>
cd <project_folder>
Step 2: Backend Setup (Spring Boot and MySQL)
Configure Docker Compose:
Ensure docker-compose.yml is properly configured for backend services.
Start Backend Services:
bash
Copy code
docker-compose up --build
Verify the Backend:
Access: http://localhost:8010
Step 3: Web Frontend Setup (Django)
Configure the Django Environment:
Ensure the environment variables in docker-compose.yml are set.
Run Frontend Services:
bash
Copy code
docker-compose up --build
Verify the Web Frontend:
Access: http://localhost:8000
Video Demonstration
Click here to watch the demonstration

Contributing
We welcome contributions to enhance PetScan! Please follow these steps:

Fork the repository.
Create a feature branch.
Submit a pull request.
Contributors
Younes Amerga (GitHub)
Mohammed Aziz (GitHub)
Mohamed Lachgar (ResearchGate)
lua
Copy code
