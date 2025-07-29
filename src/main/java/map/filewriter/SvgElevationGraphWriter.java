package map.filewriter;


import org.jfree.svg.SVGGraphics2D;
import org.jfree.svg.SVGUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Writer pour générer un graphique d'élévation au format SVG.
 */
public class SvgElevationGraphWriter extends AbstractWriter {
    @Override
    protected void writeImage(File gpxFile, Path outputFolder, String suffix, BufferedImage image) throws IOException {
        // Prépare le nom du fichier de sortie
        String baseName = gpxFile.getName().replaceFirst("\\.gpx$", "");
        String fileSuffix = (suffix != null && !suffix.isEmpty()) ? ("-" + suffix) : "";
        File svgFile = outputFolder.resolve(baseName + fileSuffix + ".svg").toFile();

        // Crée le SVG à partir du BufferedImage
        SVGGraphics2D svg2d = new SVGGraphics2D(image.getWidth(), image.getHeight());
        svg2d.drawImage(image, 0, 0, null);
        SVGUtils.writeToSVG(svgFile, svg2d.getSVGElement());
    }
}

