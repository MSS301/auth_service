# Soft Delete Implementation in Auth Service

## Overview
This document describes the soft delete implementation for the auth service. Instead of permanently deleting records from the database, records are marked as deleted and filtered out from queries.

## Implementation Details

### 1. Entity Changes

All main entities now include soft delete fields:
- `deleted` (boolean) - Flag indicating if the record is deleted (default: false)
- `deletedAt` (LocalDateTime) - Timestamp when the record was soft deleted
- `softDelete()` method - Convenience method to mark entity as deleted

**Entities Updated:**
- `User` - User accounts
- `School` - Schools
- `Class` - Class entities
- `UserProfile` - User profiles

### 2. Entity Soft Delete Method

Each entity has a `softDelete()` method:

```java
public void softDelete() {
    this.deleted = true;
    this.deletedAt = LocalDateTime.now();
}
```

### 3. Repository Changes

All repositories now use custom JPQL queries with `deleted = false` filter:

#### UserRepository
- Override `findById()` to filter deleted users
- Override `findAll(Pageable)` to filter deleted users
- Custom queries for `findByEmail()`, `findByGoogleId()`, `findByVerificationToken()`
- Search queries for email and username with deleted filter

#### SchoolRepository
- Override `findById()` to filter deleted schools
- Override `findAll(Pageable)` to filter deleted schools
- Custom queries for name search with deleted filter

#### ClassRepository
- Override `findById()` to filter deleted classes
- Override `findAll(Pageable)` to filter deleted classes
- All filter queries (by school, teacher, grade) with deleted filter
- Name search with deleted filter

#### UserProfileRepository
- Override `findById()` to filter deleted profiles
- Override `findAll(Pageable)` to filter deleted profiles
- Custom queries for all search operations with deleted filter

### 4. Service Implementation Changes

Delete methods now use soft delete instead of hard delete:

#### UserServiceImpl
```java
public void deleteUser(String userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    user.softDelete();
    userRepository.save(user);
}
```

#### SchoolServiceImpl
```java
public void deleteSchool(Integer id) {
    School school = schoolRepository.findById(id)
        .orElseThrow(() -> new AppException(ErrorCode.SCHOOL_NOT_FOUND));
    school.softDelete();
    schoolRepository.save(school);
}
```

#### ClassServiceImpl
```java
public void deleteClass(Integer id) {
    Class classEntity = classRepository.findById(id)
        .orElseThrow(() -> new AppException(ErrorCode.CLASS_NOT_FOUND));
    classEntity.softDelete();
    classRepository.save(classEntity);
}
```

#### UserProfileServiceImpl
```java
public void deleteUserProfile(Integer id) {
    UserProfile userProfile = userProfileRepository.findById(id)
        .orElseThrow(() -> new AppException(ErrorCode.USER_PROFILE_NOT_FOUND));
    userProfile.softDelete();
    userProfileRepository.save(userProfile);
}
```

### 5. Database Migration

Migration script: `V2__add_soft_delete_columns.sql`

```sql
-- Add soft delete columns to all tables
ALTER TABLE users ADD COLUMN IF NOT EXISTS deleted BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE users ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;

ALTER TABLE schools ADD COLUMN IF NOT EXISTS deleted BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE schools ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;

ALTER TABLE classes ADD COLUMN IF NOT EXISTS deleted BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE classes ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;

ALTER TABLE user_profiles ADD COLUMN IF NOT EXISTS deleted BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE user_profiles ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_users_deleted ON users(deleted);
CREATE INDEX IF NOT EXISTS idx_schools_deleted ON schools(deleted);
CREATE INDEX IF NOT EXISTS idx_classes_deleted ON classes(deleted);
CREATE INDEX IF NOT EXISTS idx_user_profiles_deleted ON user_profiles(deleted);
```

## Benefits

1. **Data Recovery**: Deleted records can be recovered if needed
2. **Audit Trail**: `deletedAt` timestamp provides history of when records were deleted
3. **Data Integrity**: Related records remain intact in the database
4. **Compliance**: Meets regulatory requirements for data retention
5. **Performance**: Indexed `deleted` column ensures fast filtering

## Querying Behavior

### Normal Queries
All normal queries automatically filter out deleted records:
- `findById()` - Returns empty Optional if record is deleted
- `findAll()` - Only returns non-deleted records
- Search queries - Only search non-deleted records
- Pagination - Only paginates non-deleted records

### Hard Delete (if needed in future)
If hard delete is required, you can still use:
```java
repository.deleteById(id); // Permanent deletion
```

## Testing Recommendations

1. Test that deleted records are not returned in queries
2. Test that soft delete sets both `deleted` and `deletedAt` fields
3. Test that relationships with deleted entities are handled correctly
4. Verify pagination excludes deleted records
5. Test search functionality excludes deleted records

## Future Enhancements

1. Add admin endpoint to view deleted records
2. Add restore functionality to undelete records
3. Add scheduled job to permanently delete old soft-deleted records
4. Add soft delete cascade for related entities
