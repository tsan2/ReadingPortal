CREATE DOMAIN email AS text
CHECK (VALUE ~* '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+[.][a-zA-Z]{2,}$');

CREATE TABLE IF NOT EXISTS users(
id bigserial PRIMARY KEY,
nickname VARCHAR(30) NOT NULL UNIQUE,
email email NOT NULL UNIQUE,
password_hash VARCHAR(255) NOT NULL,
created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS books(
id bigserial PRIMARY KEY,
title text NOT NULL,
date_changed TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS volumes(
id serial PRIMARY KEY,
title text,
volume_main_number INTEGER NOT NULL,
volume_sub_number INTEGER DEFAULT 0,
book_id INTEGER REFERENCES books(id) ON DELETE CASCADE NOT NULL,
is_default BOOLEAN DEFAULT false
);

CREATE TABLE IF NOT EXISTS chapters(
id serial PRIMARY KEY,
title text,
content text,
chapter_main_number INTEGER NOT NULL,
chapter_sub_number INTEGER DEFAULT 0,
volume_id INTEGER REFERENCES volumes(id) ON DELETE CASCADE NOT NULL
);

CREATE TABLE IF NOT EXISTS books_authors(
user_id bigint REFERENCES users(id) ON DELETE CASCADE,
book_id bigint REFERENCES books(id) ON DELETE CASCADE,
PRIMARY KEY(user_id, book_id)
);

CREATE TABLE IF NOT EXISTS genres(
    id bigserial PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS books_genres(
book_id bigint REFERENCES books(id) ON DELETE CASCADE,
genre_id bigint REFERENCES genres(id) ON DELETE CASCADE,
PRIMARY KEY(book_id, genre_id)
);

CREATE TABLE IF NOT EXISTS password_reset_codes(
id serial PRIMARY KEY,
user_id bigint REFERENCES users(id) ON DELETE CASCADE NOT NULL,
code VARCHAR(10) NOT NULL,
expires_at TIMESTAMPTZ NOT NULL,
created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS remember_me_tokens(
id serial PRIMARY KEY,
user_id bigint REFERENCES users(id) ON DELETE CASCADE NOT NULL,
token_hash VARCHAR(80) NOT NULL,
expires_at TIMESTAMPTZ NOT NULL,
created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
)

CREATE INDEX password_reset_codes_code_index
on password_reset_codes(code);

CREATE INDEX books_authors_book_id_index
on books_authors(book_id);

CREATE INDEX books_authors_user_id_index
on books_authors(user_id);

CREATE INDEX books_genres_book_id_index
on books_genres(book_id);

CREATE INDEX books_genres_genre_id_index
on books_genres(genre_id);

CREATE INDEX users_email_index
on users(email);

CREATE INDEX users_nickname_index
on users(nickname);

CREATE UNIQUE INDEX
ON volumes(book_id, volume_main_number, volume_sub_number);