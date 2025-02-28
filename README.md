# Getting Started
### About This Application
This application was built as a very simple Spring Boot application to show-case basic Spring Boot proficiency. It is a simple tool rental application with the following specifications:
  * The application is a point-of-sale tool for a store, like Home Depot, that rents big tools.
  * Customers rent a tool for a specified number of days.
  * When a customer checks out a tool, a Rental Agreement is produced.
  * The store charges a daily rental fee, whose amount is different for each tool type.
  * Some tools are free of charge on weekends or holidays.
  * Clerks may give customers a discount that is applied to the total daily charges to reduce the final
charge.
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
develop with than other JPA implementations.

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

