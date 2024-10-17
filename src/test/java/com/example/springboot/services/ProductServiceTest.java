package com.example.springboot.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.springboot.dtos.ProductRecordDto;
import com.example.springboot.models.ProductModel;
import com.example.springboot.repositories.ProductRepository;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    private ProductModel productModel;
    private ProductRecordDto productRecordDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Inicializando dados de teste
        productModel = new ProductModel();
        productModel.setIdProduct(UUID.randomUUID());
        productModel.setName("Test Product");
        productModel.setValue(BigDecimal.valueOf(100.0));

        // Usando o record diretamente
        productRecordDto = new ProductRecordDto("Test Product", BigDecimal.valueOf(100.0));
    }

    @Test
    void testSaveProduct() {
        when(productRepository.save(any(ProductModel.class))).thenReturn(productModel);

        ProductModel savedProduct = productService.saveProduct(productRecordDto);

        assertNotNull(savedProduct);
        assertEquals("Test Product", savedProduct.getName());
        assertEquals(BigDecimal.valueOf(100.0), savedProduct.getValue());
        verify(productRepository, times(1)).save(any(ProductModel.class));
    }

    @Test
    void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(productModel));

        var products = productService.getAllProducts();

        assertEquals(1, products.size());
        assertEquals("Test Product", products.get(0).getName());
        assertEquals(BigDecimal.valueOf(100.0), products.get(0).getValue());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetOneProduct() {
        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.of(productModel));

        Optional<ProductModel> foundProduct = productService.getOneProduct(productModel.getIdProduct());

        assertTrue(foundProduct.isPresent());
        assertEquals("Test Product", foundProduct.get().getName());
        assertEquals(BigDecimal.valueOf(100.0), foundProduct.get().getValue());
        verify(productRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void testUpdateProduct() {
        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.of(productModel));
        when(productRepository.save(any(ProductModel.class))).thenReturn(productModel);

        ProductModel updatedProduct = productService.updateProduct(productModel.getIdProduct(), productRecordDto);

        assertNotNull(updatedProduct);
        assertEquals("Test Product", updatedProduct.getName());
        assertEquals(BigDecimal.valueOf(100.0), updatedProduct.getValue());
        verify(productRepository, times(1)).save(any(ProductModel.class));
    }

    @Test
    void testDeleteProduct() {
        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.of(productModel));

        assertDoesNotThrow(() -> productService.deleteProduct(productModel.getIdProduct()));
        verify(productRepository, times(1)).delete(any(ProductModel.class));
    }

    @Test
    void testDeleteProductNotFound() {
        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            productService.deleteProduct(UUID.randomUUID());
        });

        assertEquals("Product not found", exception.getMessage());
    }
}
