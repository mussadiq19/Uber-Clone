# 🚖 Uber Ride Booking Frontend

A modern web frontend for the **Uber Ride Booking System**, built with **HTML, CSS, and Vanilla JavaScript**. This application communicates with a Spring Boot microservices backend through REST APIs and WebSockets to simulate a real-time ride-booking platform similar to Uber.

---

## 📖 Overview

The frontend provides separate interfaces for **Passengers** and **Drivers**, enabling users to:

* Book rides
* Update driver locations
* Find nearby drivers
* Receive ride requests in real time
* Accept or reject ride requests
* Track booking status

The application is designed to work seamlessly with the Uber Ride Booking Microservices architecture.

---

## ✨ Features

### 👤 Passenger

* Create a new ride booking
* Search nearby available drivers
* View active booking details
* Cancel an existing booking
* Real-time booking status updates

### 🚗 Driver

* Connect to WebSocket server
* Update current GPS coordinates
* Receive live ride requests
* Accept ride requests
* Reject ride requests
* Automatically update booking status

---

## 🏗️ Architecture

```text
                    Passenger Frontend
                           │
                           ▼
                   Booking Service
                           │
               ┌───────────┴───────────┐
               ▼                       ▼
      Location Service         Socket Service
               │                       │
               ▼                       ▼
      Nearby Drivers          WebSocket Server
                                       │
                                       ▼
                              Driver Frontend
```

---

## 🛠️ Tech Stack

### Frontend

* HTML5
* CSS3
* JavaScript (ES6)
* Fetch API
* STOMP.js
* SockJS

### Backend

* Spring Boot
* Spring Web
* Spring Data JPA
* Hibernate
* Retrofit
* WebSockets
* MySQL
* Eureka Service Discovery

---

## 📂 Project Structure

```text
UberFrontend/
│
├── index.html
│
├── css/
│   └── style.css
│
├── js/
│   ├── config.js
│   ├── api-client.js
│   ├── websocket-client.js
│   ├── passenger-handler.js
│   └── driver-handler.js
│
└── assets/
```

---

## 🚀 Getting Started

### Clone the repository

```bash
git clone https://github.com/mussadiq19/UberFrontend.git
```

```bash
cd UberFrontend
```

---

### Start the frontend server

Using Python

```bash
python -m http.server 8002
```

or

```bash
python3 -m http.server 8002
```

---

## 🌐 Access the Application

### Passenger

```text
http://localhost:8002/index.html?role=passenger&passengerId=1
```

### Driver

```text
http://localhost:8002/index.html?role=driver&driverId=1
```

Open both URLs in separate browser tabs to simulate the complete ride-booking flow.

---

# 🔗 Backend Microservices

The frontend depends on the following Spring Boot microservices.

| Service                 | Repository                                                       |
| ----------------------- | ---------------------------------------------------------------- |
| Booking Service         | https://github.com/mussadiq19/UberProject-BookingService         |
| Location Service        | https://github.com/mussadiq19/UberProject-LocationService        |
| WebSocket Service       | https://github.com/mussadiq19/UberProject-WebSockets             |
| Eureka Discovery Server | https://github.com/mussadiq19/UberProject-Discovery-EurekaServer |
| Entity Service          | https://github.com/mussadiq19/UberProject-EntityService          |
| Authentication Service  | https://github.com/mussadiq19/UberProject-AuthService            |
| Review Service          | https://github.com/mussadiq19/UberProject-ReviewService          |

---

## 🔄 Ride Booking Flow

```text
Passenger

      │

      ▼

Create Booking

      │

      ▼

Booking Service

      │

      ▼

Location Service

      │

      ▼

Find Nearby Drivers

      │

      ▼

Socket Service

      │

      ▼

Driver receives Ride Request

      │

      ▼

Accept / Reject Ride

      │

      ▼

Booking Updated

      │

      ▼

Passenger receives Status Update
```

---

## 📡 API Services Used

### Booking Service

```
POST /api/v1/booking
```

```
POST /api/v1/booking/{bookingId}
```

---

### Location Service

```
POST /api/location/drivers
```

```
POST /api/location/nearby/drivers
```

---

### Socket Service

```
POST /api/socket/newride
```

WebSocket Endpoint

```
/ws
```

---

## ⚙️ Configuration

The frontend endpoints are configured inside:

```
js/config.js
```

Example:

```javascript
const CONFIG = {

    BOOKING_API: "http://localhost:8000/api/v1",

    LOCATION_API: "http://localhost:7777/api/location",

    SOCKET_API: "http://localhost:8080/api/socket",

    WEBSOCKET_URL: "http://localhost:8080/ws"

};
```

---

## 📸 Demo Workflow

1. Start all backend microservices.
2. Launch the frontend.
3. Open the Passenger page.
4. Open the Driver page.
5. Driver updates location.
6. Passenger searches nearby drivers.
7. Passenger creates booking.
8. Driver receives ride request instantly.
9. Driver accepts the request.
10. Booking status updates successfully.

---

## 📋 Requirements

* Java 21+
* Spring Boot Backend
* MySQL
* Python 3 (or any static web server)
* Modern Browser
* Internet connection (for CDN libraries)

---

## 🔮 Future Improvements

* Google Maps integration
* Live driver tracking
* JWT Authentication
* Payment Gateway
* Fare Estimation
* Driver Availability Toggle
* Ride History
* User Profiles
* Booking Timeline
* Responsive Mobile UI
* Progressive Web App (PWA)
* Push Notifications

---

## 🤝 Contributing

Contributions, issues, and feature requests are welcome.

If you have ideas to improve the project, feel free to fork the repository and submit a pull request.

---

## 📜 License

This project was developed for educational purposes to demonstrate a complete **microservices-based ride booking system** using Spring Boot, REST APIs, Retrofit, WebSockets, and Vanilla JavaScript.

---

## 👨‍💻 Author

**Mussadiq Fayaz Durani**

GitHub: https://github.com/mussadiq19
GitHub Profile: https://github.com/mussadiq19
