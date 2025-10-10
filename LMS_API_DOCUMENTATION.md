# Learning Management System (LMS) API Documentation

## Overview
This document describes the RESTful APIs for the Learning Management System implemented in the auth_service.

## Base URL
```
http://localhost:8080
```

---

## API Endpoints

### 1. Schools API

#### Create School
**POST** `/schools`
```json
Request Body:
{
  "name": "ABC High School",
  "address": "123 Main Street, City"
}

Response:
{
  "code": 1000,
  "result": {
    "id": 1,
    "name": "ABC High School",
    "address": "123 Main Street, City"
  }
}
```

#### Get School by ID
**GET** `/schools/{id}`
```json
Response:
{
  "code": 1000,
  "result": {
    "id": 1,
    "name": "ABC High School",
    "address": "123 Main Street, City"
  }
}
```

#### Get All Schools
**GET** `/schools`

Query Parameters:
- `name` (optional): Search schools by name (case-insensitive)

```json
Response:
{
  "code": 1000,
  "result": [
    {
      "id": 1,
      "name": "ABC High School",
      "address": "123 Main Street, City"
    }
  ]
}
```

#### Update School
**PUT** `/schools/{id}`
```json
Request Body:
{
  "name": "ABC High School Updated",
  "address": "456 New Street, City"
}

Response:
{
  "code": 1000,
  "result": {
    "id": 1,
    "name": "ABC High School Updated",
    "address": "456 New Street, City"
  }
}
```

#### Delete School
**DELETE** `/schools/{id}`
```json
Response:
{
  "code": 1000,
  "message": "School deleted successfully"
}
```

---

### 2. User Profiles API

#### Create User Profile
**POST** `/user-profiles`
```json
Request Body:
{
  "accountId": 123,
  "schoolId": 1,
  "fullName": "John Doe",
  "dateOfBirth": "2000-01-15",
  "avatarUrl": "https://example.com/avatar.jpg",
  "role": "STUDENT"
}

Response:
{
  "code": 1000,
  "result": {
    "id": 1,
    "accountId": 123,
    "schoolId": 1,
    "schoolName": "ABC High School",
    "fullName": "John Doe",
    "dateOfBirth": "2000-01-15",
    "avatarUrl": "https://example.com/avatar.jpg",
    "role": "STUDENT",
    "createdAt": "2025-10-10T10:00:00",
    "updatedAt": "2025-10-10T10:00:00"
  }
}
```

#### Get User Profile by ID
**GET** `/user-profiles/{id}`

#### Get User Profile by Account ID
**GET** `/user-profiles/account/{accountId}`

#### Get All User Profiles
**GET** `/user-profiles`

Query Parameters:
- `schoolId` (optional): Filter by school ID
- `role` (optional): Filter by role (STUDENT, TEACHER, ADMIN, etc.)

```json
Response:
{
  "code": 1000,
  "result": [
    {
      "id": 1,
      "accountId": 123,
      "schoolId": 1,
      "schoolName": "ABC High School",
      "fullName": "John Doe",
      "dateOfBirth": "2000-01-15",
      "avatarUrl": "https://example.com/avatar.jpg",
      "role": "STUDENT",
      "createdAt": "2025-10-10T10:00:00",
      "updatedAt": "2025-10-10T10:00:00"
    }
  ]
}
```

#### Update User Profile
**PUT** `/user-profiles/{id}`
```json
Request Body:
{
  "schoolId": 1,
  "fullName": "John Doe Updated",
  "dateOfBirth": "2000-01-15",
  "avatarUrl": "https://example.com/new-avatar.jpg",
  "role": "TEACHER"
}

Response: Same as create response
```

#### Delete User Profile
**DELETE** `/user-profiles/{id}`
```json
Response:
{
  "code": 1000,
  "message": "User profile deleted successfully"
}
```

---

### 3. Classes API

#### Create Class
**POST** `/classes`
```json
Request Body:
{
  "schoolId": 1,
  "name": "Mathematics 101",
  "grade": 10,
  "teacherId": 5
}

Response:
{
  "code": 1000,
  "result": {
    "id": 1,
    "schoolId": 1,
    "schoolName": "ABC High School",
    "name": "Mathematics 101",
    "grade": 10,
    "teacherId": 5,
    "teacherName": "Jane Smith",
    "studentCount": 0
  }
}
```

#### Get Class by ID
**GET** `/classes/{id}`

