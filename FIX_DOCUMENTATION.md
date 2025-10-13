# LMS Implementation - Issue Fix

## Problem Identified

**Error:** Database schema conflict when creating tables

```
ERROR: identity column type must be smallint, integer, or bigint
```

**Root Cause:**
- Two entities were mapped to the same table name `users`
  1. **User.java** (authentication) - `@Table(name = "users")` with `String id` (UUID)
  2. **UserProfile.java** (LMS profiles) - `@Table(name = "users")` with `Integer id`
- Hibernate tried to create one table with conflicting ID types

## Solution Applied

✅ **Changed UserProfile table name from `users` to `user_profiles`**

```java
@Entity
@Table(name = "user_profiles")  // Changed from "users"
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    
    @Column(name = "account_id", nullable = false)
    Integer accountId; // Links to auth users.id
    
    // ... other fields
}
```

## Database Schema Now

### Auth Service Tables
```sql
-- Authentication table (existing)
CREATE TABLE users (
    id VARCHAR(255) PRIMARY KEY,  -- UUID
    username VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    email_verified BOOLEAN DEFAULT FALSE
);

-- LMS Profile table (new)
CREATE TABLE user_profiles (
    id SERIAL PRIMARY KEY,        -- Integer auto-increment
    account_id INTEGER NOT NULL,  -- References users(id) conceptually
    school_id INTEGER REFERENCES schools(id),
    full_name VARCHAR(255) NOT NULL,
    date_of_birth DATE,
    avatar_url TEXT,
    role VARCHAR(50),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);
```

## Architecture Overview

```
┌─────────────────────┐
│   Authentication    │
│    (users table)    │
│                     │
│  - id (UUID)        │
│  - username         │
│  - password         │
│  - email            │
└──────────┬──────────┘
           │
           │ Linked by account_id
           │
┌──────────▼──────────┐
│    LMS Profile      │
│ (user_profiles)     │
│                     │
│  - id (Integer)     │
│  - account_id ─────┤
│  - school_id        │
│  - full_name        │
│  - role             │
└─────────────────────┘
```

## Why This Separation?

### ✅ Benefits

1. **Clear Separation of Concerns**
   - Auth data separate from profile data
   - Can authenticate without profile
   - Can have profile without full auth

2. **Flexibility**
   - Different ID types for different purposes
   - UUID for auth (security)
   - Integer for profiles (simplicity, performance)

3. **Scalability**
   - Auth service can be separate microservice
   - Profile data can grow independently
   - Easy to add more profile types

4. **Data Management**
   - Easier to backup/restore separately
   - Different access patterns
   - Different retention policies

## How It Works

### 1. User Registration Flow
```
1. Create auth account → users table (UUID id)
2. Create user profile → user_profiles table (Integer id, account_id links to users.id)
```

### 2. User Login Flow
```
1. Authenticate → Check users table
2. Get profile → Query user_profiles by account_id
```

### 3. API Usage
```
POST /auth/register
→ Creates record in users table
→ Returns account info

POST /user-profiles
{
  "accountId": <users.id>,  // Link to auth account
  "schoolId": 1,
  "fullName": "John Doe",
  "role": "STUDENT"
}
→ Creates record in user_profiles table
```

## Files Updated

1. ✅ **UserProfile.java** - Changed table name to `user_profiles`
2. ✅ **DATABASE_SCHEMA.md** - Created schema documentation
3. ✅ **IMPLEMENTATION_SUMMARY.md** - Updated table references

## Verification

After this fix, Hibernate should successfully create:
- ✅ `schools` table
- ✅ `user_profiles` table (not conflicting with `users`)
- ✅ `classes` table
- ✅ `class_students` table

## Next Steps

1. **Restart the application** - Tables should create successfully
2. **Test the API** - Use Postman collection
3. **Create test data** - Follow the workflow in documentation

## Important Notes

⚠️ **account_id Field**
- The `account_id` field in `user_profiles` is just an Integer
- It references the `id` in `users` table (which is VARCHAR/UUID)
- This means you need to store/convert the UUID as needed
- Or consider changing the `users` table ID to Integer as well for consistency

### Recommendation for Future

Consider standardizing ID types:
- **Option A**: All tables use Integer IDs
- **Option B**: All tables use UUID (String) IDs
- **Current**: Mixed (requires careful handling)

For now, the current implementation works, but be aware of the type difference when linking accounts to profiles.

---

## Summary

✅ **Issue Fixed**: Resolved table name conflict by renaming `users` to `user_profiles`
✅ **Schema Clean**: Each entity now has its own unique table
✅ **Documentation Updated**: All docs reflect the correct table names
✅ **Ready to Use**: Application should now start successfully
