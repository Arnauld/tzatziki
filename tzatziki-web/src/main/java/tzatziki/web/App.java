package tzatziki.web;


import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;


/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class App extends Application<AppConfiguration> {
    private GrammarDAO grammarDAO;

    public static void main(String[] args) throws Exception {
        new App().run(args);
    }

    @Override
    public void initialize(Bootstrap<AppConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets/", "/"));
    }

    @Override
    public void run(AppConfiguration config, Environment environment) throws Exception {
        DBIFactory factory = new DBIFactory();
        DBI jdbi = factory.build(environment, config.getDataSourceFactory(), "db");
        ScenarioDAO scenarioDAO = jdbi.onDemand(ScenarioDAO.class);

        GrammarResource grammarResource = new GrammarResource(grammarDAO);
        GrammarDAOHealthCheck grammarDAOHealthCheck = new GrammarDAOHealthCheck(grammarDAO);

        ScenarioResource scenarioResource = new ScenarioResource(scenarioDAO);
        ScenarioDAOHealthCheck scenarioDAOHealthCheck = new ScenarioDAOHealthCheck(scenarioDAO);

        environment.jersey().register(grammarResource);
        environment.jersey().register(scenarioResource);
        environment.healthChecks().register("grammar-dao", grammarDAOHealthCheck);
        environment.healthChecks().register("scenario-dao", scenarioDAOHealthCheck);
    }
}
