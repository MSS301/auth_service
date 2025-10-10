# Learning Management System (LMS) - REST API Implementation

## Overview
This is a complete RESTful API implementation for a Learning Management System (LMS) built on top of the auth_service module. It provides comprehensive CRUD operations for managing schools, user profiles, classes, and student enrollments.

## Features

### 🏫 School Management
- Create, read, update, and delete schools
- Search schools by name
- Manage school information including name and address

### 👥 User Profile Management
- Create and manage user profiles linked to authentication accounts
- Support for different roles (STUDENT, TEACHER, ADMIN, etc.)
- Filter users by school and role
- Track user information including full name, date of birth, and avatar

### 📚 Class Management
- Create and manage classes within schools
- Assign teachers to classes
- Organize classes by grade level
- Filter classes by school, teacher, or grade
- Track student enrollment count

### 📝 Student Enrollment
- Enroll students in classes
- Unenroll students from classes
- View all students in a class
- View all classes for a student
- Prevent duplicate enrollments

## Architecture

### Entity Layer (`entity/`)
- **School**: Represents educational institutions
- **UserProfile**: User information and profile data
- **Class**: Course/class information
- **ClassStudent**: Junction table for student enrollments

### Repository Layer (`repository/`)
- **SchoolRepository**: JPA repository with custom query methods
- **UserProfileRepository**: Queries for user profiles by account, school, and role
- **ClassRepository**: Queries for classes by school, teacher, and grade
- **ClassStudentRepository**: Manages enrollment relationships

### Service Layer (`service/`)
- **SchoolService**: Business logic for school operations
- **UserProfileService**: User profile management logic
- **ClassService**: Class management logic
- **ClassStudentService**: Enrollment management logic

### Controller Layer (`controller/`)
- **SchoolController**: REST endpoints for schools
- **UserProfileController**: REST endpoints for user profiles
- **ClassController**: REST endpoints for classes
- **ClassStudentController**: REST endpoints for enrollments

### DTO Layer (`dto/`)

#### Request DTOs
- `SchoolRequest`: Create/update school
- `UserProfileRequest`: Create user profile
- `UserProfileUpdateRequest`: Update user profile
- `ClassRequest`: Create/update class
- `ClassStudentRequest`: Enroll student

#### Response DTOs
- `SchoolResponse`: School data
- `UserProfileResponse`: User profile with related info
- `ClassResponse`: Class with teacher and student count
- `ClassStudentResponse`: Enrollment with class and student names

### Exception Handling
Custom error codes for LMS operations:
- `1010`: School not found
- `1011`: School already exists
- `1012`: User profile not found
- `1013`: User profile already exists
- `1014`: Class not found
- `1015`: Class student enrollment not found
- `1016`: Student already enrolled in class

## API Endpoints

### Schools
```
POST   /schools              - Create school
GET    /schools              - Get all schools (with optional name filter)
GET    /schools/{id}         - Get school by ID
PUT    /schools/{id}         - Update school
DELETE /schools/{id}         - Delete school
```

### User Profiles
```
POST   /user-profiles                 - Create user profile
GET    /user-profiles                 - Get all profiles (filter by schoolId, role)
GET    /user-profiles/{id}            - Get profile by ID
GET    /user-profiles/account/{id}    - Get profile by account ID
PUT    /user-profiles/{id}            - Update profile
DELETE /user-profiles/{id}            - Delete profile
```

### Classes
```
POST   /classes              - Create class
GET    /classes              - Get all classes (filter by schoolId, teacherId, grade)
GET    /classes/{id}         - Get class by ID
PUT    /classes/{id}         - Update class
DELETE /classes/{id}         - Delete class
```

### Class Students (Enrollment)
```
POST   /class-students                     - Enroll student
GET    /class-students                     - Get all enrollments (filter by classId, studentId)
GET    /class-students/{id}                - Get enrollment by ID
DELETE /class-students/{id}                - Delete enrollment
DELETE /class-students/unenroll            - Unenroll student (params: classId, studentId)
```

## Setup and Configuration

### Prerequisites
- Java 21
- Spring Boot 3.3.5
- PostgreSQL database
- Maven

### Database Schema
The application uses the following tables:
- `schools`: School information
- `users`: User profile data (note: different from auth User table)
- `classes`: Class/course information
- `class_students`: Student enrollment records

### Running the Application

1. **Configure Database**
   Update `application.properties` with your database settings:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

2. **Build the Project**
   ```bash
   mvn clean install
   ```

3. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the API**
   Base URL: `http://localhost:8080`

## Testing

### Using Postman
Import the `LMS_API_Postman_Collection.json` file into Postman to test all endpoints.

### Example Workflow

1. **Create a School**
   ```bash
   POST /schools
   {
     "name": "ABC High School",
     "address": "123 Main St"
   }
   ```

2. **Create a Teacher Profile**
   ```bash
   POST /user-profiles
   {
     "accountId": 1,
     "schoolId": 1,
     "fullName": "Jane Smith",
     "role": "TEACHER"
   }
   ```

3. **Create a Class**
   ```bash
   POST /classes
   {
     "schoolId": 1,
     "name": "Mathematics 101",
     "grade": 10,
     "teacherId": 1
   }
   ```

4. **Enroll Students**
   ```bash
   POST /class-students
   {
     "classId": 1,
     "studentId": 5
   }
   ```

## Data Relationships

```
School
  ├── UserProfiles (1:N)
  └── Classes (1:N)

UserProfile
  ├── Teaching Classes (1:N) [as teacher]
  └── Student Enrollments (1:N) [as student]

Class
  ├── Teacher (N:1 to UserProfile)
  └── Enrolled Students (1:N to ClassStudent)

ClassStudent (Junction Table)
  ├── Class (N:1)
  └── Student (N:1 to UserProfile)
```

## Key Features Implementation

### 🔍 Advanced Filtering
- Filter user profiles by school and/or role
- Filter classes by school, teacher, and/or grade
- Filter enrollments by class or student

### ✅ Validation
- Bean validation on all request DTOs
- Business rule validation in services
- Duplicate prevention for schools and enrollments

### 🔄 Automatic Timestamps
- User profiles automatically track creation and update times
- Uses JPA lifecycle callbacks (`@PrePersist`, `@PreUpdate`)

### 📊 Enhanced Response Data
- Class responses include student count
- Profile responses include school name
- Enrollment responses include class and student names
- Reduces need for additional API calls

### 🛡️ Error Handling
- Centralized exception handling
- Consistent error response format
- Appropriate HTTP status codes
- Custom error codes for different scenarios

## Best Practices Implemented

1. **Layered Architecture**: Clear separation of concerns
2. **DTO Pattern**: Separate request/response models from entities
3. **Service Layer**: Business logic isolated from controllers
4. **Repository Pattern**: Data access abstraction
5. **Exception Handling**: Consistent error responses
6. **Validation**: Input validation at multiple levels
7. **Logging**: Comprehensive logging with SLF4J
8. **Transaction Management**: `@Transactional` for data consistency

## Documentation

- **API Documentation**: See `LMS_API_DOCUMENTATION.md`
- **Postman Collection**: See `LMS_API_Postman_Collection.json`
- **This README**: Implementation overview and setup guide

## Future Enhancements

Potential features to add:
- Pagination for list endpoints
- Sorting options
- Attendance tracking
- Grade management
- Assignment management
- Parent-student relationships
- Class schedules and timetables
- Announcement system
- File upload for avatars
- Bulk enrollment operations

## License

This project is part of the auth_service module.

## Contributors

Developed as part of the Sigma Learning Management System project.
