package com.example.course.controllers;

import com.example.course.models.ProductRequest;
import com.example.course.models.ProductResponse;
import com.example.course.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// เอาไว้บอกว่าเป็น Controller (API contact)
@Controller
// เอาไว้กำหนด base path สำหรับ controller นี้
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // @RequestBody ProductRequest request จับกับ Request DTO ที่ client call เข้ามา
    @PostMapping
    ResponseEntity<?> createProduct(@RequestBody ProductRequest request) {
        productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Http [GET] เมื่อไม่กำหนด path ตรงนี้จะหมายถึง /api/v1/products
    @GetMapping
    ResponseEntity<List<ProductResponse>> getProducts(
            @RequestParam(name = "page", defaultValue = "0") int pageIndex,
            @RequestParam(name = "size", required = false, defaultValue = "5") int pageSize
    ) {
        List<ProductResponse> products = productService.getProducts(pageIndex, pageSize);
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getProduct(@PathVariable int id) {
        try {
            ProductResponse product = productService.getProduct(id);
            return ResponseEntity.ok().body(product);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/{productId}")
    ResponseEntity<?> updateProduct(@PathVariable(name = "productId") int id, @RequestBody ProductRequest request) {
        productService.updateProduct(id, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/seasonal/{id}")
    ResponseEntity<Boolean> getPriceDrop(@PathVariable int id) {
        boolean isPriceDrop = productService.getPriceDrop(id);
        return ResponseEntity.status(HttpStatus.OK).body(isPriceDrop);
    }

}
