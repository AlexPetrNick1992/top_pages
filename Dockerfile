FROM openjdk:21-jdk

# RUN addgroup spring-boot-group && adduser --ingroup spring-boot-group spring-boot
# USER spring-boot:spring-boot-group

VOLUME /tmp
ARG JAR_FILE=pages-0.1.jar
WORKDIR /application


COPY target/${JAR_FILE} application.jar
COPY target/dependency lib

EXPOSE 8080
ENTRYPOINT exec java ${JAVA_OPTS} -cp lib/*:application.jar com.example.top.pages.PagesApplication ${0} ${@}
