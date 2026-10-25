-- Conta de teste para quem avaliar o sistema (use se o banco já existir e o script completo der erro).
USE crowdcontrol_db;
INSERT INTO usuario (nome, email, senha, cargo, departamento, status, telefone, tipo)
VALUES ('Administrador', 'admin@crowdcontrol.com', 'admin123', 'Administrador', 'TI', 'Ativo', '0000', 'ADMINISTRADOR');
