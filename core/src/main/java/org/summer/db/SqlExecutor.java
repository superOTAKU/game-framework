package org.summer.db;

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

    /**
     * 单纯查对象
     */
    public <T> List<T> executeQuery(Class<T> entityType, String whereClause, Object[] args) throws SQLException {
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        try {
            ClassMeta classMeta = getClassMeta(entityType);

            pStmt = writeConn.prepareStatement(null);
            for (int i = 1; i <= args.length; i++) {
                pStmt.setObject(i, args[i]);
            }
            rs = pStmt.executeQuery();
            while(rs.next()) {

            }
        } finally {

        }
        return null;
    }

    private ClassMeta getClassMeta(Class<?> clazz) {
        return classMetaCache.computeIfAbsent(clazz, clz -> {
            ClassMeta meta = new ClassMeta();
            meta.setName(clazz.getSimpleName().substring(0, 1).toLowerCase() + clazz.getSimpleName().substring(1));
            List<String> fieldNames = new ArrayList<>();
            for (var method : clz.getMethods()) {
                fieldNames.add(method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4));
            }
            meta.setFieldNames(fieldNames);
            //生成select sql，除了where之外
            return meta;
        });
    }


}


