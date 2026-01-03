# ♟️ Chess-Mate

A real-time multiplayer chess application backend built with Spring Boot, featuring secure authentication and live gameplay through WebSockets.

## 📋 Overview

Chess-Mate is a robust backend service for a real-time chess application that enables players to compete against each other online. The application leverages Spring Boot's powerful ecosystem to provide secure user management, real-time game updates, and accurate chess rule enforcement.

## ✨ Features

### 🔐 Authentication & Security
- **JWT-based Authentication**: Secure token-based authentication system
- **Spring Security Integration**: Role-based access control and endpoint protection
- **User Registration & Login**: Complete user management system

### ♟️ Chess Gameplay
- **Real-time Gameplay**: WebSocket-based live game updates
- **Move Validation**:  Server-side move validation using chesslib
- **Game State Management**: FEN (Forsyth-Edwards Notation) based game state tracking
- **Game Status Detection**: Automatic detection of check, checkmate, and stalemate
- **Matchmaking System**: Automatic pairing of players looking for games

### 🎮 Game Features
- Player rating system (ELO)
- Game history tracking
- Move history for each game
- Resignation support
- Multiple concurrent games support

## 🛠️ Technology Stack

- **Framework**: Spring Boot 3.5.4
- **Language**: Java 17
- **Database**: PostgreSQL
- **Authentication**:  JWT (JSON Web Tokens) with jjwt library
- **Real-time Communication**: WebSockets with STOMP protocol
- **Chess Logic**: chesslib library
- **ORM**: Spring Data JPA with Hibernate
- **Build Tool**: Maven
- **Additional Libraries**:  Lombok, SockJS

## 📁 Project Structure

```
src/main/java/com/chess/chess/
├── config/              # Configuration classes
│   ├── CorsConfig.java
│   ├── SocketConfig.java
│   ├── SecurityConfig.java
│   └── CustomUserDetailsService.java
├── controller/          # REST and WebSocket controllers
│   ├── UserController.java
│   ├── MoveController.java
│   └── GameController.java
├── dto/                 # Data Transfer Objects
│   ├── LoginDto.java
│   ├── RegisterDto.java
│   ├── MoveRequest.java
│   ├── MoveResponse.java
│   └── JoinResponse.java
├── models/              # Entity classes
│   ├── User. java
│   ├── Game.java
│   ├── Move.java
│   └── GameStatus.java
├── repo/                # Repository interfaces
│   ├── UserRepo.java
│   ├── GameRepo.java
│   └── MoveRepo.java
└── service/             # Business logic
    ├── UserService.java
    ├── GameService.java
    ├── ChessService.java
    ├── MoveService.java
    └── JWTService.java
```

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- PostgreSQL database
- Maven 3.6+

### Environment Variables

Create a `.env` file or set the following environment variables:

```properties
POSTGRES_URL=jdbc:postgresql://localhost:5432/chessmate
DB_PASSWORD=your_database_password
JWT_SECRET=your_jwt_secret_key
```

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/TusharManudi/Chess-Mate. git
   cd Chess-Mate
   ```

2. **Configure the database**
   - Create a PostgreSQL database named `chessmate`
   - Update the environment variables with your database credentials

3. **Build the project**
   ```bash
   ./mvnw clean install
   ```

4. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

The server will start on `http://localhost:8080`

## 🔌 API Endpoints

### Authentication Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/register` | Register a new user |
| POST | `/auth/login` | Login and receive JWT token |

### WebSocket Endpoints

| Destination | Description |
|-------------|-------------|
| `/app/join` | Join or create a game |
| `/app/move` | Make a chess move |
| `/app/resign` | Resign from current game |

### Subscription Topics

| Topic | Description |
|-------|-------------|
| `/topic/game-{gameId}` | Receive game updates |
| `/topic/game-start-{userId}` | Notification when game starts |
| `/topic/errors-{userId}` | Error messages for specific user |

## 🎯 WebSocket Connection

Connect to WebSocket endpoint:
```
ws://localhost:8080/games
```

Example STOMP connection with authentication:
```javascript
const socket = new SockJS('http://localhost:8080/games');
const stompClient = Stomp.over(socket);

stompClient.connect(
  { Authorization: `Bearer ${jwtToken}` },
  onConnected,
  onError
);
```

## 🎮 How It Works

### Game Flow

1. **User Registration/Login**: Users register or login to receive a JWT token
2. **Game Joining**: Players send a join request via WebSocket
3. **Matchmaking**: The system pairs waiting players or creates a new game
4. **Gameplay**: Players make moves which are validated and broadcast in real-time
5. **Game Completion**: The system detects checkmate, stalemate, or resignation

### Move Validation

All moves are validated server-side using the chesslib library, which ensures:
- Legal move validation according to chess rules
- Check and checkmate detection
- Stalemate detection
- Proper turn enforcement

## 📊 Database Schema

### User Entity
- UUID id
- String username
- String email (unique)
- String password (encrypted)
- int rating (default:  500)

### Game Entity
- UUID id
- User white (player)
- User black (player)
- GameStatus status (WAITING, IN_GAME, FINISHED)
- String result
- String fen (board state)
- LocalDateTime startTime
- LocalDateTime endTime

### Move Entity
- int id
- Game game
- String fromSquare
- String toSquare
- String promotion
- String fen
- LocalDateTime timestamp

## 🔒 Security Features

- Password encryption with BCrypt
- JWT token-based authentication
- CORS configuration for allowed origins
- WebSocket authentication via JWT
- SQL injection prevention through JPA
- XSS protection with Spring Security

## 🌐 CORS Configuration

Currently configured to allow requests from:
- `http://localhost:3000`
- `http://localhost:3001`
- `http://localhost:5173`

Update `CorsConfig.java` to modify allowed origins for production. 

## 🧪 Testing

Run the test suite:
```bash
./mvnw test
```

## 📝 Configuration

### Application Properties
Located in `src/main/resources/application.properties`:
- Database connection settings
- JPA/Hibernate configuration
- JWT secret key configuration

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is open source and available under the MIT License. 

## 👤 Author

**Tushar Manudi**
- GitHub: [@TusharManudi](https://github.com/TusharManudi)

## 🙏 Acknowledgments

- [chesslib](https://github.com/bhlangonijr/chesslib) - Chess logic library
- Spring Boot team for the excellent framework
- PostgreSQL for robust database support

## 📞 Support

For support, please open an issue in the GitHub repository. 

---

⭐ If you find this project helpful, please consider giving it a star! 
