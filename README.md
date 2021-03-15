# Generic micro service with CI

This is my first take on what is the recommended approach to build a micro service, with CI/CD in mind.
Of course the focus here is on CI. This project is not going to share knowledge on how CD should be done.

For those who are doing it for the first time, read on.

I have taken a stab with the latest libraries and SDK in mind.
It extends parent https://github.com/nischit7/java-web-base to derive the following abilities -
1. A single place to manage dependencies
2. A single place with all plugins configured
3. A single place with all code linting tools enabled.

# Objectives that were thought through while building this sample service

## 1. Using JDK 11 (Java 11) and higher

JDK 11 or higher is needed

## 2. Multi module maven project

In a real world, a single app or micro service will tend to have multiple maven modules.
We obviously need techniques to effectively run unit tests, integration tests and get a overall code coverage.
This sample service tries to achieve the same.

## 3. Using HTTP/2 and Spring boot 2.x

I am a big fan of HTTP/2 and want to use it for my future assignments.
Spring 2.x has made it so easy to build HTTP2 server.
In this approach, the spring boot app supports both HTTP/1.1 and HTTP/2 specs.
If the client supports only HTTP/1.1, it works too. Test it on firefox, you will see !!!

## 4. Junit 5.x and Spring boot 2.x

With Spring boot 2.x and introduction of Junit 5, integration of these tools has changed.
I am not specifically trying to answer how to write junit 5 test cases, there are plenty of examples in internet, 
but I am trying to provide a mechanism to integrate with surefire and failsafe plugin.

## 5. Spring boot tests

In this example, all integration tests were written using Spring boot test.
To me if you are using Spring boot, use their testing framework. It works so well !!!
If you are using intellij, a developer can simply right click on a spring boot test case and run in debug mode.
In my journey, the integration with Junit 5, surefire and failsafe plugins were not straight forward.

## 6. Jacoco code coverage

In our CI model, we need to generate code coverage report which will give insight how effectively we have done testing.
If you are using multi module maven project, it is important to get a single coverage report.

## 7. Rest API exception handling

With Spring boot, life is made easy with good exception handling. In this project, you can see the usage of @RestControllerAdvice to handle all application and non-application level exceptions. 

# Some key observations while working on this assignment

1. If you start the spring boot application, it will run on HTTP/2.
   However when running integration test cases,  I used HTTP/1.1.
   When I built this project, the rest API testing framework "Rest Assured" was not ready for HTTP/2 specs.
   Hopefully that will be available.
   I am particularly biased to Rest Assured as it gives "BDD" way of defining test cases. 
   However one can use spring rest template along with "OK http3" to achieve the objective.
   I will live it to you as what best fits your requirement.

# Using maven profiles

In this example, I am running both unit and integration tests in one go.
That is, if you run "mvn clean install", it will do all of them.
In my real word project, I moved the execution of integration tests as seperate maven profile.
I guess thats an option you can consider.

# How to build and where the coverage reports located?
To run all unit and integration tests -

    mvn clean install

To skip integration tests, run -

    mvn clean install -DskipITs

To skip all tests, run -

    mvn clean install -DskipTests

    Jacoco Coverage
        Unit tests - Each module has unit test HTML report at target/site/jacoco/jacoco-ut

        Integration tests - Each module has unit test HTML report at api-server/target/site/jacoco/jacoco-it

# Running the server
To run the server -

    mvn spring-boot:run -pl :api-server