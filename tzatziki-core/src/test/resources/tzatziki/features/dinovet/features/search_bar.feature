Feature: Search bar
  In order to quickly find clients and patients
  An employee
  wants to be able to search for clients by name, address, and phone number and
  patient by name, species, and breed
  
  @search
  Scenario: Search for client's last name
    Given I am on the search page
    And I am an employee
    And the following clients:
      |first_name| last_name |
      |  Holden  | Caulfield |
      |  Phoebe  | Caulfield |
      |  Winston |   Smith   |
      |  Hiro    |Protagonist|
    And all models have been indexed
    When I fill in "search" with "Caulfield"
    And I press "Search"
    Then I should be on the search page
    And the "search" field should contain "Caulfield"
    And I should see "Caulfield, Holden"
    And I should see "Caulfield, Phoebe"

  @search
  Scenario: Search for client's first name
    Given I am on the search page
    And I am an employee
    And the following clients:
      |first_name| last_name |
      |  Holden  | Caulfield |
      |  Phoebe  | Caulfield |
      |  Winston |   Smith   |
      |  Hiro    |Protagonist|
    And all models have been indexed
    When I fill in "search" with "Holden"
    And I press "Search"
    Then I should be on the search page
    And I should see "Caulfield, Holden"
    And the "search" field should contain "Holden"

  @search
  Scenario: Search for client's address
    Given I am on the search page
    And I am an employee
    And the following clients:
      |first_name| last_name | address_1 |
      |  Holden  | Caulfield |  1 Fake St|
      |  Phoebe  | Caulfield |  1 Fake St|
      |  Winston |   Smith   | 3 First St|
      |  Hiro    |Protagonist|1 Second St|
    And all models have been indexed
    When I fill in "search" with "3 First St"
    And I press "Search"
    Then I should be on the search page
    And I should see "Smith, Winston"
    And the "search" field should contain "3 First St"

  @search
  Scenario: Search for client's phone number
    Given I am on the search page
    And I am an employee
    And the following clients:
      |first_name| last_name |phone_numbers.number|
      |  Holden  | Caulfield |555-555-5555| 
      |  Phoebe  | Caulfield |123-456-7890| 
      |  Winston |   Smith   |222-123-4323| 
      |  Hiro    |Protagonist|234-127-1483| 
    And all models have been indexed
    When I fill in "search" with "234-127-1483"
    And I press "Search"
    Then I should be on the search page
    And I should see "Protagonist, Hiro"
    And the "search" field should contain "234-127-1483"

  @search
  Scenario: Search for client's e-mail address
    Given I am on the search page
    And I am an employee
    And the following clients:
      |first_name| last_name | email                       |
      |  Holden  | Caulfield |holden.caulfield@dinocore.net|
      |  Phoebe  | Caulfield |phoebe.caulfield@dinocore.net|
      |  Winston |   Smith   |winston.smith@dinocore.net   |
      |  Hiro    |Protagonist|hiro.protagonist@dinocore.net|
    And all models have been indexed
    When I fill in "search" with "phoebe.caulfield@dinocore.net"
    And I press "Search"
    Then I should be on the search page
    And I should see "Caulfield, Phoebe"
    And the "search" field should contain "phoebe.caulfield@dinocore.net"

  @search
  Scenario: Search for employee's e-mail address
    Given I am on the search page
    And I am an employee
    And the following employees:
      |first_name| last_name | email                       |
      |  Holden  | Caulfield |holden.caulfield@dinocore.net|
      |  Phoebe  | Caulfield |phoebe.caulfield@dinocore.net|
      |  Winston |   Smith   |winston.smith@dinocore.net   |
      |  Hiro    |Protagonist|hiro.protagonist@dinocore.net|
    And all models have been indexed
    When I fill in "search" with "phoebe.caulfield@dinocore.net"
    And I press "Search"
    Then I should be on the search page
    And I should see "Caulfield, Phoebe"
    And the "search" field should contain "phoebe.caulfield@dinocore.net"

  @search
  Scenario: Search for employee's phone number
    Given I am on the search page
    And I am an employee
    And the following employees:
      |first_name| last_name |phone_numbers.number|
      |  Holden  | Caulfield |555-555-5555        |
      |  Phoebe  | Caulfield |123-456-7890        |
      |  Winston |   Smith   |222-123-4323        |
      |  Hiro    |Protagonist|234-127-1483        |
    And all models have been indexed
    When I fill in "search" with "234-127-1483"
    And I press "Search"
    Then I should be on the search page
    And I should see "Protagonist, Hiro"
    And the "search" field should contain "234-127-1483"

  @search
  Scenario: Search for employee's last name
    Given I am on the search page
    And I am an employee
    And the following employees:
      |first_name| last_name |
      |  Holden  | Caulfield |
      |  Phoebe  | Caulfield |
      |  Winston |   Smith   |
      |  Hiro    |Protagonist|
    And all models have been indexed
    When I fill in "search" with "Caulfield"
    And I press "Search"
    Then I should be on the search page
    And the "search" field should contain "Caulfield"
    And I should see "Caulfield, Holden"
    And I should see "Caulfield, Phoebe"

  @search
  Scenario: Search for employee's first name
    Given I am on the search page
    And I am an employee
    And the following employees:
      |first_name| last_name |
      |  Holden  | Caulfield |
      |  Phoebe  | Caulfield |
      |  Winston |   Smith   |
      |  Hiro    |Protagonist|
    And all models have been indexed
    When I fill in "search" with "Holden"
    And I press "Search"
    Then I should be on the search page
    And I should see "Caulfield, Holden"
    And the "search" field should contain "Holden"

  @search
  Scenario: Search for employee's address
    Given I am on the search page
    And I am an employee
    And the following employees:
      |first_name| last_name | address_1 |
      |  Holden  | Caulfield |  1 Fake St|
      |  Phoebe  | Caulfield |  1 Fake St|
      |  Winston |   Smith   | 3 First St|
      |  Hiro    |Protagonist|1 Second St|
    And all models have been indexed
    When I fill in "search" with "3 First St"
    And I press "Search"
    Then I should be on the search page
    And I should see "Smith, Winston"
    And the "search" field should contain "3 First St"

  @search
  Scenario: Search for patient's name
    Given I am on the search page
    And I am an employee
    And the following patients:
      |   name   | breed | species | microchip |
      |  Waco    | Cat   |  Bengal |  1337     |
      |  Mongo   | Dog   |  Elo    |  4a56     |
      |  Goober  | Bird  |  Parrot |  4234     |
      |  Moose   | Dog   |  Aidi   |  3445     |
    And all models have been indexed
    When I fill in "search" with "Waco"
    And I press "Search"
    Then I should be on the search page
    And I should see "Waco"
    And I should not see "Mongo"
    And the "search" field should contain "Waco"

  @search
  Scenario: Search for patient's species
    Given I am on the search page
    And I am an employee
    And the following patients:
      |   name   | breed | species | microchip |
      |  Waco    | Cat   |  Bengal |  1337     |
      |  Mongo   | Dog   |  Elo    |  4a56     |
      |  Goober  | Bird  |  Parrot |  4234     |
      |  Moose   | Dog   |  Aidi   |  3445     |
    And all models have been indexed
    When I fill in "search" with "Bird"
    And I press "Search"
    Then I should be on the search page
    And I should see "Goober"
    And I should not see "Mongo"
    And the "search" field should contain "Bird"

  @search
  Scenario: Search for patient's breed
    Given I am on the search page
    And I am an employee
    And the following patients:
      |   name   | breed | species | microchip |
      |  Waco    | Cat   |  Bengal |  1337     |
      |  Mongo   | Dog   |  Elo    |  4a56     |
      |  Goober  | Bird  |  Parrot |  4234     |
      |  Moose   | Dog   |  Aidi   |  3445     |
    And all models have been indexed
    When I fill in "search" with "Aidi"
    And I press "Search"
    Then I should be on the search page
    And I should see "Moose"
    And I should not see "Mongo"
    And the "search" field should contain "Aidi"

  @search
  Scenario: Search for patient's microchip
    Given I am on the search page
    And I am an employee
    And the following patients:
      |   name   | breed | species | microchip |
      |  Waco    | Cat   |  Bengal |  1337     |
      |  Mongo   | Dog   |  Elo    |  4a56     |
      |  Goober  | Bird  |  Parrot |  4234     |
      |  Moose   | Dog   |  Aidi   |  3445     |
    And all models have been indexed
    When I fill in "search" with "1337"
    And I press "Search"
    Then I should be on the search page
    And I should see "Waco"
    And I should not see "Mongo"
    And the "search" field should contain "1337"

  @search
  @javascript
  Scenario: Search using the quick search bar
    Given I am on the search page
    And I am an employee
    And the following patients:
      |   name   | breed | species | microchip |
      |  Waco    | Cat   |  Bengal |  1337     |
      |  Mongo   | Dog   |  Elo    |  4a56     |
      |  Goober  | Bird  |  Parrot |  4234     |
      |  Moose   | Dog   |  Aidi   |  3445     |
    And all models have been indexed
    When I fill in "search" with "Waco"
    And I press "Search"
    And I wait for the AJAX call to finish
    Then I should be on the search page
    And I should see "Waco" within "#search-bar #search-results"
    And I should not see "Mongo"
    And the "search" field should contain "Waco"
