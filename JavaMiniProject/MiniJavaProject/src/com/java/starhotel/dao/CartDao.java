package com.java.starhotel.dao;

import java.util.List;

import com.java.starhotel.model.MenuItem;

public interface CartDao {
    public void addCartItem(Long userId, Long menuItemId);
    public List<MenuItem> getAllCartItems(Long userId) throws CartEmptyException;
    public void removeCartItem(Long userId, Long menuItemId);
}
