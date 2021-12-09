package com.java.starhotel.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.java.starhotel.model.MenuItem;
import com.java.starhotel.util.DateUtil;

public class MenuItemDaoCollectionImpl implements MenuItemDao{
    
    private static List<MenuItem> menuItemList = null;

    public MenuItemDaoCollectionImpl(){
        if(getMenuItemList() == null){
            menuItemList = new ArrayList<MenuItem>();
            menuItemList.add(new MenuItem(Long.valueOf("1"), "Sandwich", Float.valueOf("6.99"), true, DateUtil.convertToDate("02/12/2021"), "Lunch", true));
            menuItemList.add(new MenuItem(Long.valueOf("2"), "Watermelon", Float.valueOf("11.99"), true, DateUtil.convertToDate("02/12/2021"), "Breakfast", true));
            menuItemList.add(new MenuItem(Long.valueOf("3"), "Soup", Float.valueOf("3.99"), true, DateUtil.convertToDate("04/12/2021"), "Dinner", true));
        }
    }
    public List<MenuItem> getMenuItemList() {
        return menuItemList;
    }
    
    void setMenuItemList(List<MenuItem> menuItemList) {
        MenuItemDaoCollectionImpl.menuItemList = menuItemList;
    }

    public List<MenuItem> getMenuItemListAdmin() {
        return MenuItemDaoCollectionImpl.menuItemList;
    }

    public List<MenuItem> getMenuItemListCustomer() {
        List<MenuItem> currentItems = new ArrayList<MenuItem>();
        
        SimpleDateFormat formatt = new SimpleDateFormat("dd/MM/yyyy");
        Date today = null;
        try {
            today = formatt.parse(formatt.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for(MenuItem i : menuItemList){
            if(i.getDateOfLunch().equals(today)){ 
                if(i.isActive())
                    currentItems.add(i);
            }
        }

        return currentItems;
    }

    public void modifyMenuItem(MenuItem menuItem){
        for(MenuItem item : menuItemList) {
            if(item.getId().equals(menuItem.getId())){
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

    public MenuItem getMenuItem(Long menuItemId){
        for(MenuItem item : menuItemList)
            if(menuItemId == item.getId())
                return item;
        return null;
    }

    public void addMenuItem(MenuItem menuItem) {
        menuItemList.add(menuItem);
    }
    
}
