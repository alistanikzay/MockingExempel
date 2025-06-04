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
    void shouldAddNewItemToEmptyCart() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem("Apple", 10.0, 2);

        assertThat(cart.getItems())
                .hasSize(1)
                .extracting(Item::getName, Item::getPrice, Item::getQuantity)
                .containsExactly(tuple("Apple", 10.0, 2));
    }

    @Test
    void shouldIncreaseQuantityIfSameItemAdded() {
        cart.addItem("Apple", 10.0, 2);
        cart.addItem("Apple", 10.0, 3);

        assertThat(cart.getItems())
                .hasSize(1)
                .extracting(Item::getName, Item::getQuantity)
                .containsExactly(tuple("Apple", 5));
    }

    @Test
    void shouldRemoveItemFromCart() {
        cart.addItem("Apple", 10.0, 2);
        cart.addItem("Banana", 5.0, 1);

        cart.removeItem("Apple");

        assertThat(cart.getItems())
                .hasSize(1)
                .extracting(Item::getName)
                .doesNotContain("Apple");
    }

    @Test
    void shouldCalculateTotalPriceCorrectly() {
        cart.addItem("Apple", 10.0, 2);  // 20
        cart.addItem("Banana", 5.0, 3);  // 15

        double total = cart.calculateTotal();

        assertThat(total).isEqualTo(35.0);
    }

    @Test
    void shouldApplyPercentageDiscountCorrectly() {
        cart.addItem("Apple", 10.0, 2);  // 20
        cart.addItem("Banana", 5.0, 3);  // 15

        double discounted = cart.applyDiscount(10); // 10% av 35 = 3.5

        assertThat(discounted).isEqualTo(31.5);
    }

    @Test
    void shouldReturnSameTotalIfZeroDiscount() {
        cart.addItem("Apple", 10.0, 1);

        double discounted = cart.applyDiscount(0);

        assertThat(discounted).isEqualTo(10.0);
    }

    @Test
    void shouldNotAddItemWithZeroQuantity() {
        cart.addItem("Apple", 10.0, 0);

        assertThat(cart.getItems()).isEmpty();
    }


}
