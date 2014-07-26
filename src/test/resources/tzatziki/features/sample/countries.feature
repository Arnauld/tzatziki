@addressing @select
Feature: Countries and provinces
    In order to create tax and shipping zones
    As a store owner
    I want to be able to manage countries and their provinces

    Background:
        Given I am logged in as administrator
          And there are following countries:
            | name    | provinces                       |
            | France  | Lyon, Toulouse, Rennes, Nancy   |
            | China   |                                 |
            | Ukraine | Kiev, Odessa, Cherkasy, Kharkiv |

    Scenario: Seeing index of all countries
        Given I am on the dashboard page
         When I follow "Countries"
         Then I should be on the country index page
          And I should see 3 countries in the list
