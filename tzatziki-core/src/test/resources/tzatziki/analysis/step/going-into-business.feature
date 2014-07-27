Feature: Going into business

#
#  **In order to** goes into Business
#
#  **As a** shopkeeper
#
#  **I want to** ensure The coffee machine is not free anymore!
#
# <p style="text-align: right;">
#   <img src="/coins.jpeg" width="66px"/>
# </p>
#

  @Payment
  Scenario: A tea with just enough money

  The drink maker should make the drinks only if the correct amount of money is given

    Given I've inserted 0.40€ in the machine
    When I order a "Tea" with 1 sugar
    Then the instruction generated should be "T:1:0"

  @Payment
  Scenario: A tea with not enough money

  If not enough money is provided, we want to send a message to the drink maker.
  The message should contains at least the amount of money missing.

    Given I've inserted 0.30€ in the machine
    When I order a "Tea" with 1 sugar
    Then the instruction generated should be "M:Not enough money 0.10 missing"

  @Payment
  Scenario: A coffee with more than required money

  If too much money is given, the drink maker will still make the drink according
  to the instructions. The machine will handle the return of the correct change.

    Given I've inserted 2€ in the machine
    When I order a "Coffee" with 0 sugar
    Then the instruction generated should be "C::"

  @Payment
  Scenario Outline: Check missing money

    Given I've inserted <money>€ in the machine
    When I order a "<drink>" with <n> sugar
    Then the instruction generated should be "<instruction>"

  Examples:
    | money | drink     | n | instruction                     |
    | 0.25  | Coffee    | 0 | M:Not enough money 0.25 missing |
    | 0.55  | Chocolate | 0 | M:Not enough money 0.05 missing |
    | 0.05  | Tea       | 1 | M:Not enough money 0.35 missing |

