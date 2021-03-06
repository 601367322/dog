package com.j256.ormlite.stmt;

/**
 * Interface returned by the {@link UpdateBuilder#prepare()} which supports custom UPDATE statements. This should be in
 * turn passed to the {@link com.j256.ormlite.dao.Dao#update(com.j256.ormlite.stmt.PreparedUpdate)} method.
 *
 * @param <T> The class that the code will be operating on.
 * @author graywatson
 */
public interface PreparedUpdate<T> extends PreparedStmt<T> {
}
