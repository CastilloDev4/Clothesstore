package com.clothesstore.clothesstore.service.implementation;
import com.clothesstore.clothesstore.persistence.builders.ProductBuilder;
import com.clothesstore.clothesstore.persistence.entity.Country;
import com.clothesstore.clothesstore.persistence.entity.Image;
import com.clothesstore.clothesstore.persistence.entity.Product;
import com.clothesstore.clothesstore.persistence.repository.IProductRepository;
import com.clothesstore.clothesstore.presentation.dto.ImageDTO;
import com.clothesstore.clothesstore.presentation.dto.MostSearchedProductDTO;
import com.clothesstore.clothesstore.presentation.dto.ProductDTO;
import com.clothesstore.clothesstore.service.exception.DiscountException;
import com.clothesstore.clothesstore.service.exception.DuplicateNameException;
import com.clothesstore.clothesstore.service.exception.FieldEmptyException;
import com.clothesstore.clothesstore.service.exception.NegativeValueException;
import com.clothesstore.clothesstore.service.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Service
public class ProductServiceIMPL implements IProductService {

    private final S3Service s3Service;

    private final IProductRepository productRepository;
    @Autowired
    public ProductServiceIMPL(S3Service s3Service, IProductRepository iProductRepository)
    {
        this.s3Service = s3Service;
        this.productRepository = iProductRepository;
    }


    @Transactional
    @Override
    public Product save(ProductDTO productDTO) {


        //VALIDACIONES ANTES DE CREAR EL PRODUCTO

        validateDiscount(productDTO.getDiscount(), productDTO.getCountry());
        if (
                productDTO.getName() == null ||
                productDTO.getName().isEmpty() ||
                productDTO.getDescription() == null ||
                productDTO.getDescription().isEmpty() ||
                productDTO.getCountry() == null
        )
        {
            throw new FieldEmptyException("No pueden haber campos vacíos");
        }

        //campos precio y descuento
        if(
                productDTO.getPrice() <= 0)
        {
            throw new NegativeValueException("El precio debe ser mayor a 0");
        }

        if(
                productDTO.getDiscount() < 0)
        {
            throw new NegativeValueException("El descuento debe ser mayor o igual a 0");
        }

        //si existe con el mismo nombre
        if(productRepository.existsByName(productDTO.getName()))
        {
            throw new DuplicateNameException("El producto "+ productDTO.getName() + " ya existe");
        }



        //Creo el producto

        Product product = new ProductBuilder()


            .name(productDTO.getName())
            .description(productDTO.getDescription())
            .price(productDTO.getPrice())
            .discount(productDTO.getDiscount())
            .country(productDTO.getCountry())
            .discountPrice(calculateDiscountPrice(productDTO.getPrice(), productDTO.getDiscount()))
            .build();

        List<Image> images;
        try {
            images = s3Service.handleImageUpload(productDTO);
        } catch (IOException e) {

            throw new RuntimeException("Error al manejar la carga de las imagenes: " + e.getMessage(), e);
        }
        for (Image image : images) {
            image.setProduct(product); // Asigno el producto a la imagen
            product.addImage(image);
        }
        //Guardo el producto
        return productRepository.save(product);

    }

    @Override
    public Optional<ProductDTO> findById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(value -> {
            value.setSearchCount(value.getSearchCount() + 1);
            productRepository.save(value);
            ProductDTO dto = new ProductDTO();
            dto.setId(value.getId());
            dto.setName(value.getName());
            dto.setDescription(value.getDescription());
            dto.setPrice(value.getPrice());
            dto.setDiscount(value.getDiscount());
            dto.setSearchCount(value.getSearchCount());
            dto.setCountry(value.getCountry());

            List<ImageDTO> imageDTOS = value.getProductImage().stream()
                    .map(this::mapImageToImageDTO)
                    .toList();
            dto.setProductImage(imageDTOS);



            return dto;
        });
    }




    @Override
    public Optional<ProductDTO> findByName(String name) {
        Optional<Product> product = productRepository.findByName(name);
        return product.map(value -> {
            value.setSearchCount(value.getSearchCount() + 1);
            productRepository.save(value);
            ProductDTO dto = new ProductDTO();
            dto.setId(value.getId());
            dto.setName(value.getName());
            dto.setDescription(value.getDescription());
            dto.setPrice(value.getPrice());
            dto.setDiscount(value.getDiscount());
            dto.setSearchCount(value.getSearchCount());
            dto.setCountry(value.getCountry());

            List<ImageDTO> imageDTOS = value.getProductImage().stream()
                    .map(this::mapImageToImageDTO)
                    .toList();
            dto.setProductImage(imageDTOS);
            return dto;
        });
    }

    //validacion de descuento por pais
    private void validateDiscount(Integer discount, Country country) {
        if (discount > country.getMaxDiscount()) {
            throw new DiscountException("El descuento para "+ country + " no puede ser mayor al "+ country.getMaxDiscount() + "%");
        }
    }
    // calcular descuento
    private Double calculateDiscountPrice(Double price, Integer discount) {

        return price - (price * discount / 100);
    }

    //obtener productos mas buscados
    @Override
    public List<MostSearchedProductDTO> getMostSearchedProducts() {
        List<Product> mostSearchedProducts = productRepository.findMostSearchedProducts();
        if(mostSearchedProducts.isEmpty())
        {
            throw new FieldEmptyException("No hay ningun producto buscado hasta el momento");
        }

        return mostSearchedProducts.stream().map(product -> {
            MostSearchedProductDTO dto = new MostSearchedProductDTO();
            dto.setName(product.getName());
            dto.setPrice(product.getPrice());
            dto.setDiscountPrice(calculateDiscountPrice(product.getPrice(), product.getDiscount()));
            dto.setDiscount(product.getDiscount());



            // Se obtienen las imagenes frontales y traseras
            Optional<ImageDTO> frontImage = product.getProductImage().stream()
                    .filter(image -> "frontal".equalsIgnoreCase(image.getDescriptionImage()))
                    .map(this::mapImageToImageDTO) // Transformar a ImageDTO
                    .findFirst();

            Optional<ImageDTO> backImage = product.getProductImage().stream()
                    .filter(image -> "trasera".equalsIgnoreCase(image.getDescriptionImage()))
                    .map(this::mapImageToImageDTO) // Transformar a ImageDTO
                    .findFirst();

            dto.setFrontImage(frontImage.map(ImageDTO::getUrl).orElse(null));
            dto.setBackImage(backImage.map(ImageDTO::getUrl).orElse(null));
            dto.setSearchCount(product.getSearchCount());
            return dto;
        }).toList();
    }

    private ImageDTO mapImageToImageDTO(Image image) {
        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setId(image.getId());
        imageDTO.setUrl(image.getUrl());
        imageDTO.setDescriptionImage(image.getDescriptionImage());

        return imageDTO;
    }





}
