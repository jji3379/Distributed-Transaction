package com.example.monolithic.product.application;

import com.example.monolithic.product.domain.Product;
import com.example.monolithic.product.intrastructure.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Long buy(Long productId, Long quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("데이터가 존재하지 않습니다."));

        Long totalPrice = product.calculatePrice(quantity);
        product.buy(quantity);

        productRepository.save(product);

        return totalPrice;
    }
}
