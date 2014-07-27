Feature: Running Out

#  **In order to** never run out beverage
#
# **As a** shop keeper
#
# **I want to** be informed that there is a shortage and to send a email notification
#  to the company so that they can come and refill the machine.
#
#  The users of the coffee machine are complaining that there is often shortages
#  of water and/or milk. It takes weeks before the machine is refilled.
#  Your product owner wants to you to take advantage of the machine capabilities
#  to inform the user that there is a shortage

  @Notification
  Scenario: Last Coffee
    Given no more "Coffee" remaining in the machine
    When I order a "Coffee" with 1 sugar
    Then a mail should have been sent indicating "Coffee" is running out

  @Notification
  Scenario Outline: Last beverage
    Given no more "<drink>" remaining in the machine
    When I order a "<drink>"
    Then a mail should have been sent indicating "<drink>" is running out

  Examples:
    | drink        |
    | Orange juice |
    | Tea          |
    | Chocolate    |

  @manual
  @Notification
  Scenario: Manually send an email
    Given an empty machine
    When I click on the "Send Test Email" button
    Then a test mail should have been sent