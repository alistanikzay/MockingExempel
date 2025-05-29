package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ShoppingCartTest {

    private ShoppingCart cart;

    @BeforeEach
    void setUp() {
        cart = new ShoppingCart();
    }

    @Test
    void addItem_whenItemIsNew_shouldAddItToCart() {
        cart.addItem("Apple", 10.0, 2);

        assertThat(cart.getItems())
                .hasSize(1)
                .extracting(Item::getName, Item::getPrice, Item::getQuantity)
                .containsExactly(tuple("Apple", 10.0, 2));
    }

    @Test
    void addItem_whenSameItemAddedAgain_shouldAccumulateQuantity() {
        cart.addItem("Apple", 10.0, 2);
        cart.addItem("Apple", 10.0, 3);

        assertThat(cart.getItems())
                .hasSize(1)
                .extracting(Item::getName, Item::getQuantity)
                .containsExactly(tuple("Apple", 5));
    }

    @Test
    void removeItem_givenExistingItem_shouldDeleteItFromCart() {
        cart.addItem("Apple", 10.0, 2);
        cart.addItem("Banana", 5.0, 1);

        cart.removeItem("Apple");

        assertThat(cart.getItems())
                .hasSize(1)
                .extracting(Item::getName)
                .doesNotContain("Apple");
    }

    @Test
    void calculateTotal_withMultipleItems_shouldComputeAccurateSum() {
        cart.addItem("Apple", 10.0, 2);  // 20
        cart.addItem("Banana", 5.0, 3);  // 15

        double total = cart.calculateTotal();

        assertThat(total).isEqualTo(35.0);
    }

    @Test
    void applyDiscount_shouldReturnDiscountedTotal() {
        cart.addItem("Apple", 10.0, 2);  // 20
        cart.addItem("Banana", 5.0, 3);  // 15

        double discounted = cart.applyDiscount(10); // 10% rabatt på 35 = 31.5

        assertThat(discounted).isEqualTo(31.5);
    }

    @Test
    void applyDiscount_withZeroPercentage_shouldReturnTotal() {
        cart.addItem("Apple", 10.0, 1);

        double discounted = cart.applyDiscount(0);

        assertThat(discounted).isEqualTo(10.0);
    }
}
