package com.example.springboot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.repositories.ProductRepository;

@RestController
public class ProductController {
    @Autowired
    ProductRepository productRepository;
}
