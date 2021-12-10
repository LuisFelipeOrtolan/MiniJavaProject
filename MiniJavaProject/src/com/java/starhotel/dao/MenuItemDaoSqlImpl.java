package com.java.starhotel.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.java.starhotel.model.MenuItem;
import com.java.starhotel.util.DateUtil;

public class MenuItemDaoSqlImpl implements MenuItemDao {

    /*  Constructor.
        Input:
        Output: menuItemList created if not already and a new instance of MenuItemDaoCollectionImpl. */
    public MenuItemDaoSqlImpl() {
        if(getMenuItemListAdmin().isEmpty()) {
            addMenuItem(new MenuItem(Long.valueOf("1"), "Sandwich", Float.valueOf("6.99"), true, DateUtil.convertToDate("02/12/2021"), "Lunch", true));
            addMenuItem(new MenuItem(Long.valueOf("2"), "Watermelon", Float.valueOf("11.99"), true, DateUtil.convertToDate("02/12/2021"), "Breakfast", true));
            addMenuItem(new MenuItem(Long.valueOf("3"), "Soup", Float.valueOf("3.99"), true, DateUtil.convertToDate("04/12/2021"), "Dinner", true));
        }
    }
    
    /*  Get all the items on the menu for the admin.
        Input: 
        Output: All items on the menu.  */
    public List<MenuItem> getMenuItemListAdmin() {
        List<MenuItem> result = new ArrayList<MenuItem>();

        String url = "jdbc:mysql://localhost:3306/starhotel";
        String username = "root";
        String password = "root";

        try {
            Connection connection = DriverManager.getConnection(url, username, password); // Initiate the connection.

            String query = "Select * from MenuItem;"; // Get all items from the menu.
            Statement statement = connection.createStatement();
            ResultSet itemsToday = statement.executeQuery(query); // Execute the query.
            
            while(itemsToday.next()) { // For every item returned,
                // Get the info.
                Long id = itemsToday.getLong("id");
                String name = itemsToday.getString("name");
                Float price = itemsToday.getFloat("price");
                Boolean active = itemsToday.getBoolean("active");
                Date dateOfLunch = itemsToday.getDate("dateOfLunch");
                String category = itemsToday.getString("category");
                Boolean freeDelivery = itemsToday.getBoolean("freeDelivery");
                MenuItem item = new MenuItem(id, name, price, active, dateOfLunch, category, freeDelivery);
                result.add(item); // Insert the item in the list.
            }
            statement.close();
            connection.close(); // End connection with database.
        } catch (SQLException e) {
            System.out.println("Something went wrong.");
            e.printStackTrace();
        }

        return result; // Return the list.
    }

    /*  Get all the items from today's menu. Customer can only access the items from today's menu,
        Input: 
        Output: A list with all the items on today's menu. */
    public List<MenuItem> getMenuItemListCustomer() {
        List<MenuItem> result = new ArrayList<MenuItem>();

        String url = "jdbc:mysql://localhost:3306/starhotel";
        String username = "root";
        String password = "root";

        try {
            Connection connection = DriverManager.getConnection(url, username, password); // Initiate connection with database.

            String query = "Select * from MenuItem where dateOfLunch = ? and active = ?;"; // Select all items from the menu that are for today and are active.
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDate(1, new Date(Calendar.getInstance().getTime().getTime()));
            statement.setBoolean(2, true);
            ResultSet itemsToday = statement.executeQuery(); // Execute the query.
            
            while(itemsToday.next()) { // For every row in the result,
                // Get the item's info.
                Long id = itemsToday.getLong("id");
                String name = itemsToday.getString("name");
                Float price = itemsToday.getFloat("price");
                Boolean active = itemsToday.getBoolean("active");
                Date dateOfLunch = itemsToday.getDate("dateOfLunch");
                String category = itemsToday.getString("category");
                Boolean freeDelivery = itemsToday.getBoolean("freeDelivery");
                MenuItem item = new MenuItem(id, name, price, active, dateOfLunch, category, freeDelivery);
                result.add(item); // Add to the list.
            }
            statement.close();
            connection.close(); // Close the connection.
        } catch (SQLException e) {
            System.out.println("Something went wrong.");
            e.printStackTrace();
        }

        return result; // Return the result.
    }

