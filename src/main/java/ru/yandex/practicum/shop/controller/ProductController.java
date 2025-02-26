package ru.yandex.practicum.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.shop.dto.product.ProductSort;
import ru.yandex.practicum.shop.service.ProductService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final List<Integer> PAGE_SIZES = List.of(10, 20, 50, 100);

    @GetMapping("/**")
    public String productsList(@RequestParam(value = "search", required = false) String search,
                               @RequestParam(value = "page", defaultValue = "1") int page,
                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                               @RequestParam(value = "sort", defaultValue = "") ProductSort sort,
                               Model model) {

        model.addAttribute("products", productService.findAll(search, page, pageSize, sort));

        model.addAttribute("params", Map.of(
                "search", search != null ? search : "",
                "sort", sort != null ? sort : "",
                "pageSizes", PAGE_SIZES
        ));

        return "products";
    }

    @GetMapping("/{id}")
    public String findById(@RequestHeader(value = HttpHeaders.REFERER, required = false) final String referrer,
                           @PathVariable("id") Long productId, Model model) {
        model.addAttribute("product", productService.findById(productId));
        return "product";
    }
}
