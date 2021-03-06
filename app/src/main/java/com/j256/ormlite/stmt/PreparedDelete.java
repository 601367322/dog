package com.j256.ormlite.stmt;

/**
 * Interface returned by the {@link DeleteBuilder#prepare()} which supports custom DELETE statements. This should be in
 * turn passed to the {@link com.j256.ormlite.dao.Dao#delete(com.j256.ormlite.stmt.PreparedDelete)} method.
 *
 * @param <T> The class that the code will be operating on.
 * @author graywatson
 */
public interface PreparedDelete<T> extends PreparedStmt<T> {
}