    /*  Function that modifies an item on the menu.
        Input: A menuItem.
        Output: The menu item on the menu is updated with the new info. */
    public void modifyMenuItem(MenuItem menuItem) {
        String url = "jdbc:mysql://localhost:3306/starhotel";
        String username = "root";
        String password = "root";

        try {
            Connection connection = DriverManager.getConnection(url, username, password); // Initiate connection with database.

            String query = "Update MenuItem set name = ?, price = ?, active = ?, dateOfLunch = ?, category = ?, freeDelivery = ? where id = ?;"; // Update the item.
            PreparedStatement statement = connection.prepareStatement(query);
            // Set info from the menuItem recieved.
            statement.setString(1, menuItem.getName());
            statement.setFloat(2, menuItem.getPrice());
            statement.setBoolean(3, menuItem.isActive());
            java.sql.Date d = new java.sql.Date(menuItem.getDateOfLunch().getTime());
            statement.setDate(4, d);
            statement.setString(5, menuItem.getCategory());
            statement.setBoolean(6, menuItem.isFreeDelivery());
            statement.setLong(7, menuItem.getId());
            
            statement.executeUpdate(); // Execute query.
            statement.close();
            connection.close(); // Close connection with database.
        } catch (SQLException e) {
            System.out.println("Something went wrong.");
            e.printStackTrace();
        }
    }

    /*  Function that gets an item from the menu.
        Input: A long number with the item's id.
        Output: The item or null if the item doesn't exist. */
    public MenuItem getMenuItem(Long menuItemId) {
        String url = "jdbc:mysql://localhost:3306/starhotel";
        String username = "root";
        String password = "root";

        try {
            Connection connection = DriverManager.getConnection(url, username, password); // Initiate connection with database;

            String query = "Select * from MenuItem where id = (?);"; // Get the item with the items's id.
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, menuItemId);
            ResultSet result = statement.executeQuery(); // Execute query.

            while(result.next()) { // If it is found,
                // Get the info's from the database.
                String name = result.getString("name");
                Float price = result.getFloat("price");
                Boolean active = result.getBoolean("active");
                Date dateOfLunch = result.getDate("dateOfLunch");
                String category = result.getString("category");
                Boolean freeDelivery = result.getBoolean("freeDelivery");
                MenuItem item = new MenuItem(menuItemId, name, price, active, dateOfLunch, category, freeDelivery);
                statement.close();
                connection.close();
                return item; // Return the item with all the information.
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // If the item was not found, return null;
    }

    /*  Function that add a new item to the menu.
        Input: A menuItem.
        Output: The menu has now a new item.    */
    public void addMenuItem(MenuItem item) {
        String url = "jdbc:mysql://localhost:3306/starhotel";
        String username = "root";
        String password = "root";

        try {
            Connection connection = DriverManager.getConnection(url, username, password); // Initiate the connection with the database.

            String query = "Insert into MenuItem values (? , ?, ?, ?, ?, ?, ?);"; // Insert the new item in the menu.
            PreparedStatement statement = connection.prepareStatement(query);
            // Set the info from the item.
            statement.setLong(1, item.getId());
            statement.setString(2, item.getName());
            statement.setFloat(3, item.getPrice());
            statement.setBoolean(4, item.isActive());
            java.sql.Date d = new java.sql.Date(item.getDateOfLunch().getTime());
            statement.setDate(5, d);
            statement.setString(6, item.getCategory());
            statement.setBoolean(7, item.isFreeDelivery());
            
            statement.executeUpdate(); // Execute query.
            statement.close();
            connection.close(); // Close connection with the database.
        } catch (SQLException e) {
            System.out.println("Something went wrong.");
            e.printStackTrace();
        } 
    }
    
}
