package tzatziki.web;

import com.codahale.metrics.health.HealthCheck;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ScenarioDAOHealthCheck extends HealthCheck {
    private final ScenarioDAO scenarioDAO;

    public ScenarioDAOHealthCheck(ScenarioDAO scenarioDAO) {
        this.scenarioDAO = scenarioDAO;
    }


    @Override
    protected Result check() throws Exception {
        try {
            scenarioDAO.check();
            return Result.healthy();
        } catch (Exception e) {
            return Result.unhealthy(e.getMessage());
        }
    }
}
