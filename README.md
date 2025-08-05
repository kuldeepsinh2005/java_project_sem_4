# E-Sports Team Management System

A comprehensive Spring Boot application for managing e-sports teams, players, and team join requests. This system provides a complete solution for organizing competitive gaming teams with features for player registration, team management, and request handling.

## 🎮 Features

### Player Management
- **Player Registration**: Register as either a solo player or team leader
- **Role System**: Support for different player roles (Healer, Rusher, Leader, Sniper)
- **Profile Management**: Update username, password, and personal information
- **Kill Count Tracking**: Track individual player performance
- **Team Assignment**: Join teams or remain as solo players
- **Leave Team**: Players can leave their current team
- **Star Players**: View top players ranked by kill count
- **Player Dashboard**: Personalized dashboard for each player

### Team Management
- **Team Creation**: Create teams with unique names and passwords
- **Team Size Control**: Maximum 4 players per team
- **Team Leader System**: Designated team leaders with special privileges
- **Team Statistics**: Track number of players and wins
- **Team Updates**: Modify team names and passwords
- **Team Deletion**: Remove teams with proper cleanup
- **Star Teams**: View top-performing teams
- **Qualified Teams**: Teams with exactly 4 players (tournament ready)

### Request System
- **Join Requests**: Players can request to join teams
- **Request Management**: Team leaders can accept or reject join requests
- **Request Tracking**: Monitor pending requests for teams
- **Automatic Cleanup**: Remove requests when teams are full
- **Request Validation**: Prevent duplicate requests and invalid submissions

### Admin Features
- **Player Management**: Admin can view and manage all players
- **Kill Count Updates**: Admin can update player kill counts
- **Team Management**: Admin can manage all teams
- **Win Count Updates**: Admin can update team win counts
- **Qualified Teams Management**: Admin can manage tournament-ready teams
- **Star Players/Teams**: Admin can view top performers

### Authentication & Security
- **Secure Authentication**: Spring Security with BCrypt password encoding
- **Role-based Access Control**: Different permissions based on player roles
- **Database Authentication**: Custom user details service with MySQL
- **Session Management**: Maintain user sessions with proper security
- **CSRF Protection**: Disabled for API endpoints, enabled for forms

### Web Interface
- **Responsive Design**: Bootstrap-based modern UI
- **Dynamic Forms**: Interactive registration and management forms
- **Real-time Updates**: Live updates for team and player information
- **Error Handling**: Comprehensive error messages and validation
- **Landing Page**: Welcome page with registration options
- **Role-specific Pages**: Different interfaces for leaders, members, and solo players

## 🏗️ Architecture

### Technology Stack
- **Backend**: Spring Boot 3.4.3
- **Database**: MySQL with JPA/Hibernate
- **Frontend**: Thymeleaf templates with Bootstrap
- **Security**: Spring Security with BCrypt encoding
- **Build Tool**: Maven
- **Java Version**: 17

### Project Structure
```
src/main/java/com/team/management/project/
├── entity/           # JPA entities (Player, Team, Request)
├── repository/       # Data access layer
├── service/         # Business logic layer
├── rest/           # REST controllers
├── security/       # Security configuration
└── ProjectApplication.java
```

### Key Components

#### Entities
- **Player**: Represents individual players with roles and team associations
- **Team**: Represents teams with size limits and statistics
- **Request**: Manages join requests between players and teams

#### Services
- **PlayerService**: Handles player CRUD operations and team management
- **TeamService**: Manages team operations and player assignments
- **RequestService**: Handles join request processing and cleanup

#### Controllers
- **PlayerController**: Player registration and management
- **TeamRestController**: Team operations and leader functions
- **RequestRestController**: Request handling and team joining
- **AuthController**: Authentication and login management
- **AdminController**: Administrative functions
- **DemoController**: Demo/test endpoints

#### Security
- **DemoSecurityConfig**: Spring Security configuration with role-based access
- **BCryptPasswordEncoder**: Secure password hashing
- **Custom UserDetailsService**: Database-based authentication

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6+

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/kuldeepsinh2005/java_project_sem_4.git
   cd project
   ```

2. **Configure Database**
   - Create a MySQL database
   - Update `application.properties` with your database credentials

3. **Build the project**
   ```bash
   mvn clean install
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

5. **Access the application**
   - Open browser and navigate to `http://localhost:8080`
   - Register as a new player or team leader

### Database Configuration
Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

## 📋 API Endpoints

### Landing Page
- `GET /` - Welcome page with registration options

### Player Management
- `GET /players/register-solo` - Show solo player registration form
- `POST /players/register-solo` - Register new solo player
- `GET /players/create-team` - Show team creation form
- `POST /players/create-team` - Create team with leader
- `GET /players/all-list` - List all players
- `POST /players/update` - Update player information
- `POST /players/delete` - Delete player
- `GET /players/dashboard` - Player dashboard

### Team Management
- `GET /teams/team-all` - Show all teams
- `POST /teams/save` - Create new team
- `POST /teams/update` - Update team information
- `POST /teams/remove-player` - Remove player from team
- `POST /teams/delete` - Delete team

