# spring-course

[POST] .../products
```curl
curl --location 'http://localhost:8080/api/v1/products' \
--header 'Content-Type: application/json' \
--data '{
    "id": 6,
    "productName": "Product-6",
    "price": 10
}'
```

[GET] .../products?page=<pageIndex>
```curl
curl --location 'http://localhost:8080/api/v1/products?page=0'
```

[GET] .../products/<productId>
```curl
curl --location 'http://localhost:8080/api/v1/products/1'
```

[PUT] .../products/1
```curl
curl --location --request PUT 'http://localhost:8080/api/v1/products/1' \
--header 'Content-Type: application/json' \
--data '{
    "id": 1,
    "productName": "Product-1-Edit",
    "price": 20
}'
```

[DELETE] .../products/1
```curl
curl --location --request DELETE 'http://localhost:8080/api/v1/products/1'
```

[GET] .../products/seasonal/1
```curl
curl --location 'http://localhost:8080/api/v1/products/seasonal/1'
```