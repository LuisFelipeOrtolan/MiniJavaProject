package com.java.starhotel.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.java.starhotel.model.MenuItem;

public class CartDaoSqlImpl implements CartDao {
    
    /*  Constructor.
        Input: 
        Output: a new CartDaoSqlImpl instance.   */
    public CartDaoSqlImpl() { 
    }

    /* Function that adds an item to a customer's cart.
        Input: A long with the user's id and a long with the item's id.
        Output: An item added to the user's cart.   */
    public void addCartItem(Long userId, Long menuItemId) {
        String url = "jdbc:mysql://localhost:3306/starhotel";
        String username = "root";
        String password = "root";

        try {
            Connection connection = DriverManager.getConnection(url, username, password); // Create the connection with the MySql.

            Boolean alreadyBought = false; // Variable that tells if an item is current on the user's cart.
            try {
                List<MenuItem> currentItems = getAllCartItems(userId); // Get all the items in the customer's cart.
                for(MenuItem item : currentItems) { // Iterate through all of them.
                    if(item.getId().equals(menuItemId)) { // If the id we are looking for is the current id,
                        alreadyBought = true; // The item is on the customer's cart.
                        break; // Stop the search.
                    }
                }
            } catch(Exception e) { // If the cart is empty, it will throw an exception, but there is nothing to do, since we only want to add an item.

            }

            if(!alreadyBought) { // If the item is not already in the customer's list.
                String query = "Select * from MenuItem where id = (?)"; // Get the item from the menu.
                PreparedStatement statement = connection.prepareStatement(query); 
                statement.setLong(1, menuItemId); // Set the query.
                ResultSet result = statement.executeQuery(); // Execute the query.
                Boolean exists = false; // Variable that tells if an item exists or not.
                
                while(result.next()) // If the query found an item, it exists!
                    exists = true;
                
                if(!exists) { // If the item doesn't exists, then don't add anything.
                    System.out.println("Item doesn't exist.");
                    statement.close();
                    connection.close();
                    return;
                }
                // If exists, insert a new line with the menuItemId, userId and 1, since before the amount was 0.
                query = "Insert into Carts values (? , ?, 1);";
                statement = connection.prepareStatement(query);
                statement.setLong(1, menuItemId);
                statement.setLong(2, userId);
                statement.executeUpdate();  
                statement.close();
                connection.close();
            } 
            else { // If the item is already on the list, 
                updateAmount(userId, menuItemId, true); // We just need to increase the amount by 1.
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong.");
            e.printStackTrace();
        } 
    }

    /*  Function that returns all the items in a customer's cart.
        Input: A long with the user's id.
        Output: A list of all the Menu Items bought by the customer.    
        Obs: Throws an exception if the cart is empty.*/
    public List<MenuItem> getAllCartItems(Long userId) throws CartEmptyException {
        List<MenuItem> resultList = new ArrayList<MenuItem>();
        
        String url = "jdbc:mysql://localhost:3306/starhotel";
        String username = "root";
        String password = "root";

        try {
            Connection connection = DriverManager.getConnection(url, username, password); // Create the connection with the MySql.

            String query = "Select * from Carts where idBuyer = (?);"; // Select all items from the customer's cart.  
            PreparedStatement statement = connection.prepareStatement(query); 
            statement.setLong(1, userId); // Set Id.
            ResultSet result = statement.executeQuery(); // Get results.
            
            int size = 0; // Variable that show the size of the cart.
            while(result.next()) { // For every line in the database.
                size++; // For every item, increase the size.
                Long menuItemId = result.getLong("idItem"); 
                String localQuery = "Select * from MenuItem where id = (?);"; // Get the info from the item.
                PreparedStatement statementLocal = connection.prepareStatement(localQuery); 
                statementLocal.setLong(1, menuItemId);
                ResultSet resultLocal = statementLocal.executeQuery(); // Execute the query.
                while(resultLocal.next()) { // Get the item's data.
                    String name = resultLocal.getString("name");
                    Float price = resultLocal.getFloat("price");
                    Boolean active = resultLocal.getBoolean("active");
                    Date dateOfLunch = resultLocal.getDate("dateOfLunch");
                    String category = resultLocal.getString("category");
                    Boolean freeDelivery = resultLocal.getBoolean("freeDelivery");
                    MenuItem item = new MenuItem(menuItemId, name, price, active, dateOfLunch, category, freeDelivery);
                    Integer amount = result.getInt("amount"); // Check how many times an item was added in the cart.
                    while(amount > 0) { // Add the items how many times it was in the cart.
                        resultList.add(item);
                        amount--;
                    }
                }

            }

            if(size == 0){ // If the cart was empty, throw exception.
                throw new CartEmptyException();
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Something went wrong.");
            e.printStackTrace();
        }

        return resultList;
    }

    /* Function that removes an item from a customer's cart.
        Input: A long with the user's id and a long with the item's id.
        Output: An item removed from the user's cart.   */
    public void removeCartItem(Long userId, Long menuItemId) {
        String url = "jdbc:mysql://localhost:3306/starhotel";
        String username = "root";
        String password = "root";

        try {
            Connection connection = DriverManager.getConnection(url, username, password); // Create the connection.

            // Check how many times an item is on the cart.
            Integer count = 0; 
            try {
                List<MenuItem> currentItems = getAllCartItems(userId);
                for(MenuItem item : currentItems) { 
                    if(item.getId().equals(menuItemId))
                        count++;
                }
            } catch(Exception e) {

            }
            if(count == 0)
                return;
            // If the item amount is 1, we need to delete the row.
            if(count == 1) {
                String query = "Delete from Carts where (idItem = (?) and idBuyer = (?));";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setLong(1, menuItemId);
                statement.setLong(2, userId);
                statement.executeUpdate();
                statement.close();
                connection.close();
                return;
            }
            else { // If the item amount is greater than 1, we just need to update the amount.
                updateAmount(userId, menuItemId, false);
                connection.close();
                return;
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong.");
            e.printStackTrace();
        } 
    }

    /*  Function that calculates the total value of a customer's cart.
        Input: A long number with the customer's id.
        Output: A double with the total cost of the customer's cart.    */
    public Double getTotal(Long idBuyer) {
        String url = "jdbc:mysql://localhost:3306/starhotel";
        String username = "root";
        String password = "root";

        try {
            Connection connection = DriverManager.getConnection(url, username, password); // Initiate connection.
            String query = "select sum(amount*price) as gasto from Carts, MenuItem where idItem = id and idBuyer = (?);"; // Get the total value.
            PreparedStatement statement = connection.prepareStatement(query); 
            statement.setLong(1, idBuyer);
            ResultSet result = statement.executeQuery(); // Execute the query.
            Double total = Double.parseDouble("0");
            
            while(result.next()) {
                total = result.getDouble("gasto");
            }

            statement.close();
            connection.close();

            return total; 
        } catch (SQLException e) {
            System.out.println("Something went wrong.");
            e.printStackTrace();
        } 
        
        return null;
    }

    /*  Function that updates the "Amount" column in SQL.
        Input: A long number with customer's id, a long number with id from the item that will be updated and a boolean (true indicates +1 false indicates -1).
        Output: The Carts table on SQL updated.  */
    public void updateAmount(Long idBuyer, Long idItem, Boolean increase) {
        String url = "jdbc:mysql://localhost:3306/starhotel";
        String username = "root";
        String password = "root";

        try {
            Connection connection = DriverManager.getConnection(url, username, password); // Start connection with bank.

            String query = "Select amount from Carts where idBuyer = ? and idItem = ?;"; // Select the current amount for that buyer and id.
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, idBuyer);
            statement.setLong(2, idItem);
            ResultSet result = statement.executeQuery();  // Execute the query.

            Integer amount = 0;
            while(result.next()){
                amount = result.getInt("amount"); // Get the current amount.
            }

            query = "Update Carts set amount = ? where idItem = ? and idBuyer = ?;"; // Update the amount with the new value.
            statement = connection.prepareStatement(query);

            if(increase) // We want one more item.
                statement.setInt(1,(amount+1));
            else // We want one less item.
                statement.setInt(1,(amount-1));
            statement.setLong(2, idItem);
            statement.setLong(3, idBuyer);
            
            statement.executeUpdate(); // Execute query.
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Something went wrong.");
            e.printStackTrace();
        }
    }
}
