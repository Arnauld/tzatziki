Feature: Manage diagnosis categories
  In order to organize diagnoses
  An employee
  wants to be able to add, edit, and delete categories for diagnoses

  Scenario: Add a new diagnosis category
    Given I am on the new diagnosis category page
    And I am an employee
    When I fill in "Name" with "Viruses"
    And I fill in "Description" with "Diagnoses dealing with viruses"
    And I press "Create Diagnosis category"
    Then I should be on the diagnoses page
    And I should see "Category created successfully"

  Scenario Outline: Add a new diagnosis category with invalid data
    Given I am on the new diagnosis category page
    And I am an employee
    When I fill in "Name" with "<name>"
    And I fill in "Description" with "<description>"
    And I press "Create Diagnosis category"
    Then I should see "Failed to create category"
    
  Examples:
    | name | description |
    |      |    Test     |

  Scenario: Edit an existing diagnosis category
    Given the following diagnosis categories:
      |   name    |          description           |
      |  Viruses  | Diagnoses dealing with viruses |
    And I am on the edit diagnosis category page for "Viruses"
    And I am an employee
    When I fill in "Name" with "Crocodiles"
    And I fill in "Description" with "This makes no sense"
    And I press "Update Diagnosis category"
    Then I should be on the diagnoses page
    And I should see "Category updated successfully"
    And I should see "Crocodiles"

  @javascript
  Scenario: Create a diagnosis category
    Given I am on the diagnoses page
    And I am an employee
    When I follow "Create a new category"
    And I wait for the AJAX call to finish
    And I fill in "Name" with "Diseases"
    And I fill in "Description" with "Awful, typically fatal diseases."
    And I press "Create Diagnosis category"
    And I wait for the AJAX call to finish
    Then I should see "Diseases" within "#diagnoses"

  @javascript
  Scenario: Update a diagnosis category
    Given I have created a diagnosis category named "Diseases"
    And I am on the diagnoses page
    And I am an employee
    When I follow "Diseases"
    And I wait for the AJAX call to finish
    And I fill in "Name" with "Viruses"
    And I fill in "Description" with "Good luck"
    And I press "Update Diagnosis category"
    And I wait for the AJAX call to finish
    Then I should see "Viruses" within "#diagnoses"
    Then I should not see "Diseases" within "#diagnoses"
