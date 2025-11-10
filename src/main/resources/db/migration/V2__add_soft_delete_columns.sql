-- Add soft delete columns to users table
ALTER TABLE users ADD COLUMN IF NOT EXISTS deleted BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE users ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;

-- Add soft delete columns to schools table
ALTER TABLE schools ADD COLUMN IF NOT EXISTS deleted BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE schools ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;

-- Add soft delete columns to classes table
ALTER TABLE classes ADD COLUMN IF NOT EXISTS deleted BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE classes ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;

-- Add soft delete columns to user_profiles table
ALTER TABLE user_profiles ADD COLUMN IF NOT EXISTS deleted BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE user_profiles ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;

-- Create indexes for better query performance on deleted column
CREATE INDEX IF NOT EXISTS idx_users_deleted ON users(deleted);
CREATE INDEX IF NOT EXISTS idx_schools_deleted ON schools(deleted);
CREATE INDEX IF NOT EXISTS idx_classes_deleted ON classes(deleted);
CREATE INDEX IF NOT EXISTS idx_user_profiles_deleted ON user_profiles(deleted);
