package com.java.starhotel.model;

import java.util.Date;

public class MenuItem {
    private Long id;
    private String name;
    private Float price;
    private Boolean active;
    private Date dateOfLunch;
    private String category;
    private Boolean freeDelivery;

    public MenuItem(Long id, String name, Float price, Boolean active, Date dateOfLunch, String category, Boolean freeDelivery){
        this.setId(id);
        this.setName(name);
        this.setPrice(price);
        this.setActive(active);
        this.setDateOfLunch(dateOfLunch);
        this.setCategory(category);       
        this.setFreeDelivery(freeDelivery);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Date getDateOfLunch() {
        return dateOfLunch;
    }

    public void setDateOfLunch(Date dateOfLunch) {
        this.dateOfLunch = dateOfLunch;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean isFreeDelivery() {
        return freeDelivery;
    }

    public void setFreeDelivery(Boolean freeDelivery) {
        this.freeDelivery = freeDelivery;
    }

    @Override
    public String toString() {
        String result = "Menu Item " + getName() + " has ID " + getId() + ", costs " + getPrice() + " is ";
        if(!isActive())
            result += "not ";
        result += "active, has date as " + getDateOfLunch() + ", category is " + getCategory();
        if(isFreeDelivery())
            result += " and has free delivery!";

        return result;
    }

    @Override
    public boolean equals(Object compare) {
        if(compare == null)
            return false;
        
        if(!(compare instanceof MenuItem))
            return false;
        
        MenuItem i = (MenuItem) compare;
        
        if(i.getId() == this.getId())
            return true;
        return false;
    }

}