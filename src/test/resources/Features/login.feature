#@allure.label.layer:web
@allure.label.owner:Krish
#@allure.label.epic:Web
Feature: Testing tagged hooks

  Scenario: Test the search results
    Given open Google in the browser
    When User searches for Test
    Then Results are displayed

#  @Issue("MAEAUTO-15212")
#  @allure.issue:MAEAUTO-15212
#  @allure.label.jira:MAEAUTO-15212
  @MAEAUTO-15212 @Regression @NSI
  Scenario: Test the search results again
    Given Google is open in the browser
    When User searches for Selenium cross browser testing
    Then Results are displayed