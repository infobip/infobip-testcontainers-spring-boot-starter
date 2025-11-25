# Infobip Testcontainers Spring Boot Starter

[![](https://github.com/infobip/infobip-testcontainers-spring-boot-starter/workflows/maven/badge.svg)](https://github.com/infobip/infobip-testcontainers-spring-boot-starter/actions?query=workflow%3Amaven)
[![Maven Central](https://maven-badges.sml.io/sonatype-central/com.infobip/infobip-testcontainers-spring-boot-starter/badge.svg)](https://maven-badges.sml.io/sonatype-central/com.infobip/infobip-testcontainers-spring-boot-starter)
[![Coverage Status](https://coveralls.io/repos/github/infobip/infobip-testcontainers-spring-boot-starter/badge.svg?branch=master)](https://coveralls.io/github/infobip/infobip-testcontainers-spring-boot-starter?branch=master)

Library containing Spring Boot starters which manage lifecycle (start/stop) of [testcontainers](https://www.testcontainers.org/).

Usual use cases include:
- tests (container is started during test spring context initialization and stopped during context destruction)
- local development (e.g. to remove manual setup of local DB)

## Contents

* [Changelog](#Changelog)
* [Usage](#Usage)
    * [General](#General)
    * [Reusable](#Reusable)
    * [MSSQL](#MSSQL)
        * [Tests](#MSSQLTests)
        * [Local development](#MSSQLLocalDevelopment)
        * [Docker image version](#MSSQLDockerImage)
        * [Initialization script](#MSSQLInitScript)
    * [MySQL](#MySQL)
      * [Tests](#MySQLTests)
      * [Local development](#MySQLLocalDevelopment)
      * [Docker image version](#MySQLDockerImage)
      * [Initialization script](#MySQLInitScript)
    * [PostgreSQL](#PostgreSQL)
        * [Tests](#PostgreSQLTests)
        * [Local development](#PostgreSQLLocalDevelopment)
        * [Docker image version](#PostgreSQLDockerImage)
        * [Initialization script](#PostgreSQLInitScript)
    * [Redis](#Redis)
        * [Tests](#RedisTests)
        * [Local development](#RedisDevelopment)
        * [Docker image version](#RedisDockerImage)
    * [Kafka](#Kafka)
        * [Automatic topic creation](#KafkaTopic)
        * [Tests](#KafkaTests)
        * [Local development](#KafkaLocalDevelopment)
        * [Docker image version](#KafkaDockerImageVersion)
    * [RabbitMQ](#RabbitMq)
        * [Tests](#RabbitMqTests)
        * [Local development](#RabbitMqLocalDevelopment)
        * [Docker image version](#RabbitMqDockerImage)
    * [ClickHouse](#ClickHouse)
        * [Tests](#ClickHouseTests)
        * [Local development](#ClickHouseLocalDevelopment)
        * [Docker image version](#ClickHouseDockerImage)
        * [Initialization script](#ClickHouseInitScript)

<a id="Changelog"></a>
## Changelog

For changes check the [changelog](CHANGELOG.md).

<a id="Usage"></a>
## Usage

<a id="General"></a>
### General

This library tries to reuse existing Spring Boot configuration classes and enhance their behaviour by performing some extra steps around them.
Generally, in cases where port placeholders are used (`<port>`), the library will make sure that the appropriate Docker container is started on
a randomly selected open port and that the selected value will be used by the configuration in the runtime.
You can use a concrete value instead of the placeholder - in that case the library will attempt to start the container on the specified port.

<a id="Reusable"></a>
### Reusable
If [reuse is enabled](https://java.testcontainers.org/features/reuse/) this project automatically marks all created containers for reuse.

<a id="MSSQL"></a>
### MSSQL

**Disclaimer**: by using this testcontainer you accept the EULA for mssql docker image as [required here](https://hub.docker.com/_/microsoft-mssql-server).

<a id="MSSQLTests"></a>
#### Tests: 

Include the dependency:

```xml
<dependency>
	<groupId>com.infobip</groupId>
	<artifactId>infobip-mssql-testcontainers-spring-boot-starter</artifactId>
	<version>${infobip-mssql-testcontainers-spring-boot-starter.version}</version>
	<scope>test</scope>
</dependency>
```

jTDS:

```yaml
spring:
  datasource:
    url: jdbc:jtds:sqlserver://<host>:<port>/FooBarDb
```

Microsoft driver:

```yaml
spring:
  datasource:
    url: jdbc:sqlserver://<host>:<port>;database=FooBarDb
```

Logical database is automatically created.
Container IP address is resolved based on running host, meaning on local machine `<host>` will resolve to `localhost`
while inside Docker placeholder will resolve to `containerIp`.
When `<port>` placeholder is used, container will be mapped on random port and automatically substituted.

<a id="MSSQLLocalDevelopment"></a>
#### Local Development: 

Add the following profile:

```xml
<profiles>
    <profile>
        <id>development</id>
        <dependencies>
            <dependency>
                <groupId>com.infobip</groupId>
                <artifactId>infobip-mssql-testcontainers-spring-boot-starter</artifactId>
                <version>${infobip-mssql-testcontainers-spring-boot-starter.version}</version>
            </dependency>
        </dependencies>
    </profile>
</profiles>
```

Before starting the application locally, activate development profile:

![profile.png](profile.png)

and update your local configuration (e.g. application-development.yaml):

jTDS:

```yaml
spring:
  datasource:
    url: jdbc:jtds:sqlserver://<host>:<port>/FooBarDb_test_${user.name}
```

Microsoft driver:

```yaml
spring:
  datasource:
    url: jdbc:sqlserver://<host>:<port>;database=FooBarDb_test_${user.name}
```

<a id="MSSQLDockerImage"></a>
#### Docker image: 

To change the docker image used simply add the following property (e.g. in yaml):

```yaml
testcontainers.mssql.docker.image: mcr.microsoft.com/mssql/server:2017-CU12
```

<a id="MSSQLInitScript"></a>
#### Initialization script

To add an SQL script with which the container will be initialized with add the following property (e.g. in yaml):

```yaml
testcontainers.mssql.init-script: db/init-script.sql
```

<a id="MySQL"></a>
### MySQL

<a id="MySQLTests"></a>
#### Tests:

Include the dependency:

```xml
<dependency>
	<groupId>com.infobip</groupId>
	<artifactId>infobip-mysql-testcontainers-spring-boot-starter</artifactId>
	<version>${infobip-mysql-testcontainers-spring-boot-starter.version}</version>
	<scope>test</scope>
</dependency>
```

Configuration:

```yaml
spring:
  datasource:
    url: jdbc:mysql://<host>:<port>/FooBarDb
    username: test
    password: test
```

Logical database is automatically created.
Container IP address is resolved based on running host, meaning on local machine `<host>` will resolve to `localhost`
while inside Docker placeholder will resolve to `containerIp`.
When `<port>` placeholder is used, container will be mapped on random port and automatically substituted.

<a id="MySQLLocalDevelopment"></a>
#### Local Development:

Add the following profile:

```xml
<profiles>
    <profile>
        <id>development</id>
        <dependencies>
            <dependency>
                <groupId>com.infobip</groupId>
                <artifactId>infobip-mysql-testcontainers-spring-boot-starter</artifactId>
                <version>${infobip-mysql-testcontainers-spring-boot-starter.version}</version>
                <scope>test</scope>
            </dependency>
        </dependencies>
    </profile>
</profiles>
```

Before starting the application locally, activate development profile:

![profile.png](profile.png)

and update your local configuration (e.g. application-development.yaml):

```yaml
spring:
  datasource:
    url: jdbc:mysql://<host>:<port>/FooBarDb_test_${user.name}
    username: test
    password: test
```

<a id="MySQLDockerImage"></a>
#### Docker image:

To change the docker image used simply add the following property (e.g. in yaml):

```yaml
testcontainers.mysql.docker.image: mysql:5.7.34
```

<a id="MySQLInitScript"></a>
#### Initialization script

To add an SQL script with which the container will be initialized with add the following property (e.g. in yaml):

```yaml
testcontainers.mysql.init-script: db/init-script.sql
```

<a id="PostgreSQL"></a>
### PostgreSQL

<a id="PostgreSQLTests"></a>
#### Tests: 

Include the dependency:

```xml
<dependency>
	<groupId>com.infobip</groupId>
	<artifactId>infobip-postgresql-testcontainers-spring-boot-starter</artifactId>
	<version>${infobip-postgresql-testcontainers-spring-boot-starter.version}</version>
	<scope>test</scope>
</dependency>
```

Configuration:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://<host>:<port>/FooBarDb
    username: test
    password: test
```

Logical database is automatically created.
Container IP address is resolved based on running host, meaning on local machine `<host>` will resolve to `localhost`
while inside Docker placeholder will resolve to `containerIp`.
When `<port>` placeholder is used, container will be mapped on random port and automatically substituted.

<a id="PostgreSQLLocalDevelopment"></a>
#### Local Development: 

Add the following profile:

```xml
<profiles>
    <profile>
        <id>development</id>
        <dependencies>
            <dependency>
                <groupId>com.infobip</groupId>
                <artifactId>infobip-postgresql-testcontainers-spring-boot-starter</artifactId>
                <version>${infobip-postgresql-testcontainers-spring-boot-starter.version}</version>
                <scope>test</scope>
            </dependency>
        </dependencies>
    </profile>
</profiles>
```

Before starting the application locally, activate development profile:

![profile.png](profile.png)

and update your local configuration (e.g. application-development.yaml):

```yaml
spring:
  datasource:
    url: jdbc:postgresql://<host>:<port>/FooBarDb_test_${user.name}
    username: test
    password: test
```

<a id="PostgreSQLDockerImage"></a>
#### Docker image: 

To change the docker image used simply add the following property (e.g. in yaml):

```yaml
testcontainers.postgresql.docker.image: postgres:15
```

<a id="PostgreSQLInitScript"></a>
#### Initialization script

To add an SQL script with which the container will be initialized with add the following property (e.g. in yaml):

```yaml
testcontainers.postgresql.init-script: db/init-script.sql
```

<a id="Redis"></a>
### Redis

<a id="RedisTests"></a>
#### Tests: 

Include the dependency:

```xml
<dependency>
	<groupId>com.infobip</groupId>
	<artifactId>infobip-redis-testcontainers-spring-boot-starter</artifactId>
	<version>${infobip-redis-testcontainers-spring-boot-starter.version}</version>
	<scope>test</scope>
</dependency>
```

Configuration:

```yaml
spring:
  redis:
    url: redis://<host>:<port>
```

Container IP address is resolved based on running host, meaning on local machine `<host>` will resolve to `localhost`
while inside Docker placeholder will resolve to `containerIp`.
When `<port>` placeholder is used, container will be mapped on random port and automatically substituted.

<a id="RedisDevelopment"></a>
#### Local Development: 

Add the following profile:

```xml
<profiles>
    <profile>
        <id>development</id>
        <dependencies>
            <dependency>
                <groupId>com.infobip</groupId>
                <artifactId>infobip-redis-testcontainers-spring-boot-starter</artifactId>
                <version>${infobip-redis-testcontainers-spring-boot-starter.version}</version>
                <scope>test</scope>
            </dependency>
        </dependencies>
    </profile>
</profiles>
```

Before starting the application locally, activate development profile:

![profile.png](profile.png)

and update your local configuration (e.g. application-development.yaml):

```yaml
spring:
  redis:
    url: redis://<host>:<port>
```

<a id="RedisDockerImage"></a>
#### Docker image: 

To change the docker image used simply add the following property (e.g. in yaml):

```yaml
testcontainers.redis.docker.image: redis:5.0.7-alpine
```

<a id="Kafka"></a>
### Kafka

<a id="KafkaTopic"></a>
#### Automatic topic creation

Format: `<topicName>:<numPartitions>:<replicationFactor>`

```yaml
testcontainers.kafka.topics: test-topic:1:1, test-topic-2:1:1
```

<a id="KafkaTests"></a>
#### Tests: 

Include the dependency:

```xml
<dependency>
	<groupId>com.infobip</groupId>
	<artifactId>infobip-kafka-testcontainers-spring-boot-starter</artifactId>
	<version>${infobip-kafka-testcontainers-spring-boot-starter.version}</version>
	<scope>test</scope>
</dependency>
```

Configuration:

```yaml
spring:
  kafka:
    bootstrap-servers: <host>:<port>
```

Logical database is automatically created.
Container IP address is resolved based on running host, meaning on local machine `<host>` will resolve to `localhost`
while inside Docker placeholder will resolve to `containerIp`.
When `<port>` placeholder is used, container will be mapped on random port and automatically substituted.

<a id="KafkaLocalDevelopment"></a>
#### Local Development: 

Add the following profile:

```xml
<profiles>
    <profile>
        <id>development</id>
        <dependencies>
            <dependency>
                <groupId>com.infobip</groupId>
                <artifactId>infobip-kafka-testcontainers-spring-boot-starter</artifactId>
                <version>${infobip-kafka-testcontainers-spring-boot-starter.version}</version>
                <scope>test</scope>
            </dependency>
        </dependencies>
    </profile>
</profiles>
```

Before starting the application locally, activate development profile:

![profile.png](profile.png)

and update your local configuration (e.g. application-development.yaml):

```yaml
spring:
  kafka:
    bootstrap-servers: <host>:<port>
```

<a id="KafkaDockerImage"></a>
#### Docker image: 

To change the docker image version used simply add the following property (e.g. in yaml):

```yaml
testcontainers.kafka.docker.image.version: 2.1.0
```

<a id="RabbitMq"></a>
### RabbitMq

<a id="RabbitMqTests"></a>
#### Tests: 

Include the dependency:

```xml
<dependency>
	<groupId>com.infobip</groupId>
	<artifactId>infobip-rabbitmq-testcontainers-spring-boot-starter</artifactId>
	<version>${infobip-rabbitmq-testcontainers-spring-boot-starter.version}</version>
	<scope>test</scope>
</dependency>
```

#### Test Configuration:

To configure RabbitMq in tests you need to create it's configuration for example:

```java
@Configuration
@Profile({"test", "development"})
public class RabbitConfigTestEnv {

    @Bean
    public Queue testQueue() {
        return QueueBuilder.durable("test.queue").build();
    }

    @Bean
    public TopicExchange testExchange() {
        return new TopicExchange("test.exchange");
    }

    @Bean
    public Binding bindToTestExchange() {
        return bind(testQueue()).to(testExchange()).with("test.key.#");
    }
}
```

This class should live inside test files and there you can create queues, exchanges and key routing bindings or receivers.
In this example method:
* `testQueue` creates queue with name `test.queue`
* `testExchange` creates exchange with name `test.exchange`
* `bindToTestExchange` tells Rabbit to send any message sent to test exchange, with key of value `test.key.#` to our test queue

<b>Important</b>: Queues are declared in Rabbit only after some message is sent to the queue.
If you log into `docker` and try to find queue, it won't be listed if no message was sent to it.

<a id="RabbitMqLocalDevelopment"></a>
#### Local Development: 

Add the following profile:

```xml
<profiles>
    <profile>
        <id>development</id>
        <dependencies>
            <dependency>
                <groupId>com.infobip</groupId>
                <artifactId>infobip-rabbitmq-testcontainers-spring-boot-starter</artifactId>
                <version>${infobip-rabbitmq-testcontainers-spring-boot-starter.version}</version>
                <scope>test</scope>
            </dependency>
        </dependencies>
    </profile>
</profiles>
```

Before starting the application locally, activate development profile:

![profile.png](profile.png)

<a id="RabbitMqDockerImage"></a>
#### Docker image: 

To change the docker image used simply add the following property (e.g. in yaml):

```yaml
testcontainers.rabbit.docker.image: rabbitmq:3.6.14-alpine
```
<a id="ClickHouse"></a>
### ClickHouse

<a id="ClickHouseTests"></a>
#### Tests

Include the dependency:

```xml
<dependency>
	<groupId>com.infobip</groupId>
	<artifactId>infobip-clickhouse-testcontainers-spring-boot-starter</artifactId>
	<version>${infobip-clickhouse-testcontainers-spring-boot-starter.version}</version>
	<scope>test</scope>
</dependency>
```

<a id="ClickHouseLocalDevelopment"></a>
#### Local development

Add the following profile:

```xml
<profiles>
    <profile>
        <id>development</id>
        <dependencies>
            <dependency>
                <groupId>com.infobip</groupId>
                <artifactId>infobip-clickhouse-testcontainers-spring-boot-starter</artifactId>
                <version>${infobip-clickhouse-testcontainers-spring-boot-starter.version}</version>
                <scope>test</scope>
            </dependency>
        </dependencies>
    </profile>
</profiles>
```

Before starting the application locally, activate development profile:

![profile.png](profile.png)

and update your local configuration (e.g. application-development.yaml):

```yaml
spring:
  datasource:
    jdbc-url: <host>:<port>
```
In case your datasource configuration is different from default one you can provide custom configuration property path
```yaml
testcontainers.clickhouse.custom-path: "spring.datasource.clickhouse"
```
in this case your configuration would look like this

```yaml
spring:
  datasource:
      clickhouse:
        jdbc-url: <host>:<port>
```

<a id="ClickHouseDockerImage"></a>
#### Docker image version

To change the docker image used simply add the following property (e.g. in yaml):

```yaml
testcontainers.clickhouse.docker.image: clickhouse:latest
```

<a id="ClickHouseInitScript"></a>
#### Initialization script

To add an SQL script with which the container will be initialized with add the following property (e.g. in yaml):

```yaml
testcontainers.clickhouse.init-script: db/init-script.sql
```

## <a id="Contributing"></a> Contributing

If you have an idea for a new feature or want to report a bug please use the issue tracker.

Pull requests are welcome!

## <a id="License"></a> License

This library is licensed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

