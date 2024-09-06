# Getting Started
### The Following Were Used to Develop this Application
* Spring Boot 3.3.3
* Java JDK 17 (Amazon Corretto implementation, to be precise)
* JVM 17.0.10 (Amazon Corretto)
* Gradle 8.10

### Gradle Commands
The following Gradle commands can be used to run the application or tests.
* _Application:_ `gradle bootRun --console=plain`
* _Test:_ `gradle :test`

### A Note About Spring Data JDBC
This application was built using Spring Data JDBC (see below ref document). This should **not** to be confused with 
Spring JDBC (IE: jdbcTemplate, etc). Spring Data JDBC is an ORM, like Hibernate, but is implemented to be much simpler to
develop with that other JPA implementations.

Here's how the table structure is defined.

![Database table structure](https://github.com/joshspillers/js0924/blob/master/doc/tool_rental_database_design.png?raw=true)

### Reference Documentation
For further reference, please consider the following sections:

* [Spring Data JDBC](https://docs.spring.io/spring-boot/docs/3.3.3/reference/htmlsingle/index.html#data.sql.jdbc)
* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/3.3.3/gradle-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.3.3/gradle-plugin/packaging-oci-image.html)

### Guides
The following guides illustrate how to use some features concretely:

* [Using Spring Data JDBC](https://github.com/spring-projects/spring-data-examples/tree/master/jdbc/basics)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

