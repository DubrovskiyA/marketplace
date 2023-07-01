package ru.inno.market;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.inno.market.core.Catalog;
import ru.inno.market.model.Client;
import ru.inno.market.model.Order;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {
    private Order order;
    private Catalog catalog;
    @BeforeEach
    public void initOrderAndCatalog(){
        order=new Order(1, new Client(1,"Ivan"));
        catalog=new Catalog();
    }
    @Test
    public void shouldAddItem(){
        assertTrue(order.getItems().isEmpty());
        order.addItem(catalog.getItemById(3));
        assertEquals(1, order.getItems().size());
    }
    @ParameterizedTest
    @ValueSource(ints = {-100,-1,0,13,100})
    public void shouldNotAddItemOutsideTheCatalog(int id) {
        assertTrue(order.getItems().isEmpty());
        assertThrows(NoSuchElementException.class,() ->order.addItem(catalog.getItemById(id)));
        assertEquals(0, order.getItems().size());
    }
    @Test
    public void shouldApplyDiscount() {
        order.addItem(catalog.getItemById(2));
        order.applyDiscount(0.5);
        assertEquals(9745,order.getTotalPrice());
    }
    @Test
    public void shouldNotApplyDiscountIfDiscountAlreadyApplied() {
        order.addItem(catalog.getItemById(5));
        order.applyDiscount(0.15);
        order.applyDiscount(0.45);
        assertEquals(76491.5,order.getTotalPrice());
    }
}
