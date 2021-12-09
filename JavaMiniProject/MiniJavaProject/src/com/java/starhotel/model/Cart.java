package com.java.starhotel.model;

import java.util.List;

public class Cart {
    private List<MenuItem> menuItemList;
    private Double total;

    public Cart(List<MenuItem> menuItemList, Double total) {
        setMenuItemList(menuItemList);
        setTotal(total);
    }

    public List<MenuItem> getMenuItemList() {
        return menuItemList;
    }

    public void setMenuItemList(List<MenuItem> menuItemList) {
        this.menuItemList = menuItemList;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}

