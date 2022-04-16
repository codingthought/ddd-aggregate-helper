package org.ddd.example.test;

import org.ddd.example.application.PlaceOrderService;
import org.ddd.example.application.data.request.ItemFieldRequest;
import org.ddd.example.application.data.request.OrderItemRequest;
import org.ddd.example.application.data.request.OrderRequest;
import org.ddd.example.application.data.request.ShippingAddressRequest;
import org.ddd.example.domain.repository.OrderRepository;
import org.ddd.example.inforstructure.data.ItemFieldPO;
import org.ddd.example.inforstructure.data.OrderItemPO;
import org.ddd.example.inforstructure.data.OrderPO;
import org.ddd.example.inforstructure.mapper.ItemFieldMapper;
import org.ddd.example.inforstructure.mapper.OrderItemMapper;
import org.ddd.example.inforstructure.mapper.OrderMapper;
import org.ddd.example.inforstructure.repository.OrderRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    public static final String ORDER_NO = "O-10000000001";
    @InjectMocks
    private PlaceOrderService orderController;
    @InjectMocks
    private OrderRepository orderRepository = spy(OrderRepositoryImpl.class);
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private OrderItemMapper orderItemMapper;
    @Mock
    private ItemFieldMapper itemFieldMapper;


    @Test
    void should_save_all_entity_when_create_new_aggregation() {
        when(orderMapper.save(any(OrderPO.class))).thenReturn(new OrderPO());
        when(orderItemMapper.save(any(OrderItemPO[].class))).thenReturn(new OrderItemPO[0]);
        when(itemFieldMapper.save(any(ItemFieldPO[].class))).thenReturn(new ItemFieldPO[0]);
        when(orderMapper.get(any(String.class))).thenReturn(new OrderPO());

        orderController.placeOrder(buildOrderRequest());

        verify(orderMapper).save(any(OrderPO.class));
        verify(orderItemMapper).save(any(OrderItemPO[].class));
        verify(itemFieldMapper).save(any(ItemFieldPO[].class));
    }

    @Test
    void should_save_order_only_when_update_remark() {
        OrderPO orderPO = buildOrderPO();
        when(orderMapper.get(eq(ORDER_NO))).thenReturn(orderPO, orderPO);
        when(orderMapper.save(any(OrderPO.class))).thenReturn(new OrderPO());

        orderController.updateRemark(ORDER_NO, "尽快发货");

        verify(orderMapper, times(1)).save(any(OrderPO.class));
        verify(orderItemMapper, times(0)).save(any(OrderItemPO[].class));
        verify(itemFieldMapper, times(0)).save(any(ItemFieldPO[].class));
    }

    @Test
    void should_save_order_item_when_update_item_remark() {
        OrderPO orderPO = buildOrderPO();
        when(orderMapper.get(eq(ORDER_NO))).thenReturn(orderPO, orderPO);
        when(orderMapper.save(any(OrderPO.class))).thenReturn(new OrderPO());

        orderController.updateRemark(ORDER_NO, 1, "要今年生产的");

        verify(orderMapper, times(1)).save(any(OrderPO.class));
        verify(orderItemMapper, times(1)).save(any(OrderItemPO[].class));
        verify(itemFieldMapper, times(0)).save(any(ItemFieldPO[].class));
    }

    private OrderPO buildOrderPO() {
        OrderPO orderPO = new OrderPO();
        orderPO.setNo(ORDER_NO);
        return orderPO;
    }

    private OrderRequest buildOrderRequest() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setNo(Optional.of(ORDER_NO));

        OrderItemRequest item1 = new OrderItemRequest();
        item1.setName("润喉糖");
        item1.setSkuId("R-1");
        item1.setPrice(10);

        OrderItemRequest item2 = new OrderItemRequest();
        item2.setName("康泰克");
        item2.setSkuId("KTK-2");
        item2.setPrice(20);
        ItemFieldRequest itemField = new ItemFieldRequest();
        itemField.setKey("身份证好");
        itemField.setType("身份证");
        itemField.setValue("1228471237849127XX");
        item2.setFields(List.of(itemField));

        orderRequest.setItems(List.of(item1, item2));

        ShippingAddressRequest shippingAddress = new ShippingAddressRequest();
        shippingAddress.setAddress("xxx");
        shippingAddress.setPhone("232132199278");
        shippingAddress.setReceiverName("xxx");
        orderRequest.setShippingAddress(shippingAddress);
        return orderRequest;
    }
}