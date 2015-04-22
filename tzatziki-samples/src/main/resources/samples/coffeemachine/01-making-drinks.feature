Feature: Making Drinks

#
#  **In order** to send commands to the drink maker
#  **As a** developer
#  **I want to** implement the logic that translates orders from customers of the coffee machine to the drink maker
#
# {width:66px, height:100px}
# ![Coffee cup](${imageDir}/coffee-cup.jpeg)
#
# The code will use the drink maker protocol (see below) to send commands to the drink maker.
#
# The coffee machine can serves 3 type of drinks:
#
#  * tea,
#  * coffee,
#  * chocolate.
#
#
#```ditaa
#
#  /---------+     +------------+
#  |  Order  |---->|  Protocol  |
#  +---------/     +------------+
#
#```

  @takeOrder @wip @tea @sugar @protocol
  Scenario: A tea with 1 sugar and a stick

    When I order a "Tea" with 1 sugar
    Then the instruction generated should be "T:1:0"

  @takeOrder @chocolate @noSugar @protocol
  Scenario: A chocolate with no sugar - and therefore no stick

    When I order a "Chocolate" with 0 sugar
    Then the instruction generated should be "H::"

  @takeOrder @coffee @sugar @protocol
  Scenario: A tea with 1 sugar and a stick

    When I order a "Coffee" with 2 sugar
    Then the instruction generated should be "C:2:0"

  @message @protocol
  Scenario Outline: any message received is forwarded for the customer to see
    When the message "<message>" is sent
    Then the instruction generated should be "<expected>"

  Examples:
    | message          | expected           |
    | Hello            | M:Hello            |
    | Not enough money | M:Not enough money |