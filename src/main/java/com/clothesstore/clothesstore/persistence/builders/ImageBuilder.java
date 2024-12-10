package com.clothesstore.clothesstore.persistence.builders;

import com.clothesstore.clothesstore.persistence.entity.Image;
import com.clothesstore.clothesstore.persistence.entity.Product;

public class ImageBuilder {

    private String url;
    private Long size;
    private String descriptionImage;
    private Product product;



    public ImageBuilder url(String url) {
        this.url = url;
        return this;
    }

    public ImageBuilder size(Long size) {
        this.size = size;
        return this;
    }
    public ImageBuilder description(String description) {
        this.descriptionImage = description;
        return this;
    }

    public ImageBuilder product(Product product) {
        this.product = product;
        return this;
    }

    public Image build() {
        Image image = new Image();
        image.setUrl(this.url);
        image.setSize(this.size);
        image.setDescriptionImage(this.descriptionImage);
        image.setProduct(this.product);
        return image;
    }
}
