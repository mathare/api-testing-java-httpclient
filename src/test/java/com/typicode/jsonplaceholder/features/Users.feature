Feature: Users Endpoint

  Scenario: Get all users
    When I make a GET request to the Users endpoint
    Then the response has a status code of 200
    And the response body follows the "MultipleUsers" JSON schema
    And the results array contains 10 elements
    And the response body matches the "AllUsers" expected response