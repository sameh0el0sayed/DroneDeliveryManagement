# Drone Delivery Management System

A backend system for managing drone-based delivery operations.  
The platform supports **users**, **drones**, and **administrators**, enabling order creation, assignment, tracking, and real-time drone heartbeats using event-driven communication.

---

## 1. Technologies Used

### Backend
- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security (JWT Authentication)
- Hibernate
- Jackson (JSON serialization/deserialization)

### Messaging
- NATS (event-driven communication for drone heartbeats)

### Database
- H2 (development)
- PostgreSQL

### Testing & Tools
- JUnit 5
- Mockito
- Postman (API testing)

---

## 2. API Endpoints

| Method | Endpoint | Description | Role |
|------|---------|------------|------|
| POST | `/auth/users/register` | Register a new user (ADMIN, USER, DRONE) | ADMIN |
| POST | `/auth/users/login` | Authenticate user and return JWT | ALL |
| GET | `/api/admin/orders` | Retrieve all orders | ADMIN |
| PUT | `/api/admin/orders/{orderId}/location` | Update order origin or destination | ADMIN |
| GET | `/api/admin/drones` | Retrieve all drones | ADMIN |
| PUT | `/api/admin/drones/{droneId}/broken` | Mark a drone as broken | ADMIN |
| PUT | `/api/admin/drones/{droneId}/fixed` | Mark a drone as fixed | ADMIN |
| POST | `/api/user/orders` | Submit a new order | USER |
| GET | `/api/user/orders` | Retrieve user orders | USER |
| GET | `/api/user/orders/{orderId}` | Retrieve order by ID | USER |
| POST | `/api/user/orders/{orderId}/withdraw` | Withdraw an order | USER |
| POST | `/api/drone/orders/reserve` | Reserve an available order | DRONE |
| POST | `/api/drone/orders/{orderId}/grab` | Grab a reserved order | DRONE |
| PUT | `/api/drone/orders/{orderId}/delivered` | Mark order as delivered | DRONE |
| PUT | `/api/drone/orders/{orderId}/failed` | Mark order as failed | DRONE |
| PUT | `/api/drone/broken` | Mark the current drone as broken | DRONE |
| PUT | `/api/drone/heartbeat` | Send drone location heartbeat | DRONE |
| GET | `/api/drone/orders/current` | Retrieve current assigned order | DRONE |

---

## 3. How to Implement and Use the Application

### Step 1: Clone the Repository
```bash
git clone https://github.com/sameh0el0sayed/DroneDeliveryManagement.git
cd DroneDeliveryManagement
```
### Step 2. Create PostgreSQL Database

Create a database in PostgreSQL:
```bash
CREATE DATABASE DroneAppDB;
```

## Step 3. Download and Run NATS
Default port used by the application:
```bash
4222
```

## Step 4. Run the Application

```
mvn clean install
mvn spring-boot:run
```
The application will start at:
```
http://localhost:8080
```
### Step 5. Test APIs Using Postman Collection
Import the Postman collection included in the repository:
[DroneApp.postman_collection.json ](https://github.com/sameh0el0sayed/DroneDeliveryManagement/blob/master/DroneApp.postman_collection.json)

### Step 6. Authentication and Roles
Use authentication APIs to register and login users.
```
Supported roles:

ROLE_ADMIN
ROLE_DRONE
ROLE_USER
```
### Step 7. Unit Testing
Controller unit tests are located at:
```
src/test/java/com/example/DroneApp/ControllerTest
```
Run tests using:
```
mvn test
```
