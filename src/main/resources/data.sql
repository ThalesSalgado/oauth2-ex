DROP SCHEMA IF EXISTS oauth2-ex;
--DROP TABLE IF EXISTS user_permissao;
--DROP TABLE IF EXISTS user;
--DROP TABLE IF EXISTS permissao;


-- Insere permissao:
INSERT INTO permissao (codigo, descricao) VALUES (1, 'ROLE_ADMIN');
INSERT INTO permissao (codigo, descricao) VALUES (2, 'ROLE_USER');

-- Insere usuario:
INSERT INTO user (codigo, created_at, customerid, login, password, updated_at)
  VALUES (1, now(), 'a1-b2', 'thales', '$2a$10$5mh0hmsKiLxb9aadf0vv7uxK9W3N0yUy.MATVgwLbfEcM3LHBB0my', now());

-- Insere user_permissao:
INSERT INTO user_permissao (codigo_user, codigo_permissao)
  VALUES (1, 1);
INSERT INTO user_permissao (codigo_user, codigo_permissao)
  VALUES (1, 2);