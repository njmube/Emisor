FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/emisor-0.0.1.jar app.jar
ENV JAVA_OPTS="-Xmx300m"
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar -Dspring.profiles.active=dev /app.jar
