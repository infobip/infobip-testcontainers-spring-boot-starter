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
