Feature: Comments Endpoint

  Scenario: Get all comments
    When I make a GET request to the Comments endpoint
    Then the response has a status code of 200
    And the response body follows the "MultipleComments" JSON schema
    And the results array contains 500 elements
    And the response body matches the "AllComments" expected response