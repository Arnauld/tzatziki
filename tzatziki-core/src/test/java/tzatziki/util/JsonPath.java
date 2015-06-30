package tzatziki.util;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class JsonPath {
    private final String expr;

    public JsonPath(String expr) {
        this.expr = expr;
    }

    public JsonNode evaluate(JsonNode node) {
        return evaluate(node, expr);
    }

    public String evaluateString(JsonNode node) {
        JsonNode found = evaluate(node);
        if (found == null)
            return null;
        return found.textValue();
    }

    private JsonNode evaluate(JsonNode node, String expr) {
        String[] path = removeTrailingSlash(expr).split("/");

        JsonNode tNode = node;
        for (String pathFragment : path) {
            tNode = evaluateLocally(tNode, pathFragment);
        }
        return tNode;
    }

    private JsonNode evaluateLocally(JsonNode tNode, String pathFragment) {
        if (hasIndexedValue(pathFragment)) {
            return indexedValue(tNode, pathFragment);
        }
        return tNode.get(pathFragment);
    }

    private JsonNode indexedValue(JsonNode node, String pathFragment) {
        int startOf = pathFragment.lastIndexOf("[");
        int endOf = pathFragment.lastIndexOf("]");

        if (startOf == -1 || endOf == -1)
            throw new InvalidExpressionException("Unbalanced '[' and ']' from fragment: '" + pathFragment + "'");

        String idxAsString = pathFragment.substring(startOf + 1, endOf);
        int index = Integer.parseInt(idxAsString);

        String subPath = pathFragment.substring(0, startOf);
        JsonNode sub = evaluateLocally(node, subPath);
        return sub.get(index);
    }

    private boolean hasIndexedValue(String path) {
        return path.contains("[");
    }

    private static String removeTrailingSlash(String expr) {
        return expr.startsWith("/") ? expr.substring(1) : expr;
    }


    public static class InvalidExpressionException extends RuntimeException {
        public InvalidExpressionException(String message) {
            super(message);
        }
    }
}

