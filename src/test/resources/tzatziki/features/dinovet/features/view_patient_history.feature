Feature: View Patient History
  In order to safely treat patients
  An employee
  needs to be able to view a patient's medical history

  Scenario: View all patient history
    Given I have added a client and patient
    And I have made a diagnosis
    When I go to the patient events page
    Then I should see a list of events
