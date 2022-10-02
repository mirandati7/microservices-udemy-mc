# Criar a rede
docker network create cursoms-network
# Criar container Eureka Server
docker run --name cursoms-eureka -p 8761:8761 --network cursoms-network cursosms-eureka
# Criar container RabbitMQ
docker run -it --name cursoms-rabbitmq -p 5672:5672 -p 15672:15672 --network cursoms-network rabbitmq:3.9-management
docker run --name cursoms-keycloak -p 8081:8080 -e  KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin --network cursoms-network quay.io/keycloak/keycloak:18.0.0 start-dev 
# Criar container CursoMS-Cartoes
docker run --name cursoms-cartoes --network cursoms-network -e RABBITMQ_SERVER=cursoms-rabbitmq -e EUREKA_SERVER=cursoms-eureka -d cursosms-cartoes
docker run --name cursoms-clientes --network cursoms-network -e EUREKA_SERVER=cursoms-eureka -d cursosms-clientes
docker run --name cursoms-avaliadorcredito --network cursoms-network -e RABBITMQ_SERVER=cursoms-rabbitmq -e EUREKA_SERVER=cursoms-eureka -d cursosms-avaliadorcredito

docker run --name cursoms-gateway -p 8080:8080 -e KEYCLOAK_SERVER=cursoms-keycloak -e KEYCLOAK_PORT=801 -e EUREKA_SERVER=cursoms-eureka --network cursoms-network  -d cursosms-gateway


# Replicas portas aleatorias
docker run --name cursoms-cartoes-01 --network cursoms-network -e RABBITMQ_SERVER=cursoms-rabbitmq -e EUREKA_SERVER=cursoms-eureka -P -d cursosms-cartoes