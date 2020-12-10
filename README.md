# donus-digital
Projeto teste para vaga de dev na donus, pock de conta bancária.

Desenvolvi o conceito para as principais funcionalidades que precisam para funcionar a conta corrente e 
criei a estrutura numa arquitetura de interfaces facilitando a implementação de novas operações.

## Pré-requisitos
- Instalar [Java 11 SDK ](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- Instalar [Maven3+](https://maven.apache.org/download.cgi)
- Banco de dados - Optei por H2, pois o intuito é mostrar o conhecimento com a linguagem

## Rodar 

1. Buildar o projeto:  
`mvn clean verify`

2. Após isso, digite o comando:  
`cd target/`

3. Execute o projeto com o comando (caso o java home seja a versão 11 padrão):  
`java -jar donus-digital-1.0.0.jar`

3.1 Caso você já tenha uma outra JDK como padrão, procure onde esteja path da JDK 11 e rode assim:
`<path_to_jdk11>/bin/java -jar donus-digital-1.0.0.jar`

Ex: `/Users/gabrielcastro/conf_devs/desenvolvimento/jdks/jdk-11.0.7.jdk/Contents/Home/bin/java -jar donus-digital-1.0.0.jar `

## Endpoints disponíveis

CONTAS:   
* `[POST] /api/v1/contas`
* `[GET] /api/v1/contas/{numeroConta}`
* `[PATCH] /api/v1/contas/{numeroConta}/depositos`
* `[PATCH] /api/v1/contas/{numeroConta}/saques`
* `[PATCH] /api/v1/contas/{numeroConta}/transferencias`


HISTÓRICOS DE CONTAS:   
* `[GET] /api/V1/transacoes/contas/{numeroConta}`


## Exemplos uso da API por comando de linha:

Criação de conta:   
`curl -H "Content-Type: application/json" -X POST -d '{ "nome": "Teste", "cpf": "960.269.278-24" }' http://localhost:8080/api/v1/contas`

Consulta conta:   
`curl -H http://localhost:8080/api/v1/contas/1`

Depósitos:   
`curl -H "Content-Type: application/json" -X PATCH -d '{ "valor": 150.00 }' http://localhost:8080/api/v1/contas/1/depositos`

Saques:   
`curl -H "Content-Type: application/json" -X PATCH -d '{ "valor": 150.00 }' http://localhost:8080/api/v1/contas/1/saques`

Transferências:   
`curl -H "Content-Type: application/json" -X PATCH -d '{ "valor": 150.00, "contaDestino": 2, "cpfTitular": "960.269.278-24" }' http://localhost:8080/api/v1/contas/1/transferencias`

Consulta transações:   
`curl -H http://localhost:8080/api/v1/transacoes/contas/1`

Consulta transações informando período:   
`curl -H http://localhost:8080/api/v1/transacoes/contas/1?dataInicio=2020-12-01&dataFim=2020-12-20`