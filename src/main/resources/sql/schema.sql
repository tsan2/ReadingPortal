CREATE DOMAIN email AS text
CHECK (VALUE ~* '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+[.][a-zA-Z]{2,}$');

CREATE TABLE IF NOT EXISTS users(
id serial PRIMARY KEY,
nickname VARCHAR(30) NOT NULL UNIQUE,
email email NOT NULL UNIQUE,
password_hash VARCHAR(255) NOT NULL,
created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS books(
id serial PRIMARY KEY,
title text NOT NULL,
date_changed TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS volumes(
id serial PRIMARY KEY,
title text,
volume_number INTEGER NOT NULL,
book_id INTEGER REFERENCES books(id) ON DELETE CASCADE NOT NULL
);

CREATE TABLE IF NOT EXISTS chapters(
id serial PRIMARY KEY,
title text,
content text,
chapter_number INTEGER NOT NULL,
volume_id INTEGER REFERENCES volumes(id) ON DELETE CASCADE NOT NULL
);

CREATE TABLE IF NOT EXISTS books_authors(
user_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
book_id INTEGER REFERENCES books(id) ON DELETE CASCADE,
PRIMARY KEY(user_id, book_id)
);

CREATE TABLE IF NOT EXISTS genres(
    id serial PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS books_genres(
book_id INTEGER REFERENCES books(id) ON DELETE CASCADE,
genre_id INTEGER REFERENCES genres(id) ON DELETE CASCADE,
PRIMARY KEY(book_id, genre_id)
);