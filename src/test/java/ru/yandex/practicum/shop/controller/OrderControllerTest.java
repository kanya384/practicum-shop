package ru.yandex.practicum.shop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.shop.dto.order.OrderItemResponseDTO;
import ru.yandex.practicum.shop.dto.order.OrderResponseDTO;
import ru.yandex.practicum.shop.dto.product.ProductResponseDTO;
import ru.yandex.practicum.shop.exception.NoProductsInOrderException;
import ru.yandex.practicum.shop.exception.ResourceNotFoundException;
import ru.yandex.practicum.shop.service.OrderService;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    OrderService orderService;


    @Test
    void placeOrder_shouldPlaceOrder() throws Exception {
        when(orderService.placeOrder())
                .thenReturn(new OrderResponseDTO());

        mockMvc.perform(post("/orders")
                        .header("referer", "/orders"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(containsString("redirect:orders/")));

        verify(orderService, times(1)).placeOrder();
    }

    @Test
    void placeOrder_shouldReturnErrorIfNoItemsInOrder() throws Exception {
        when(orderService.placeOrder())
                .thenThrow(new NoProductsInOrderException(""));

        mockMvc.perform(post("/orders"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("oops"));
    }

    @Test
    void findAll_shouldReturnOrdersList() throws Exception {
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setId(1L);
        orderResponseDTO.setItems(List.of(new OrderItemResponseDTO()));
        orderResponseDTO.setCreatedAt(LocalDate.now());

        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setId(1L);
        productResponseDTO.setTitle("title");
        productResponseDTO.setDescription("description");
        productResponseDTO.setImage("image");
        productResponseDTO.setPrice(100);
        productResponseDTO.setCount(0);

        OrderItemResponseDTO orderItemResponseDTO = new OrderItemResponseDTO();
        orderItemResponseDTO.setId(1L);
        orderItemResponseDTO.setProduct(productResponseDTO);
        orderItemResponseDTO.setCount(1);

        orderResponseDTO.setItems(List.of(orderItemResponseDTO));


        when(orderService.findAll())
                .thenReturn(List.of(orderResponseDTO));

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders"))
                .andExpect(xpath("//div[contains(@class, \"order-item\")]").nodeCount(1));
    }

    @Test
    void findById_shouldReturnOrderById() throws Exception {

        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setId(1L);
        orderResponseDTO.setItems(List.of(new OrderItemResponseDTO()));
        orderResponseDTO.setCreatedAt(LocalDate.now());

        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setId(1L);
        productResponseDTO.setTitle("title");
        productResponseDTO.setDescription("description");
        productResponseDTO.setImage("image");
        productResponseDTO.setPrice(100);
        productResponseDTO.setCount(0);

        OrderItemResponseDTO orderItemResponseDTO = new OrderItemResponseDTO();
        orderItemResponseDTO.setId(1L);
        orderItemResponseDTO.setProduct(productResponseDTO);
        orderItemResponseDTO.setCount(1);

        orderResponseDTO.setItems(List.of(orderItemResponseDTO));

        when(orderService.findById(anyLong()))
                .thenReturn(orderResponseDTO);

        mockMvc.perform(get("/orders/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("order"));
    }

    @Test
    void findById_shouldReturn404IfOrderNotExists() throws Exception {
        when(orderService.findById(anyLong()))
                .thenThrow(new ResourceNotFoundException("Заказ", -1L));

        mockMvc.perform(get("/orders/{id}", -1))
                .andExpect(status().isNotFound())
                .andExpect(view().name("oops"));
    }


}
