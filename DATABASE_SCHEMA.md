# Database Schema for LMS

## SQL Schema

```sql
-- Schools table
CREATE TABLE schools (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address TEXT
);

-- User profiles table (separate from auth users table)
CREATE TABLE user_profiles (
    id SERIAL PRIMARY KEY,
    account_id INT NOT NULL, -- link to auth users table
    school_id INT REFERENCES schools(id),
    full_name VARCHAR(255) NOT NULL,
    date_of_birth DATE,
    avatar_url TEXT,
    role VARCHAR(50),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- Classes table
CREATE TABLE classes (
    id SERIAL PRIMARY KEY,
    school_id INT REFERENCES schools(id),
    name VARCHAR(100),
    grade INT,
    teacher_id INT REFERENCES user_profiles(id)
);

-- Class students enrollment table
CREATE TABLE class_students (
    id SERIAL PRIMARY KEY,
    class_id INT REFERENCES classes(id),
    student_id INT REFERENCES user_profiles(id)
);
```

## Table Relationships

```
schools (1) ───< (N) user_profiles
schools (1) ───< (N) classes

user_profiles (1 as teacher) ───< (N) classes
user_profiles (1 as student) ───< (N) class_students

classes (1) ───< (N) class_students
```

## Important Notes

1. **user_profiles** table is separate from the **users** table (auth table)
2. The `account_id` field in `user_profiles` links to the `id` field in the auth `users` table
3. This separation allows for flexible profile management while keeping authentication separate
4. The `users` table (auth) stores authentication data (username, password, email)
5. The `user_profiles` table stores profile/LMS data (full name, school, role, etc.)

## Index Recommendations

For better performance, consider adding these indexes:

```sql
-- Index on account_id for quick lookup
CREATE INDEX idx_user_profiles_account_id ON user_profiles(account_id);

-- Index on school_id for filtering by school
CREATE INDEX idx_user_profiles_school_id ON user_profiles(school_id);
CREATE INDEX idx_classes_school_id ON classes(school_id);

-- Index on teacher_id for filtering by teacher
CREATE INDEX idx_classes_teacher_id ON classes(teacher_id);

-- Index on class_id and student_id for enrollment lookups
CREATE INDEX idx_class_students_class_id ON class_students(class_id);
CREATE INDEX idx_class_students_student_id ON class_students(student_id);

-- Composite index for common queries
CREATE INDEX idx_user_profiles_school_role ON user_profiles(school_id, role);
CREATE INDEX idx_classes_school_grade ON classes(school_id, grade);
```
