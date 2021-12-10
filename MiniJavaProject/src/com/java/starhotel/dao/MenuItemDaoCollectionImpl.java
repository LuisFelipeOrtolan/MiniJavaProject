package com.java.starhotel.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.java.starhotel.model.MenuItem;
import com.java.starhotel.util.DateUtil;

public class MenuItemDaoCollectionImpl implements MenuItemDao{
    
    private static List<MenuItem> menuItemList = null; // List with all items on the menu.

    /*  Constructor.
        Input:
        Output: menuItemList created if not already and a new instance of MenuItemDaoCollectionImpl. */
    public MenuItemDaoCollectionImpl(){
        if(getMenuItemList() == null){ // Create the initial list if not created yet.
            menuItemList = new ArrayList<MenuItem>();
            menuItemList.add(new MenuItem(Long.valueOf("1"), "Sandwich", Float.valueOf("6.99"), true, DateUtil.convertToDate("02/12/2021"), "Lunch", true));
            menuItemList.add(new MenuItem(Long.valueOf("2"), "Watermelon", Float.valueOf("11.99"), true, DateUtil.convertToDate("02/12/2021"), "Breakfast", true));
            menuItemList.add(new MenuItem(Long.valueOf("3"), "Soup", Float.valueOf("3.99"), true, DateUtil.convertToDate("04/12/2021"), "Dinner", true));
        }
    }

    /*  Getter for menuItemList. 
        Input: 
        Output: A list with all items on the menu. */
    public List<MenuItem> getMenuItemList() {
        return menuItemList;
    }
    
    /*  Setter for menuItemList.
        Input: A list of MenuItems.
        Output: The menuItemList is updated. */
    void setMenuItemList(List<MenuItem> menuItemList) {
        MenuItemDaoCollectionImpl.menuItemList = menuItemList;
    }

    /*  Get all the items on the menu for the admin.
        Input: 
        Output: All items on the menu.  */
    public List<MenuItem> getMenuItemListAdmin() {
        return MenuItemDaoCollectionImpl.menuItemList;
    }

    /*  Get all the items from today's menu. Customer can only access the items from today's menu,
        Input: 
        Output: A list with all the items on today's menu. */
    public List<MenuItem> getMenuItemListCustomer() {
        List<MenuItem> currentItems = new ArrayList<MenuItem>();
        
        SimpleDateFormat formatt = new SimpleDateFormat("dd/MM/yyyy");
        Date today = null;
        try {
            today = formatt.parse(formatt.format(new Date())); // Set today's date.
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for(MenuItem i : menuItemList){ // For every item on the menu, 
            if(i.getDateOfLunch().equals(today)){  // Checks if the date is today.
                if(i.isActive()) // If it is active, add the item.
                    currentItems.add(i);
            }
        }

        return currentItems;
    }

    /*  Function that modifies an item on the menu.
        Input: A menuItem.
        Output: The menu item on the menu is updated with the new info. */
    public void modifyMenuItem(MenuItem menuItem){
        for(MenuItem item : menuItemList) { // Check every item on the menu,
            if(item.getId().equals(menuItem.getId())){ // Until you found the one we are looking for.
                // Set all the info on the menu with the new item info.
                item.setName(menuItem.getName()); 
                item.setActive(menuItem.isActive());
                item.setCategory(menuItem.getCategory());
                item.setDateOfLunch(menuItem.getDateOfLunch());
                item.setFreeDelivery(menuItem.isFreeDelivery());
                item.setPrice(menuItem.getPrice());
                break;
            }
        }
    }

    /*  Function that gets an item from the menu.
        Input: A long number with the item's id.
        Output: The item or null if the item doesn't exist. */
    public MenuItem getMenuItem(Long menuItemId){
        for(MenuItem item : menuItemList) // For every item on menu,
            if(menuItemId == item.getId()) // Check if it is the one we are looking for.
                return item;
        return null; // If the item was not found, it doesn't exist.
    }

    /*  Function that add a new item to the menu.
        Input: A menuItem.
        Output: The menu has now a new item.    */
    public void addMenuItem(MenuItem menuItem) {
        menuItemList.add(menuItem);
    }
}
