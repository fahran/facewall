package data.dao.database.query;

import data.dao.database.QueryResultRow;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import util.CompositeMatcher;
import util.IterableMatchers;

import java.util.Iterator;

import static util.IterableMatchers.contains;

public class FacewallQueryResultsMatcher extends CompositeMatcher<Iterable<QueryResultRow>> {

    private FacewallQueryResultsMatcher() {}

    public static FacewallQueryResultsMatcher areQueryResults() {
        return new FacewallQueryResultsMatcher();
    }

    public FacewallQueryResultsMatcher containingARow(Matcher<QueryResultRow> rowMatcher) {
        final Matcher<Iterable<QueryResultRow>> resultsMatcher = contains(rowMatcher);
        add(new TypeSafeMatcher<Iterable<QueryResultRow>>() {
            @Override
            public boolean matchesSafely(Iterable<QueryResultRow> queryResultRows) {
                return resultsMatcher.matches(queryResultRows);
            }

            @Override
            public void describeTo(Description description) {
                description.appendDescriptionOf(resultsMatcher);
            }
        });
        return this;
    }

    public FacewallQueryResultsMatcher withRowCount(final int expectedCount) {
        add(new TypeSafeMatcher<Iterable<QueryResultRow>>() {
            @Override
            public boolean matchesSafely(Iterable<QueryResultRow> queryResultRows) {
                int rowCount = 0;
                for (Iterator iterator = queryResultRows.iterator(); iterator.hasNext(); iterator.next()) {
                    rowCount++;
                }

                return expectedCount == rowCount;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(" with ").appendValue(expectedCount).appendText(" rows.");
            }
        });
        return this;    }
}