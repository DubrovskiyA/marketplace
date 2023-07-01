package ru.inno.market.core;

import ru.inno.market.model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class MarketService {

    private int orderCounter;
    private Map<Integer, Order> orders;

    public MarketService() {
        orderCounter = 0;
        orders = new HashMap<>();
    }

    public int createOrderFor(Client client){
        int id = orderCounter++;
        Order order = new Order(id, client);
        orders.put(id, order);

        return order.getId();
    }

    public void addItemToOrder(Item item, int orderId ){
        Catalog catalog=new Catalog();
        catalog.getItemById(item.getId());
        orders.get(orderId).addItem(item);
    }

    public double applyDiscountForOrder(int orderId, PromoCodes codes){
        Order order = orders.get(orderId);
        order.applyDiscount(codes.getDiscount());
        return order.getTotalPrice();
    }

    public Order getOrderInfo(int id) {
        if (orders.containsKey(id)){
            return orders.get(id);
        }
        throw new NoSuchElementException("Заказ не создан");
    }
}

