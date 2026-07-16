package com.mzrt.erp_lite.edomain.ports;

import com.mzrt.erp_lite.domain.product.ProductImage;

public interface ImageStorageService {

    ProductImage upload(String name, byte[] imageData);
    void delete(ProductImage img);
    byte[] download(ProductImage img);
}
