-- =============================================================
-- DADOS SIMULADOS PARA DESENVOLVIMENTO
-- Banco: PostgreSQL | Tabelas geradas pelo Hibernate (ddl-auto=update)
--
-- SENHAS (BCrypt):
--   admin123  -> hash do admin
--   senha123  -> hash dos usuários comuns
--
-- Como executar manualmente:
--   psql -h 127.0.0.1 -p 5434 -U api_user -d minha_api_db -f data.sql
-- =============================================================

-- -------------------------------------------------------------
-- USUÁRIOS
-- is_admin = true apenas para o primeiro usuário (administrador)
-- -------------------------------------------------------------
INSERT INTO usuarios (nome, email, senha, saldo, is_admin)
VALUES
  ('Administrador', 'admin@loja.com',  '$2a$10$8K1p/a0dL1LXMIgoEDFrwOfMQbLgtnOoKsWc.6U47uQHqmTtynrai', 0.00,    true),
  ('Iago Alves',    'iago@email.com',  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lihC', 500.00,  false),
  ('Maria Souza',   'maria@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lihC', 250.00,  false),
  ('Carlos Lima',   'carlos@email.com','$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lihC', 1000.00, false),
  ('Ana Paula',     'ana@email.com',   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lihC', 75.50,   false)
ON CONFLICT (email) DO NOTHING;

-- -------------------------------------------------------------
-- JOGOS
-- -------------------------------------------------------------
INSERT INTO jogo (nome, preco, tipo, url_imagem, descricao)
VALUES
  ('The Witcher 3: Wild Hunt', 69.90, 'RPG',
   'https://images.igdb.com/igdb/image/upload/t_cover_big/co1wyy.jpg',
   'Um RPG de mundo aberto épico onde você joga como Geralt de Rívia, um caçador de monstros profissional. Explore um vasto mundo repleto de escolhas morais, personagens memoráveis e combates viscerais.'),

  ('Red Dead Redemption 2', 119.90, 'Ação',
   'https://images.igdb.com/igdb/image/upload/t_cover_big/co1q1f.jpg',
   'Uma história épica sobre a vida na América no alvorecer da era moderna. Arthur Morgan e a gangue Van der Linde tentam sobreviver enquanto o governo federal os persegue.'),

  ('Cyberpunk 2077', 99.90, 'RPG',
   'https://images.igdb.com/igdb/image/upload/t_cover_big/co4hna.jpg',
   'Uma aventura de RPG de ação em mundo aberto ambientada no megalópole Night City, onde você joga como V, um mercenário ciberpunk em busca de um implante único que garante imortalidade.'),

  ('Counter-Strike 2', 0.00, 'FPS',
   'https://images.igdb.com/igdb/image/upload/t_cover_big/co7x4b.jpg',
   'O FPS tático mais popular do mundo. Equipes de terroristas e contra-terroristas se enfrentam em partidas estratégicas de alta intensidade.'),

  ('EA Sports FC 25', 299.90, 'Esporte',
   'https://images.igdb.com/igdb/image/upload/t_cover_big/co8vkd.jpg',
   'O simulador de futebol mais completo do mercado, com modos Ultimate Team, carreira e clubes pro. Licenças oficiais das principais ligas do mundo.'),

  ('Hollow Knight', 19.90, 'Ação',
   'https://images.igdb.com/igdb/image/upload/t_cover_big/co1rgi.jpg',
   'Um desafiador e belíssimo jogo de ação e aventura ambientado em um vasto reino subterrâneo de insetos e heróis esquecidos.'),

  ('God of War: Ragnarök', 249.90, 'Aventura',
   'https://images.igdb.com/igdb/image/upload/t_cover_big/co5s5v.jpg',
   'Kratos e seu filho Atreus devem fazer uma jornada pelas Nove Reinos enquanto o Fimbulwinter — o prelúdio do Ragnarök — começa.'),

  ('Minecraft', 44.90, 'Aventura',
   'https://images.igdb.com/igdb/image/upload/t_cover_big/co49x5.jpg',
   'Um jogo sandbox de construção e sobrevivência com geração procedural infinita. Construa, explore e sobreviva em um mundo de blocos.'),

  ('Elden Ring', 199.90, 'RPG',
   'https://images.igdb.com/igdb/image/upload/t_cover_big/co4jni.jpg',
   'Um RPG de ação e fantasia desenvolvido pela FromSoftware em colaboração com George R. R. Martin. Explore as Terras Intermédias em busca do Anel Anciã.'),

  ('Stardew Valley', 29.90, 'RPG',
   'https://images.igdb.com/igdb/image/upload/t_cover_big/co5s6t.jpg',
   'Você herdou a velha fazenda do seu avô em Stardew Valley. Com algumas ferramentas e algumas moedas, você começa uma nova vida longe do estressante mundo corporativo.')
ON CONFLICT DO NOTHING;

-- -------------------------------------------------------------
-- COMPRAS
-- Usa subquery para referenciar por email/nome, evitando hardcode de IDs
-- -------------------------------------------------------------
INSERT INTO compra (user_id, jogo_id, data_compra, valor_pago)
SELECT u.id, j.id, '2025-01-10 14:32:00', j.preco
FROM usuarios u, jogo j
WHERE u.email = 'iago@email.com' AND j.nome = 'The Witcher 3: Wild Hunt'
ON CONFLICT DO NOTHING;

INSERT INTO compra (user_id, jogo_id, data_compra, valor_pago)
SELECT u.id, j.id, '2025-02-20 09:15:00', j.preco
FROM usuarios u, jogo j
WHERE u.email = 'iago@email.com' AND j.nome = 'Elden Ring'
ON CONFLICT DO NOTHING;

INSERT INTO compra (user_id, jogo_id, data_compra, valor_pago)
SELECT u.id, j.id, '2025-03-05 18:45:00', j.preco
FROM usuarios u, jogo j
WHERE u.email = 'maria@email.com' AND j.nome = 'Stardew Valley'
ON CONFLICT DO NOTHING;

INSERT INTO compra (user_id, jogo_id, data_compra, valor_pago)
SELECT u.id, j.id, '2025-03-15 21:00:00', j.preco
FROM usuarios u, jogo j
WHERE u.email = 'maria@email.com' AND j.nome = 'Hollow Knight'
ON CONFLICT DO NOTHING;

INSERT INTO compra (user_id, jogo_id, data_compra, valor_pago)
SELECT u.id, j.id, '2025-04-01 11:20:00', j.preco
FROM usuarios u, jogo j
WHERE u.email = 'carlos@email.com' AND j.nome = 'Red Dead Redemption 2'
ON CONFLICT DO NOTHING;

INSERT INTO compra (user_id, jogo_id, data_compra, valor_pago)
SELECT u.id, j.id, '2025-04-12 16:00:00', j.preco
FROM usuarios u, jogo j
WHERE u.email = 'carlos@email.com' AND j.nome = 'God of War: Ragnarök'
ON CONFLICT DO NOTHING;

-- -------------------------------------------------------------
-- BIBLIOTECA DOS USUÁRIOS (Many-to-Many: usuario_jogos)
-- Vincula os jogos comprados à biblioteca de cada usuário
-- -------------------------------------------------------------
INSERT INTO usuario_jogos (user_id, jogo_id)
SELECT u.id, j.id FROM usuarios u, jogo j
WHERE u.email = 'iago@email.com' AND j.nome = 'The Witcher 3: Wild Hunt'
ON CONFLICT DO NOTHING;

INSERT INTO usuario_jogos (user_id, jogo_id)
SELECT u.id, j.id FROM usuarios u, jogo j
WHERE u.email = 'iago@email.com' AND j.nome = 'Elden Ring'
ON CONFLICT DO NOTHING;

INSERT INTO usuario_jogos (user_id, jogo_id)
SELECT u.id, j.id FROM usuarios u, jogo j
WHERE u.email = 'maria@email.com' AND j.nome = 'Stardew Valley'
ON CONFLICT DO NOTHING;

INSERT INTO usuario_jogos (user_id, jogo_id)
SELECT u.id, j.id FROM usuarios u, jogo j
WHERE u.email = 'maria@email.com' AND j.nome = 'Hollow Knight'
ON CONFLICT DO NOTHING;

INSERT INTO usuario_jogos (user_id, jogo_id)
SELECT u.id, j.id FROM usuarios u, jogo j
WHERE u.email = 'carlos@email.com' AND j.nome = 'Red Dead Redemption 2'
ON CONFLICT DO NOTHING;

INSERT INTO usuario_jogos (user_id, jogo_id)
SELECT u.id, j.id FROM usuarios u, jogo j
WHERE u.email = 'carlos@email.com' AND j.nome = 'God of War: Ragnarök'
ON CONFLICT DO NOTHING;
