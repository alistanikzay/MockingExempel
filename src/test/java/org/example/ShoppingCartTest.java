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



}
