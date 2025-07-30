package map.filewriter;

import map.gpx.DefaultGpxMapper;
import map.gpx.GpxStyler;
import map.gpx.GraphToMapPosition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

class SvgWriterIT {
    public static final int SIZE = 500;
    public static final Path resourceDirectory = Paths.get("src", "test", "resources");
    public static final int CHART_HEIGHT = 100;

    @TempDir
    public Path tempDir;

    @Test
    void it_should_generate_cart_in_svg_format() throws IOException {
        // Given a GPX mapper and a styler
        GpxStyler gpxStyler = new GpxStyler.builder()
                .withGraphFillColor(new Color(0, 49, 223))
                .withGraphLineColor(new Color(0, 255, 0))
                .withStrokeColor(new Color(216, 0, 0))
                .withGraphPosition(GraphToMapPosition.TOP)
                .withStrokeWidth(5)
                .build();

        DefaultGpxMapper gpxMapper = new DefaultGpxMapper.builder()
                .withWidth(SIZE)
                .withHeight(SIZE)
                .withChartHeight(CHART_HEIGHT)
                .withGpxStyler(gpxStyler)
                .setFileWriter(new SvgWriter())
                .build();

        // When the map is created
        File gpxFile = resourceDirectory.resolve("test.gpx").toFile();
        gpxMapper.map(gpxFile, tempDir);

        // Then the SVG file should être créé et identique au SVG attendu
        File generatedSvg = tempDir.resolve("test.svg").toFile();
        File expectedSvg = resourceDirectory.resolve("ugly_result.svg").toFile();
        String generatedContent = Files.readString(generatedSvg.toPath());
        String expectedContent = Files.readString(expectedSvg.toPath());
        assertThat(generatedContent).isEqualToNormalizingNewlines(expectedContent);
    }
}
