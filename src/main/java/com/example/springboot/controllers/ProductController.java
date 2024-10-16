package com.example.springboot.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.springboot.dtos.ProductRecordDto;
import com.example.springboot.models.ProductModel;
import com.example.springboot.services.ProductService;

import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class ProductController {
    
    @Autowired
    private ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.saveProduct(productRecordDto));
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductModel>> getAllProducts() {
        List<ProductModel> productsList = productService.getAllProducts();
        
        productsList.forEach(product -> {
            UUID id = product.getIdProduct();
            product.add(linkTo(methodOn(ProductController.class).getOneProduct(id)).withSelfRel());
        });

        return ResponseEntity.ok(productsList);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Object> getOneProduct(@PathVariable(value = "id") UUID id) {
        Optional<ProductModel> productO = productService.getOneProduct(id);
        if (productO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
        productO.get().add(linkTo(methodOn(ProductController.class).getAllProducts()).withRel("Products List"));
        return ResponseEntity.ok(productO.get());
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id,
                                                @RequestBody @Valid ProductRecordDto productRecordDto) {
        try {
            return ResponseEntity.ok(productService.updateProduct(id, productRecordDto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") UUID id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok("Product deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
