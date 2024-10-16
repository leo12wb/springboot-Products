package com.example.springboot.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.dtos.ProductRecordDto;
import com.example.springboot.models.ProductModel;
import com.example.springboot.repositories.ProductRepository;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;

    public ProductModel saveProduct(ProductRecordDto productRecordDto) {
        var productModel = new ProductModel();
        BeanUtils.copyProperties(productRecordDto, productModel);
        return productRepository.save(productModel);
    }

    public List<ProductModel> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<ProductModel> getOneProduct(UUID id) {
        return productRepository.findById(id);
    }

    public ProductModel updateProduct(UUID id, ProductRecordDto productRecordDto) {
        var productModel = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        BeanUtils.copyProperties(productRecordDto, productModel);
        return productRepository.save(productModel);
    }

    public void deleteProduct(UUID id) {
        var productModel = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(productModel);
    }
}

