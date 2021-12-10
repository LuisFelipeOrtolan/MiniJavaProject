package com.java;

import java.util.List;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Date;

import com.java.starhotel.dao.MenuItemDaoCollectionImpl;
import com.java.starhotel.dao.MenuItemDaoSqlImpl;
import com.java.starhotel.model.MenuItem;
import com.java.starhotel.util.DateUtil;
import com.java.starhotel.dao.CartDaoCollectionImpl;
import com.java.starhotel.dao.CartDaoSqlImpl;
import com.java.starhotel.dao.CartEmptyException;

public class Class {

    private static Scanner s = new Scanner(System.in);
    private static MenuItemDaoCollectionImpl menuDao = new MenuItemDaoCollectionImpl();
    private static MenuItemDaoSqlImpl menuDaoSql = new MenuItemDaoSqlImpl();
    private static CartDaoCollectionImpl cartDao = new CartDaoCollectionImpl();
    private static CartDaoSqlImpl cartDaoSql = new CartDaoSqlImpl();
    private static Long currentId = Long.valueOf(menuDao.getMenuItemList().size()) + 1;

    public static void main(String args[]) throws CartEmptyException{
        String nextLine;

        System.out.println("Welcome to your Star-Hotel manage system.");
        System.out.println("Are you an admin or an user?");
    
        nextLine = s.nextLine();
        
        while(!nextLine.equals("exit")){
            Boolean admin = false;
            if(nextLine.equalsIgnoreCase("admin"))
                admin = true;

            if(admin)
                while(displayMenuAdmin());
            else{
                System.out.println("What is your id?");
                Long myId = Long.parseLong(s.nextLine());
                while(displayMenuUser(myId));
            }

            System.out.println("Welcome to your Star-Hotel manage system.");
            System.out.println("Are you an admin or an user?");
            
            nextLine = s.nextLine();
        }

        String url = "jdbc:mysql://localhost:3306/starhotel";
        String username = "root";
        String password = "root";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            String query = "delete from Carts;";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            query = "delete from MenuItem;";
            statement = connection.createStatement();
            statement.executeUpdate(query);
            statement.close();
            connection.close();
        } catch (Exception e) {

        }
    }

    public static boolean displayMenuAdmin() throws CartEmptyException {
        System.out.println("What do you want to do?");
        System.out.println("1. View Menu's item list.");
        System.out.println("2. Modify an item in the menu.");
        System.out.println("3. Add a new item to the menu.");
        System.out.println("4. Add an item to a customer's cart.");
        System.out.println("5. Get all items from a customer's cart.");
        System.out.println("6. Remove an item from a customer's cart.");
        System.out.println("7. Exit");
        System.out.println("Please type the number that corresponds to the action you want to do.");

        Integer option = null;
        
        while(option == null) {
            try{
                option = Integer.parseInt(s.nextLine());
            } catch(Exception e) {
                System.out.println("Please, digit an integer.");
            }
        }
        switch(option) {
            case 1:
                System.out.println("Collection: ");
                List<MenuItem> menuList = menuDao.getMenuItemListAdmin();
                for(MenuItem item : menuList)
                    System.out.println(item);
                System.out.println("SQL: ");
                menuList = menuDaoSql.getMenuItemListAdmin();
                for(MenuItem item : menuList)
                    System.out.println(item);
                break;
            case 2:
                System.out.println("What's the id for the item?");
                Long id = null;
                while(id == null) {
                    try {
                        id = Long.parseLong(s.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Please, digit a long number.");
                    }
                }
                MenuItem item = menuDao.getMenuItem(id);
                if(item != null) {
                    System.out.println("Current name is " + item.getName() + " what is the new name?");
                    item.setName(s.nextLine());
                    System.out.println("Current price is " + item.getPrice() + " what is the new price? (Ex. 6.99)");
                    Float price = null;
                    while(price == null) {
                        try {
                            price = Float.parseFloat(s.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Please, digit a float number.");
                        }
                    }
                    item.setPrice(price);
                    System.out.print("Current, the item is ");
                    if(!item.isActive())
                        System.out.print("not ");
                    System.out.println("active. Now, it is active, true or false?");
                    item.setActive(Boolean.parseBoolean(s.nextLine()));
                    System.out.println("Current Date for the item is " + item.getDateOfLunch() + " what is the new date? (dd/MM/yyy)");
                    item.setDateOfLunch(DateUtil.convertToDate(s.nextLine()));
                    System.out.println("Current category for the item is " + item.getCategory() + " what is the new category?");
                    item.setCategory(s.nextLine());
                    System.out.print("Current, the item is ");
                    if(!item.isFreeDelivery())
                        System.out.print(" not ");
                    System.out.println("free delivery. Now, it has free delivery, true or false?");
                    item.setFreeDelivery(Boolean.parseBoolean(s.nextLine()));
                    menuDao.modifyMenuItem(item);
                    menuDaoSql.modifyMenuItem(item);
                }
                break;
            case 3:
                System.out.println("What is the item's name?");
                String name = s.nextLine();
                System.out.println("What is the item's price? (Ex. 6.99)");
                Float price = null;
                while(price == null) {
                    try {
                        price = Float.parseFloat(s.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Please, digit a float number.");
                    }
                }
                System.out.println("The item is active, true or false?");
                Boolean active = Boolean.parseBoolean(s.nextLine());
                System.out.println("What is the item's date? (dd/MM/yyy)");
                Date dateOfLunch = DateUtil.convertToDate(s.nextLine());
                System.out.println("What is the item's category?");
                String category = s.nextLine();
                System.out.println("The item has free delivery, true or false?");
                Boolean freeDelivery = Boolean.parseBoolean(s.nextLine());
                MenuItem newItem = new MenuItem(currentId++, name, price, active, dateOfLunch, category, freeDelivery);
                menuDao.addMenuItem(newItem);
                menuDaoSql.addMenuItem(newItem);
                break;
            case 4:
                System.out.println("What is the Id of the item?");
                Long itemId = null;
                while(itemId == null) {
                    try {
                        itemId = Long.parseLong(s.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Please, digit a long number.");
                    }
                }
                System.out.println("What is the id of the customer?");
                Long userId = null;
                while(userId == null) {
                    try {
                        userId = Long.parseLong(s.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Please, digit a long number");
                    }
                }
                cartDao.addCartItem(userId, itemId);
                cartDaoSql.addCartItem(userId, itemId);
                break;
            case 5:
                System.out.println("What is the id of the customer?");
                Long idC = null;
                while(idC == null) {
                    try {
                        idC = Long.parseLong(s.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Please, digit a long number.");
                    }
                }
                System.out.println("Collection: ");
                List<MenuItem> itemsFromCustomer = cartDao.getAllCartItems(idC);
                for(MenuItem it : itemsFromCustomer)
                    System.out.println(it);
                System.out.println("Total: " + String.format("%.2f", cartDao.getCart(idC).getTotal()));

                System.out.println("SQL: ");
                itemsFromCustomer = cartDaoSql.getAllCartItems(idC);
                for(MenuItem it : itemsFromCustomer)
                    System.out.println(it);
                System.out.println("Total: " + String.format("%.2f",cartDaoSql.getTotal(idC)));

                break;
            case 6:
                System.out.println("What is the Id of the item?");
                Long itId = null;
                while(itId == null) {
                    try {
                        itId = Long.parseLong(s.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Please, digit a long number");
                    }
                }
                System.out.println("What is the id of the customer?");
                Long usId = null;
                while(usId == null) { 
                    try { 
                        usId = Long.parseLong(s.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Please, digit a long number.");
                    }
                }
                cartDao.removeCartItem(usId, itId);
                cartDaoSql.removeCartItem(usId, itId);
                break;
            case 7:
                return false;
        }
        return true;
    }

    public static Boolean displayMenuUser(Long myId) throws CartEmptyException {
        System.out.println("What do you want to do?");
        System.out.println("1. View Menu's item list.");
        System.out.println("2. Add an item to your cart.");
        System.out.println("3. Get all items from your cart.");
        System.out.println("4. Exit");
        System.out.println("Please type the number that corresponds to the action you want to do.");
        
        Integer option = null;
        
        while(option == null) {
            try{
                option = Integer.parseInt(s.nextLine());
            } catch(Exception e) {
                System.out.println("Please, digit an integer.");
            }
        }
        switch(option) {
            case 1:
                System.out.println("Collection: ");
                List<MenuItem> menuList = menuDao.getMenuItemListCustomer();
                for(MenuItem item : menuList)
                    System.out.println(item);
                
                System.out.println("SQL: ");
                menuList = menuDaoSql.getMenuItemListCustomer();
                for(MenuItem item : menuList)
                    System.out.println(item);
                break;
            case 2:
                System.out.println("What is the Id of the item?");
                Long itemId = null;
                while(itemId == null) {
                    try {
                        itemId = Long.parseLong(s.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Please, digit a long number.");
                    }
                }
                cartDao.addCartItem(myId, itemId);
                cartDaoSql.addCartItem(myId, itemId);
                break;
            case 3:
                System.out.println("Collection: ");
                List<MenuItem> items = cartDao.getAllCartItems(myId);
                if(items != null) { 
                    for(MenuItem item : items) 
                        System.out.println(item);
                    System.out.println("Total: " + String.format("%.2f",cartDao.getCart(myId).getTotal()));
                }

                System.out.println("SQL: ");
                items = cartDaoSql.getAllCartItems(myId);
                for(MenuItem item : items) 
                    System.out.println(item);
                System.out.println("Total: " + String.format("%.2f", cartDaoSql.getTotal(myId)));

                break;
            case 4:
                return false;
        }
        return true;
    }
}
