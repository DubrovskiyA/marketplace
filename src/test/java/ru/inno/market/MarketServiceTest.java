package ru.inno.market;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.inno.market.core.Catalog;
import ru.inno.market.core.MarketService;
import ru.inno.market.model.*;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class MarketServiceTest {
        private MarketService marketService;
        private Client client;
        @BeforeEach
        public void initServiceAndClient(){
            marketService=new MarketService();
            client=new Client(5,"Fil");
        }
        @Test
        public void shouldCreateOrder(){
            int orderId=marketService.createOrderFor(client);
            assertEquals(0, orderId);
            Order order=marketService.getOrderInfo(orderId);
            assertEquals(5,order.getClient().getId());
            assertEquals("Fil", order.getClient().getNickname());
            assertEquals(0,order.getTotalPrice());
        }
        @Test
        public void shouldAddItemToOrder(){
            Catalog catalog=new Catalog();
            Item item=catalog.getItemById(1);
            int orderId=marketService.createOrderFor(client);
            assertTrue(marketService.getOrderInfo(orderId).getItems().isEmpty());
            marketService.addItemToOrder(item,orderId);
            Order order=marketService.getOrderInfo(orderId);
            assertEquals(1,order.getItems().size());
        }
        @ParameterizedTest
        @ValueSource(ints = {-100,-1,0,1,100})
        public void shouldNotAddItemToNonCreatedOrder(int orderId){
            Catalog catalog=new Catalog();
            Item item=catalog.getItemById(1);
            assertThrows(NullPointerException.class, ()->marketService.addItemToOrder(item,orderId));
        }
        @Test
        public void shouldNotAddItemOutsideCatalogToOrder(){
            int orderId=marketService.createOrderFor(client);
            Item item=new Item(234,"qwerty",Category.SOFTWARE,456000);
            assertThrows(NoSuchElementException.class, ()->marketService.addItemToOrder(item,orderId));
        }
        @Test
        public void shouldApplyDiscountForOrder(){
            Catalog catalog=new Catalog();
            Item item=catalog.getItemById(1);
            int orderId=marketService.createOrderFor(client);
            marketService.addItemToOrder(item,orderId);
            double totalPriceWithDiscount=marketService.applyDiscountForOrder(orderId, PromoCodes.FIRST_ORDER);
            assertEquals(78392.0, totalPriceWithDiscount);
        }
        @ParameterizedTest
        @ValueSource(ints = {-100,-1,0,1,100})
        public void shouldNotApplyDiscountForNonCreatedOrder(int orderId){
            assertThrows(NullPointerException.class, ()->marketService.applyDiscountForOrder(orderId,
            PromoCodes.FIRST_ORDER));
        }
        @Test
        public void shouldGetOrderInfo() {
            int orderId=marketService.createOrderFor(client);
            Order order=marketService.getOrderInfo(orderId);
            assertEquals(5,order.getClient().getId());
            assertEquals("Fil", order.getClient().getNickname());
        }
        @Test
        public void shouldNotGetOrderInfoForNonCreatedOrder() {
            assertThrows(NoSuchElementException.class,()->marketService.getOrderInfo(1));
        }
}
