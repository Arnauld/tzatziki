Feature: Manage diagnoses
  In order to maintain a standard list of diagnoses
  An employee
  wants to be able to add, edit, and delete diagnoses

  Scenario: View a list of all diagnosis
    Given the following diagnoses:
      | code |   name   |     description     | categories.name   |
      | 1234 |  Rabies  |  A viral infection  |     Viruses       |
      | 1235 | Boneitis | A fictional disease |Fictional Diseases |
    And I am an employee
    When I go to the diagnoses page
    Then I should see a list of diagnoses
    

  Scenario: Add a new diagnosis
    Given I am on the new diagnosis page
    And I am an employee
    When I fill in "Name" with "Rabies"
    And I fill in "Code" with "ZR8182"
    And I fill in "Description" with "A viral disease that causes acute encephalitis in warm-blooded animals."
    And I press "Create Diagnosis"
    Then I should be on the diagnoses page
    And I should see "Diagnosis created successfully"

  Scenario Outline: Add a new diagnosis with invalid data
    Given I am on the new diagnosis page
    And I am an employee
    When I fill in "Name" with "<name>"
    And I fill in "Code" with "<code>"
    And I fill in "Description" with "<description>"
    And I press "Create Diagnosis"
    Then I should be on the diagnoses page
    And I should see "Failed to create diagnosis"

  Examples:
    | code |    name    |
    |      |   Rabies   |
    | 9999 |            |

  Scenario Outline: Add a new diagnosis that is not unique
    Given the following diagnoses:
      | code |   name   |     description     |
      | 1234 |  Rabies  |  A viral infection  |
    And I am on the new diagnosis page
    And I am an employee
    When I fill in "Name" with "<name>"
    And I fill in "Code" with "<code>"
    And I fill in "Description" with "<description>"
    And I press "Create Diagnosis"
    Then I should be on the diagnoses page
    And I should see "Failed to create diagnosis"

  Examples:
    | code |      name      |
    | 1234 |     Rabies     |
    | 1234 |   Not Rabies   |
    | 9999 |     Rabies     |


  Scenario: Edit an existing diagnosis
    Given the following diagnoses:
      | code |   name   |     description     | categories.name |
      | 1234 |  Rabies  |  A viral infection  |    Fictional    |
    And I am on the edit diagnosis page for "Rabies"
    And I am an employee
    When I fill in "Code" with "1394"
    And I fill in "Name" with "Boneitis"
    And I fill in "Description" with "A fictional degenerative bone disease"
    And I press "Update Diagnosis"
    Then I should be on the diagnoses page
    And I should see "Diagnosis updated successfully"

  @javascript
  Scenario: Create a diagnosis
    Given I have created a diagnosis category named "Diseases"
    And I am on the diagnoses page
    And I am an employee
    When I follow "Create a new diagnosis"
    And I wait for the AJAX call to finish
    And I fill in "Name" with "Boneitis"
    And I fill in "Code" with "1337"
    And I select "Diseases" from "Categories"
    And I fill in "Description" with "A degenerative bone disease."
    And I press "Create Diagnosis"
    And I wait for the AJAX call to finish
    Then I should see "Diseases" within "#diagnoses"
    And I should see "Boneitis" within "#diagnoses"

  @javascript
  Scenario: Update a diagnosis
    Given I have created a diagnosis category named "Diseases"
    And I have created a diagnosis named "Boneitis"
    And I am on the diagnoses page
    And I am an employee
    When I follow "Boneitis"
    And I wait for the AJAX call to finish
    And I fill in "Name" with "Snow Crash"
    And I fill in "Code" with "31337"
    And I select "Diseases" from "Categories"
    And I fill in "Description" with "A neuro-linguistic virus"
    And I press "Update Diagnosis"
    And I wait for the AJAX call to finish
    Then I should see "Diseases" within "#diagnoses"
    And I should see "Snow Crash" within "#diagnoses"
    And I should not see "Boneitis" within "#diagnoses"
