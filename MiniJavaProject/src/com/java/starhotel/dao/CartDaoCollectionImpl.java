package com.java.starhotel.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.java.starhotel.model.Cart;
import com.java.starhotel.model.MenuItem;

public class CartDaoCollectionImpl implements CartDao{
    private static HashMap<Long,Cart> userCarts; // Key is the UserId and it maps to the user's carts.

    /*  Constructor.
        Input: 
        Output: a new CartDaoCollectionImpl instance.   */
    public CartDaoCollectionImpl() {
        if(userCarts == null){ // If the map is not instanciated, instanciate it.
            userCarts = new HashMap<Long,Cart>();
        }
    }
    
    /* Function that adds an item to a customer's cart.
        Input: A long with the user's id and a long with the item's id.
        Output: An item added to the user's cart.   */
    public void addCartItem(Long userId, Long menuItemId){
        MenuItemDaoCollectionImpl menu = new MenuItemDaoCollectionImpl();
        MenuItem item = menu.getMenuItem(menuItemId); // Gets the item by the id.
        if(item == null) { // Check if the item exists in the menu.
            System.out.println("Item doesn't exixst.");
            return;
        }

        
        if(userCarts.containsKey(userId)){ // Checks if the user already has a cart.
            Cart cart = userCarts.get(userId); // Get the user's cart.
            List<MenuItem> menuItemList = cart.getMenuItemList(); // Get the list of bought items by the customer.
            menuItemList.add(item); // Add the new item.
            cart.setMenuItemList(menuItemList); // Puts the new list on the cart.
        }
        else{ // If the user doesn't have a cart.
            Cart newCart = new Cart(new ArrayList<MenuItem>(), Double.valueOf("0")); // Create a new Cart.
            List<MenuItem> menuItemsList = newCart.getMenuItemList(); // Get the empty list.
            menuItemsList.add(item); // Add the new item.
            newCart.setMenuItemList(menuItemsList); // Puts the new list on the cart.
            userCarts.put(userId, newCart); // Puts the new cart on the map.
        }
    }
    
    /*  Function that returns all the items in a customer's cart.
        Input: A long with the user's id.
        Output: A list of all the Menu Items bought by the customer.    
        Obs: Throws an exception if the cart is empty.*/
    public List<MenuItem> getAllCartItems(Long userId) throws CartEmptyException{
        if(userCarts.containsKey(userId)){ // Checks if the user already has a cart.
            Cart cart = userCarts.get(userId); // Gets the cart.
            List<MenuItem> items = cart.getMenuItemList(); // Get all the items bought.
            if(items.isEmpty()){ // If the list is empty, throw exception.
                throw new CartEmptyException();
            }
            else{ 
                Float total = Float.valueOf("0");
                for(MenuItem item : items){ // For every item in the list,
                    total += item.getPrice(); // Sum the current value.
                }
                cart.setTotal(Double.valueOf(total)); // Set the total value of the cart.
            }
            return cart.getMenuItemList(); // Return the list.
        } else{ // If the user doesn't have a card, throw exception.
            throw new CartEmptyException();
        }
    }

    /* Function that removes an item from a customer's cart.
        Input: A long with the user's id and a long with the item's id.
        Output: An item removed from the user's cart.   */
    public void removeCartItem(Long userId, Long menuItemId) {
        if(!userCarts.containsKey(userId)) // If the user has not a Cart, there is nothing to remove.
            return;
        Cart cart = userCarts.get(userId); // Get the user's cart.
        List<MenuItem> items = cart.getMenuItemList(); // Get all the items bought.
        for(MenuItem item : items) { // Search through every item.
            if(item.getId().equals(menuItemId)){ // If the item is the one we are looking for,
                items.remove(item); // Remove it.
                break; // Stop the search.
            }
        }
    }

    /*  Function that returns the cart from a user.
        Input: A long number with the customer's id.
        Output: The customer's cart.    */
    public Cart getCart(Long userId) {
        return userCarts.get(userId);
    }

}
