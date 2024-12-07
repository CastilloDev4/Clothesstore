package com.clothesstore.clothesstore.service.implementation;

import com.clothesstore.clothesstore.persistence.builders.ImageBuilder;
import com.clothesstore.clothesstore.persistence.entity.Image;
import com.clothesstore.clothesstore.presentation.dto.ImageDTO;
import com.clothesstore.clothesstore.presentation.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;


;import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class S3Service {


    private final S3Client s3Client;
    @Value("${aws.bucket.name}")
    private String bucketName;
    @Autowired
    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public List<Image> handleImageUpload(ProductDTO productDTO) throws IOException {
        List<Image> images = new ArrayList<>();
        System.out.println("Iniciando carga de imagenes para el producto: " + productDTO.getName());

        for (ImageDTO imageDTO : productDTO.getProductImage()) {
            String imageUrl = imageDTO.getUrl();
            System.out.println("Procesando imagen: " + imageUrl);
            long imageSize = actualSizeImage(imageUrl);
            System.out.println("Tamaño de la imagen: " + imageSize);

            // Si la imagen es demasiado grande, redimensionarla
            if (imageSize > 1_000_000) {
                BufferedImage originalImage = downloadImage(imageUrl);
                BufferedImage resizedImage = resizeImage(originalImage);
                System.out.println("Imagen redimensionada.");

                // Genera nueva clave para la imagen redimensionada, para que no se sobrescriba en s3
                String resizedImageKey = generateImageKey(productDTO.getName());
                System.out.println("Subiendo imagen redimensionada con la clave: " + resizedImageKey);

                // Subir la imagen redimensionada
                imageUrl = uploadImage(resizedImage, resizedImageKey);
                imageSize = calculateSize(resizedImage);
            }

            // Crear entidad Image y establecer el tamaño
            Image image = new ImageBuilder()
                .url(imageUrl)
                .description(imageDTO.getDescriptionImage())
                .size(imageSize)
                .product(null)
                .build();
                images.add(image);




//            Image image = new Image();
//            image.setUrl(imageUrl);
//            image.setSize(imageSize); // Establecer el tamaño calculado en la bd
//            image.setDescriptionImage(imageDTO.getDescriptionImage());
//            image.setProduct(null); // se asigna en productservice
//            images.add(image);
//

        }

        System.out.println("Carga de las imagenes completada.");
        return images;
    }






    private BufferedImage downloadImage(String imagenUrl) throws IOException {
        URL url = new URL(imagenUrl);
        BufferedImage image = ImageIO.read(url);
        if (image == null) {
            throw new IOException("No se pudo descargar la imagen desde la URL: " + imagenUrl);
        }
        return image;
    }

    private BufferedImage resizeImage(BufferedImage originalImage)throws IOException  {
        BufferedImage resizedImage = originalImage;
        while (calculateSize(resizedImage) > 1_000_000) {
            int nuevaAncho = resizedImage.getWidth() / 2;
            int nuevaAlto = resizedImage.getHeight() / 2;
            resizedImage = new BufferedImage(nuevaAncho, nuevaAlto, originalImage.getType());
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(originalImage, 0, 0, nuevaAncho, nuevaAlto, null);
            g.dispose();
        }
        return  resizedImage;
    }
//crea la carpeta resized-images y le asigna un nombre aleatorio
    private String generateImageKey(String productName) {
        return "resized-images/" + productName + "-" + UUID.randomUUID() + ".jpg";
    }
    //calcula la imagen DESPUES de redimensionada
    private long calculateSize(BufferedImage imagen) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(imagen, "jpg", baos);
        baos.flush();
        long size = baos.size();
        baos.close();
        return size;
    }
// calcula el tamaño de la imagen SIN redimensionar
    private long actualSizeImage(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        URLConnection connection = url.openConnection();
        connection.connect();
        return connection.getContentLengthLong(); // Devuelve el tamaño del contenido
    }
        //sube la imagen a S3
    public String uploadImage(BufferedImage imagen, String key) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(imagen, "jpg", baos);
        baos.flush();
        byte[] imagenBytes = baos.toByteArray();
        baos.close();

        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .contentType("image/jpeg")
                            .contentDisposition("inline")
                            .build(),
                    software.amazon.awssdk.core.sync.RequestBody.fromBytes(imagenBytes)
            );
        } catch (S3Exception e) {
            throw new IOException("Error al subir la imagen al bucket S3", e);
        }

        String region = "us-east-2";
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, key);
    }

    private ImageDTO mapImageToImageDTO(Image image) {
        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setId(image.getId());
        imageDTO.setUrl(image.getUrl());
        imageDTO.setDescriptionImage(image.getDescriptionImage());

        return imageDTO;
    }
}





