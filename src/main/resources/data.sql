-- Создаём пользователя
INSERT INTO users
(id, username, password, image,
 is_account_non_expired, is_account_non_locked,
 is_credentials_non_expired, is_enabled)
VALUES
    (1, 'john.doe', 'securePassword', '/images/john.png',
     true, true, true, true);

-- Добавляем роли
INSERT INTO user_roles (user_id, roles)
VALUES (1, 'ROLE_ADMIN');

INSERT INTO user_roles (user_id, roles)
VALUES (1, 'ROLE_USER');