#### Get All Classes
**GET** `/classes`

Query Parameters:
- `schoolId` (optional): Filter by school ID
- `teacherId` (optional): Filter by teacher ID
- `grade` (optional): Filter by grade level
- Can combine `schoolId` and `grade` together

```json
Response:
{
  "code": 1000,
  "result": [
    {
      "id": 1,
      "schoolId": 1,
      "schoolName": "ABC High School",
      "name": "Mathematics 101",
      "grade": 10,
      "teacherId": 5,
      "teacherName": "Jane Smith",
      "studentCount": 25
    }
  ]
}
```

#### Update Class
**PUT** `/classes/{id}`
```json
Request Body:
{
  "schoolId": 1,
  "name": "Advanced Mathematics",
  "grade": 11,
  "teacherId": 5
}

Response: Same as create response
```

#### Delete Class
**DELETE** `/classes/{id}`
```json
Response:
{
  "code": 1000,
  "message": "Class deleted successfully"
}
```

---

### 4. Class Students (Enrollment) API

#### Enroll Student in Class
**POST** `/class-students`
```json
Request Body:
{
  "classId": 1,
  "studentId": 10
}

Response:
{
  "code": 1000,
  "result": {
    "id": 1,
    "classId": 1,
    "className": "Mathematics 101",
    "studentId": 10,
    "studentName": "John Doe"
  }
}
```

#### Get Enrollment by ID
**GET** `/class-students/{id}`

#### Get All Enrollments
**GET** `/class-students`

Query Parameters:
- `classId` (optional): Get all students in a specific class
- `studentId` (optional): Get all classes for a specific student

```json
Response:
{
  "code": 1000,
  "result": [
    {
      "id": 1,
      "classId": 1,
      "className": "Mathematics 101",
      "studentId": 10,
      "studentName": "John Doe"
    }
  ]
}
```

#### Delete Enrollment by ID
**DELETE** `/class-students/{id}`
```json
Response:
{
  "code": 1000,
  "message": "Enrollment deleted successfully"
}
```

#### Unenroll Student from Class
**DELETE** `/class-students/unenroll?classId={classId}&studentId={studentId}`

Query Parameters:
- `classId`: Class ID
- `studentId`: Student ID

```json
Response:
{
  "code": 1000,
  "message": "Student unenrolled successfully"
}
```

---

## Error Codes

| Code | Message | HTTP Status |
|------|---------|-------------|
| 1000 | Success | 200 |
| 1010 | School not found | 404 |
| 1011 | School with this name already exists | 409 |
| 1012 | User profile not found | 404 |
| 1013 | User profile already exists for this account | 409 |
| 1014 | Class not found | 404 |
| 1015 | Class student enrollment not found | 404 |
| 1016 | Student is already enrolled in this class | 409 |

---

## Common Use Cases

### 1. Setting up a new school
```
POST /schools
→ Create school
```

### 2. Adding a teacher
```
POST /user-profiles
→ Create user profile with role="TEACHER"
```

### 3. Creating a class with a teacher
```
POST /classes
→ Create class with teacherId
```

### 4. Enrolling students in a class
```
POST /class-students
→ Enroll each student
```

### 5. Getting all students in a class
```
GET /class-students?classId={id}
→ Returns all enrolled students
```

### 6. Getting all classes for a student
```
GET /class-students?studentId={id}
→ Returns all classes the student is enrolled in
```

### 7. Getting all teachers in a school
```
GET /user-profiles?schoolId={id}&role=TEACHER
→ Returns all teachers in the school
```

### 8. Getting all classes for a specific grade in a school
```
GET /classes?schoolId={id}&grade={grade}
→ Returns all classes matching criteria
```

---

## Data Model Relationships

```
School (1) ───< (N) UserProfile
School (1) ───< (N) Class

UserProfile (1) ───< (N) Class (as teacher)
UserProfile (1) ───< (N) ClassStudent (as student)

Class (1) ───< (N) ClassStudent
```

---

## Notes

1. All endpoints return responses wrapped in an `ApiResponse` object with `code`, `message`, and `result` fields.
2. The `accountId` field in UserProfile links to the User entity in the authentication system.
3. Student enrollment is managed through the ClassStudent entity (many-to-many relationship).
4. Timestamps (`createdAt`, `updatedAt`) are automatically managed for UserProfile entities.
5. All DELETE operations will cascade delete related entities where appropriate.
