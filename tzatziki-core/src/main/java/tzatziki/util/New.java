package tzatziki.util;

import java.util.Stack;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class New {
    public static <T> Stack<T> newStack() {
        return new Stack<T>();
    }
}
