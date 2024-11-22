#------Build--------
#doceker file uses gradle 8
FROM gradle:8-jdk21-alpine AS builder

#copy all files to /home/gradle/src
#COPY --chown=gradle:gradle product-service /home/gradle/src
COPY --chown=gradle:gradle . /home/gradle/src

#change work dir
WORKDIR /home/gradle/src

RUN gradle build -x test

#Package Stage
#using openjdk to create final container
FROM openjdk:21-jdk

#create /app dir inside container
RUN mkdir /app
#copy already created jar file inside of homegradle/src/libs into /app folder name it ps.jar
COPY --from=builder /home/gradle/src/build/libs/*.jar /app/order-service.jar

#
ENV POSTGRES_USERNAME=admin \
    POSTGRES_PASSWORD=password
#expose point 8084 of our container
EXPOSE 8082

#entrypoint defines command to run when container starts
#command line analogy: java -har /app/product-service.jar
ENTRYPOINT ["java", "-jar", "/app/order-service.jar"]
