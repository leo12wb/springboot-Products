FROM openjdk:21

# Instala Maven
RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Define o diretório de trabalho
WORKDIR /app

# Copia o arquivo pom.xml primeiro para aproveitar o cache do Docker
COPY pom.xml .

# Instala as dependências do Maven
RUN mvn dependency:go-offline

# Copia o restante do código
COPY . .

# Executa o Spring Boot
CMD ["mvn", "spring-boot:run"]
