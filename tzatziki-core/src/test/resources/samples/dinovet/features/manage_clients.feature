Feature: Manage clients
  In order to keep track of clients
  An employee
  wants to be able to add, edit, and delete clients
  
  Scenario: Add a new client
    Given I am on the new client page
    And I am an employee
    When I fill in "First name" with "Holden"
    And I fill in "Last name" with "Caulfield"
    And I fill in "Address" with "1234 1st Street"
    And I fill in "Apt." with "Apt. 23"
    And I fill in "City" with "New York"
    And I select "New York" from "State"
    And I fill in "Zipcode" with "10001"
    And I select "Home" from "client_phone_numbers_attributes_0_label"
    And I fill in "client_phone_numbers_attributes_0_number" with "1"
    And I fill in "E-mail" with "holden@dinocore.net"
    And I press "Create Client"
    Then I should be on the edit client page for "Holden"
    And I should see "Client added successfully"

  Scenario: View a list of all clients
    Given I am an employee
    And I have added a client named "Holden"
    And I am on the clients page
    Then I should see "Holden"

  Scenario: Edit an existing client
    Given I am an employee
    And I have added a client named "Holden"
    And I am editing the client
    When I fill in "First name" with "Phoebe"
    And I press "Update Client"
    Then I should be on the edit client page for "Phoebe"
    And I should see "Phoebe"
    And I should see "Client updated successfully"

  Scenario: Enter invalid data for an existing client
    Given I am an employee
    And I have added a client named "Holden"
    And I am editing the client
    When I fill in "First name" with ""
    And I press "Update Client"
    Then I should see "Failed to update client"

  Scenario Outline: Invalid client data entered
    Given I am on the new client page
    And I am an employee
    When I fill in "First name" with "<first_name>"
    And I fill in "Last name" with "<last_name>"
    And I fill in "Address" with "<address_1>"
    And I fill in "City" with "<city>"
    And I select "<state>" from "State"
    And I fill in "Zipcode" with "<zipcode>"
    And I fill in "client_phone_numbers_attributes_0_number" with "<phone_number>"
    And I fill in "E-mail" with "<email>"
    And I press "Create Client"
    Then I should be on the clients page
    And I should see "Failed to create client"

  Examples:
    |first_name|last_name|address_1|  city  |  state |zipcode|       email       | phone_number |
    |          |Caulfield|1 Fake St|New York|New York| 10001 |holden@dinocore.net| 555-555-5555 |
    |  Holden  |         |1 Fake St|New York|New York| 10001 |holden@dinocore.net| 555-555-5555 |
    |  Holden  |Caulfield|         |New York|New York| 10001 |holden@dinocore.net| 555-555-5555 |
    |  Holden  |Caulfield|1 Fake St|        |New York| 10001 |holden@dinocore.net| 555-555-5555 |
    |  Holden  |Caulfield|1 Fake St|New York|        | 10001 |holden@dinocore.net| 555-555-5555 |
    |  Holden  |Caulfield|1 Fake St|New York|New York|       |holden@dinocore.net| 555-555-5555 |
    |  Holden  |Caulfield|1 Fake St|New York|New York| 10001 |       fail        | 555-555-5555 |
    |  Holden  |Caulfield|1 Fake St|New York|New York| 10001 |holden@dinocore.net|              |

  @javascript
  Scenario: Remove a phone number
    Given I am an employee
    And I have added a client
    And I have added an additional client phone number
    And I am editing the client
    When I follow "Delete"
    And I confirm
    And I wait for the AJAX call to finish
    And I press "Update Client"
    And I wait for the AJAX call to finish
    Then I should see the "client_phone_numbers_attributes_0_number" field
    And I should not see the "client_phone_numbers_attributes_1_number" field

  @javascript
  Scenario: Add an additional phone number
    Given I am an employee
    And I have added a client
    And I am editing the client
    When I follow "Add"
    And I wait for the AJAX call to finish
    And I fill in "client_phone_numbers_attributes_1_number" with "444-555-5555"
    And I press "Update Client"
    And I wait for the AJAX call to finish
    Then the "client_phone_numbers_attributes_1_number" field should contain "444-555-5555"
    And I should see "Client updated successfully"

  @javascript
  Scenario: Update a client
    Given I have added a client
    And I am editing the client
    And I am an employee
    When I fill in "First name" with "Holden"
    And I press "Update Client"
    And I wait for the AJAX call to finish
    Then I should see "Holden" within "#client .header"
    And I should see "Client updated successfully"

  @javascript
  Scenario: Fail to update a client 
    Given I have added a client
    And I am editing the client
    And I am an employee
    When I fill in "First name" with ""
    And I press "Update Client"
    And I wait for the AJAX call to finish
    Then I should see "Failed to update client"
