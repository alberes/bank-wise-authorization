# Projeto: Servidor de authorização, recursos, cadastros de clients, accounts e transactions.
Este projeto é responsável por cadastro de usuários e clientes (ClientId e ClientSecret)      
e trabalha junto com authorizarion server e resource server.    
Também cadastrar transações, consultas e calcula juros.

Após o cadastrar as credenciais (Cliente e Conta) o usuário estará apto a criar transações.

O sistema usa o padrão arquitetural **CQRS** (Command Query Responsibility Segregation) que é um padrão para separar as operações de escrita e leitura para otimização além de outros benefícios.

* Funcionalidades
  * Usuários:          
    Cadastro, consulta de lista de usuários, consulta simples, atualização e exclusão.
  * Clientes (ClientId e ClientSecret):        
    Cadastro, consulta de lista de clientes, consulta simples, atualização e exclusão.
  * Suporte a múltiplos grant types:          
    Authorization Code, Client Credentials, Refresh Token, e outros.
  * Criação de transações (**deposito, pagamento, saque e consulta das transações e saldo**)
  * Cálculo de juros:
    * Em uma transação de depósito o sistema verifica se o saldo está negativo e caso gera uma nova transação com jutos de 1.02 (configurado)  e atualiza o saldo.
    * Exemplo: O cliente deposita um valor o sistema verifica o saldo e caso esteja negativo o sistema criará uma nova transação de juros com base no saldo negativo e faz o cálculo novamente para atualizar o saldo.
      * Saldo negativo dde R$ 100,00
      * Depósito de R$ 200,00
      * Sistema criará uma nova transação de 1.02 com saldo negativo R$ 100,00
      * Atualiza o saldo R$ 98,00
    * Caso o saldo ainda continue negativo o sistema irá calcular novos juros até o saldo ficar positivo.

Exemplo 1:
|  Ordem| Tipo | Valor| Saldo|
|--|--|--|--|
|1|Depósito|200.00|200.00|
|2|Pagamento|300.00|-100.00|
|3|Deposito|200.00|100.00|
|4|Juros|1.02|98.98|

Exemplo 2:
|  Ordem| Tipo | Valor| Saldo|
|--|--|--|--|
|1|Depósito|100.00|100.00|
|2|Pagamento|200.00|-100.00|
|3|Depósito|10.00|-90.00|
|4|Juros|1.02|-91.02|
|5|Depósito|50.00|-41.02|
|6|Juros|0.93|-41.95|
|7|Depósito|60.00|18.05|
|8|Depósito|0.43|17.62|


### Tecnologias Utilizadas
* Linguagem:
  * Java e SQL
