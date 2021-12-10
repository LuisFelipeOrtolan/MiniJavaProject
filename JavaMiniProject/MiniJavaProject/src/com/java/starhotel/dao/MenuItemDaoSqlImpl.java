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

    public MenuItemDaoSqlImpl() {
        addMenuItem(new MenuItem(Long.valueOf("1"), "Sandwich", Float.valueOf("6.99"), true, DateUtil.convertToDate("02/12/2021"), "Lunch", true));
        addMenuItem(new MenuItem(Long.valueOf("2"), "Watermelon", Float.valueOf("11.99"), true, DateUtil.convertToDate("02/12/2021"), "Breakfast", true));
        addMenuItem(new MenuItem(Long.valueOf("3"), "Soup", Float.valueOf("3.99"), true, DateUtil.convertToDate("04/12/2021"), "Dinner", true));
    }
    @Override
    public List<MenuItem> getMenuItemListAdmin() {
        List<MenuItem> result = new ArrayList<MenuItem>();

        String url = "jdbc:mysql://localhost:3306/starhotel";
        String username = "root";
        String password = "root";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);

            String query = "Select * from MenuItem;";
            Statement statement = connection.createStatement();
            ResultSet itemsToday = statement.executeQuery(query);
            
            while(itemsToday.next()) {
                Long id = itemsToday.getLong("id");
                String name = itemsToday.getString("name");
                Float price = itemsToday.getFloat("price");
                Boolean active = itemsToday.getBoolean("active");
                Date dateOfLunch = itemsToday.getDate("dateOfLunch");
                String category = itemsToday.getString("category");
                Boolean freeDelivery = itemsToday.getBoolean("freeDelivery");
                MenuItem item = new MenuItem(id, name, price, active, dateOfLunch, category, freeDelivery);
                result.add(item);
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Something went wrong.");
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public List<MenuItem> getMenuItemListCustomer() {
        List<MenuItem> result = new ArrayList<MenuItem>();

        String url = "jdbc:mysql://localhost:3306/starhotel";
        String username = "root";
        String password = "root";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);

            String query = "Select * from MenuItem where dateOfLunch = (?);";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDate(1, new Date(Calendar.getInstance().getTime().getTime()));
            ResultSet itemsToday = statement.executeQuery();
            
            while(itemsToday.next()) {
                Long id = itemsToday.getLong("id");
                String name = itemsToday.getString("name");
                Float price = itemsToday.getFloat("price");
                Boolean active = itemsToday.getBoolean("active");
                Date dateOfLunch = itemsToday.getDate("dateOfLunch");
                String category = itemsToday.getString("category");
                Boolean freeDelivery = itemsToday.getBoolean("freeDelivery");
                MenuItem item = new MenuItem(id, name, price, active, dateOfLunch, category, freeDelivery);
                result.add(item);
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Something went wrong.");
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void modifyMenuItem(MenuItem menuItem) {
        String url = "jdbc:mysql://localhost:3306/starhotel";
        String username = "root";
        String password = "root";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);

            String query = "Update MenuItem set name = ?, price = ?, active = ?, dateOfLunch = ?, category = ?, freeDelivery = ? where id = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, menuItem.getName());
            statement.setFloat(2, menuItem.getPrice());
            statement.setBoolean(3, menuItem.isActive());
            java.sql.Date d = new java.sql.Date(menuItem.getDateOfLunch().getTime());
            statement.setDate(4, d);
            statement.setString(5, menuItem.getCategory());
            statement.setBoolean(6, menuItem.isFreeDelivery());
            statement.setLong(7, menuItem.getId());
            
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Something went wrong.");
            e.printStackTrace();
        }
    }

    @Override
    public MenuItem getMenuItem(Long menuItemId) {
        String url = "jdbc:mysql://localhost:3306/starhotel";
        String username = "root";
        String password = "root";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);

            String query = "Select * from MenuItem where id = (?);";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, menuItemId);
            ResultSet result = statement.executeQuery();

            while(result.next()) {
                String name = result.getString("name");
                Float price = result.getFloat("price");
                Boolean active = result.getBoolean("active");
                Date dateOfLunch = result.getDate("dateOfLunch");
                String category = result.getString("category");
                Boolean freeDelivery = result.getBoolean("freeDelivery");
                MenuItem item = new MenuItem(menuItemId, name, price, active, dateOfLunch, category, freeDelivery);
                return item;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addMenuItem(MenuItem item) {
        String url = "jdbc:mysql://localhost:3306/starhotel";
        String username = "root";
        String password = "root";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);

            String query = "Insert into MenuItem values (? , ?, ?, ?, ?, ?, ?);";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, item.getId());
            statement.setString(2, item.getName());
            statement.setFloat(3, item.getPrice());
            statement.setBoolean(4, item.isActive());
            java.sql.Date d = new java.sql.Date(item.getDateOfLunch().getTime());
            statement.setDate(5, d);
            statement.setString(6, item.getCategory());
            statement.setBoolean(7, item.isFreeDelivery());
            
            statement.executeUpdate();  
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Something went wrong.");
            e.printStackTrace();
        } 
    }
    
}
