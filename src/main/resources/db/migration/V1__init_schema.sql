CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       username VARCHAR(255) UNIQUE NOT NULL,
                       email VARCHAR(255) UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       role VARCHAR(50) NOT NULL DEFAULT 'USER',
                       created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE refresh_tokens (
                                id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                token VARCHAR(255) UNIQUE NOT NULL,
                                expiry_date TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE TABLE categories (
                            id UUID PRIMARY KEY,
                            user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                            name VARCHAR(255) NOT NULL,
                            type VARCHAR(50) NOT NULL,
                            description TEXT,
                            deleted BOOLEAN NOT NULL DEFAULT FALSE,
                            updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE tracked_items (
                               id UUID PRIMARY KEY,
                               user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                               category_id UUID REFERENCES categories(id) ON DELETE CASCADE,
                               title VARCHAR(255) NOT NULL,
                               subtitle VARCHAR(255),
                               status VARCHAR(50) NOT NULL,
                               score DOUBLE PRECISION,
                               cover_url TEXT,
                               description TEXT,
                               deleted BOOLEAN NOT NULL DEFAULT FALSE,
                               created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                               metadata JSONB NOT NULL DEFAULT '{}'::jsonb
);
CREATE TABLE deleted_records (
                                 record_id UUID NOT NULL,
                                 user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                 table_name VARCHAR(100) NOT NULL,
                                 deleted_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                 PRIMARY KEY (record_id, table_name)
);
CREATE INDEX idx_tracked_items_user_id ON tracked_items(user_id);
CREATE INDEX idx_refresh_tokens_user_id ON refresh_tokens(user_id);
CREATE INDEX idx_refresh_tokens_token ON refresh_tokens(token);
CREATE INDEX idx_tracked_items_category_id ON tracked_items(category_id);
CREATE INDEX idx_tracked_items_metadata ON tracked_items USING GIN (metadata);
CREATE INDEX idx_categories_user_id ON categories(user_id);
