package org.summer.util;

import com.google.common.base.CaseFormat;

public class SqlUtil {

    public static String toFieldName(String name) {
        return '`' + CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name) + "`";
    }

    public static String toFieldValue(Object val) {
        if (val == null) {
            return "NULL";
        } else if (val instanceof String) {
            return "'" + ((String) val).replaceAll("'", "''") + "'";
        } else {
            return val.toString();
        }
    }

}
