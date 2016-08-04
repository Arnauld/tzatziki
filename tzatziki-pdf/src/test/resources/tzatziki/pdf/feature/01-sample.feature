Feature: A really basic feature

  Scenario Outline: A template scenario <type>

    Given a element of <type>
    When I shake it
    Then it should be <shaked-or-not>

    Examples: Thinks
      | type     | shaked-or-not |
      | car      | shaked        |
      | building | not shaked    |

    Examples: Animals
      | type | shaked-or-not |
      | ant  | shaked        |
      | bee  | shaked        |


  Scenario Outline: An other template scenario <type>

    Given a element of <type>
    When I shake it
    Then it should be <shaked-or-not>

    Examples: Thinks
      | type     | shaked-or-not |
      | car      | shaked        |
      | building | not shaked    |

    Examples: Animals
      | type | shaked-or-not |
      | ant  | shaked        |
      | bee  | shaked        |