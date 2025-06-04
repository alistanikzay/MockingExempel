package org.example;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private final List<Item> items = new ArrayList<>();

    public void addItem(String name, double price, int quantity) {
        for (Item item : items) {
            if (item.getName().equals(name)) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        items.add(new Item(name, price, quantity));
    }

    public void removeItem(String name) {
        items.removeIf(item -> item.getName().equals(name));
    }

    public List<Item> getItems() {
        return items;
    }



}
