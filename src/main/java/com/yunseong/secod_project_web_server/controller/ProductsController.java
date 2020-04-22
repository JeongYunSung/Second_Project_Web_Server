package com.yunseong.secod_project_web_server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunseong.secod_project_web_server.common.Util;
import com.yunseong.secod_project_web_server.controller.dto.CategoryDto;
import com.yunseong.secod_project_web_server.controller.dto.ProductDto;
import com.yunseong.secod_project_web_server.controller.dto.ProductForm;
import com.yunseong.secod_project_web_server.controller.dto.ProductsSearchCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ProductsController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/products")
    public String getProducts(Model model) throws JsonProcessingException {
        ResponseEntity<String> responseAll = this.restTemplate.getForEntity(Util.REST_API_SERVER_URL + "/products",  String.class);

        return getResponse(model, responseAll);
    }

    @GetMapping("/products/search")
    public String getProductsSearch(Model model, ProductsSearchCondition condition) throws JsonProcessingException {
        String text = "";
        List<Object> list = new ArrayList<>();
        if (condition != null) {
            text += "?";
            if(condition.getCategoryName() != null) {
                text += "categoryName={categoryName}&";
                list.add(condition.getCategoryName());
            }
            if(condition.getProductName() != null) {
                text += "productName={productName}&";
                list.add(condition.getProductName());
            }
            if(condition.getMax() != null) {
                text += "max={max}&";
                list.add(condition.getMax());
            }
            if(condition.getMin() != null) {
                text += "min={min}&";
                list.add(condition.getMin());
            }
            text = text.substring(0, text.length()-1);
        }

        ResponseEntity<String> responseAll = this.restTemplate.getForEntity(Util.REST_API_SERVER_URL + "/products/search" + text,  String.class, list.toArray());

        return getResponse(model, responseAll);
    }

    private String getResponse(Model model, ResponseEntity<String> responseAll) throws JsonProcessingException {
        Map<String, Object> all = this.objectMapper.readValue(responseAll.getBody(), HashMap.class);

        List list = null;
        Map detailMap = (LinkedHashMap) all.get("_embedded");

        if (detailMap != null) {
            list = ((ArrayList<Map>) detailMap.get("productResponseList")).stream().map(ProductDto::new).collect(Collectors.toList());
        }
        model.addAttribute("all", list);

        return "/products";
    }

    @GetMapping("/products/{id}")
    public String getProduct(@PathVariable Long id, Model model) throws JsonProcessingException {
        ResponseEntity<String> responseEntity = this.restTemplate.getForEntity(Util.REST_API_SERVER_URL + "/products/{id}?view={view}", String.class, id, true);

        Map<String, Object> map = this.objectMapper.readValue(responseEntity.getBody(), HashMap.class);

        model.addAttribute("product", new ProductDto(map));

        return "/productdetails";
    }

    @GetMapping("/products/register")
    public String getProductRegister(Model model) throws JsonProcessingException {
        ResponseEntity<String> responseEntity = this.restTemplate.getForEntity(Util.REST_API_SERVER_URL + "/categories/all", String.class);

        List<Map> list = this.objectMapper.readValue(responseEntity.getBody(), List.class);

        List<CategoryDto> categoryDtos = new ArrayList<>();

        for(Map map : list) {
            if(map.get("parentId") != null) continue;
            CategoryDto dto = new CategoryDto((int)map.get("id"), (String)map.get("categoryName"));
            List<LinkedHashMap> childs = (ArrayList) map.get("child");
            if(childs != null){
                List<CategoryDto> childDtos = new ArrayList<>();
                for(Map child : childs) {
                    childDtos.add(new CategoryDto((int)child.get("childId"), (String)child.get("childName")));
                }
                dto.setChilds(childDtos);
            }
            categoryDtos.add(dto);
        }

        model.addAttribute("categories", categoryDtos);
        model.addAttribute("productForm", new ProductForm());

        return "/productregister";
    }

    @PostMapping("/products/register")
    public String postProductRegister(ProductForm productForm) {
        ResponseEntity<String> responseEntity = this.restTemplate.postForEntity(Util.REST_API_SERVER_URL + "/products/register", new HttpEntity<>(productForm), String.class);

        List<String> location = responseEntity.getHeaders().get(HttpHeaders.LOCATION);
        String id = location.get(0).split("/v1/products/")[1];

        return "redirect:/products/" + id;
    }
}