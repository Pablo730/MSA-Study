FROM confluentinc/cp-kafka-connect:7.2.15

# Confluent JDBC Connector 설치
RUN confluent-hub install --no-prompt confluentinc/kafka-connect-jdbc:latest

# MySQL JDBC 드라이버 추가 (버전은 필요 시 고정)
ADD https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar /opt/connectors/mysql-connector-j.jar
