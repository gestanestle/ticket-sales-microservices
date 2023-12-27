CREATE TABLE IF NOT EXISTS public.userprofile (
    user_id BIGSERIAL PRIMARY KEY,
    last_name VARCHAR(255),
    first_name VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(255),
    birthdate TIMESTAMP,
    registered_at TIMESTAMP
);