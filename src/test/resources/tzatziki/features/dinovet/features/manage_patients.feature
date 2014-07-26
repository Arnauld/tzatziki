Feature: Manage patients
  In order to keep track of patients
  An employee
  wants to be able to add, edit, and delete patients

  Scenario: Go to new patient page
    Given I have added a client
    And I have added a species and breed
    And I am editing the client
    And I am an employee
    When I follow "Create a new patient"
    Then I should be on the new patient page
    And I should see the new patient form

  Scenario: Go to an existing patient page
    Given I have added a client and patient
    And I have added a species and breed
    And I am editing the client
    And I am an employee
    When I select the patient from the patients list
    Then I should be on the edit patient page
    And I should see the edit patient form

  Scenario: Go directly to a patient page
    Given I have added a client named "Holden" and patient named "Mongo"
    And I have added a species and breed
    And I am on the clients page
    When I follow "Mongo"
    Then I should see "Holden" within "#client"
    And I should see "Mongo" within "#patient"

  Scenario: Add a new patient
    Given I have added a client
    And I have added a species and breed
    And I am on the new patient page
    And I am an employee
    When I fill in "Name" with "Moose"
    And I select "Male" from "Sex"
    And I select "Dog" from "Species"
    And I select "Yorkshire Terrier" from "Breed"
    And I fill in "Weight" with "10"
    And I select "2005" from "patient_birth_date_1i"
    And I select "Jan" from "patient_birth_date_2i"
    And I select "11" from "patient_birth_date_3i"
    And I fill in "Color" with "Brown"
    And I fill in "Microchip" with "111234"
    And I press "Create Patient"
    Then I should be on the edit patient page for "Moose"
    And I should see "Patient created successfully"

  Scenario Outline: Invalid patient data entered
    Given I have added a client
    And I have added a species and breed
    And I am on the new patient page
    And I am an employee
    When I fill in "Name" with "<name>"
    And I press "Create Patient"
    Then I should see "Failed to create patient"

  Examples:
    |    name    |
    |            |

  Scenario: Edit an existing patient
    Given I have added a client and patient
    And I have added a species and breed
    And I am on the edit patient page
    And I am an employee
    When I fill in "Name" with "Crocodile"
    And I press "Update Patient"
    Then I should be on the edit patient page for "Crocodile"
    And I should see "Patient updated successfully"
  
  Scenario: Enter invalid data for an existing patient
    Given I have added a client and patient
    And I have added a species and breed
    And I am on the edit patient page
    And I am an employee
    When I fill in "Name" with ""
    And I press "Update Patient"
    Then I should see "Failed to update patient"

  @javascript
  Scenario: Select a different species
    Given I have added a client and patient
    And I have added a species and breed
    And I am on the new patient page
    And I am an employee
    When I select "Cat" from "Species"
    And I wait for the AJAX call to finish
    Then I should see "American Longhair"

  @javascript
  Scenario: Show new patient panel
    Given I have added a client
    And I am editing the client
    And I am an employee
    When I follow "Create a new patient"
    And I wait for the AJAX call to finish
    Then I should see "Patient" within "#patient"

  @javascript
  Scenario: Show edit patient panel
    Given I have added a client and patient
    And I have added a patient named "Crocodile"
    And I am editing the client
    And I am an employee
    When I follow "Crocodile"
    And I wait for the AJAX call to finish
    Then I should see "Crocodile" within "#patient"

  @javascript
  Scenario: Update a patient
    Given I have added a client and patient
    And I have added a patient named "Crocodile"
    And I am editing the client
    And I am an employee
    When I follow "Crocodile"
    And I fill in "Name" with "Alligator"
    And I press "Update Patient"
    And I wait for the AJAX call to finish
    Then I should see "Alligator" within "#patient .header"
    And I should see "Patient updated successfully"

  @javascript
  Scenario: Fail to update a patient
    Given I have added a client and patient
    And I have added a patient named "Crocodile"
    And I am editing the client
    And I am an employee
    When I follow "Crocodile"
    And I fill in "patient_weight" with "a lot"
    And I press "Update Patient"
    And I wait for the AJAX call to finish
    Then I should see "Failed to update patient"

  @javascript
  Scenario: Create a patient
    Given I have added a client
    And I have added a species and breed
    And I am editing the client
    And I am an employee
    When I follow "Create a new patient"
    And I fill in "Name" with "Crocodile"
    And I select "Cat" from "Species"
    And I wait for the AJAX call to finish
    And I select "American Longhair" from "Breed"
    And I fill in "patient_weight" with "100"
    And I press "Create Patient"
    And I wait for the AJAX call to finish
    Then I should not see "Crocodile" within "#patient .header"
    And I should see "Patient created successfully"

  @javascript
  Scenario: Fail to create a patient
    Given I have added a client
    And I have added a species and breed
    And I am editing the client
    And I am an employee
    When I follow "Create a new patient"
    And I fill in "Name" with "Crocodile"
    And I fill in "patient_weight" with "a ton"
    And I press "Create Patient"
    And I wait for the AJAX call to finish
    Then I should see "Failed to create patient"
