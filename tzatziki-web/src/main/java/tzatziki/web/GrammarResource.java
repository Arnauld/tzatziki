package tzatziki.web;

import com.codahale.metrics.annotation.Timed;
import tzatziki.analysis.java.Grammar;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@Path("/grammar")
@Produces(MediaType.APPLICATION_JSON)
public class GrammarResource {

    private final GrammarDAO grammarDAO;

    public GrammarResource(GrammarDAO grammarDAO) {
        this.grammarDAO = grammarDAO;
    }

    @GET
    @Timed
    public Grammar grammar() {
        return grammarDAO.getGrammar();
    }
}
