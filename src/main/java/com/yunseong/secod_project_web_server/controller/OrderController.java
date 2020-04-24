package com.yunseong.secod_project_web_server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunseong.secod_project_web_server.common.Util;
import com.yunseong.secod_project_web_server.controller.dto.OrderDto;
import com.yunseong.secod_project_web_server.controller.dto.OrderForm;
import com.yunseong.secod_project_web_server.controller.dto.OrderProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/{id}/payment")
    public String paymentOrder(@PathVariable Long id) {
        try {
            this.restTemplate.postForEntity(Util.REST_API_SERVER_URL + "/orders/{id}/payment", null, String.class, id);
        }catch(HttpClientErrorException e) {
            if(e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                System.out.println(e.getResponseBodyAsString() + " : 돈이 부족합니다.");
            }
        }

        return "redirect:/myinfo/purchase";
    }

    @PostMapping
    public String postOrder(@RequestParam("orderId") List<Long> ids) {
        OrderForm orderItems = new OrderForm();
        orderItems.setOrderItems(ids);
        this.restTemplate.postForEntity(Util.REST_API_SERVER_URL + "/orders", new HttpEntity<>(orderItems), String.class);

        return "redirect:/orders";
    }

    @PostMapping("/{id}")
    public String deleteOrder(@PathVariable Long id) {
        this.restTemplate.exchange(Util.REST_API_SERVER_URL + "/orders/{id}", HttpMethod.PUT, null, String.class, id);

        return "redirect:/orders";
    }

    @GetMapping("/{id}")
    public String getOrder(@PathVariable Long id, Model model) throws JsonProcessingException {
        ResponseEntity<String> responseEntity = this.restTemplate.getForEntity(Util.REST_API_SERVER_URL + "/orders/{id}", String.class, id);

        Map map = this.objectMapper.readValue(responseEntity.getBody(), HashMap.class);

        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId((int)map.get("id"));
        getOrderProducts(map, orderDto);
        model.addAttribute("order", orderDto);

        return "/orderdetail";
    }

    @GetMapping
    public String getOrders(Model model) throws JsonProcessingException {
        ResponseEntity<String> responseEntity = this.restTemplate.getForEntity(Util.REST_API_SERVER_URL + "/orders/my", String.class);

        Map map = this.objectMapper.readValue(responseEntity.getBody(), HashMap.class);

        List<OrderDto> orders = new ArrayList<>();
        Object object = ((Map)map.get("_embedded")).get("orderResponseList");

        if(object != null && object instanceof List) {
            List<Map> list = (ArrayList) object;
            list.stream().forEach((element) -> {
                OrderDto orderDto = new OrderDto();
                orderDto.setOrderId((int)element.get("id"));
                getOrderProducts(element, orderDto);
                orders.add(orderDto);
            });

            model.addAttribute("orders", orders);
        }

        return "/orders";
    }

    private void getOrderProducts(Map map, OrderDto orderDto) {
        List<OrderProductDto> orderProductDtoList = new ArrayList<>();
        for(Map element : ((ArrayList<Map>) map.get("orderProductResponses"))) {
            OrderProductDto dto = new OrderProductDto();
            dto.setProductId((int) element.get("id"));
            dto.setProductName((String) element.get("productName"));
            int price = (int) element.get("price");
            dto.setProductPrice(price);
            orderProductDtoList.add(dto);
        }
        orderDto.setProducts(orderProductDtoList);
        orderDto.setTotalPrice((int)map.get("totalPrice"));
        orderDto.setOrderStatus((String)map.get("orderStatus"));
        orderDto.setPaymentStatus((String)map.get("paymentStatus"));
    }
}
