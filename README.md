🛒 ShopVerse – E-commerce Backend (Spring Boot)

ShopVerse is a modern e-commerce backend API built with Java Spring Boot, featuring secure authentication, role-based access control, and robust order & product management.
This repository contains the fully functional backend for the ShopVerse application.
A React.js frontend is currently in development to provide a clean, minimalist, and modern user interface.

<p align="center">
  <img width="800" alt="ShopVerse Screenshot" src="https://github.com/user-attachments/assets/92b83045-0ebb-4573-80e5-fd5ceaf1cd0e" />
</p>


⸻

Features
	User Authentication & Authorization
	•	JWT-based authentication
	•	Role-based access (USER, ADMIN)
	•	Password hashing for security
	Product Management
	•	Add, update, delete, and view products
	•	Category association
	Cart & Order System
	•	Add/remove items from cart
	•	Place and cancel orders
	•	Payment simulation endpoint
	Security & Documentation
	•	Spring Security Integration
	•	Method-level authorization using @PreAuthorize
	•	Swagger API Documentation
	•	Easy API testing through Swagger UI
	Deployment
	•	Dockerized backend and ready for containerized deployment

⸻

Tech Stack

Backend:
	•	Java 21
	•	Spring Boot
	•	Spring Security & JWT
	•	JPA / Hibernate
	•	MySQL
	•	Maven

Frontend (Ongoing Development):
	•	React.js
	•	Axios for API calls
	•	TailwindCSS / Styled Components for UI styling

Installation & Setup

Clone Repository
git clone https://github.com/jFern1010/Shopverse.git

cd shopverse-backend


Configure Database

Update application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/shopverse

spring.datasource.username=<your-db-username>

spring.datasource.password=<your-db-password>


Run Backend Application
mvn spring-boot:run

Backend will be available at:
http://localhost:5000

Run Frontend (Development)
Open a new terminal and navigate to the frontend folder:

cd Shopverse/frontend

npm install

npm start

The React frontend will run on http://localhost:5173
It communicates with the backend at http://localhost:5000
The frontend is not fully complete but functional enough to interact with backend APIs.


Docker Setup
Build the image:
docker build -t shopverse-app .

Run the container:
docker run -p 5000:5000 shopverse-app


API Documentation

Swagger UI:
http://localhost:5000/swagger-ui/index.html

⸻

Current Status
	•	Backend: Fully functional and tested
	•	Frontend: React.js development in progress
(Minimalist and modern UI design inspired by top e-commerce platforms)

⸻

Notes

This backend project is production-ready and currently being integrated with a React frontend.
Follow the repository for updates on the complete full-stack release.
Database credentials are intentionally placeholders for security. Users should configure their own local database.

