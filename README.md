[![Continuous Integration Status](https://github.com/mathare/api-testing-java-httpclient/actions/workflows/ci.yml/badge.svg)](https://github.com/mathare/api-testing-java-httpclient/actions)

# API Testing with Java

## Overview
This project provides an example for testing a RESTful API using core Java functionality and JUnit4 combined with Cucumber BDD feature files as an alternative to automated API testing with Postman. As such, it can be used to kickstart testing of other APIs with minimal changes to the project.

NB This project is not a complete implementation of a test suite for the target API but shows enough to act as a good template for other projects and shows examples of common query types. It is an example of how to structure an API test suite in Java but only a subset of the possible tests have been written.

## Why Use Java HTTP Client?
Java 11 adds a new HTTP Client package for sending requests and managing the responses in a simple manner. It is part of the Java core so doesn't require additional packages to be installed, unlike API packages such as RestAssured that need to be managed via the `pom.xml`. 

The first step to using this functionality is to create a new HttpClient using the builder provided in the package. Once we have an HttpClient we can use that to send our API requests as HttpRequests. Our HttpRequests are created using the supplied builder which allows us to specify the request type (GET, DELETE, POST or PUT), the URI, any request headers and also the request body (if any). The response can be assigned to an HttpResponse variable making it easy to access data such as the response status code, headers and body.

By using the Java HTTP Client this project illustrates how a powerful API testing solution can be built using only core Java functionality.

## API Under Test
The REST API being tested by this project is the [JSON Placeholder API](https://jsonplaceholder.typicode.com/), a simple third-party API that has endpoints suitable for demonstrating many key principles of automated API testing. It is often put forward as a suitable candiate for learning automated API testing, making it an excellent choice for this project, which itself is intended to help anyone looking to learn how to implement API testing using Cucumber and Java.

The JSON Placeholder API has six main endpoints:
* [/posts](https://jsonplaceholder.typicode.com/posts) - 100 posts
* [/comments](https://jsonplaceholder.typicode.com/comments) - 500 comments
* [/albums](https://jsonplaceholder.typicode.com/albums) - 100 albums
* [/photos](https://jsonplaceholder.typicode.com/photos) - 5000 photos
* [/todos](https://jsonplaceholder.typicode.com/todos) - 200 todos
* [/users](https://jsonplaceholder.typicode.com/users) - 10 users

Path parameters can be used to return a specific data object. For example,  a single post can be returned by specifying an ID path parameter e.g. [/posts/1](https://jsonplaceholder.typicode.com/posts/1). Alternatively, query parameters can be specified in the URI to filter the data e.g. [/posts?userId=1](https://jsonplaceholder.typicode.com/posts?userId=1) will return all posts created by the user with a userId of 1. Some of the API resources are related to one another e.g., posts have comments, albums have photos, users make posts etc. so URIs can have nested path parameters. For example, to get the comments associated with a single post one can make a request to [/posts/1/comments](https://jsonplaceholder.typicode.com/posts/1/comments).  

The main drawback of using this API is endpoints return static responses - there is no underlying database or backend storage. This means that when creating new data for a given endpoint (e.g. via a POST request to the [/posts](https://jsonplaceholder.typicode.com/posts) endpoint) a valid API response will be returned but no new data is actually created. When testing a real-world API one may send a POST request to create new data then send a GET request to ensure the new data has been created correctly but that won't work with this API. However, that doesn't significantly impact our testing and where there are knock-on effects I have tried to highlight these in the Cucumber feature files. 

On the plus side, the JSON Placeholder API is free to use and has no rate limits, unlike some other APIs that are put forward as suitable automated testing candidates.

Note, the API has no authorisation/authentication applied so that side of REST API testing is not covered in this project.

## Test Framework
As stated above, this project contains a Java test framework suitable for REST APIs and utilises Cucumber BDD. The use of Cucumber means the tests themselves are clean and clear, written in plain English, so they can be understood by anyone working with the project, including non-technical roles. Although this project is just an example of how to set up API testing in Java, in a real-life project the use of BDD is essential for collaboration between QAs, developers, and business roles (e.g. Product Owners, Business Analysts etc). Quality is everyone’s responsibility, which means the tests themselves need to be easily understood by all stakeholders.

### Tech Stack
As this is a Java project, build and dependency management is handled by Maven, so there is a `pom.xml` file defining the versions of the dependencies:
* Java v11
* JUnit v4.13.2
* Cucumber v6.11.0
* JSON v20210307
* JSON Schema for org.json API v1.5.1

The code is written in Java and built using v11 of the JDK. There are more up-to-date JDK versions available  - Oracle is up to Java 17 at the time of writing. However, I used Amazon Coretto 11 (the latest LTS release of this popular OpenJDK build) as it is the distribution I am most used to.

I have chosen to use the latest JUnit 4 release rather than JUnit 5 (also known as JUnit Jupiter) for easier integration with Cucumber (which is built primarily around JUnit 4).

The Cucumber version is the latest version at the time of writing (released May 2021).

The JSON encoders/decoders for Java package is also the latest version at the time of writing (released March 2021), as is the JSON Schema package I have opted to use in order to easily verify the schema of the various responses is as expected.

### Project Structure
The project uses a standard structure and naming convention, incorporating the URL of the website under test, i.e. the test code is all stored under `src/test/java/com/typicode/jsonplaceholder`. Below that we have:
* `features`  - this folder contains the Cucumber `.feature` files, one per API endpoint. Separating out the tests for each endpoint into separate feature files makes it easier to extend the framework in the future. Each feature file is named after the endpoint it tests, e.g. `Albums.feature` contains the tests for the Albums endpoint. Most of the endpoints have only a single test but `Posts.feature` has been built up with an extensive set of tests to illustrate what can be tested.
* `helpers` - this package contains a single class: `RequestHelpers.java`. This class contains all the methods that send the HTTP requests, separating them out from the step definitions for clarity and easy re-use. You could argue this package would be better named `utils` or similar but that is all down to personal preference
* `steps` - a collection of classes containing the implementation of the steps from the Cucumber feature files. The main class is `CommonSteps.java` containing the implementation of the steps that would be used by more than one feature file, avoiding duplication of code across individual steps classes. There is also a `PostsSteps.java` class containing a small number of steps that are specific to the tests in the `Posts.feature` file. In reality, as the API test suite is built out these steps are likely to be genericised and moved to the `CommonSteps.java` class so they can easily be shared between features but I have chosen to show the use of common and specific test steps classes in this project.
* `TestRunner.java` - the main JUnit test runner class, decorated with the annotation required to run Cucumber tests. The class itself is empty but the `CucumberOptions` annotation defines the location of the features and associated steps.

There is also a test resources folder `src/test/resources` containing a number of files against which responses can be verified. The folder is split as follows:
* `expectedResponses` - the expected reponse bodies of a number of requests are stored here as JSON files
* `schemas` -  JSON format schemas for each of the endpoints

### Running Tests
The tests are easy to run as they are bound to the Maven `test` goal so running the tests is as simple as executing `mvn test` within the directory in which the repo has been cloned. Alternatively, the empty `TestRunner` class can be executed using a JUnit runner within an IDE.

#### Test Reports
A report is generated for each test run, using the Cucumber `pretty` plugin to produce an HTML report called `cucumber-report.html` in the `target` folder. This is a simple report showing a summary of the test run (number of tests run, number passed/failed/skipped, duration, environment etc) above a breakdown of each individual feature file, showing the status of each scenario and the individual steps within that scenario, including a stack trace for failing steps. 

### CI Pipeline
This repo contains a CI pipeline implemented using [GitHub Actions](https://github.com/features/actions). Any push to the `main` branch or any pull request on the `main` branch will trigger the pipeline, which runs in a Linux VM on the cloud within GitHub. The pipeline consists of a single `run-tests` job which checks out the repo then runs the test suite via `mvn test`. Once the tests have finished, whether they pass or fail, a test report is uploaded as a GitHub artifact. At the end of the steps the environment tears itself down and produces a [status report](https://github.com/mathare/api-testing-java-httpclient/actions). Each status report shows details of the test run, including logs for each step and a download link for the test report artifact.

In addition to the automated triggers above, the CI pipeline has a manual trigger actionable by clicking "Run workflow" on the [Continuous Integration](https://github.com/mathare/api-testing-java-httpclient/actions/workflows/ci.yml) page. This allows the user to select the branch to run the pipeline on, so tests can be run on a branch without the need for a pull request. This option is only visible if you are the repo owner.