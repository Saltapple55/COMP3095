#------Build--------
#doceker file uses gradle 8- this is an image that your docker container will be based on
FROM gradle:8-jdk21-alpine AS builder

#copy all files to /home/gradle/src
#COPY --chown=gradle:gradle - change owner to (user) gradle: (group)gradle - when image is initialized
 #from '.', which is inventory-service directory into filesystem of image /home/gradle/src
COPY --chown=gradle:gradle . /home/gradle/src

#change work dir
WORKDIR /home/gradle/src

#gradle is the gradle command - creates the jar file - everything else is destroyedd
RUN gradle build -x test

#Package Stage
#using openjdk image to create final container
FROM openjdk:21-jdk

#create /app dir inside container
RUN mkdir /app
#copy already created jar file inside of homegradle/src/libs into /app folder name it ps.jar
# if no --from=image, then will look at host.
COPY --from=builder /home/gradle/src/build/libs/*.jar /app/inventory-service.jar

#
ENV POSTGRES_USERNAME=admin \
    POSTGRES_PASSWORD=password
#expose point 8084 of our container
EXPOSE 8083

#entrypoint defines command to run when container starts
#command line analogy: java -har /app/product-service.jar - runs the container
ENTRYPOINT ["java", "-jar", "/app/inventory-service.jar"]
