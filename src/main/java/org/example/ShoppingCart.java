package org.example;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private final List<Item> items = new ArrayList<>();

    public void addItem(String name, double price, int quantity) {
        // Kolla om varan redan finns i listan
        for (Item item : items) {
            if (item.getName().equals(name)) {
                item.setQuantity(item.getQuantity() + quantity); // Uppdatera kvantiteten
                return;
            }
        }
        // Lägg till varan om den inte redan finns
        items.add(new Item(name, price, quantity));
    }

    public void removeItem(String name) {
        // Ta bort varan om den finns
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

    // Applicera rabatt på totalbeloppet
    public double applyDiscount(double percentage) {
        double total = calculateTotal();
        double discountAmount = total * (percentage / 100);
        return total - discountAmount;
    }
}
