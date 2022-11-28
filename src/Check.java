import java.sql.*;
 
public class Check {
    public static void main(String arg[])
    {
        Connection connection = null;
        try {
            // below two lines are used for connectivity.
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/mydb",
                "root", "Maanu1107%");
 
            // mydb is database
            // mydbuser is name of database
            // mydbuser is password of database
 
            Statement statement;
            statement = connection.createStatement();
            ResultSet resultSet, resultSet1;
            resultSet = statement.executeQuery(
                "select * from FRUITS");
            // resultSet1=statement.executeQuery("update FRUITS set fruit_quantity_avl=3 where fruit_index=0");
            int index;
            String name;
            int quantity;
            while (resultSet.next()) {
                index = resultSet.getInt("fruit_index");
                name = resultSet.getString("fruit_name").trim();
                quantity = resultSet.getInt("fruit_quantity_avl");
                System.out.println("FRUIT INDEX : " + index
                                   + " FRUIT NAME : " + name
                                   + " FRUIT QUANTITY: "+quantity);
            }
            
             // create the java mysql update preparedstatement
            String query = "update FRUITS set fruit_quantity_avl=? where fruit_index=?";
      PreparedStatement preparedStmt = connection.prepareStatement(query);
      preparedStmt.setInt(1,3);
      preparedStmt.setInt(2,0);
      // execute the java preparedstatement
      preparedStmt.executeUpdate();

            resultSet.close();
            statement.close();
            connection.close();
        }
        catch (Exception exception) {
            System.out.println(exception);
        }
    } // function ends
} // class ends