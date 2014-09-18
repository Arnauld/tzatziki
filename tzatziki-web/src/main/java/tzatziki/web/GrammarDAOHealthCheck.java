package tzatziki.web;

import com.codahale.metrics.health.HealthCheck;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class GrammarDAOHealthCheck extends HealthCheck {
    private final GrammarDAO grammarDAO;

    public GrammarDAOHealthCheck(GrammarDAO grammarDAO) {
        this.grammarDAO = grammarDAO;
    }

    @Override
    protected Result check() throws Exception {
        if (grammarDAO.getGrammar() == null)
            return Result.unhealthy("No grammar available");
        return Result.healthy();
    }
}
