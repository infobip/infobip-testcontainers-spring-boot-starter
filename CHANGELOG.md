### 4.1.0

* Added the testcontainers.db-name.init-script property to all the database starters
* Added tests to cover the new feature
* Fixed inconsistent formatting & typos in various places
* Uplift clickhouse driver version
* Removed redundant property from PostgreSQL starter

### 4.0.0

* upgraded to Spring Boot 3.0.0 and testcontainers 1.17.6

### 3.5.1
* renamed infobip-rabbitmq-testcontainers-spring-boot module to infobip-rabbitmq-testcontainers-spring-boot-starter

### 3.5.0
* upgraded to spring-boot-dependencies 2.7.0 and testcontainers 1.17.1
* fixed DatabaseCreator to support mssql-jdbc with encrypted flag set to true (which is now the default behavior in upgraded version)

### 3.4.0
* upgraded to spring-boot-dependencies 2.6.2 and testcontainers 1.16.2

### 3.3.0
General:
* Added support for starting containers on a predefined port

### 3.2.0

#### ClickHouse:
* Added support for ClickHouse
* Custom path for ClickHouse jdbc properties can be provided under `testcontainers.clickhouse.custom-path` property. In case custom path is not provided, properties under `spring.datasource` will be used

### 3.1.0

#### MSSQL:
* username and password are now automatically injected and don't need to be explicitly set in configuration (note that they are not overriden if they do exist)
* `spring.flyway` and `spring.r2dbc` are now supported
