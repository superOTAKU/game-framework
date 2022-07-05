package org.summer.db;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import org.summer.util.SqlUtil;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 *  单条写连接，异步顺序写sql
 *  读连接配置成连接池
 */
public class SqlExecutor {
    //写连接提供者，当写连接失效，负责重连
    private Supplier<Connection> writeConnSupplier;
    //写连接
    private Connection writeConn;
    //读连接池
    private DataSource readDataSource;

    //prepareStatement缓存，用于batch操作
    private final ConcurrentMap</*sql*/String, /*statement*/PreparedStatement> preparedStatementCache = new ConcurrentHashMap<>();
    //类元信息缓存
    private final ConcurrentMap<Class<?>, ClassMeta> classMetaCache = new ConcurrentHashMap<>();

    public int executeUpdate(String sql, Object[] args) {

        return 0;
    }

    public <T> List<T> executeQuery(Class<T> entityType, String whereClause, Object[] args) throws SQLException {
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        try {
            ClassMeta classMeta = getClassMeta(entityType);
            pStmt = writeConn.prepareStatement(classMeta.getQuerySql() + " " + whereClause);
            for (int i = 1; i <= args.length; i++) {
                pStmt.setObject(i, args[i]);
            }
            rs = pStmt.executeQuery();
            List<T> list = new ArrayList<>();
            while(rs.next()) {
                T obj = ReflectUtil.newInstance(entityType);
                int i = 1;
                for (var setter : classMeta.getSetters()) {
                    ClassUtil.invoke(entityType.getName(), setter.getName(), new Object[] {rs.getObject(i++)});
                }
                list.add(obj);
            }
            return list;
        } finally {
            IoUtil.close(rs);
            IoUtil.close(pStmt);
        }
    }

    private ClassMeta getClassMeta(Class<?> clazz) {
        return classMetaCache.computeIfAbsent(clazz, clz -> {
            ClassMeta meta = new ClassMeta();
            meta.setName(clazz.getSimpleName().substring(0, 1).toLowerCase() + clazz.getSimpleName().substring(1));
            List<String> fieldNames = new ArrayList<>();
            List<Method> setters = new ArrayList<>();
            fieldNames.add("id");
            setters.add(ClassUtil.getPublicMethod(clazz, "setId", Long.class));
            for (var method : clz.getMethods()) {
                if (method.getName().startsWith("set") && !method.getName().equals("setId")) {
                    fieldNames.add(method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4));
                    setters.add(method);
                }
            }
            meta.setFieldNames(fieldNames);
            meta.setSetters(setters);
            //生成select sql，除了where之外
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT `id`,");
            for (var fieldName : fieldNames) {
                sb.append(SqlUtil.toFieldName(fieldName)).append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(" FROM ").append(SqlUtil.toFieldName(meta.getName()));
            meta.setQuerySql(sb.toString());
            return meta;
        });
    }


}


