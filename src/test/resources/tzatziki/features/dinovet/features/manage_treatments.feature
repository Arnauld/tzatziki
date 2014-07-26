Feature: Manage treatments
  In order to maintain a standard list of treatments
  An employee
  wants to be able to add, edit, and delete treatments

  Scenario: View a list of all treatment
    Given the following treatments:
      | code |   name   |     description     | categories.name   |
      | 1234 |  Rabies  |  A viral infection  |     Viruses       |
      | 1235 | Boneitis | A fictional disease |Fictional Diseases |
    And I am an employee
    When I go to the treatments page
    Then I should see a list of treatments
    

  Scenario: Add a new treatment
    Given I am on the new treatment page
    And I am an employee
    When I fill in "Name" with "Rabies"
    And I fill in "Code" with "ZR8182"
    And I fill in "Description" with "A viral disease that causes acute encephalitis in warm-blooded animals."
    And I press "Create Treatment"
    Then I should be on the treatments page
    And I should see "Treatment created successfully"

  Scenario Outline: Add a new treatment with invalid data
    Given I am on the new treatment page
    And I am an employee
    When I fill in "Name" with "<name>"
    And I fill in "Code" with "<code>"
    And I fill in "Description" with "<description>"
    And I press "Create Treatment"
    Then I should be on the treatments page
    And I should see "Failed to create treatment"

  Examples:
    | code |    name    |
    |      |   Rabies   |
    | 9999 |            |

  Scenario Outline: Add a new treatment that is not unique
    Given the following treatments:
      | code |   name   |     description     |
      | 1234 |  Rabies  |  A viral infection  |
    And I am on the new treatment page
    And I am an employee
    When I fill in "Name" with "<name>"
    And I fill in "Code" with "<code>"
    And I fill in "Description" with "<description>"
    And I press "Create Treatment"
    Then I should be on the treatments page
    And I should see "Failed to create treatment"

  Examples:
    | code |      name      |
    | 1234 |     Rabies     |
    | 1234 |   Not Rabies   |
    | 9999 |     Rabies     |


  Scenario: Edit an existing treatment
    Given the following treatments:
      | code |   name   |     description     | categories.name |
      | 1234 |  Rabies  |  A viral infection  |    Fictional    |
    And I am on the edit treatment page for "Rabies"
    And I am an employee
    When I fill in "Code" with "1394"
    And I fill in "Name" with "Boneitis"
    And I fill in "Description" with "A fictional degenerative bone disease"
    And I press "Update Treatment"
    Then I should be on the treatments page
    And I should see "Treatment updated successfully"

  @javascript
  Scenario: Create a treatment
    Given I have created a treatment category named "Diseases"
    And I am on the treatments page
    And I am an employee
    When I follow "Create a new treatment"
    And I wait for the AJAX call to finish
    And I fill in "Name" with "Boneitis"
    And I fill in "Code" with "1337"
    And I select "Diseases" from "Categories"
    And I fill in "Description" with "A degenerative bone disease."
    And I press "Create Treatment"
    And I wait for the AJAX call to finish
    Then I should see "Diseases" within "#treatments"
    And I should see "Boneitis" within "#treatments"

  @javascript
  Scenario: Update a treatment
    Given I have created a treatment category named "Diseases"
    And I have created a treatment named "Boneitis"
    And I am on the treatments page
    And I am an employee
    When I follow "Boneitis"
    And I wait for the AJAX call to finish
    And I fill in "Name" with "Snow Crash"
    And I fill in "Code" with "31337"
    And I select "Diseases" from "Categories"
    And I fill in "Description" with "A neuro-linguistic virus"
    And I press "Update Treatment"
    And I wait for the AJAX call to finish
    Then I should see "Diseases" within "#treatments"
    And I should see "Snow Crash" within "#treatments"
    And I should not see "Boneitis" within "#treatments"
