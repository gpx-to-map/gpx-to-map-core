package map.filewriter;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class SvgElevationGraphWriterIT {
    @Test
    void shouldWriteSimpleElevationGraphToSvg() throws Exception {
        // Création d'une image d'exemple (graphique d'élévation simplifié)
        int width = 400;
        int height = 100;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, width, height);
        g2.setColor(Color.BLUE);
        g2.drawLine(0, height - 10, width / 2, 10);
        g2.drawLine(width / 2, 10, width, height - 30);
        g2.dispose();

        // Préparation du writer et du dossier temporaire
        SvgElevationGraphWriter writer = new SvgElevationGraphWriter();
        Path tempDir = Files.createTempDirectory("svg-elevation-test");
        File fakeGpx = new File("test.gpx");

        // Génération du SVG
        writer.writeMapImageToFile(fakeGpx, tempDir, null, image);

        // Vérification de la présence du fichier SVG
        File svgFile = tempDir.resolve("test.svg").toFile();
        assertThat(svgFile).exists();
        String content = Files.readString(svgFile.toPath());
        assertThat(content).contains("<svg");
    }
}

