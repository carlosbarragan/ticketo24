CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE concerts
(
    id           UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    concert_name VARCHAR(255) NOT NULL,
    artist_name  VARCHAR(255) NOT NULL,
    description  TEXT         NOT NULL,
    capacity     INTEGER      NOT NULL
);
