# <center> People Manager API - Java </center>

###### <center>API para gerenciamento de pessoas e endereços.</center>

## Tecnologias utilizadas:

- Java 17
- Spring Boot
- Swagger
- H2
- JUnit e Mockito
- Maven
- Git
- Docker & Docker Compose
- Deploy AWS [OFF]

## Deploy [OFF]

O deploy da API foi feito na plataforma de computação em nuvem AWS. O serviço utilizado foi o Elastic Beanstalk, que permite realizar a implantação de aplicações WEB de forma simplificada.
[Clique aqui](http://peoplemanagerapi-env.eba-zjrxexgq.us-east-1.elasticbeanstalk.com/swagger-ui/index.html#/) para acessar a API.

## Como executar a API localmente:

Se preferir executar a aplicação em seu próprio computador, há duas maneiras:
1. Clonando o repositório.
2. Utilizando Docker e Docker Compose.
 
### Clonando o repositório
1. Clone este repositório utilizando o comando: ```git clone https://github.com/T-Lobato/peoplemanagerapi.git```
2. Abra o projeto em sua IDE de preferência e execute o arquivo: **PeopleManagerApiApplication.java**
3. Para consultar os endpoints disponíveis na API acesse no seu navegador o endereço: ``` http://localhost:5000/swagger-ui/index.html#/ ```

### Utilizando Docker e Docker Compose
1. Certifique-se de ter o Docker e o Docker Compose instalados na sua máquina.
2. Na pasta raiz do projeto, abra um terminal e execute o seguinte comando: ```docker compose up -d```
3. Para consultar os endpoints disponíveis na API acesse no seu navegador o endereço: ``` http://localhost:8080/swagger-ui/index.html#/ ``` 

## Recursos da API:

Abaixo serão apresentados os recursos que a API fornece. Com a aplicação executando você poderá:

- Registrar, atualizar e deletar pessoas;
- Buscar uma pessoa pelo seu id;
- Listar todas as pessoas registradas;
- Salvar endereços para uma pessoa;
- Tornar um endereço como principal;
- Listar todos os endereços de uma pessoa.

