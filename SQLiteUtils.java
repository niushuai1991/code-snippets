/**
  * -------------------------------------------------------------------------
  * (C) Copyright Gyyx Tec Corp. 1996-2017 - All Rights Reserved
  * @版权所有：北京光宇在线科技有限责任公司
  * @项目名称：check-role
  * @作者：niushuai
  * @联系方式：niushuai@gyyx.cn
  * @创建时间：2017年3月31日 下午6:14:36
  * @版本号：0.0.1
  *-------------------------------------------------------------------------
  */
package sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
  * <p>
  *   SQLiteUtils描述
  * </p>
  *  
  * @author niushuai
  * @since 0.0.1
  */
public class SQLiteUtils {
    
    private static final String DRIVER_NAME = "org.sqlite.JDBC";
    private final String url;
    
    public SQLiteUtils(String url) throws ClassNotFoundException{
        Class.forName(DRIVER_NAME);
        this.url = url;
    }
    
    /**
      * <p>
      *    插入一条数据
      * </p>
      *
      * @action
      *    niushuai 2017年3月31日 下午6:22:39 描述
      *
      * @param sql
      * @return
      * @throws SQLException int
     */
    public int executeUpdate(String sql) throws SQLException{
        Connection conn = DriverManager.getConnection(url);
        conn.setAutoCommit(true);
        try(Statement stmt = conn.createStatement()) {
            return stmt.executeUpdate(sql);
        }
    }
    
    public ResultSet executeQuery(String sql) throws SQLException {
        Connection conn = DriverManager.getConnection(url);
        conn.setAutoCommit(true);
        try(Statement stmt = conn.createStatement()) {
            return stmt.executeQuery(sql);
        }
    }
    
    /**
      * <p>
      * 获取statement (自动提交)
      * </p>
      *
      * @action
      *    niushuai 2017年3月31日 下午7:23:00 描述
      *
      * @param sql
      * @return
      * @throws SQLException PreparedStatement
     */
    public PreparedStatement getPreparedStatement(String sql) throws SQLException{
        return getPreparedStatement(sql,true);
    }
    
    /**
      * <p>
      *  获取statement
      * </p>
      *
      * @action
      *    niushuai 2017年3月31日 下午7:23:27 描述
      *
      * @param sql
      * @param autoCommit
      * @return
      * @throws SQLException PreparedStatement
     */
    public PreparedStatement getPreparedStatement(String sql,boolean autoCommit) throws SQLException{
        Connection conn = DriverManager.getConnection(url);
        conn.setAutoCommit(autoCommit);
        return conn.prepareStatement(sql);
    }
}
