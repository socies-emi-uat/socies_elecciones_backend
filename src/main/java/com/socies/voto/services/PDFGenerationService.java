package com.socies.voto.services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.socies.voto.dtos.Voto.PDFVotoDTO;
import com.socies.voto.models.Usuario;
import com.socies.voto.repositories.UsuarioRepository;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PDFGenerationService {

    @Autowired private UsuarioRepository usuarioRepository;

    public byte[] generateVoterPDF(Long votanteId) {
        // Obtener el votante desde la base de datos
        Usuario usuario = usuarioRepository.findById(votanteId).orElse(null);

        if (usuario == null) {
            throw new RuntimeException("Votante no encontrado");
        }

        // Verificar si el votante está activo
        if (!usuario.isEstado()) {
            throw new RuntimeException("El votante no está activo.");
        }

        // Verificar si el votante ha realizado algún voto
        if (usuario.getVotos().isEmpty()) {
            throw new RuntimeException("El votante no ha realizado ningún voto.");
        }

        // Crear el PDFVotoDTO con los datos del votante
        PDFVotoDTO votoDTO = new PDFVotoDTO();
        votoDTO.setNombreCompleto(
                usuario.getNombre()
                        + " "
                        + usuario.getApellidoPaterno()
                        + " "
                        + usuario.getApellidoMaterno());
        votoDTO.setFechaNacimiento(usuario.getFechaNacimiento().toLocalDate());
        votoDTO.setNumeroCarnet(usuario.getCedulaIdentidad());
        votoDTO.setFotoUrl(usuario.getFotoUrl());
        votoDTO.setNumeroDocumento(usuario.getCedulaIdentidad());

        // Generar el QR Code (con el número de carnet)
        String qrContent =
                usuario.getCedulaIdentidad(); // El contenido del QR es el número de carnet
        byte[] qrCodeImage = generateQRCode(qrContent);

        // Crear el documento PDF
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfDocument pdfDocument =
                new PdfDocument(new com.itextpdf.kernel.pdf.PdfWriter(outputStream));
        Document document = new Document(pdfDocument);

        // Título del documento
        document.add(new Paragraph("Carnet de Sufragio").setFontSize(18).setBold());

        // Crear una tabla para mostrar los datos del votante
        Table table = new Table(2);
        table.addCell(new Cell().add(new Paragraph("Nombre Completo")));
        table.addCell(new Cell().add(new Paragraph(votoDTO.getNombreCompleto())));
        table.addCell(new Cell().add(new Paragraph("Fecha de Nacimiento")));
        table.addCell(new Cell().add(new Paragraph(votoDTO.getFechaNacimiento().toString())));
        table.addCell(new Cell().add(new Paragraph("Número de Carnet")));
        table.addCell(new Cell().add(new Paragraph(votoDTO.getNumeroCarnet())));
        table.addCell(new Cell().add(new Paragraph("Número de Documento")));
        table.addCell(new Cell().add(new Paragraph(votoDTO.getNumeroDocumento())));

        // Agregar la imagen de la foto del votante
        try {
            iTextImage fotoVotante =
                    iTextImage.getInstance(
                            votoDTO.getFotoUrl()); // Aquí puedes cargar la foto desde la URL
            // proporcionada.
            fotoVotante.scaleToFit(50, 50); // Ajusta el tamaño de la foto
            table.addCell(new Cell().add(fotoVotante));
        } catch (Exception e) {
            table.addCell(new Cell().add(new Paragraph("Foto no disponible")));
        }

        // Agregar el QR Code
        try {
            ByteArrayOutputStream qrStream = new ByteArrayOutputStream();
            qrStream.write(qrCodeImage);
            iTextImage qrImage = iTextImage.getInstance(qrStream.toByteArray());
            qrImage.scaleToFit(100, 100); // Ajustar el tamaño del QR
            table.addCell(new Cell().add(qrImage));
        } catch (IOException e) {
            table.addCell(new Cell().add(new Paragraph("QR no disponible")));
        }

        // Agregar la tabla al documento
        document.add(table);

        // Cerrar el documento
        document.close();

        return outputStream.toByteArray();
    }

    private byte[] generateQRCode(String content) {
        try {
            MultiFormatWriter writer = new MultiFormatWriter();
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 200, 200);
            Path path = Paths.get("qrcode.png");
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
            return path.toFile().getAbsolutePath().getBytes(); // Retornar el QR como imagen
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el QR Code", e);
        }
    }
}
