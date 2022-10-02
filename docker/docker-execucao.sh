# Criar a rede
docker network create cursoms-network
# Criar container Eureka Server
docker run --name cursoms-eureka -p 8761:8761 --network cursoms-network cursosms-eureka
# Criar container RabbitMQ
docker run -it --name cursoms-rabbitmq -p 5672:5672 -p 15672:15672 --network cursoms-network rabbitmq:3.9-management
# Criar container CursoMS-Cartoes
docker run --name cursoms-cartoes --network cursoms-network -e RABBITMQ_SERVER=cursoms-rabbitmq -e EUREKA_SERVER=cursoms-eureka -d cursoms-cartoes
docker run --name cursoms-clientes --network cursoms-network -e EUREKA_SERVER=cursoms-eureka -d cursosms-clientes
docker run --name cursoms-avaliadorcredito --network cursoms-network -e RABBITMQ_SERVER=cursoms-rabbitmq -e EUREKA_SERVER=cursoms-eureka -d cursosms-avaliadorcredito