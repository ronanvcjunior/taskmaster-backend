-- Versão: V1__Create_tables.sql
-- Descrição: Criação das tabelas com herança de campos da classe Auditable

-- Criação da tabela users
CREATE TABLE users (
                        id SERIAL PRIMARY KEY,
                        user_id VARCHAR(255) UNIQUE NOT NULL,
                        first_name VARCHAR(255),
                        last_name VARCHAR(255),
                        email VARCHAR(255) UNIQUE NOT NULL,
                        login_attempts INTEGER,
                        last_login TIMESTAMPTZ,
                        account_unexpired BOOLEAN DEFAULT FALSE,
                        account_unlocked BOOLEAN DEFAULT FALSE,
                        enabled BOOLEAN DEFAULT FALSE,
                        created_by BIGINT REFERENCES users(id),
                        updated_by BIGINT REFERENCES users(id),
                        created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                        updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Criação da tabela roles
CREATE TABLE roles (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(255),
                        authorities VARCHAR(255),
                        created_by BIGINT REFERENCES users(id),
                        updated_by BIGINT REFERENCES users(id),
                        created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                        updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Criação da tabela tasks
CREATE TABLE tasks (
                        id SERIAL PRIMARY KEY,
                        task_id VARCHAR(255) UNIQUE NOT NULL,
                        name VARCHAR(255) NOT NULL,
                        cost NUMERIC(15, 2),
                        payment_deadline TIMESTAMPTZ,
                        task_order INTEGER NOT NULL,
                        user_id BIGINT REFERENCES users(id),
                        created_by BIGINT REFERENCES users(id),
                        updated_by BIGINT REFERENCES users(id),
                        created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                        updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                        CONSTRAINT unique_task_order_user UNIQUE (user_id, task_order)
);

-- Criação da tabela confirmations
CREATE TABLE confirmations (
                        id SERIAL PRIMARY KEY,
                        user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
                        confirmation_key VARCHAR(255),
                        created_by BIGINT REFERENCES users(id),
                        updated_by BIGINT REFERENCES users(id),
                        created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                        updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Criação da tabela credentials
CREATE TABLE credentials (
                        id SERIAL PRIMARY KEY,
                        user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
                        password VARCHAR(255),
                        created_by BIGINT REFERENCES users(id),
                        updated_by BIGINT REFERENCES users(id),
                        created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                        updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Criação da tabela de associação user_roles
CREATE TABLE user_roles (
                        user_id BIGINT REFERENCES users(id),
                        role_id BIGINT REFERENCES roles(id),
                        PRIMARY KEY (user_id, role_id)
);
