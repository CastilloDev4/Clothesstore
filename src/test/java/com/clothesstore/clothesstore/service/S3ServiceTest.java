package com.clothesstore.clothesstore.service;

import com.clothesstore.clothesstore.persistence.entity.Image;
import com.clothesstore.clothesstore.presentation.dto.ImageDTO;
import com.clothesstore.clothesstore.presentation.dto.ProductDTO;
import com.clothesstore.clothesstore.service.implementation.S3Service;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;



@ExtendWith(MockitoExtension.class)
class S3ServiceTest {



    @InjectMocks
    private S3Service s3Service;

    @Test
    void testHandleImageUpload() throws IOException {

        ProductDTO productDTO = new ProductDTO();
        List<ImageDTO> productImages = new ArrayList<>();
        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setUrl("https://ejemplo.com/imagen.jpg");
        imageDTO.setDescriptionImage("Test image");
        productImages.add(imageDTO);
        productDTO.setProductImage(productImages);

        BufferedImage mockImage = new BufferedImage(2000, 2000, BufferedImage.TYPE_INT_RGB);


        URLConnection connectionMock = Mockito.mock(URLConnection.class);
        Mockito.lenient().when(connectionMock.getContentLengthLong()).thenReturn(2_000_000L);


        URL urlMock = Mockito.mock(URL.class);
        Mockito.lenient().when(urlMock.openConnection()).thenReturn(connectionMock);


        try (MockedStatic<ImageIO> mockedStatic = Mockito.mockStatic(ImageIO.class)) {
            mockedStatic.when(() -> ImageIO.read(Mockito.any(URL.class))).thenReturn(mockImage);
            mockedStatic.when(() -> ImageIO.write(Mockito.any(BufferedImage.class), Mockito.eq("jpg"), Mockito.any(ByteArrayOutputStream.class))).thenAnswer(invocation -> {
                ByteArrayOutputStream baos = invocation.getArgument(2);
                baos.write(new byte[500_000]); // Mock resized image size
                return true;
            });

            List<Image> result = s3Service.handleImageUpload(productDTO);

            Assertions.assertNotNull(result);
            Assertions.assertEquals(1, result.size());
            Image uploadedImage = result.get(0);
            Assertions.assertEquals("https://ejemplo.com/imagen.jpg", uploadedImage.getUrl());
            Assertions.assertTrue(uploadedImage.getSize() <= 1_000_000L, "Tamaño deberia ser menor a 1mb");
        }
    }}


