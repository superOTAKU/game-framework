package org.summer.db;

public interface SqlBuilder {

    String preparedSql(GenericEntity entity);

    Object[] preparedArgs(GenericEntity entity);

}
