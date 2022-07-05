package org.summer.db;

import org.summer.util.SqlUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MysqlSqlBuilder implements SqlBuilder {

    @Override
    public String preparedSql(GenericEntity entity) {
        StringBuilder sb = new StringBuilder();
        switch (entity.getState()) {
            case INSERTING:
                sb.append("INSERT INTO ").append(SqlUtil.toFieldName(entity.getName())).append("(");
                sb.append("id,");
                sb.append(entity.getFieldValues().keySet().stream().map(SqlUtil::toFieldName).collect(Collectors.joining(","))).append(") VALUES(");
                sb.append("?,".repeat(entity.getFieldValues().size() + 1));
                sb.deleteCharAt(sb.length() - 1);
                sb.append(")");
                break;
            case MODIFYING:
                sb.append("UPDATE ").append(SqlUtil.toFieldName(entity.getName())).append(" SET ");
                for (var entry : entity.getFieldValues().entrySet()) {
                    sb.append(SqlUtil.toFieldName(entry.getKey())).append("=?,");
                }
                sb.deleteCharAt(sb.length() - 1);
                appendWhere(sb);
                break;
            case DELETING:
                sb.append("DELETE FROM ").append(SqlUtil.toFieldName(entity.getName()));
                appendWhere(sb);
                break;
            case DELETED:
                throw new IllegalStateException("entity already deleted!");
        }
        return sb.toString();
    }

    @Override
    public Object[] preparedArgs(GenericEntity entity) {
        switch (entity.getState()) {
            case INSERTING:
                List<Object> args = new ArrayList<>();
                args.add(entity.getIdValue());
                args.addAll(entity.getFieldValues().values());
                return args.toArray(new Object[0]);
            case DELETING:
                return new Object[] {entity.getIdValue()};
            case MODIFYING:
                args = new ArrayList<>(entity.getFieldValues().values());
                args.add(entity.getIdValue());
                return args.toArray(new Object[0]);
            case DELETED:
                throw new IllegalStateException("entity already deleted");
        }
        throw new UnsupportedOperationException("unsupported state " + entity.getState());
    }
    private static void appendWhere(StringBuilder sb) {
        sb.append(" WHERE id=?");
    }

    public static void main(String[] args) {
        GenericEntity entity = new GenericEntity();
        entity.setName("user");
        entity.setIdValue(1L);
        entity.setState(EntityState.INSERTING);
        entity.getFieldValues().put("name", "Jack");
        entity.getFieldValues().put("sex", 1);
        SqlBuilder sqlBuilder = new MysqlSqlBuilder();
        System.out.println(sqlBuilder.preparedSql(entity));
        System.out.println(Arrays.toString(sqlBuilder.preparedArgs(entity)));
        entity.setState(EntityState.MODIFYING);
        System.out.println(sqlBuilder.preparedSql(entity));
        System.out.println(Arrays.toString(sqlBuilder.preparedArgs(entity)));
        entity.setState(EntityState.DELETING);
        System.out.println(sqlBuilder.preparedSql(entity));
        System.out.println(Arrays.toString(sqlBuilder.preparedArgs(entity)));
    }


}
