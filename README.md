NixMash Microservices
============

- **NixMash Jangles** - small backend for cloud configuration and shared components [(GitHub)](https://github.com/mintster/jangles)
- **Bootique** - a lightweight Java Framework [http://bootique.io/](http://bootique.io/)
- **Jetty** - embedded web server
- **Jersey** - *REST* services for JAX-RS
- **Jackson** - JSON/POJO binding
- **Apache Shiro** - user authentication and authorization, including using a BearerAuthenticationToken in REST (v0.2.0)
- **Google Guice** - injection
- **Mustache** - the Spullara Java Mustache Implementation for "logic free" templating [(GitHub)](https://github.com/spullara/mustache.java)
- **Bootstrap** - responsive web theming

## Contents

- [NixMash Posts by Branch](#nixmash-posts-by-branch)
- [Installation](#installation)
- [Running the Application](#running-the-application)
- [User Service](#user-service)
- [Web Client](#web-client)

## NixMash Posts by Branch

### microservices-v0.2.0

- [NixMash : Bearer Token Design in Bootique Shiro for REST Authentication](https://nixmash.com/post/bearer-token-design-in-bootique-shiro-for-rest-authentication)

 ### microservices-v0.1.0
 
 - [Multiple Bootique Module Shaded Jars with Maven ](http://nixmash.com/post/multiple-bootique-module-shaded-jars-with-maven)
 - [Testing MVC Controller Methods with REST Endpoints in JUnit ](http://nixmash.com/post/testing-mvc-controller-methods-with-rest-endpoints-in-junit)
 - [Localized Resource Properties with Java Mustache](http://nixmash.com/post/localized-resource-properties-with-java-mustache)
 - [Adding Static Paths in Bootique](http://nixmash.com/post/adding-static-paths-in-bootique)
 - [Roll Your Own Mustache Template Resolver in Bootique MVC ](http://nixmash.com/post/roll-your-own-mustache-template-resolver-in-bootique-mvc)
 - [Configuring Jersey Declarative Linking in Bootique ](http://nixmash.com/post/configuring-jersey-declarative-linking-in-bootique)
 - [Custom REST Links with Jersey Conditional Link Injection](http://nixmash.com/post/custom-rest-links-with-jersey-conditional-link-injection)
 - [Retrieving REST Data Object Lists with Jersey and Jackson ](http://nixmash.com/post/retrieving-rest-data-object-lists-with-jersey-and-jackson)
 - [Microservice Applications with Bootique](http://nixmash.com/post/microservice-applications-with-bootique)
 - [REST Notes: JAX-RS, Jersey and Jackson](http://nixmash.com/post/rest-notes-jax-rs-jersey-and-jackson)
 
## Installation

The [Jangles](https://github.com/mintster/jangles) Module sets the application configuration in External Property and Config files. 

1. Create a MySQL database. 
2. MySQL Setup scripts are located in **jangles:/install/sql.** Run **schema.sql** and **data.sql.**
3. Copy external files in **/install/external** to a subdirectory of your **/home/user** directory. Enter the path to those files in **jangles:/resources/jangles.properties** and update the MySQL Connection settings in **connections.xml**.

## Running the Application

**NixMash Microservices** currently consists of a **User Microservice** and a **Web Client.** Use Maven or your IDE to run the `Userservice` module followed by the`Web` REST Client. 

If running the application with Maven, the following will produce JARs for `userservice` and `webclient`
 
 ```bash
{PROJECT_ROOT}/$ mvn clean install
```

Then run the resulting JARs in two terminal windows.

```bash
{PROJECT_ROOT}/$ java -jar userservice/target/userservice.jar
{PROJECT_ROOT}/$ java -jar web/target/webclient.jar
```

### User Service 

`Userservice` displays a list of users from the MySQL database in JSON. The URL for the User Service is `http://localhost:8000.` The initial JSON output from the REST User Service looks like this.

```json
{
    "applicationId": "userservice",
    "serviceName": "User Microservice",
    "users": {
    "params": {
    "rel": "users"
    },
    "href": "http://localhost:8000/users"
    }
}
```
The JSON output at `http://localhost:8000/users` would look like this.

```json
[
    {
        "userId": 1,
        "applicationId": "Jim",
        "serviceName": "Jim Johnson",
        "lastUpdated": "06-12-2017 09:00:21",
        "isActive": true,
        "link": "http://localhost:8000/users/1"
    },
    {
        "userId": 2,
        "applicationId": "Bill",
        "serviceName": "Bill Blaster",
        "lastUpdated": "06-12-2017 09:00:21",
        "isActive": true,
        "link": "http://localhost:8000/users/2"
    }
]
```

### Web Client

The `Web` client module URL is `http://localhost:9001.`  This is the page with url `http://localhost:9001/users` displaying users from `Users Service`.

![](http://nixmash.com/x/pics/github/micro0710a.png)

**Last Updated:** *4/23/18*

