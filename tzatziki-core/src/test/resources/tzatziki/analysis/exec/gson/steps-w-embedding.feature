Feature: Embedding at the step level

  Scenario: Step should be able to emit embedding

    Given a series of 10 values named 'xs'
    When I apply the following formula:
    """
    result = xs.reduce(0, (x, sum) -> sum + x) / xs.size()
    """
    Then the result should be greater or equal to the lowest value of 'xs'
    And the result should be lower or equal to the highest value of 'xs'