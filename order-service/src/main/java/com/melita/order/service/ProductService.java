package com.melita.order.service;

import com.melita.order.entities.Product;
import com.melita.order.exception.RecordNotFoundException;
import com.melita.order.repos.ProductRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.melita.order.enums.ErrorCode.PRODUCT_NOT_FOUND;

@Data
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public Product save(Product product) {
        product.getBundles().forEach(bundle -> bundle.setProduct(product));
        return productRepository.save(product);
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(PRODUCT_NOT_FOUND));
    }

    public Product update(Product o, Long id) {
        Product product = getById(id);
        product.setName(o.getName());
        productRepository.save(product);
        return product;
    }

    public void delete(Long id) {
        Product o = getById(id);
        productRepository.delete(o);
    }
}
