package com.yunseong.secod_project_web_server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunseong.secod_project_web_server.common.Util;
import com.yunseong.secod_project_web_server.controller.dto.*;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.*;

@Controller
public class ProductsController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/products")
    public String getProductsSearch(Model model, @ModelAttribute ProductsSearchCondition condition, @RequestParam(defaultValue = "1") String page) throws JsonProcessingException {
        String text = "?";
        if (condition != null) {
            if (hasText(condition.getCategory())) {
                text += "category=" + condition.getCategory() + "&";
            }
            if (hasText(condition.getProduct())) {
                text += "product=" + condition.getProduct() + "&";
            }
            if (condition.getMin() != null && condition.getMin() > -1) {
                text += "min=" + condition.getMin() + "&";
            }
            if (condition.getMax() != null && condition.getMax() > -1) {
                text += "max=" + condition.getMax() + "&";
            }
        }
        if (StringUtils.hasText(page)) {
            text += "page=" + (Integer.parseInt(page)-1) + "&";
        }

        text = text.substring(0, text.length()-1);

        ResponseEntity<String> responseAll = this.restTemplate.getForEntity(Util.REST_API_SERVER_URL + "/products/search" + text, String.class);

        return getResponse(model, responseAll);
    }

    private String getResponse(Model model, ResponseEntity<String> responseAll) throws JsonProcessingException {
        Map<String, Object> all = this.objectMapper.readValue(responseAll.getBody(), HashMap.class);

        List list = null;
        Map detailMap = (LinkedHashMap) all.get("_embedded");

        if (detailMap != null) {
            list = ((ArrayList<Map>) detailMap.get("productResponseList")).stream().map(ProductDto::new).collect(Collectors.toList());
        }

        Map page = (LinkedHashMap) all.get("page");

        model.addAttribute("all", list);
        int totalPage = (int)page.get("totalPages") == 0 ? 1 : (int)page.get("totalPages");
        int currentPage = (int)page.get("number") + 1;
        int startPage = (int)(((double)currentPage / 10) - 1) * 10 + 1;
        int endPage = totalPage - startPage >= 10 ? startPage + 9 : totalPage;

        model.addAttribute("page", new ProductPage(currentPage, startPage, endPage, totalPage));

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

    @GetMapping("/products/{id}/recommend")
    public String getProductRecommend(@PathVariable String id) {
        this.restTemplate.exchange(Util.REST_API_SERVER_URL + "/products/{id}/recommend", HttpMethod.PUT, null, String.class, id);
        return "redirect:/products/" + id;
    }

    @PostMapping("/products/register")
    public String postProductRegister(ProductForm productForm) {
        ResponseEntity<String> responseEntity = this.restTemplate.postForEntity(Util.REST_API_SERVER_URL + "/products/register", new HttpEntity<>(productForm), String.class);

        List<String> location = responseEntity.getHeaders().get(HttpHeaders.LOCATION);
        String id = location.get(0).split("/v1/products/")[1];

        return "redirect:/products/" + id;
    }
}