* Banco de Dados Relacional: [h2database](https://h2database.com/html/main.html)
* * Banco de Dados Relacional: [Mongodb](https://www.mongodb.com/)
* Frameworks:
  * [Spring Boot 3.5.6](https://start.spring.io/)
* Dependencias:
  * [Spring Security](https://docs.spring.io/spring-security/reference/index.html)
  * [Spring Oauth2 Resource Server](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/index.html)
  * [JPA](https://docs.spring.io/spring-data/jpa/reference/index.html)
  * [Lombok](https://projectlombok.org/features/)
  * [Mapstruct](https://mapstruct.org/documentation/)
  * [OpenApi](https://springdoc.org/)
  * [actuator](https://docs.spring.io/spring-boot/docs/2.0.x/actuator-api/html/)
* JDK:
  * versão 17
* SO
  * Ubuntu 24.04.3 LTS 
* IDE:
  * [Intellij](https://www.jetbrains.com/idea/)
* Gerenciado de dependencias:
  * [Apache Maven 3.9.9](https://maven.apache.org/)
* Container:
  * [Docker](https://www.docker.com/)
  * [Docker Hub](https://hub.docker.com/)
* Kubernates
  * [kubectl](https://kubernetes.io/pt-br/docs/tasks/tools/install-kubectl-linux/)
  * [Minikube](https://minikube.sigs.k8s.io/docs/start/?arch=%2Flinux%2Fx86-64%2Fstable%2Fbinary+download)
* Ferramentas:
  * [Postman](https://www.postman.com/)
  * [Beekeeper](https://www.beekeeperstudio.io/)
  * [MongoDB Compass](https://www.mongodb.com/products/tools/compass)
* Ferramentas auxiliares:
  * [Markdown](https://stackedit.io/app#)
  * [Criar diagramas](https://docs.github.com/pt/get-started/writing-on-github/working-with-advanced-formatting/creating-diagrams)
  * [JWT IO](https://jwt.io/)
  * [4DEV](https://www.4devs.com.br/)

### Como Executar
1. Clone o repositório: git clone [bank-wise-authorization](https://github.com/alberes/bank-wise-authorization)
```
git clone https://github.com/alberes/bank-wise-authorization.git
```
2. O Banco de dados utilizado foi o H2 que sobe junto com aplicação:
- As configurações econtra-se no arquivo application-dev.yaml
- Banco de dados: bank-wise
- Tabelas: O arquivo DDL.sql com os dados        
  Certifique-se de que o banco de dados está configurado corretamente.          
  A aplicação irá criar as tabelas automaticamento caso não exista ou execute o script antes que se encontra no projeto.          
  Localizar os arquivos [SUB_DIRETORIOS]/bank-wise-authorization/DDL.sql e [SUB_DIRETORIOS]/bank-wise-authorization/DML.sql
  - [Acessar o h2-console](http://localhost:8080/h2-console)
  - ![H2](h2.png)
3. Banco de dados não relacional MongoDB:
- As configurações econtra-se no arquivo application-dev.yaml e deverá ser alterado caso tenha necessidade.
- No projeto foi utilizado uma imagem docker para facilitar o desenvolvimento e caso tenha instalado o MongoDB na máquina mude apenas as credenciais.  
```
docker run -d --name mongodb -p 27017:27017 -e MONGO_INITDB_ROOT_USERNAME=bank-wise-admin -e MONGO_INITDB_ROOT_PASSWORD=bank-wise-pass mongo:latest
```
4. [Swagger](http://localhost:8080/swagger-ui/index.html)
5. Executar o projeto
- Abrir o terminal no diretório raiz do projeto [SUB_DIRETORIOS]/bank-wise-authorization e executar o comando abaixo para gerar o pacote.
``` 
mvn -DskipTests=true clean package 
``` 
- No termial entrar no diretório [SUB_DIRETORIOS]/bank-wise-authorization/target
 ``` 
java -jar bank-wise-authorization-0.0.1-SNAPSHOT.jar
 ``` 

A aplicação subirá na porta 8080

A aplicação tem um client Admin para poder cadastrar os outros usuários.

[Collection Postman](bank-wise-authorization.postman_collection.json)

API para autenticação client_credencial para obter um token e cadastrar outros clients/Account
```
curl --location 'http://localhost:8080/oauth2/token' \
--header 'Authorization: Basic YWRtaW4tY2xpZW50LWlkOmFkbWluLWNsaWVudC1zZWNyZXQ=' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--header 'Cookie: JSESSIONID=A27C1772CA58891E66C875AA89B12A6F' \
--data-urlencode 'grant_type=client_credentials' \
--data-urlencode 'scope=ADMIN'
``` 

Cadastrar um novo client    

```
curl --location 'http://localhost:8080/api/v1/clients' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJraWQiOiI0NjZmYzlkMS0yMjRlLTQ5MzQtYWVjYS1hMDU4MjJmZTljZWUiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhZG1pbi1jbGllbnQtaWQiLCJhdWQiOiJhZG1pbi1jbGllbnQtaWQiLCJuYmYiOjE3NjAzMTAyODIsInNjb3BlIjpbIkFETUlOIl0sImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MCIsImlkIjoiM2RlMzI0Y2EtY2M2Yy00ZWVlLTk4OTAtYmQwNzZmOGFkMDk4IiwiZXhwIjoxNzYwMzI4MjgyLCJpYXQiOjE3NjAzMTAyODIsImp0aSI6IjBkMmM1NzNlLTZiMjctNDQyYy1iNmQyLThkZDhmY2E1YWQxOCJ9.f2YKLF2vdaGO8ugF62Mz2L-KRcQq6VL7V9tC_6HQWb-zRTVfG-cBhpCHW7l_BQkSWe869vE2PSdf3VXI67aanIA4iD7Hw0JzZcjS3jBgROfZ3QbLwvwUIODJQ6vAhQ9XK0RroxLe76Zk41wP-1o2NC5YLp6-yaxALntBdU6JgExkbjRPO8mOrqBec2zUx-OuzJTV_vxSft-UERihJNEJYxoDouyAi5dW6jTSbt9JHjnPCPIXZvr5OHLTtOoKdEtgzFn0-h_yYPLQyXMltQJt1Z4UXB89_FutjEwZemRzYBi3gB9bTt0xuivm-ilsmKUYgmK4vw_aK_Rs0iHPwl_uVQ' \
--data '{
    "clientId": "postman-client",
    "clientSecret": "postman-secret",
    "redirectURI": "http://localhost:9090/login/oauth2/code/bank-wise-client-oidc",
    "scope": "USER"
}'
```

Cadastrar uma nova conta    
``` 
curl --location 'http://localhost:8080/api/v1/accounts' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer [TOKEN]' \
--data-raw '{
    "name": "Posterman User",
    "legalEntityNumber": "59964669020",
    "login": "postman@postman.com",
    "password": "postman123",
    "scopes": [
    "write",
    "read",
    "update",
    "delete"
  ]
}'
```

Para criar uma nova tranação utilize o Postman para fazer autenticação (Authorization Code) e seiga os passos abaixo:  
1. Criar uma nova requisição  
2. Clique em Authorization  
3. Token Name: Um nome para identificação do token  
4. Grant type: Authorization Code  
5. Callback URL: Deve ser o valor que foi cadastrado no client em redirectURI  
6. Auth URL: http://localhost:8080/oauth2/authorize  
7. Access Token URL: http://localhost:8080/oauth2/token  
8. Client ID: Deve ser o valor que foi cadastrado no client em clientId  
9. Client Secret: Deve ser o valor que foi cadastrado no client em clientSecret  
10. Client Authentication: Send as Basic Auth header  
11. Clique em **Clear cookies** para limpar os cookies e depois em **Get New Access Token**  
12. Abrirá um formulário solicitando usuário e senha, preecher com os valores cadastrados na conta.    
    ![Autorization Code](Postman-Authotization-Code.png)    
    Com acesso ao token o usuário poderá criar transações e fazer consultas.  
  
Para cadastrar uma nova tranação utilizar o cURL abaixo e substitua os valores:      
[ACCOUNT_ID]: Id da conta cadastrada      
[TOKEN_OBTIDO_NA_AUTENTICAÇÃO]: Token obtido na autenticação Authorization Code      
Valores válidos para Type: DEPOSIT | WITHDRAW | PAYMENT  
```
curl --location 'http://localhost:8080/api/v1/accounts/[ACCOUNT_ID]/transactions' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer [TOKEN_OBTIDO_NA_AUTENTICAÇÃO]' \
--data '{
  "type": "DEPOSIT",
  "transactionValue": 200.00
 }'
```

Consultar as transações
- Adicionar o token no header [ACCOUNT_ID]: Id da conta cadastrada [TOKEN_OBTIDO_NA_AUTENTICAÇÃO]: Token obtido na autenticação
```
curl --location 'http://localhost:8080/api/v1/accounts/[ACCOUNT_ID]/transactions' \
--header 'Authorization: Bearer [TOKEN_OBTIDO_NA_AUTENTICAÇÃO]'
```

* Observabilidade e métricas
  - [Monitoramento](http://localhost:8081/actuator)
  - [Log](http://localhost:8081/actuator/logfile)
  - [Metricas](http://localhost:8081/actuator/metrics)
  - [DataSource](http://localhost:8081/actuator/metrics/hikaricp.connections.active)
  - [Memória](http://localhost:8081/actuator/metrics/jvm.buffer.memory.used)
  - [CPU](http://localhost:8081/actuator/metrics/process.cpu.usage)
  - [Autorzação](http://localhost:8081/actuator/metrics/spring.security.authorizations.active)
  - [Sessões](tomcat.sessions.active.current)


1. Montando um ambiente Docker
- Criando uma imagem da aplicação
```  
docker build --tag alberes/bank-wise-authorization:1.0.0 .
```
Criar uma network para comunicação entre os containeres
```
docker network create bank-wise-authorization-network
```
Subir o MongoDB com a rede criada
```
docker run -d --name mongodb -p 27017:27017 -e MONGO_INITDB_ROOT_USERNAME=bank-wise-admin -e MONGO_INITDB_ROOT_PASSWORD=bank-wise-pass --network bank-wise-authorization-network -d mongo:8.0.15
```
- Subindo um container Docker da aplicação bank-wise-authorization com variáveis
```
docker run --name bank-wise-authorization -p 8080:8080 -e USERNAME=sa -e PASSWORD= -e JPA_HIBERNATE_DDL_AUTO=update -e JPA_HIBERNATE_SHOW_SQL=true -e JPA_PROPERTIES_HIBERNATE=true -e MONGODB_URI=mongodb://bank-wise-admin:bank-wise-pass@mongodb:27017/bank-wise?authSource=admin -e MANAGEMENT_SERVER_PORT=8081 -e LOGGIN_FILE_NAME=bank-wise-authorization.log -e INTEREST_RATE=1.02 -e ACCESS_TOKEN_EXPIRATION=300 -e REFRESH_TOKEN_EXPIRATION=600 -e METRICS_SYSTEM_PROCESSOR=false --network bank-wise-authorization-network -d alberes/bank-wise-authorization:1.0.0
```

2. Já existe as imagens no repostório docker então basta executar o comando abaixo para subir a aplicação.
```
docker-compose up -d
```

3. Ambiente Linux
   Caso a aplicação bank-wise-authorization não responda siga os passos abaixo:
   3.1 Verifique se a aplicação está no ar usando o comando:
```
docker ps
```
Caso não esteja na lista verifique o logs do container
```
docker logs bank-wise-authorization
```
Caso contenha o erro abaixo o problema está na monitoração do **actuator** e devera seguir alguns passos no ambiente Linux:
> java.lang.NullPointerException: Cannot invoke "jdk.internal.platform.CgroupInfo.getMountPoint()" because "anyController" is null

**Passo 1: Verifique a versão do cgroups**  
Confirme se você está usando o cgroups v2 com o comando:
```
stat -fc %T /sys/fs/cgroup/
```
Se a saída for  `cgroup2fs`, você está usando o cgroups v2.

**Passo 2: Edite o arquivo de configuração do GRUB**  
Modifique o arquivo  `/etc/default/grub`  para adicionar os parâmetros do kernel que habilitam o cgroups v1 para a memória e desabilitam o cgroups v2:

1.  Abra o arquivo com um editor de texto com privilégios de superusuário:
```
sudo nano /etc/default/grub
```
2.  Encontre a linha que começa com  `GRUB_CMDLINE_LINUX_DEFAULT`  e adicione os seguintes parâmetros dentro das aspas:  
    `cgroup_enable=memory systemd.unified_cgroup_hierarchy=0`  
    A linha deve ficar assim:  
    `GRUB_CMDLINE_LINUX_DEFAULT="... cgroup_enable=memory systemd.unified_cgroup_hierarchy=0"`

    > **Importante:**  Não apague outros parâmetros existentes, apenas adicione os novos ao final.
    **Passo 3: Atualize a configuração do GRUB**  
    Execute o comando para aplicar as alterações:
```
sudo update-grub
```
**Passo 4: Reinicie o sistema**  
Para que as mudanças nos parâmetros do kernel entrem em vigor, você deve reiniciar o computador:
```
sudo reboot
```
Após o reinício, execute  `cat /proc/cgroups`  novamente. A linha  `memory`  deverá aparecer.

5. Melhorando a aplicação com uso do Kubernates (K8S)

Na pasta K8S temos os arquivos para criação do ambiente
[bank-wise-authorization-namespace.yaml](k8s/bank-wise-authorization-namespace.yaml)
[bank-wise-authorization-mongodb-configmap-secret.yaml](k8s/bank-wise-authorization-mongodb-configmap-secret.yaml)
[bank-wise-authorization-mongodb-deployment.yaml](k8s/bank-wise-authorization-mongodb-deployment.yaml)
[bank-wise-authorization-mongodb-service.yaml](k8s/bank-wise-authorization-mongodb-service.yaml)
[bank-wise-authorization-configmap-secret.yaml](k8s/bank-wise-authorization-configmap-secret.yaml)
[bank-wise-authorization-deployment.yaml](k8s/bank-wise-authorization-deployment.yaml)
[bank-wise-authorization-service.yaml](k8s/bank-wise-authorization-service.yaml)

5.1 Criar um namespace para aplicação (bank-wise-authorization-ns)
```
kubectl apply -f bank-wise-authorization-namespace.yaml
```
5.2 Configurar o contexto do namespace
```
kubectl config set-context --current --namespace=bank-wise-authorization-ns
```
5.3 Criar o ConfigMap do mongodb
```
kubectl apply -f bank-wise-authorization-mongodb-configmap-secret.yaml
```
5.4 Criar um deployment do mongodb
```
kubectl apply -f bank-wise-authorization-mongodb-deployment.yaml
```
5.5 Criar um service para o deployment mongodb
```
kubectl apply -f bank-wise-authorization-mongodb-service.yaml
```
5.6 Obter o endereço IP dp service do mongodb
```
minikube service mongodb-service --url --namespace=bank-wise-authorization-ns
```
5.5 Localizar a variável MONGODB_URI no arquivo k8s/bank-wise-authorization-deployment.yaml e substituir o IP porta na linha value.

5.6 Criar o ConfigMap do bank-wise-authorization
```
kubectl apply -f bank-wise-authorization-configmap-secret.yaml
```
5.7 Criar um deployment da aplicação bank-wise
```
kubectl apply -f bank-wise-authorization-deployment.yaml
```
5.8 Criar um service para o deployment bank-wise
```
kubectl apply -f bank-wise-authorization-service.yaml
```
5.9 Obter o endereço IP dp service da aplicação bank-wise - Utilizar na collection Postman
```
minikube service bank-wise-authorization-service --url --namespace=bank-wise-authorization-ns
```

