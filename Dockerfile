FROM    maven

RUN     mkdir /docker

WORKDIR /docker

COPY    pom.xml .
RUN     mvn dependency:resolve

COPY    src .

RUN     mvn clean install