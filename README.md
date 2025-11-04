# POS TECH - Tech Challenge (Módulo Usuários)

## 🛠️ Arquitetura e Desenvolvimento Java - Fase 1
Este projeto implementa o backend robusto em Spring Boot para o módulo de gestão de usuários do sistema unificado de restaurantes. O desenvolvimento seguiu rigorosamente os princípios de SOLID , Orientação a Objetos e boas práticas de codificação.

____

## 🎯 Requisitos de Funcionalidade (Fase 1 - Usuários)
### O sistema atende a todos os requisitos especificados  para a gestão de usuários:

- Tipos de Usuário: Implementa obrigatoriamente Dono de restaurante e Cliente.

- CRUD e Busca: Permite Cadastro, atualização e exclusão de usuários, e busca por nome.

- Regras: Garante unicidade do e-mail.

- Login: Possui um serviço para validação de login obrigatória.

- Endpoints Separados: Contempla um endpoint separado para Troca de Senha e um endpoint distinto para Atualização de Dados.

- Data: Registra a data da última alteração.

____

## 🐳 Executando a Aplicação com Docker Compose (Passo a Passo)
### O sistema é dockerizado e utiliza Docker Compose para orquestrar o backend com o banco de dados MySQL.


Pré-requisitos
Instalação e execução do Docker e Docker Compose.
Obs: Não pode ter serviço de MySQL rodando no seu computador.

### 1. Compilação e Criação da Imagem
Navegue até a raiz do projeto (onde estão o Dockerfile e o docker-compose.yml) e execute o comando para construir as imagens:

````Bash
docker compose up --build
````
O backend estará acessível na porta 8080.

### 2. Acesso e Documentação
- Documentação Swagger/OpenAPI: http://localhost:8080/swagger-ui.html

____

## ⚙️ Arquitetura e Documentação

- Backend: Spring Boot 3.5.7 e Java 21.
- Banco de Dados: MySQL (Rodando em container Docker).
- Versionamento: Estratégia de versionamento de API via URI (/api/v1).
- Tratamento de Erros: Padrão ProblemDetail (RFC 7807).

____

## Coleção Postman
O arquivo TechChallenge-Backend.postman_collection.json está incluído neste repositório e cobre todos os cenários obrigatórios  (Cadastro, Login, Atualização de Senha, Duplicidade, etc.).
