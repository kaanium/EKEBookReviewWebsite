package ekebookreview;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBConnection {

    private static String dbURL = "jdbc:derby://localhost:1527/BookSiteDatabase;user=admin;password=admin";
    public Connection conn;
    public Statement stmt;

    public DBConnection() {
        conn = createConnection();
    }

    private static Connection createConnection() {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            //Get a connection
            return DriverManager.getConnection(dbURL);
        } catch (Exception except) {
            except.printStackTrace();
            return null;
        }
    }

    public static List<Map<String, Object>> executeQuery(String query) {
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        Map<String, Object> row = null;

        try {
            Connection conn = createConnection();
            Statement stmt;
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                row = new HashMap<String, Object>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), rs.getObject(i));
                }
                resultList.add(row);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
            System.out.println(sqlExcept.toString());
        }
        return resultList;
    }

    public static void execute(String str) {
        try {
            Connection conn = createConnection();
            Statement stmt;
            stmt = conn.createStatement();
            stmt.execute(str);

            stmt.close();
            conn.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
    }

    public static int executeUpdate(String str) {
        int key = 0;
        try {
            Connection conn = createConnection();
            Statement stmt;
            stmt = conn.createStatement();
            stmt.executeUpdate(str, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                key = rs.getInt(1); //this is the auto-generated key for your use
            }
            stmt.close();
            conn.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return key;
    }
}
