package com.app.ecom_application.service;

import com.app.ecom_application.dto.ProductRequest;
import com.app.ecom_application.dto.ProductResponse;
import com.app.ecom_application.model.Product;
import com.app.ecom_application.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product=new Product();
        updateProductFromRequest(product,productRequest);
        Product saveproduct=productRepository.save(product);
        return mapToProductResponse(saveproduct);
    }

    private ProductResponse mapToProductResponse(Product saveproduct) {
        ProductResponse response=new ProductResponse();
        response.setId(saveproduct.getId());
        response.setName(saveproduct.getName());
        response.setActive(saveproduct.getActive());
        response.setCategory(saveproduct.getCategory());
        response.setDescription(saveproduct.getDescription());
        response.setPrice(saveproduct.getPrice());
        response.setImageUrl(saveproduct.getImageUrl());
        response.setStockQuantity(saveproduct.getStockQuantity());
        return response;
    }

    private void updateProductFromRequest(Product product, ProductRequest productRequest) {
        product.setName(productRequest.getName());
        product.setCategory(productRequest.getCategory());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setImageUrl(productRequest.getImageUrl());
        product.setStockQuantity(productRequest.getStockQuantity());
    }

    public Optional<ProductResponse> updateProduct(Long id, ProductRequest productRequest) {
        return productRepository.findById(id)
                .map(exitingProduct ->{
                    updateProductFromRequest(exitingProduct,productRequest);
                    Product savedProduct =productRepository.save(exitingProduct);
                    return mapToProductResponse(savedProduct);
                });
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findByActiveTrue().stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    public boolean deleteProduct(Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setActive(false);
                    productRepository.save(product);
                    return true;
                }).orElse(false);
    }

    public List<ProductResponse> searchProducts(String keyword) {
       return productRepository.searchProducts(keyword).stream()
               .map(this::mapToProductResponse)
               .collect(Collectors.toList());
    }
}
