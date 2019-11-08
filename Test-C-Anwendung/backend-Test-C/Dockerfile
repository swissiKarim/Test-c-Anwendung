FROM maven:3-jdk-8-alpine
VOLUME /tmp
COPY ./ /
RUN ls -l

CMD ["mvn","spring-boot:run"]