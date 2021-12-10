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
    public CartDaoSqlImpl() { 
    }

    @Override
    public void addCartItem(Long userId, Long menuItemId) {
        String url = "jdbc:mysql://localhost:3306/starhotel";
        String username = "root";
        String password = "root";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);

            Boolean alreadyBought = false;
            try {
                List<MenuItem> currentItems = getAllCartItems(userId);
                for(MenuItem item : currentItems) {
                    if(item.getId().equals(menuItemId))
                        alreadyBought = true;
                }
            } catch(Exception e) {

            }

            if(!alreadyBought) {
                String query = "Select * from MenuItem where id = (?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setLong(1, menuItemId);
                ResultSet result = statement.executeQuery();
                Boolean exists = false;
                
                while(result.next())
                    exists = true;
                
                if(!exists) {
                    System.out.println("Item doesn't exist.");
                    statement.close();
                    connection.close();
                    return;
                }
                query = "Insert into Carts values (? , ?, 1);";
                statement = connection.prepareStatement(query);
                statement.setLong(1, menuItemId);
                statement.setLong(2, userId);
                statement.executeUpdate();  
                statement.close();
                connection.close();
            } 
            else {
                updateAmount(userId, menuItemId, true);
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong.");
            e.printStackTrace();
        } 
    }

    @Override
    public List<MenuItem> getAllCartItems(Long userId) throws CartEmptyException {
        List<MenuItem> resultList = new ArrayList<MenuItem>();
        
        String url = "jdbc:mysql://localhost:3306/starhotel";
        String username = "root";
        String password = "root";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);

            String query = "Select * from Carts where idBuyer = (?);";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, userId);
            ResultSet result = statement.executeQuery();  
            
            if(result.getFetchSize() == 0) {
                statement.close();
                connection.close();
                throw new CartEmptyException();
            }
            while(result.next()) {
                Long menuItemId = result.getLong("idItem");
                String localQuery = "Select * from MenuItem where id = (?);";
                PreparedStatement statementLocal = connection.prepareStatement(localQuery);
                statementLocal.setLong(1, menuItemId);
                ResultSet resultLocal = statementLocal.executeQuery();
                while(resultLocal.next()) {
                    String name = resultLocal.getString("name");
                    Float price = resultLocal.getFloat("price");
                    Boolean active = resultLocal.getBoolean("active");
                    Date dateOfLunch = resultLocal.getDate("dateOfLunch");
                    String category = resultLocal.getString("category");
                    Boolean freeDelivery = resultLocal.getBoolean("freeDelivery");
                    MenuItem item = new MenuItem(menuItemId, name, price, active, dateOfLunch, category, freeDelivery);
                    Integer amount = result.getInt("amount");
                    while(amount > 0) {
                        resultList.add(item);
                        amount--;
                    }
                }

            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Something went wrong.");
            e.printStackTrace();
        }

        return resultList;
    }

    @Override
    public void removeCartItem(Long userId, Long menuItemId) {
        String url = "jdbc:mysql://localhost:3306/starhotel";
        String username = "root";
        String password = "root";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);

            Integer count = 1;
            try {
                List<MenuItem> currentItems = getAllCartItems(userId);
                for(MenuItem item : currentItems) {
                    if(item.getId().equals(menuItemId))
                        count++;
                }
            } catch(Exception e) {

            }

            if(count == 1) {
                String query = "Delete from Carts where (idItem = (?) and idBuyer = (?));";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setLong(1, menuItemId);
                statement.setLong(2, userId);
                statement.executeUpdate();
                statement.close();
                connection.close();
            }
            else {
                updateAmount(userId, menuItemId, false);
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong.");
            e.printStackTrace();
        } 
    }

    public Double getTotal(Long idBuyer) {
        String url = "jdbc:mysql://localhost:3306/starhotel";
        String username = "root";
        String password = "root";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            String query = "select sum(amount*price) as gasto from Carts, MenuItem where idItem = id and idBuyer = (?);";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, idBuyer);
            ResultSet result = statement.executeQuery();
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

    public void updateAmount(Long idBuyer, Long idItem, Boolean increase) {
        String url = "jdbc:mysql://localhost:3306/starhotel";
        String username = "root";
        String password = "root";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);

            String query = "Select amount from Carts where idBuyer = ? and idItem = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, idBuyer);
            statement.setLong(2, idItem);
            ResultSet result = statement.executeQuery();  

            Integer amount = 0;
            while(result.next()){
                amount = result.getInt("amount");
            }

            query = "Update Carts set amount = ? where idItem = ? and idBuyer = ?;";
            statement = connection.prepareStatement(query);

            if(increase)
                statement.setInt(1,(amount+1));
            else
                statement.setInt(1,(amount-1));
            statement.setLong(2, idItem);
            statement.setLong(3, idBuyer);
            
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Something went wrong.");
            e.printStackTrace();
        }
    }
}
