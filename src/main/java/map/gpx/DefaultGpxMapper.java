package map.gpx;

import files.ExtractedGpxResult;
import io.jenetics.jpx.Track;
import io.jenetics.jpx.WayPoint;
import map.ElevationGraphCreator;
import map.StaticMapCreator;
import map.filewriter.FileWriter;
import map.filewriter.PngWriter;
import map.filewriter.SvgWriter;
import org.jfree.chart.JFreeChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * This class can be extended to implement your own GpxMapper. However, it can be used <i>as is</i>,
 * to provide default functionalities.
 * <p>
 * The {@link builder} should be used to create and configure an instance.
 */
public record DefaultGpxMapper(int width, int height, int chartHeight, GpxStyler styler, FileWriter mapWriter,
                               FileWriter elevationGraphWriter) implements IGpxMapper {
    public static final Logger LOGGER = LoggerFactory.getLogger(DefaultGpxMapper.class);

    public ExtractedGpxResult map(File gpxFile) throws IOException {
        return map(gpxFile, null);
    }

    public ExtractedGpxResult map(File gpxFile, Path outputFolder) throws IOException {
        LOGGER.info("Parsing GPX file {}...", gpxFile.getName());
        List<Track> tracks = GpxParser.getTracks(gpxFile);
        List<WayPoint> wayPoints = GpxParser.getWayPoints(tracks);
        LOGGER.info("Parsed {} waypoints", wayPoints.size());

        BufferedImage map = StaticMapCreator.createMap(wayPoints, width, height, styler);
        // Écrire d'abord la carte
        mapWriter.writeMapImageToFile(gpxFile, outputFolder, styler, map, width, height);

        // Ensuite le graphique d'élévation si nécessaire
        if (styler.displayElevationGraph()) {
            JFreeChart elevationGraph = ElevationGraphCreator.getElevationGraph(wayPoints, styler);
            elevationGraphWriter.writeMapImageToFile(gpxFile, outputFolder, styler, elevationGraph, width, chartHeight);
        }

        return GpxMetadataExtractor.extract(gpxFile.getName(), tracks, wayPoints);
    }

    /**
     * Builder for the {@link DefaultGpxMapper}
     */
    public static class builder {
        /**
         * Default map width. The elevation graph will always use the exact same width
         */
        private int width = 1000;
        /**
         * Default map height
         */
        private int height = 1400;
        /**
         * Default chart height. Resulting image height will be {@link #height} + chartHeight
         */
        private int chartHeight = 150;
        private GpxStyler gpxStyler;
        private FileWriter mapWriter = new PngWriter();
        private FileWriter elevationGraphWriter = new SvgWriter();

        public builder withWidth(int width) {
            this.width = width;
            return this;
        }

        public builder withHeight(int height) {
            this.height = height;
            return this;
        }

        public builder withChartHeight(int chartHeight) {
            this.chartHeight = chartHeight;
            return this;
        }

        public builder withGpxStyler(GpxStyler gpxStyler) {
            this.gpxStyler = gpxStyler;
            return this;
        }

        public builder withMapWriter(FileWriter mapWriter) {
            this.mapWriter = mapWriter;
            return this;
        }

        public builder withElevationGraphWriter(FileWriter elevationGraphWriter) {
            this.elevationGraphWriter = elevationGraphWriter;
            return this;
        }

        public DefaultGpxMapper build() {
            if (this.gpxStyler == null) {
                this.gpxStyler = GpxStyler.getDefaultStyler();
            }
            return new DefaultGpxMapper(this.width, this.height, this.chartHeight,
                    this.gpxStyler, this.mapWriter, this.elevationGraphWriter);
        }
    }
}
