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
    When I make a PUT request with an empty body to the Posts endpoint
    Then the response has a status code of 404
    And the response body is an empty JSON object

  Scenario: Post request to all posts endpoint creates new post
    When I make a POST request with an empty body to the Posts endpoint
    Then the response has a status code of 201
    And the response body matches the following
      | key | value |
      | id  | 101   |

  Scenario: Delete post
  # If this was a real API supported by a working data source I would make a GET request to the Posts endpoint after
  # deleting the post to ensure that the total number of posts has decreased and that post 1 had been deleted
    When I make a DELETE request to the Posts endpoint with a path parameter of 1
    Then the response has a status code of 200
    And the response body is an empty JSON object

  Scenario: Update post with empty request body
  # This API isn't backed by a working data source so there is no follow up GET request to confirm that the
  # post has been properly updated in the back end
    When I make a PUT request with an empty body to the Posts endpoint with a path parameter of 1
    Then the response has a status code of 200
    And the response body matches the following
      | key | value |
      | id  | 1     |

  Scenario: Update post with partial request body
    When I make a PUT request with the following body to the Posts endpoint with a path parameter of 1
      | key   | value                  |
      | title | Updated Post Title     |
      | body  | Updated post body text |
    Then the response has a status code of 200
    And the response body matches the following
      | key   | value                  |
      | id    | 1                      |
      | title | Updated Post Title     |
      | body  | Updated post body text |

  Scenario: Update post with full request body
    When I make a PUT request with the following body to the Posts endpoint with a path parameter of 1
      | key    | value                  |
      | title  | Updated Post Title     |
      | body   | Updated post body text |
      | userId | 5                      |
    Then the response has a status code of 200
    And the response body matches the following
      | key    | value                  |
      | id     | 1                      |
      | title  | Updated Post Title     |
      | body   | Updated post body text |
      | userId | 5                      |

  Scenario: Update post with invalid fields in request body
  # As this API isn't linked to a working data source there is no validation of the field names passed in via the
  # request body. If this were a fully working API I would expect this to raise an error
    When I make a PUT request with the following body to the Posts endpoint with a path parameter of 1
      | key   | value     |
      | key_1 | New value |
      | key_2 | New value |
    Then the response has a status code of 200
    And the response body matches the following
      | key   | value     |
      | id    | 1         |
      | key_1 | New value |
      | key_2 | New value |

  Scenario Outline: Delete post with invalid ID - post #<ID>
  # As this is a fake API without an underlying data source that updates based on the API requests, a delete request
  # returns a success response regardless of the post ID passed in via the path parameters. This should raise an error
  # for a real API
    When I make a DELETE request to the Posts endpoint with a path parameter of <ID>
    Then the response has a status code of 200
    And the response body is an empty JSON object
    Examples:
      | ID  |
      | 0   |
      | 101 |
      | -1  |

