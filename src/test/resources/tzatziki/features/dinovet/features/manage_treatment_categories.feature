Feature: Manage treatment categories
  In order to organize treatments
  An employee
  wants to be able to add, edit, and delete categories for treatments

  Scenario: Add a new treatment category
    Given I am on the new treatment category page
    And I am an employee
    When I fill in "Name" with "Viruses"
    And I fill in "Description" with "Treatments dealing with viruses"
    And I press "Create Treatment category"
    Then I should be on the treatments page
    And I should see "Category created successfully"

  Scenario Outline: Add a new treatment category with invalid data
    Given I am on the new treatment category page
    And I am an employee
    When I fill in "Name" with "<name>"
    And I fill in "Description" with "<description>"
    And I press "Create Treatment category"
    Then I should see "Failed to create category"
    
  Examples:
    | name | description |
    |      |    Test     |

  Scenario: Edit an existing treatment category
    Given the following treatment categories:
      |   name    |          description           |
      |  Viruses  | Treatments dealing with viruses |
    And I am on the edit treatment category page for "Viruses"
    And I am an employee
    When I fill in "Name" with "Crocodiles"
    And I fill in "Description" with "This makes no sense"
    And I press "Update Treatment category"
    Then I should be on the treatments page
    And I should see "Category updated successfully"
    And I should see "Crocodiles"

  @javascript
  Scenario: Create a treatment category
    Given I am on the treatments page
    And I am an employee
    When I follow "Create a new category"
    And I wait for the AJAX call to finish
    And I fill in "Name" with "Diseases"
    And I fill in "Description" with "Awful, typically fatal diseases."
    And I press "Create Treatment category"
    And I wait for the AJAX call to finish
    Then I should see "Diseases" within "#treatments"

  @javascript
  Scenario: Update a treatment category
    Given I have created a treatment category named "Diseases"
    And I am on the treatments page
    And I am an employee
    When I follow "Diseases"
    And I wait for the AJAX call to finish
    And I fill in "Name" with "Viruses"
    And I fill in "Description" with "Good luck"
    And I press "Update Treatment category"
    And I wait for the AJAX call to finish
    Then I should see "Viruses" within "#treatments"
    Then I should not see "Diseases" within "#treatments"
