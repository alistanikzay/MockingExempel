package org.example;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private final List<Item> items = new ArrayList<>();

    public void addItem(String name, double price, int quantity) {
        if (quantity <= 0) {
            return;
        }

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

    public double calculateTotal() {
        double total = 0;
        for (Item item : items) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    public double applyDiscount(double percentage) {
        if (percentage < 0) {
            return calculateTotal();
        }

        double total = calculateTotal();
        double discountAmount = total * (percentage / 100);
        return total - discountAmount;
    }

}
