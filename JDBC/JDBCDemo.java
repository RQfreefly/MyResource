/*
1. 功能描述：连接MySQL数据库，用try with resource释放实现AutoCloseable接口的类。

2. MySQL5所需要的Maven依赖：
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.46</version>
        </dependency>

3. MySQL8以上JDBC驱动名及数据库URL：
        String driver = "com.mysql.cj.jdbc.Driver";  
        String url = "jdbc:mysql://localhost:3306/database?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

4. 通过PreparedStatement防止SQL Injection
      对JDBC而言，SQL注入攻击只对Statement有效，对PreparedStatement无效，因为PreparedStatement不允许在插入参数时改变SQL语句的逻辑结构。
      PreparedStatement是Statement的子类，表示预编译的SQL语句的对象。在使用PreparedStatement对象执行SQL命令时，命令被数据库编译和解析，
      并放入命令缓冲区，缓冲区中的预编译SQL命令可以重复使用。
            String sql = "update user set username = ? where id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);

            preparedStatement.setString(1, "test");
            preparedStatement.setInt(2, 1);
*/

import java.sql.*;

public class JDBCDemo {
    public static void main(String[] args) {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/sakila?autoReconnect=true&useSSL=false";
        String userName = "root";
        String password = "000000";
        String sql = "select city_id, city from city where country_id = 87;";
        try (
                Connection connection = DriverManager.getConnection(url, userName, password);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
        ){
            Class.forName(driver);
            while (resultSet.next()) {
                int cityId = resultSet.getInt("city_id");
                String cityName = resultSet.getString("city");
                System.out.println("city_id = " + cityId + ", city = " + cityName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}