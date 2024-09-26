ALTER TABLE purchased_tickets
    ADD COLUMN reserved_at TIMESTAMP WITH TIME ZONE NULL;

ALTER TABLE purchased_tickets
    ALTER COLUMN purchased_at DROP NOT NULL;