FROM confluentinc/cp-kafka-connect-base:7.8.0

ENV CONNECT_PLUGIN_PATH="/usr/share/java"

RUN confluent-hub install --no-prompt --component-dir /usr/share/java confluentinc/kafka-connect-jdbc:10.7.4
RUN curl -L -o /usr/share/java/confluentinc-kafka-connect-jdbc/lib/mysql-connector-java-8.0.23.jar \
    https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.23/mysql-connector-java-8.0.23.jar


EXPOSE 8083

CMD ["/etc/confluent/docker/run"]
