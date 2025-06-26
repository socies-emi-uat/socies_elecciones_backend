package com.socies.voto.services;

import com.socies.voto.config.BackblazeConfig;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

@Service
public class BackBlazeService {

    private static final Logger logger = LoggerFactory.getLogger(BackBlazeService.class);

    public S3Client s3Client;
    public final BackblazeConfig backblazeConfig;

    @Autowired
    public BackBlazeService(BackblazeConfig backblazeConfig) {
        this.backblazeConfig = backblazeConfig;
        this.s3Client = createS3Client();
        logger.info("BackBlazeService inicializado con configuración: {}", backblazeConfig);
    }

    private S3Client createS3Client() {
        try {
            AwsBasicCredentials awsCredentials =
                    AwsBasicCredentials.create(
                            backblazeConfig.getKeyId(), backblazeConfig.getApplicationKey());

            return S3Client.builder()
                    .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                    .endpointOverride(URI.create(backblazeConfig.getEndpoint()))
                    .region(Region.US_EAST_1)
                    .forcePathStyle(true)
                    .build();

        } catch (Exception e) {
            logger.error("Error al crear cliente S3: {}", e.getMessage());
            throw new RuntimeException("No se pudo inicializar el cliente S3", e);
        }
    }

    /** Retorna la imagen en Base64 si existe, si no, retorna un string vacío. */
    public String findFileAsBase64(String fullPath) {
        try {
            if (!fileExists(fullPath)) {
                logger.info("Archivo no encontrado: {}", fullPath);
                return "";
            }

            logger.info("Archivo encontrado: {}, iniciando descarga", fullPath);
            return downloadImageAsBase64(fullPath);

        } catch (Exception e) {
            logger.error(
                    "Error al obtener imagen como Base64 para {}: {}", fullPath, e.getMessage());
            return "";
        }
    }

    private boolean fileExists(String fileName) {
        try {
            HeadObjectRequest request =
                    HeadObjectRequest.builder()
                            .bucket(backblazeConfig.getBucketName())
                            .key(fileName)
                            .build();

            s3Client.headObject(request);
            logger.debug("Archivo {} existe en el bucket", fileName);
            return true;

        } catch (NoSuchKeyException e) {
            logger.debug("Archivo {} no existe en el bucket", fileName);
            return false;
        } catch (S3Exception e) {
            logger.error(
                    "Error al verificar existencia de {}: {}",
                    fileName,
                    e.awsErrorDetails().errorMessage());
            return false;
        }
    }

    private String downloadImageAsBase64(String fileName) {
        try {
            GetObjectRequest request =
                    GetObjectRequest.builder()
                            .bucket(backblazeConfig.getBucketName())
                            .key(fileName)
                            .build();

            try (ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(request)) {
                byte[] imageBytes = readAllBytes(s3Object);
                return Base64.getEncoder().encodeToString(imageBytes);
            }

        } catch (S3Exception | IOException e) {
            logger.error("Error al descargar o procesar imagen {}: {}", fileName, e.getMessage());
            return "";
        }
    }

    private byte[] readAllBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        return outputStream.toByteArray();
    }
}
