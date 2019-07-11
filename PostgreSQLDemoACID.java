import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author luisamorales - JDBC for PostgreSQL ACID implemented
 */

public class PostgreSQLDemoACID {

  public static void main(String args[]) throws SQLException, IOException, ClassNotFoundException {

    // Load the PostgreSQL driver
	  Class.forName("org.postgresql.Driver");

    // Connect to the database
    Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost/postgres_test");

    // For atomicity
    conn.setAutoCommit(false);

    // For isolation
    conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

    Statement stmt = null;
    try {
      // create statement object
      stmt = conn.createStatement();

      // delete rows with "p1" as prodid from Stock table
      // must be done first to satisfy foreign key constraint on Stock table
      stmt.executeUpdate("DELETE FROM Stock WHERE prodid = 'p1'");
      // delete row with "p1" as prodid from Product table
      stmt.executeUpdate("DELETE FROM Product WHERE prodid = 'p1'");
    } catch (SQLException e) {
      System.out.println("catch Exception: " + e);
      // For atomicity
      conn.rollback();
      stmt.close();
      conn.close();
      return;
    } // main

    conn.commit(); // atomicity
    stmt.close();
    conn.close();
  }
}