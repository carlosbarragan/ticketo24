CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE purchased_tickets
(
    id           UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    concert_id   UUID                     NOT NULL,
    purchased_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX purchased_tickets_concert_id_idx ON purchased_tickets (concert_id);