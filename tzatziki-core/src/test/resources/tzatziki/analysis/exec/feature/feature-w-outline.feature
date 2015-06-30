Feature: A feature with an Outline

  @wip
  Scenario Outline: Template scenario

    Given a simple step
    When I activate the <behavior>
    Then I should have a log that indicate the activation of the <behavior>

  @activated
  Examples: Activated Features
    | behavior |
    | print    |
    | login    |

  @deactivated
  Examples: Deactivated Features
    | behavior |
    | delete   |
    | clone    |