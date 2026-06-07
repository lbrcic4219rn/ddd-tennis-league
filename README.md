# DDD Tennis League

A Domain-Driven Design (DDD) implementation of a tennis league management system built with Spring Boot.

## Overview

DDD Tennis League is a modern Java application that manages tennis leagues with players, groups organized by skill level, matches, and leaderboards. The project demonstrates clean architecture principles using Domain-Driven Design patterns, application services, and a layered architecture.

## Architecture

The project follows Domain-Driven Design principles with a layered architecture:

```
src/main/java/com/github/lbrcic4219rn/dddtennisleague/
├── domain/                 # Domain Layer (business logic, entities, value objects)
│   ├── league/             # League aggregate root
│   ├── player/             # Player aggregate root
│   └── standing/           # Match and leaderboard aggregates
├── application/            # Application Layer (use cases, DTOs)
│   ├── league/             # League application services
│   ├── player/             # Player application services
│   └── standing/           # Match and standings application services
├── infrastructure/         # Infrastructure Layer (persistence, bootstrap)
│   ├── persistence/        # Repository implementations with H2
│   └── bootstrap/          # Data initialization
└── presentation/           # Presentation Layer (REST controllers)
    ├── league/             # League endpoints
    ├── player/             # Player endpoints
    └── standing/           # Match and standings endpoints
```

### Key Concepts

- **Domain Layer**: Contains pure business logic with no external dependencies
  - Aggregates: `League`, `Player`, `Match`, `Leaderboard`
  - Value Objects: `PlayerId`, `LeagueId`, `GroupId`, `SkillLevel`
  - Repositories: Interfaces for data persistence
  
- **Application Layer**: Orchestrates domain logic
  - Application Services handle use cases
  - DTOs (Data Transfer Objects) for request/response
  
- **Infrastructure Layer**: Technical implementations
  - JPA/Hibernate repositories for database operations
  - H2 embedded database for persistent storage
  - Bootstrap service for initial data loading

- **Presentation Layer**: REST API endpoints
  - Standard HTTP communication with clients
  - Request/response validation

## Features

### Players
- Register new tennis players with contact information
- Retrieve player profiles
- Delete player records
- List all players

### Leagues
- Create tennis leagues with start and end dates
- Organize players into groups by skill level (BEGINNER, INTERMEDIATE, ADVANCED)
- Manage league membership
- Delete leagues and groups

### Matches & Standings
- Create and schedule matches between players in a group
- Record match results with set scores
- Support for tie-breaks in sets
- Calculate leaderboard rankings based on match results
- Track match history and statistics

## Getting Started

### Prerequisites
- Java 21 or higher
- Maven 3.6+

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd ddd-tennis-league
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

## API Endpoints

### Players

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/players` | Register a new player |
| GET | `/api/players` | Get all players |
| GET | `/api/players/{playerId}` | Get player by ID |
| DELETE | `/api/players/{playerId}` | Delete a player |

**Example: Register Player**
```bash
POST /api/players
Content-Type: application/json

{
  "firstName": "Roger",
  "lastName": "Federer",
  "email": "roger@example.com",
  "phoneNumber": "+1-555-0001",
  "dateOfBirth": "1981-08-08"
}
```

### Leagues

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/leagues` | Create a new league |
| GET | `/api/leagues` | Get all leagues |
| GET | `/api/leagues/{leagueId}` | Get league by ID |
| DELETE | `/api/leagues/{leagueId}` | Delete a league |

**Example: Create League**
```bash
POST /api/leagues
Content-Type: application/json

{
  "name": "Spring Tennis League 2026",
  "startDate": "2026-05-07T10:00:00Z",
  "endDate": "2026-07-06T10:00:00Z"
}
```

### Groups

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/leagues/{leagueId}/groups` | Create a group in a league |
| GET | `/api/leagues/groups/{groupId}` | Get group by ID |
| DELETE | `/api/leagues/groups/{groupId}` | Delete a group |

**Example: Create Group**
```bash
POST /api/leagues/{leagueId}/groups
Content-Type: application/json

{
  "skillLevel": "INTERMEDIATE"
}
```

Skill levels: `BEGINNER`, `INTERMEDIATE`, `ADVANCED`

### Memberships

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/memberships` | Add player to group |
| GET | `/api/memberships` | Get all memberships |
| GET | `/api/memberships/{groupId}` | Get group memberships |
| DELETE | `/api/memberships/{membershipId}` | Remove player from group |

### Matches

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/matches` | Create a new match |
| POST | `/api/matches/{matchId}/complete` | Complete a match with results |
| GET | `/api/matches` | Get all matches |
| GET | `/api/matches/{matchId}` | Get match by ID |
| GET | `/api/matches/group/{groupId}` | Get matches in a group |
| DELETE | `/api/matches/{matchId}` | Delete a match |

**Example: Create Match**
```bash
POST /api/matches
Content-Type: application/json

{
  "groupId": "550e8400-e29b-41d4-a716-446655440000",
  "player1Id": "660e8400-e29b-41d4-a716-446655440001",
  "player2Id": "770e8400-e29b-41d4-a716-446655440002"
}
```

**Example: Complete Match**
```bash
POST /api/matches/{matchId}/complete
Content-Type: application/json

{
  "sets": [
    {
      "player1Games": 6,
      "player2Games": 4,
      "tieBreak": null
    },
    {
      "player1Games": 6,
      "player2Games": 3,
      "tieBreak": null
    }
  ]
}
```

### Standings

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/standings` | Create leaderboard for group |
| GET | `/api/standings` | Get all leaderboards |
| GET | `/api/standings/{leaderboardId}` | Get leaderboard by ID |
| GET | `/api/standings/group/{groupId}` | Get leaderboard for group |
| DELETE | `/api/standings/{leaderboardId}` | Delete leaderboard |

## Data Persistence

The application currently has only in-memory data persistence. 

## Bootstrap Data

On application startup, sample data is automatically loaded if the database is empty:
- 6 sample players (tennis legends)
- 1 league with 3 groups (BEGINNER, INTERMEDIATE, ADVANCED)
- 6 sample matches with results

This can be disabled by modifying `DataBootstrapper.java` or implementing conditional bootstrap logic.

## Development

### Running Tests
```bash
mvn test
```

### Building JAR
```bash
mvn clean package
```

The JAR will be in `target/ddd-tennis-league-0.0.1-SNAPSHOT.jar`

### Debugging
Set `spring.jpa.show-sql=true` in `application.properties` to see SQL queries.

## Project Structure Details

### Domain Models

**League Aggregate**
- League: Root entity with name and season
- Group: Sub-entities organized by skill level
- Season: Value object with start/end dates

**Player Aggregate**
- Player: Individual tennis player
- ContactInformation: Value object (email, phone)

**Standing Aggregate**
- Match: Root entity representing a tennis match
- Leaderboard: Root entity with standings
- Set: Value object representing a tennis set
- TieBreak: Value object for tie-break scores

### Repository Interfaces (Domain Layer)
- `LeagueRepo`, `GroupRepo`
- `PlayerRepo`
- `MatchRepo`, `LeaderboardRepo`, `MembershipRepo`


## License

This project is licensed under the MIT License.