### Request Management
- `GET /api/requests/page` - Show request page
- `POST /api/requests/player/addrequest` - Send join request
- `POST /api/requests/accept` - Accept join request
- `POST /api/requests/reject` - Reject join request

### Authentication
- `GET /login` - Show login form
- `POST /login` - Player login
- `GET /team/login` - Show team login form
- `POST /team/login` - Team login
- `GET /logout` - Logout

### Admin Functions
- `GET /api/admin/list` - Admin player list
- `POST /api/admin/showFormForUpdate` - Update player kill count
- `GET /api/admin/qualified` - View qualified teams
- `POST /api/admin/updateWinCount` - Update team win count
- `GET /api/admin/starteams` - View star teams
- `GET /api/admin/starplayers` - View star players

### Role-specific Pages
- `GET /api/auth/leader-page` - Team leader dashboard
- `GET /api/auth/member-page` - Team member dashboard
- `GET /api/auth/solo-page` - Solo player dashboard

### Demo/Test
- `GET /hello/hello` - Demo endpoint
- `POST /hello/hello` - Demo endpoint

## 🔐 Security Features

### Authentication System
- **BCrypt Password Encoding**: All passwords are securely hashed using BCrypt
- **Database Authentication**: Custom UserDetailsService for player authentication
- **Session Management**: Proper session handling with Spring Security

### Role-Based Access Control
The application implements comprehensive role-based access control:

#### Admin Role (`ROLE_ADMIN`)
- Access to all administrative functions
- Full system management capabilities
- Can update player kill counts and team win counts
- Can view star players and teams

#### Leader Role (`ROLE_LEADER`)
- Team management operations
- Accept/reject join requests
- Update team information
- Delete teams
- Access leader-specific pages

#### Player Roles (`ROLE_SNIPER`, `ROLE_HEALER`, `ROLE_RUSHER`)
- View team information
- Send join requests
- Update personal information
- Leave teams
- Access member-specific pages

### Security Configuration
```java
// Key security features implemented:
- BCryptPasswordEncoder for password hashing
- Custom UserDetailsService for database authentication
- Role-based URL access control
- CSRF protection (disabled for API endpoints)
- Form-based authentication
- Session management
```

### Password Security
- All passwords are encoded using BCrypt algorithm
- Secure password storage in database
- Password validation and strength requirements
- Encrypted password transmission

## 🎯 Key Features Explained

### Player Registration System
Players can register in two modes:
1. **Solo Player**: Individual registration without team association
2. **Team Leader**: Registration with automatic team creation

### Team Management
- Teams have a maximum size of 4 players
- Team leaders have special privileges for managing members
- Teams can accept or reject join requests
- Automatic cleanup when teams become full

### Request System
- Players can send join requests to teams
- Team leaders can accept/reject requests
- Automatic request cleanup when teams are full
- Prevention of duplicate requests

### Admin Features
- **Player Management**: View and update player information
- **Kill Count Management**: Update player performance metrics
- **Team Management**: Manage team statistics and win counts
- **Qualified Teams**: Manage tournament-ready teams
- **Star System**: View top performers

### Role-Based Access
- **Team Leaders**: Can manage team members and accept requests
- **Team Members**: Can view team information and leave team
- **Solo Players**: Can browse teams and send join requests
- **Admins**: Full system access and management

## 🔧 Configuration

### Security Settings
The application implements proper security measures:
1. **BCrypt Password Encoding**: All passwords are securely hashed
2. **Role-based Access Control**: Different permissions for different roles
3. **Database Authentication**: Custom authentication provider
4. **Session Security**: Proper session management

### Database Settings
- Configure MySQL connection in `application.properties`
- Set appropriate JPA settings for your environment
- Ensure database user has proper permissions
- Security queries configured for authentication

### Security Configuration Details
```properties
# Security-related database queries:
- Users query: SELECT username, password, true FROM player WHERE username = ?
- Authorities query: SELECT username, role FROM player WHERE username = ?
- All users are considered active (true flag)
- Roles are stored as-is in the database
```

## 🐛 Troubleshooting

### Common Issues
1. **Database Connection**: Ensure MySQL is running and credentials are correct
2. **Port Conflicts**: Change server port in `application.properties` if needed
3. **Template Errors**: Check Thymeleaf template syntax
4. **Security Issues**: Verify authentication configuration
5. **Password Issues**: Ensure BCrypt encoding is working properly

### Security Troubleshooting
1. **Authentication Failures**: Check database user table and role assignments
2. **Access Denied**: Verify user roles and URL permissions
3. **Password Encoding**: Ensure BCrypt is properly configured
4. **Session Issues**: Check session configuration and timeout settings

### Logs
Check application logs for detailed error information:
```bash
tail -f logs/application.log
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.


**Note**: This application implements proper security measures including BCrypt password encoding, role-based access control, and secure session management. All passwords are securely hashed and stored in the database.
