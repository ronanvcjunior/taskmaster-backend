-- Migration para adicionar dados iniciais de roles e usuário do sistema

-- Inserindo o usuário do sistema
INSERT INTO users(
    id, user_id, first_name, last_name, email, account_unexpired, account_unlocked, enabled, created_by, updated_by)
VALUES (
           0,
           '00000000-0000-0000-0000-0000000000000',
           'System',
           'System',
           'system@gmail.com',
           TRUE,
           TRUE,
           TRUE,
           0,
           0
       );

-- Inserindo roles USER e ADMIN
INSERT INTO roles (id, name, authorities)
VALUES
    (1, 'USER', 'task:create,task:read,task:update,task:delete'),
    (2, 'ADMIN', 'user:create,user:read,user:update,user:delete,task:read,task:update,task:delete');