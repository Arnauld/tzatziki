Feature: Manage diagnosis events
  In order to know what treatment will be necessary for a patient
  An employee
  wants to be able to add diagnoses for the patient

  Scenario: Add a new diagnosis event
    Given I have added a client and patient
    And I have created a diagnosis
    And I am on the new diagnosis event page
    And I am an employee
    When I select "Rabies" from "Diagnosis"
    And I fill in "Comment" with "This is a bizzare case of mutant Rabies"
    And I press "Make Diagnosis"
    Then I should see "Diagnosis recorded"
    And I should see "Rabies"

  Scenario: Add two diagnosis of the same type
    Given I have added a client and patient
    And I have created a diagnosis
    And I am on the new diagnosis event page
    And I am an employee
    When I select "Rabies" from "Diagnosis"
    And I fill in "Comment" with "This is a bizzare case of mutant Rabies"
    And I press "Make Diagnosis"
    And I go to the new diagnosis event page
    And I select "Rabies" from "Diagnosis"
    And I fill in "Comment" with "This is a bizzare case of mutant Rabies"
    And I press "Make Diagnosis"
    Then I should see "Diagnosis recorded"
    And I should see "Rabies"
