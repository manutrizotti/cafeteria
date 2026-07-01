-- 1. Conecta ao banco de dados padrão do PostgreSQL para conseguir executar comandos de sistema
\c postgres

-- 2. Limpa o ambiente caso a pessoa já tenha rodado o script antes (evita duplicação)
DROP DATABASE IF EXISTS cafeteria_db;

-- 3. Cria o banco de dados do zero
CREATE DATABASE cafeteria_db;

-- 4. Conecta no banco de dados que acabou de ser criado
\c cafeteria_db

-- 5. Criação das Tabelas
CREATE TABLE mesas (
    id_mesa SERIAL PRIMARY KEY,
    numero INT NOT NULL,
    status_ocupacao VARCHAR(20) NOT NULL DEFAULT 'Disponível'
);

CREATE TABLE funcionarios (
    id_funcionario SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    idade INT,
    cargo VARCHAR(50) NOT NULL
);

CREATE TABLE produtos (
    id_produto SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    preco DECIMAL(10, 2) NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    tempo_preparo INT
);

CREATE TABLE comandas (
    id_comanda SERIAL PRIMARY KEY,
    id_mesa INT NOT NULL,
    id_funcionario_caixa INT NOT NULL,
    id_funcionario_garcom INT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ABERTA',
    forma_pgto VARCHAR(50),
    gorjeta DECIMAL(10, 2) DEFAULT 0.00,
    data TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_mesa) REFERENCES mesas(id_mesa),
    FOREIGN KEY (id_funcionario_caixa) REFERENCES funcionarios(id_funcionario),
    FOREIGN KEY (id_funcionario_garcom) REFERENCES funcionarios(id_funcionario)
);

CREATE TABLE itens (
    id_item SERIAL PRIMARY KEY,
    id_comanda INT NOT NULL,
    id_produto INT NOT NULL,
    qtd INT NOT NULL DEFAULT 1,
    FOREIGN KEY (id_comanda) REFERENCES comandas(id_comanda) ON DELETE CASCADE,
    FOREIGN KEY (id_produto) REFERENCES produtos(id_produto)
);

-- 6. Inserts Iniciais para testes
INSERT INTO mesas (numero, status_ocupacao) VALUES 
(11, 'Disponível'), (12, 'Disponível'), (21, 'Ocupada'), (22, 'Disponível'), (31, 'Disponível');

INSERT INTO funcionarios (nome, idade, cargo) VALUES 
('Ana Souza', 25, 'Caixa'), ('Carlos Pereira', 30, 'Garçom'), ('Marcos Silva', 22, 'Garçom'), ('Juliana Costa', 28, 'Cozinheiro');

INSERT INTO produtos (nome, preco, categoria, tempo_preparo) VALUES 
('Coca-Cola 350ml', 6.50, 'Bebida', 2), ('Suco de Laranja', 9.00, 'Bebida', 5), ('Coxinha de Frango', 7.50, 'Salgado', 5), ('Hambúrguer Artesanal', 28.90, 'Salgado', 20), ('Pudim de Leite', 12.00, 'Doce', 3), ('Brownie com Sorvete', 18.50, 'Doce', 10);

INSERT INTO comandas (id_mesa, id_funcionario_caixa, id_funcionario_garcom, status, gorjeta) VALUES 
(3, 1, 2, 'ABERTA', 0.00);

INSERT INTO itens (id_comanda, id_produto, qtd) VALUES 
(1, 4, 2), (1, 1, 2), (1, 6, 1);
