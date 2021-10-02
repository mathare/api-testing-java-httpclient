Feature: Posts Endpoint

  Scenario: Get all posts
    When I make a GET request to the Posts endpoint
    Then the response has a status code of 200
    And the response body follows the "GetAllPosts" JSON schema
    And the results array contains 100 elements
    And the response body matches the "GetAllPosts" expected response

  Scenario: Verify single post against expected response in file
    When I make a GET request to the Posts endpoint with a path parameter of 1
    Then the response has a status code of 200
    And the response body follows the "GetSinglePost" JSON schema
    And the response body matches the 1st post in the "GetAllPosts" expected response

  Scenario: Verify single post using datatable
    When I make a GET request to the Posts endpoint with a path parameter of 1
    Then the response has a status code of 200
    And the response body follows the "GetSinglePost" JSON schema
    And the response body matches the following
      | key    | value                                                                                                                                                             |
      | id     | 1                                                                                                                                                                 |
      | title  | sunt aut facere repellat provident occaecati excepturi optio reprehenderit                                                                                        |
      | body   | quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto |
      | userId | 1                                                                                                                                                                 |

  Scenario: Verify single post field by field
    When I make a GET request to the Posts endpoint with a path parameter of 1
    Then the response has a status code of 200
    And the response body follows the "GetSinglePost" JSON schema
    And the "id" field in the response body has a value of 1
    And the "title" field in the response body has a value of "sunt aut facere repellat provident occaecati excepturi optio reprehenderit"
    And the "body" field in the response body has a value of
    """
    quia et suscipit
    suscipit recusandae consequuntur expedita et cum
    reprehenderit molestiae ut ut quas totam
    nostrum rerum est autem sunt rem eveniet architecto
    """
    And the "userId" field in the response body has a value of 1

  Scenario Outline: Get post with invalid ID - post #<ID>
    When I make a GET request to the Posts endpoint with a path parameter of <ID>
    Then the response has a status code of 404
    And the response body is an empty JSON object
    Examples:
      | ID  |
      | 0   |
      | 101 |
      | -1  |

  Scenario: Delete request to all posts endpoint raises error
    When I make a DELETE request to the Posts endpoint
    Then the response has a status code of 404
    And the response body is an empty JSON object

  Scenario: Put request to all posts endpoint raises error
    When I make a PUT request to the Posts endpoint
    Then the response has a status code of 404
    And the response body is an empty JSON object

  Scenario: Post request to all posts endpoint creates new post
    When I make a POST request to the Posts endpoint
    Then the response has a status code of 201
    And the response body matches the following
      | key | value |
      | id  | 101   |
  
