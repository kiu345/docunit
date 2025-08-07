/*
 * Copyright (c) 2025-2025 the original author or authors
 *
 * See the README file(s) distributed with this work for additional information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package net.qdevzone.docunit.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import net.qdevzone.docunit.AbstractDocAssert;
import net.qdevzone.docunit.DocumentAssert;
import net.qdevzone.docunit.DocumentAssert.FileType;
import net.qdevzone.docunit.MimeType;

public class ImageAssertions extends AbstractDocAssert<ImageAssertions> {
    private static final FileType[] ALLOWED_TYPES = { FileType.PNG, FileType.JPG, FileType.GIF };

    private final DocumentAssert base;
    @SuppressWarnings("unused")
    private FileType type;

    private BufferedImage image;
    private Throwable loadError;
    private Map<String, String> metaData = new HashMap<>();

    public ImageAssertions(DocumentAssert base, FileType type) {
        super(ImageAssertions.class);

        if (!Arrays.asList(ALLOWED_TYPES).contains(type)) {
            throw new IllegalArgumentException(type.toString());
        }

        this.base = base;
        this.type = type;

        try {
            loadImage(base);

            loadMeta(base);

        }
        catch (IOException e) {
            loadError = e;
        }
    }

    private void loadImage(DocumentAssert base) throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(base.actual());
        image = ImageIO.read(is);
        is.close();
    }

    private void loadMeta(DocumentAssert base) throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(base.actual());
        Parser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();
        try {
            parser.parse(is, handler, metadata, context);
        }
        catch (SAXException | TikaException e) {
            throw new IOException("could not read metadata", e);
        }
//        System.out.println(handler.toString());

        // getting the list of all meta data elements
        String[] metadataNames = metadata.names();

        for (String name : metadataNames) {
            metaData.put(name, metadata.get(name));
//            System.out.println(name + ": " + metadata.get(name));
        }
        is.close();
    }

    public String mimeType() {
        Tika tika = new Tika();
        return tika.detect(base.actual());
    }

    @Override
    public ImageAssertions isValid() {
        if (loadError != null) {
            throw failure("unloadable image: %s", loadError.getMessage());
        }
        if (image == null) {
            throw failure("image loader returned null");
        }
        return this;
    }

    @Override
    public ImageAssertions isNotValid() {
        if (loadError == null && image != null) {
            throw failure("image was valid");
        }
        return this;
    }

    public ImageAssertions isPng() {
        if (!MimeType.Image.PNG.equals(mimeType())) {
            throw failure("type is not PNG");
        }
        return this;
    }

    public ImageAssertions isJpeg() {
        if (!MimeType.Image.JPEG.equals(mimeType())) {
            throw failure("type is not JPEG");
        }
        return this;
    }

    public ImageAssertions isGif() {
        if (!MimeType.Image.GIF.equals(mimeType())) {
            throw failure("type is not GIF");
        }
        return this;
    }

    public ImageAssertions hasImageSize(int width, int height) {
        if (image.getWidth() != width || image.getHeight() != height) {
            throw failure("image size %d/%d does not match %d/%d", image.getWidth(), image.getHeight(), width, height);
        }
        return this;
    }

    public ImageAssertions hasKeyword(String expected) {
        if (!metaData.containsKey("Keywords")) {
            throw failure("Keyword metadata not found");
        }
        String keywords = metaData.get("Keywords");
        if (!keywords.contains(expected)) {
            throw failure("Keyword %s not found in %s", expected, keywords);
        }
        return this;
    }

    public ImageAssertions hasCaption(String expected) {
        if (!metaData.containsKey("Caption/Abstract")) {
            throw failure("Caption metadata not found");
        }
        String caption = metaData.get("Caption/Abstract");
        if (!caption.contains(expected)) {
            throw failure("Caption %s not found in %s", expected, expected);
        }
        return this;
    }

    public ImageAssertions hasAverageRgbDeltaLessThan(byte[] otherImageData, double maxDelta) {
        BufferedImage otherImage;
        try {
            otherImage = ImageIO.read(new ByteArrayInputStream(otherImageData));
        }
        catch (IOException e) {
            throw failureWithCause(e, "could not load compare image: " + e.getMessage());
        }

        if (image.getWidth() != otherImage.getWidth() || image.getHeight() != otherImage.getHeight()) {
            throw failure(
                "image sizes do not match: %dx%d vs %dx%d",
                image.getWidth(), image.getHeight(),
                otherImage.getWidth(), otherImage.getHeight()
            );
        }

        long totalPixels = (long) image.getWidth() * image.getHeight();
        double totalDelta = 0;

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb1 = image.getRGB(x, y);
                int rgb2 = otherImage.getRGB(x, y);

                int r1 = (rgb1 >> 16) & 0xFF;
                int g1 = (rgb1 >> 8) & 0xFF;
                int b1 = rgb1 & 0xFF;

                int r2 = (rgb2 >> 16) & 0xFF;
                int g2 = (rgb2 >> 8) & 0xFF;
                int b2 = rgb2 & 0xFF;

                double delta = Math.sqrt(
                    Math.pow(r1 - r2, 2) +
                        Math.pow(g1 - g2, 2) +
                        Math.pow(b1 - b2, 2)
                );

                totalDelta += delta;
            }
        }

        double averageDelta = totalDelta / totalPixels;

        if (averageDelta > maxDelta) {
            throw failure(
                "max delta (%.2f) beyond allowed (%.2f)",
                averageDelta, maxDelta
            );
        }

        return this;
    }

    @Override
    public byte[] actual() {
        return base.actual();
    }
}
