Feature: Making Money

#
#  **In order to** have daily reports of what is sold and when
#
# **As a** shop keeper
#
# **I want to** track statistics of machine usage
#
# [formula]
#  ----
#    {\eta \leq C(\delta(\eta) +\Lambda_M(0,\delta))
#  ----
#
# {% formula %}
#
#    \Re{z} =\frac{n\pi \dfrac{\theta +\psi}{2}}{
#            \left(\dfrac{\theta +\psi}{2}\right)^2 + \left( \dfrac{1}{2}
#            \log \left\vert\dfrac{B}{A}\right\vert\right)^2}.
#
# {% formula %}
#
#

  @math
  @Reporting
  Scenario: Statistics collect basic usage

    Given the following orders:
      | time     | drink     |
      | 08:05:23 | Coffee    |
      | 08:06:43 | Coffee    |
      | 08:10:23 | Coffee    |
      | 08:45:03 | Tea       |
      | 10:05:47 | Coffee    |
      | 10:05:47 | Chocolate |
    When I query for a report
    Then the report output should be
    """
    chocolate: 1
    coffee: 4
    tea: 1
    ---
    Total: 3.00€
    """

  @Reporting
  Scenario: Statistics collect no usage

    # [asciidiag]
    # ----
    #   /---------------+-------------\
    #   |cRED Chocolate |cBLU 1       |-----\
    #   +---------------+-------------+     :
    #   |cGRE Coffee    |cPNK 4       |     |
    #   +---------------+-------------+     |
    #   |cAAA               Total     |<----/
    #   +-----------------------------+         /-------\
    #   |cCCC               3.00€     |---+---->| DB    |
    #   |               +-------------+         |    {s}|
    #   |               |cYEL YEL     |         \-------/
    #   \---------------+-------------/
    # ----
    #

    When I query for a report
    Then the report output should be
    """
    ---
    Total: 0.00€
    """