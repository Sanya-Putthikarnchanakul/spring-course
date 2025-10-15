package com.example.course.services;

import com.example.course.components.StringComponent;
import com.example.course.configs.ProductConfig;
import com.example.course.entities.ProductEntity;
import com.example.course.models.ProductRequest;
import com.example.course.models.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

// Business logic layer
@Service
public class ProductService {

    private static List<ProductEntity> products = new ArrayList<>();

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private StringComponent stringComponent;

    @Autowired
    private ProductConfig productConfig;

    public void createProduct(ProductRequest request) {
        boolean isValid = stringComponent.isProductNameValid(request.productName());

        if (!isValid) return;

        products.add(new ProductEntity(
                request.id(),
                request.productName(),
                request.price()
        ));
    }

    public List<ProductResponse> getProducts(int pageIndex, int pageSize) {
        products.sort(Comparator.comparingInt(ProductEntity::getId));
        return products
                .stream()
                .map(p -> new ProductResponse(
                        p.getId(),
                        p.getProductName(),
                        p.getPrice()
                ))
                .skip((long) pageIndex * pageSize)
                .limit(pageSize)
                .toList();
    }

    public ProductResponse getProduct(int id) throws Exception {
        Optional<ProductEntity> optProduct = products
                .stream()
                .filter(p -> p.getId() == id)
                .findFirst();

        if (optProduct.isEmpty()) {
            throw new Exception("Product not found");
        }

        ProductEntity productEntity = optProduct.get();
        return new ProductResponse(
                productEntity.getId(),
                productEntity.getProductName(),
                productEntity.getPrice()
        );
    }

    public void updateProduct(int id, ProductRequest request)  {
        products
                .forEach(p -> {
                    if (p.getId() == id) {
                        p.setProductName(request.productName());
                        p.setPrice(request.price());
                    }
                });
    }

    public void deleteProduct(int id) {
        products.removeIf(p -> p.getId() == id);
    }

    public boolean getPriceDrop(int id) {
        return productConfig.getIdsPriceDrop().contains(id);
    }

}
