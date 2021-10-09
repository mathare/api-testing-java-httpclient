Feature: Albums Endpoint

  Scenario: Get all albums
    When I make a GET request to the Albums endpoint
    Then the response has a status code of 200
    And the response body follows the "MultipleAlbums" JSON schema
    And the results array contains 100 elements
    And the response body matches the "AllAlbums" expected response