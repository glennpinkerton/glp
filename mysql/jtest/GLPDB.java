package mysql.jtest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

// Notice, do not import com.mysql.jdbc.*
// or you will have problems!

public class GLPDB {

    public static Connection glpConnect () throws Throwable {

      try {

          // The following note was on a tutorial site.  I
          // take it at face value and it seems to work this way,
          //
          // The newInstance() call is a work around for some
          // broken Java implementations

          Class.forName("com.mysql.jdbc.Driver").newInstance();

      }
      catch (Exception ex) {
          System.out.println ("Exception in LoadDriver main Method");
          throw (ex);
      }

      Connection  conn = null;

      try {
        conn =
           DriverManager.getConnection(
              "jdbc:mysql://localhost/glp?" +
              "user=glenn&password=Dog4-J3ssi3&useSSL=false");
      }
      catch (SQLException ex) {
        System.out.println ("Exception in getConnection stuff.");
        throw (ex);
      }
      finally {
        return conn;
      }
    }


    Connection  conn = null;

    GLPDB (Connection myconn) throws Throwable {
        if (myconn == null) {
            throw (new Exception ("Cannot use null connection."));
        }
        conn = myconn;
        doStuff ();
    }


    private void doStuff ()
    {
        Statement stmt = null;
        ResultSet rs = null;

        String sq = "select * from objects " +
                    "where spindex > 100";
        try {
            stmt = conn.createStatement ();
            rs = stmt.executeQuery (sq);
            getRsData (rs);
        }
        catch (SQLException ex) {
            System.out.println ();
            System.out.println ("SQLException: " + ex.getMessage());
            System.out.println ("SQLState: " + ex.getSQLState());
            System.out.println ("VendorError: " + ex.getErrorCode());
            System.out.println ();
        }
        finally {
            try {
              rs.close ();
              rs = null;
            }
            catch (SQLException exrs) {
            }
            try {
              stmt.close ();
              stmt = null;
            }
            catch (SQLException exstmt) {
            }
        }
    }

    private void getRsData (ResultSet rs)
    {

      int        oid = 0;
      double     xmin, ymin, xmax, ymax;

      try {
        rs.first ();

    // Do the first line before nexting the row cursor

        oid = rs.getInt ("object_id");
        xmin = rs.getDouble ("xmin");
        ymin = rs.getDouble ("ymin");
        xmax = rs.getDouble ("xmax");
        ymax = rs.getDouble ("ymax");
        processObj (oid, xmin, ymin, xmax, ymax);

    // Do the rest of the result set.

        while (rs.next ()) {
          oid = rs.getInt ("object_id");
          xmin = rs.getDouble ("xmin");
          ymin = rs.getDouble ("ymin");
          xmax = rs.getDouble ("xmax");
          ymax = rs.getDouble ("ymax");
          processObj (oid, xmin, ymin, xmax, ymax);
        }
      }
      catch (SQLException ex) { }

    }


    private void processObj (int obid,
                             double xmin,
                             double ymin,
                             double xmax,
                             double ymax)
    {
System.out.println ();
System.out.println ("object id: " + obid);
System.out.println ("  xmin: " + xmin + "   ymin: " + ymin);
System.out.println ("  xmax: " + xmax + "   ymax: " + ymax);
System.out.println ();
    }

}
