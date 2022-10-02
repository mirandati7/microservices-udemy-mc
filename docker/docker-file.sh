 docker build cursosms-eureka .
 docker build --tag cursosms-eureka:1.0 .
 docker run --name teste-eureka -p 8761:8761 cursosms-eureka
 docker build --tag cursosms-cartoes .

 docker network create cursoms-network
 docker run --name cursoms-eureka -p 8761:8761 --network cursoms-network cursosms-eureka

 docker run --name cursosms-cartoes --network cursoms-network -e RABBITMQ_SERVER=cursoms-rabbitmq -e EUREKA_SERVER=cursoms-eureka -d cursoms-cartoes