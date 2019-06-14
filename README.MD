# Payment api

This is a simple **test** app written in kotlin to provide payment services.
The payment process itself is not implemented, since the intent with 
this service is to offer a structure to be expanded with possible 
providers for those services.

## Architecture

This code was developed based on some principles of DDD and 
hexagonal architecture, so we have basically 3 main structural divisions: 

- Application -> it is basically a layer that offers a way for external applications and services to interact with this app.
May provide some decoration on the results presented, since some information from domain may not be exposed
- Domain -> is where the business intelligence of this application is concentrated. This domain is exposed trough the Payments
class, that can express in a easy way what actions are available in this domain.
- Infrastructure -> is where infrastructure code is present (like string utilities or logic really specific to 
infrastructure intelligence,like queries for a specific database or ways to interact with the file system). 
 
To avoid http request threads being blocked during internal processes, a deferred result is returned during controller calls.
This is done using an internal function called *defer*.

The internal processes are executed by just two internal threads. It was done this way thinking about using really small
machines on a cloud environment, and this solution already showed efficiency on environments like that (but it is just
a starting point. No load tests wore done on this app, and maybe other approaches can bring better results)

### Stack

- Java 8 or higher
- Kotlin 1.2.71
- Maven 3
- Spring-boot (MVC, Actuator, JDBC)
- Undertow (embedded)
- H2 Database (embedded)

### API Endpoints

We have basically 3 endpoints:

- ```POST``` -> /payments to generate a payment request
- ```GET``` -> /payments/{ticket} to check the status of a payment request
- ```POST``` -> /validation/card as an aux endpoint for card validation

The full documentation can be obtained at ```http://localhost:8080/swagger-ui.html```

## Running this app

Just execute ```mvn spring-boot:run``` -> this app runs by default on port 8080. It will use the database file present on *project_path*/db/database

In case you want to package and distribute this app, just run ```mvn package -Pprod``` to build a jar file, and then use the good and
old ```java -jar builded.jar``` command replacing *builded.jar* with the generated jar file. To build this project for production,
you will need to provide the env variable ```H2_DB_LOCATION``` containing the full path for a valid h2 database (the sql script to
create it is at ```payment-api/src/main/resources/database/sql```). 

### H2 Console

Running the default profile (dev profile), the h2 console is enabled. So it is possible to check the database schema
and data accessing ```http://localhost:8080/h2-console```

## IMPORTANT

As the payment process is mocked, it will return ACCEPTED for even payment amounts and REJECTED for odd payment amounts.