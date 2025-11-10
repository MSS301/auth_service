-- Add password column to classes table
ALTER TABLE classes ADD COLUMN IF NOT EXISTS password VARCHAR(100);

-- Create index for faster class search by school and name
CREATE INDEX IF NOT EXISTS idx_classes_school_name ON classes(school_id, name) WHERE deleted = false;
