CREATE DATABASE crowdcontrol_db;
USE crowdcontrol_db;

CREATE TABLE usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    senha VARCHAR(100),
    cargo VARCHAR(50),
    departamento VARCHAR(50),
    status VARCHAR(20),
    telefone VARCHAR(20),
    tipo VARCHAR(50)
);

CREATE TABLE ordem_servico (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100),
    descricao TEXT,
    status VARCHAR(50),
    prioridade VARCHAR(20),
    solicitante VARCHAR(100),
    categoria VARCHAR(50),
    atribuida_para VARCHAR(100),
    local VARCHAR(100),
    prazo DATE
);

CREATE TABLE chamado (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100),
    descricao TEXT,
    solicitante VARCHAR(100),
    categoria VARCHAR(50),
    atribuida_para VARCHAR(100),
    prioridade VARCHAR(20),
    status VARCHAR(50),
    data_abertura DATETIME DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO usuario (nome, email, senha, cargo, departamento, status, telefone, tipo) VALUES
('Matheus', 'matheus@gmail.com', '123', 'Dev', 'TI', 'Ativo', '0000', 'KEY_USER'),
('Ana', 'ana@gmail.com', '123', 'Administradora', 'Gestão', 'Ativo', '1111', 'ADMINISTRADOR'),
('Carlos', 'carlos@gmail.com', '123', 'Suporte', 'TI', 'Ativo', '2222', 'FUNCIONARIO'),
('Juliana', 'juliana@gmail.com', '123', 'RH', 'Recursos Humanos', 'Ativo', '3333', 'FUNCIONARIO'),
('Pedro', 'pedro@gmail.com', '123', 'Analista', 'Financeiro', 'Inativo', '4444', 'FUNCIONARIO'),
('Lucas', 'lucas@gmail.com', '123', 'Dev', 'TI', 'Ativo', '5555', 'KEY_USER'),
('Administrador', 'admin@crowdcontrol.com', 'admin123', 'Administrador', 'TI', 'Ativo', '0000', 'ADMINISTRADOR');

INSERT INTO chamado (titulo, descricao, solicitante, categoria, atribuida_para, prioridade, status) VALUES
('Computador não liga', 'Sem energia', 'Lucas', 'Hardware', 'Carlos', 'critica', 'ABERTO'),
('Erro no ERP', 'Falha ao salvar dados', 'Ana', 'Sistema', 'Matheus', 'alta', 'EM_ANDAMENTO'),
('Wi-Fi instável', 'Quedas frequentes', 'Pedro', 'Rede', 'Carlos', 'media', 'ABERTO'),
('Monitor apagando', 'Tela piscando', 'Juliana', 'Hardware', 'Ana', 'baixa', 'RESOLVIDO'),
('Acesso negado', 'Sem permissão no sistema', 'João', 'Sistema', 'Matheus', 'alta', 'EM_ANDAMENTO'),
('Email não sincroniza', 'Outlook travado', 'Maria', 'Sistema', 'Carlos', 'media', 'ABERTO'),
('Teclado não funciona', 'Sem resposta', 'Lucas', 'Hardware', 'Ana', 'baixa', 'RESOLVIDO'),
('Sistema financeiro caiu', 'Erro crítico', 'Pedro', 'TI', 'Matheus', 'critica', 'EM_ANDAMENTO');
