package tzatziki.analysis.exec.model;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class DataTable {
    public List<Row> rows = Lists.newArrayList();

    public void declareRow(Row row) {
        rows.add(row);
    }

    public boolean isEmpty() {
        return rows.isEmpty();
    }

    public int nbColumns() {
        if (isEmpty())
            throw new IllegalStateException("Table is empty");
        return rows.get(0).nbColumns();
    }

    public FluentIterable<Row> rows() {
        return FluentIterable.from(rows);
    }

    public static class Row {
        private List<String> cells;
        private List<String> comments;

        public Row(List<String> cells, List<String> comments) {
            this.cells = cells;
            this.comments = comments;
        }

        public FluentIterable<String> cells() {
            return FluentIterable.from(cells);
        }

        public FluentIterable<String> comments() {
            return FluentIterable.from(comments);
        }

        public int nbColumns() {
            return cells.size();
        }
    }
}
