Feature: Photos Endpoint

  Scenario: Get all photos
    When I make a GET request to the Photos endpoint
    Then the response has a status code of 200
    And the response body follows the "MultiplePhotos" JSON schema
    And the results array contains 5000 elements
    And the response body matches the "AllPhotos" expected response