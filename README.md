# Sistema de Gerenciamento de Comandas - Cafeteria

Sistema desktop em Java para controle operacional de uma cafeteria, contemplando a gestão de mesas, produtos, funcionários e ciclo de vida de comandas.

## 🚀 Funcionalidades
* **Comandas:** Abertura, controle de itens, gorjetas e fechamento (integração direta com status das mesas).
* **Mesas:** Mapeamento em tempo real (Disponível, Ocupada, Reservada).
* **Cadastros:** CRUD de Produtos e Funcionários.

## 🛠️ Tecnologias Utilizadas
* **Java** (Swing para Interface Gráfica)
* **JDBC** (Comunicação com o banco de dados)
* **PostgreSQL** (Banco de dados relacional)
* **Maven** (Gerenciamento de dependências)

## 📦 Como executar
1. Execute o script `database/script_banco.sql` no PostgreSQL (certifique-se de ter um banco chamado `cafeteria_db`).
2. Ajuste as credenciais (usuário e senha) no arquivo `ConexaoDB.java`.
3. Compile e rode o projeto pela classe `TelaPrincipal.java`.
