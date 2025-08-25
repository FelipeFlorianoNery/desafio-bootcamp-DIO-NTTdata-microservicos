# Desafio de Arquitetura de Microsserviços com Java e Spring Cloud

Este repositório contém a implementação de um sistema de catálogo de produtos e simulação de pedidos, desenvolvido como parte de um desafio técnico para demonstrar conceitos fundamentais de arquiteturas distribuídas utilizando Java, Spring Boot e o ecossistema Spring Cloud.

##  Visão Geral da Arquitetura

O sistema foi projetado para ser modular, resiliente e escalável, seguindo os princípios de microsserviços. A arquitetura é composta por quatro serviços principais que se comunicam de forma síncrona via requisições HTTP, com a descoberta de serviços e o roteamento de borda geridos por componentes dedicados do Spring Cloud.

![Imagem da Arquitetura Proposta](https://hermes.dio.me/files/assets/c2e4ece2-999a-4c35-b4b2-3171ac7d0308.png)

---

##  Componentes da Arquitetura

Cada componente é um projeto Spring Boot independente, com responsabilidades bem definidas.

### 1. Eureka Server (`desafio-NttData`)
- **Função:** Atua como o **Service Discovery** (Registro e Descoberta de Serviços) da nossa aplicação. É o "diretório" central onde cada microsserviço se regista ao iniciar. Isto permite que os serviços se encontrem dinamicamente na rede através de um nome lógico (ex: `product-catalog-service`), eliminando a necessidade de configurar IPs e portas fixas.
- **Porta:** `8761`
- **Painel de Controlo:** `http://localhost:8761`

### 2. API Gateway (`api-gateway`)
- **Função:** Serve como o **ponto de entrada único** (Single Point of Entry) para todas as requisições vindas do cliente. Ele é responsável por rotear o tráfego para o microsserviço apropriado com base no caminho da URL (`/products/**`, `/orders/**`, etc.). Além do roteamento, um Gateway é o local ideal para centralizar funcionalidades transversais como autenticação, limitação de taxa (rate limiting) e logging.
- **Porta:** `8080`

### 3. Product Catalog Service (`product-catalog-service`)
- **Função:** Este microsserviço é o responsável por gerir todo o ciclo de vida dos produtos. Ele expõe uma API REST para operações de **CRUD** (Criar, Ler, Atualizar, Apagar). Utiliza um banco de dados em memória (H2) para simplificar a configuração do ambiente de desenvolvimento.
- **Porta:** `8081`

### 4. Order Simulation Service (`ordersimulation-service`)
- **Função:** Responsável por simular a criação de um pedido. Para obter os detalhes de um produto necessário para a simulação, este serviço faz uma chamada HTTP interna para o `Product Catalog Service`. A comunicação é facilitada pelo `RestTemplate` do Spring, configurado com `@LoadBalanced` para resolver o nome do serviço através do Eureka Server.
- **Porta:** `8082`

---

##  Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.x**: Framework principal para a criação dos microsserviços.
- **Spring Cloud**:
  - **Spring Cloud Gateway**: Para a implementação do API Gateway.
  - **Spring Cloud Netflix Eureka**: Para o Service Discovery.
- **Spring Data JPA**: Para a camada de persistência de dados no serviço de catálogo.
- **Maven**: Gestor de dependências e build do projeto.
- **H2 Database**: Banco de dados relacional em memória para fins de desenvolvimento e teste.

---

##  Pré-requisitos

Antes de executar o projeto, garanta que tem as seguintes ferramentas instaladas e configuradas no seu ambiente:
- **JDK 17** ou superior
- **Apache Maven 3.8+**
- **Git**
- Uma ferramenta de cliente API como o **Postman** ou **Insomnia** para realizar os testes.

---

##  Como Executar o Projeto

A ordem de inicialização dos serviços é crucial para o correto funcionamento do sistema. Siga os passos abaixo:

1.  **Clone o Repositório:**
    ```bash
    git clone [https://github.com/FelipeFlorianoNery/Desafio-Bootcamp-DIO-NTTData-microsservicos.git](https://github.com/FelipeFlorianoNery/Desafio-Bootcamp-DIO-NTTData-microsservicos.git)
    cd Desafio-Bootcamp-DIO-NTTData-microsservicos
    ```

2.  **Inicie o Eureka Server:**
    - Abra o projeto `desafio-NttData` no seu IDE.
    - Execute a aplicação Spring Boot.
    - Verifique se o painel do Eureka está acessível em `http://localhost:8761`.

3.  **Inicie o Product Catalog Service:**
    - Abra o projeto `product-catalog-service` numa nova janela do IDE.
    - Execute a aplicação.
    - Atualize o painel do Eureka para confirmar que o serviço `PRODUCT-CATALOG-SERVICE` se registou com sucesso.

4.  **Inicie o Order Simulation Service:**
    - Abra o projeto `ordersimulation-service` numa nova janela do IDE.
    - Execute a aplicação.
    - Verifique no painel do Eureka se o serviço `ORDER-SIMULATION-SERVICE` também foi registado.

5.  **Inicie o API Gateway:**
    - Abra o projeto `api-gateway` numa nova janela do IDE.
    - Execute a aplicação.
    - Verifique o registo do `API-GATEWAY` no Eureka.

Neste ponto, todos os quatro microsserviços estarão a ser executados e a comunicar entre si.

---

##  Como Testar (Endpoints da API)

Todas as requisições devem ser direcionadas para a porta do **API Gateway (`8080`)**.

#### 1. Criar um novo produto
- **Método:** `POST`
- **URL:** `http://localhost:8080/products`
- **Body (JSON):**
    ```json
    {
        "name": "Notebook Gamer Pro",
        "price": 8999.90
    }
    ```

#### 2. Consultar um produto por ID
- **Método:** `GET`
- **URL:** `http://localhost:8080/products/1`

#### 3. Listar todos os produtos
- **Método:** `GET`
- **URL:** `http://localhost:8080/products`

#### 4. Simular um pedido
- **Método:** `POST`
- **URL:** `http://localhost:8080/orders/simulate`
- **Body (JSON):**
    ```json
    {
        "productId": 1,
        "quantity": 2
    }
    ```
- **Resposta Esperada:** Uma mensagem de sucesso com o nome do produto e o valor total calculado.
Simulação de Pedido bem-sucedida! Produto: Notebook Gamer Pro, Quantidade: 2, Total: R$ 17999.80
---

## ✒️ Autor

**Felipe Floriano Nery**

* **GitHub:** [FelipeFlorianoNery](https://github.com/FelipeFlorianoNery)
