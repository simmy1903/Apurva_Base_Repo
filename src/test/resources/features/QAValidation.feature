Feature: Validate the user has been added to the table and Delete the user "novak" from the table and validate the user has been deleted.

#  Background:
#    Given launch the URL

  Scenario: Validate the user has been added to the table
    When the user tries to add new user
    Then user is added

#  Scenario: Delete t      eleted