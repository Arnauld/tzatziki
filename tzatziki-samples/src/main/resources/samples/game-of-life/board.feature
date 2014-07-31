Feature: Petri board of the game of life should...


  In order to play a game of life
  As a computer scientist
  I need to be able to setup the board and click start

  @ui @toggle
  Scenario: Click on a dead cell
    Given a board
    When I click on a dead cell
    Then it should come to life


  @ui @toggle
  Scenario: Click on an alive cell
    Given a board
    When I click on an alive cell
    Then it should kill the cell

  @toggle
  Scenario: Toggle a cell on the board

    Given a 5 by 5 game
    When I toggle the cell at (2, 3)
    Then the grid should look like
    """.....
       .....
       .....
       ..X..
       ....."""
    When I toggle the cell at (2, 4)
    Then the grid should look like
    """.....
       .....
       .....
       ..X..
       ..X.."""
    When I toggle the cell at (2, 3)
    Then the grid should look like
    """.....
       .....
       .....
       .....
       ..X.."""