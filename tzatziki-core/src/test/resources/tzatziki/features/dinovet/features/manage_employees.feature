Feature: Manage employees
  In order to keep track of employees
  An employee
  wants to be able to add, edit, and delete employees
  
  Scenario: Add a new employee
    Given I am on the new employee page
    And I am an employee
    When I fill in "First name" with "Holden"
    And I fill in "Last name" with "Caulfield"
    And I fill in "Title" with "Doctor"
    And I fill in "Address" with "1234 1st Street"
    And I fill in "Apt." with "Apt. 23"
    And I fill in "City" with "New York"
    And I select "New York" from "State"
    And I fill in "Zipcode" with "10001"
    And I select "Home" from "employee_phone_numbers_attributes_0_label"
    And I fill in "employee_phone_numbers_attributes_0_number" with "1"
    And I fill in "E-mail" with "holden@dinocore.net"
    And I press "Create Employee"
    Then I should be on the employees page
    And I should see "Employee added successfully"

  Scenario: View a list of all employees
    Given I am an employee
    And I have added an employee named "Holden"
    And I am on the employees page
    Then I should see "Holden"

  Scenario: Edit an existing employee
    Given I am an employee
    And I have added an employee named "Holden"
    And I am editing the employee
    When I fill in "First name" with "Phoebe"
    And I press "Update Employee"
    Then I should be on the edit employee page for "Phoebe"
    And I should see "Phoebe"
    And I should see "Employee updated successfully"

  Scenario: Enter invalid data for an existing employee
    Given I am an employee
    And I have added an employee named "Holden"
    And I am editing the employee
    When I fill in "First name" with ""
    And I press "Update Employee"
    Then I should see "Failed to update employee"

  Scenario Outline: Invalid employee data entered
    Given I am on the new employee page
    And I am an employee
    When I fill in "First name" with "<first_name>"
    And I fill in "Last name" with "<last_name>"
    And I fill in "Title" with "<title>"
    And I fill in "Address" with "<address_1>"
    And I fill in "City" with "<city>"
    And I select "<state>" from "State"
    And I fill in "Zipcode" with "<zipcode>"
    And I fill in "employee_phone_numbers_attributes_0_number" with "<phone_number>"
    And I fill in "E-mail" with "<email>"
    And I press "Create Employee"
    Then I should be on the employees page
    And I should see "Failed to create employee"

  Examples:
    |first_name|last_name|title |address_1|  city  |  state |zipcode|       email       | phone_number |
    |          |Caulfield|Doctor|1 Fake St|New York|New York| 10001 |holden@dinocore.net| 555-555-5555 |
    |  Holden  |         |Doctor|1 Fake St|New York|New York| 10001 |holden@dinocore.net| 555-555-5555 |
    |  Holden  |Caulfield|      |1 Fake St|New York|New York| 10001 |holden@dinocore.net| 555-555-5555 |
    |  Holden  |Caulfield|Doctor|         |New York|New York| 10001 |holden@dinocore.net| 555-555-5555 |
    |  Holden  |Caulfield|Doctor|1 Fake St|        |New York| 10001 |holden@dinocore.net| 555-555-5555 |
    |  Holden  |Caulfield|Doctor|1 Fake St|New York|        | 10001 |holden@dinocore.net| 555-555-5555 |
    |  Holden  |Caulfield|Doctor|1 Fake St|New York|New York|       |holden@dinocore.net| 555-555-5555 |
    |  Holden  |Caulfield|Doctor|1 Fake St|New York|New York| 10001 |       fail        | 555-555-5555 |
    |  Holden  |Caulfield|Doctor|1 Fake St|New York|New York| 10001 |holden@dinocore.net|              |

  @javascript
  Scenario: Remove a phone number
    Given I am an employee
    And I have added an employee
    And I have added an additional employee phone number
    And I am editing the employee
    When I follow "Delete"
    And I confirm
    And I wait for the AJAX call to finish
    And I press "Update Employee"
    And I wait for the AJAX call to finish
    Then I should see the "employee_phone_numbers_attributes_0_number" field
    And I should not see the "employee_phone_numbers_attributes_1_number" field

  @javascript
  Scenario: Add an additional phone number
    Given I am an employee
    And I have added an employee
    And I am editing the employee
    When I follow "Add"
    And I wait for the AJAX call to finish
    And I fill in "employee_phone_numbers_attributes_1_number" with "444-555-5555"
    And I press "Update Employee"
    And I wait for the AJAX call to finish
    Then the "employee_phone_numbers_attributes_1_number" field should contain "444-555-5555"
    And I should see "Employee updated successfully"
