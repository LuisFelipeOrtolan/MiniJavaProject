package com.java.starhotel.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.java.starhotel.model.Cart;
import com.java.starhotel.model.MenuItem;

public class CartDaoCollectionImpl implements CartDao{
    private static HashMap<Long,Cart> userCarts;

    public CartDaoCollectionImpl() {
        if(userCarts == null){
            userCarts = new HashMap<Long,Cart>();
        }
    }
    
    public void addCartItem(Long userId, Long menuItemId){
        MenuItemDaoCollectionImpl menu = new MenuItemDaoCollectionImpl();
        MenuItem item = menu.getMenuItem(menuItemId);
        
        if(userCarts.containsKey(userId)){
            Cart cart = userCarts.get(userId);
            List<MenuItem> menuItemList = cart.getMenuItemList();
            menuItemList.add(item);
            cart.setMenuItemList(menuItemList);
        }
        else{
            Cart newCart = new Cart(new ArrayList<MenuItem>(), Double.valueOf("0"));
            List<MenuItem> menuItemsList = newCart.getMenuItemList();
            menuItemsList.add(item);
            newCart.setMenuItemList(menuItemsList);
            userCarts.put(userId, newCart);
        }
    }
    
    public List<MenuItem> getAllCartItems(Long userId) throws CartEmptyException{
        if(userCarts.containsKey(userId)){
            Cart cart = userCarts.get(userId);
            List<MenuItem> items = cart.getMenuItemList();
            if(items.isEmpty()){
                throw new CartEmptyException();
            }
            else{
                Float total = Float.valueOf("0");
                for(MenuItem item : items){
                    total += item.getPrice();
                }
                cart.setTotal(Double.valueOf(total));
            }
            return cart.getMenuItemList();
        }
        return null;
    }

    public void removeCartItem(Long userId, Long menuItemId) {
        Cart cart = userCarts.get(userId);
        List<MenuItem> items = cart.getMenuItemList();
        for(MenuItem item : items) {
            if(item.getId().equals(menuItemId)){
                items.remove(item);
            }
        }
    }

    public Cart getCart(Long userId) {
        return userCarts.get(userId);
    }

}
