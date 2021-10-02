Feature: Posts Endpoint

  Scenario: Get all posts
    When I make a GET request to the Posts endpoint
    Then the response has a status code of 200
    And the response body follows the "GetAllPosts" JSON schema
    And the results array contains 100 elements
    And the response body matches the "GetAllPosts" expected response

