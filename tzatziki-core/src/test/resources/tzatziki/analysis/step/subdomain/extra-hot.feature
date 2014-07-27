Feature: Extra Hot and Orange Juice

#  **In order to** provide more choice and to attract more customer
#
#  **As a** shopkeeper
#
#  **I want to** be able to make orange juice and to deliver extra hot drinks

  @ProtocolOrder
  Scenario: An extra hot tea with 1 sugar

    When I order an extra hot "Tea" with 1 sugar
    Then the instruction generated should be "Th:1:0"

  @ProtocolOrder
  Scenario: An Orange juice

    When I order an "Orange Juice"
    Then the instruction generated should be "O::"

  @ProtocolOrder
  @wip
  Scenario: Extra sugar with Orange Juice is ignored

    When I order an "Orange Juice" with 1 sugar
    Then the instruction generated should be "O::"

  @ProtocolOrder
  Scenario: Extra hot with Orange Juice is ignored

    When I order an extra hot "Orange Juice" with 1 sugar
    Then the instruction generated should be "O::"
