Feature: ToDos Endpoint

  Scenario: Get all todos
    When I make a GET request to the ToDos endpoint
    Then the response has a status code of 200
    And the response body follows the "MultipleToDos" JSON schema
    And the results array contains 200 elements
    And the response body matches the "AllToDos" expected response