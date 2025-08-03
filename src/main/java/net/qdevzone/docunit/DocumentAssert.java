package net.qdevzone.docunit;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;

import net.qdevzone.docunit.archive.ZipAssertions;
import net.qdevzone.docunit.doc.WordAssertions;
import net.qdevzone.docunit.image.ImageAssertions;
import net.qdevzone.docunit.pdf.PdfAssertions;
import net.qdevzone.docunit.spreadsheet.ExcelAssertions;
import net.qdevzone.docunit.struct.CSVAssertions;
import net.qdevzone.docunit.struct.JSONAssertions;
import net.qdevzone.docunit.struct.XmlAssertions;
import net.qdevzone.docunit.text.TextAssertions;

public class DocumentAssert extends AbstractDocAssert<DocumentAssert> {

    public enum FileType {
        WORD,
        EXCEL,
        CSV,
        JSON,
        XML,
        PDF,
        PNG,
        JPG,
        GIF,
        RTF,
        ZIP
    }

    private byte[] data;

    protected DocumentAssert(byte[] data) {
        super(DocumentAssert.class);
        this.data = data;
    }

    public static DocumentAssert forData(byte[] actual) {
        return new DocumentAssert(actual);
    }

    @Override
    public DocumentAssert isValid() {
        if (data == null) {
            throw failure("null");
        }
        return myself;
    }

    @Override
    public DocumentAssert isNotValid() {
        if (data != null) {
            throw failure("not null");
        }
        return myself;
    }

    public DocumentAssert isOfType(FileType... types) {
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(data);
            TikaConfig tika = new TikaConfig();
            Metadata metadata = new Metadata();
            MediaType mimetype = tika.getDetector().detect(TikaInputStream.get(is), metadata);
            is.close();

            String mimeTypeString = mimetype.toString();
            boolean found = false;

            for (FileType type : types) {
                List<String> checkType = new ArrayList<>();
                switch (type) {
                    case CSV:
                        checkType.add(MimeType.Text.PLAIN);
                        checkType.add(MimeType.Text.CSV);
                        break;
                    case EXCEL:
                        checkType.add(MimeType.Application.VND_OPENXMLFORMATS_OFFICEDOCUMENT_SPREADSHEETML_SHEET);
                        break;
                    case GIF:
                        checkType.add(MimeType.Image.GIF);
                        break;
                    case JPG:
                        checkType.add(MimeType.Image.JPEG);
                        break;
                    case PNG:
                        checkType.add(MimeType.Image.PNG);
                        break;
                    case JSON:
                        checkType.add(MimeType.Text.PLAIN);
                        break;
                    case PDF:
                        checkType.add(MimeType.Application.PDF);
                        break;
                    case RTF:
                        checkType.add(MimeType.Application.RTF);
                        break;
                    case WORD:
                        checkType.add(MimeType.Application.VND_OPENXMLFORMATS_OFFICEDOCUMENT_WORDPROCESSINGML_DOCUMENT);
                        break;
                    case XML:
                        checkType.add(MimeType.Text.PLAIN);
                        checkType.add(MimeType.Text.XML);
                        break;
                    case ZIP:
                        checkType.add(MimeType.Application.ZIP);
                        break;
                }
                if (checkType.contains(mimeTypeString)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw failure("content not detected as any of %s", Arrays.toString(types));
            }
        }
        catch (IOException | TikaException e) {
            e.printStackTrace();
        }

        return this;
    }

    public ImageAssertions asGif() {
        return new ImageAssertions(this, FileType.GIF);
    }

    public ImageAssertions asJpeg() {
        return new ImageAssertions(this, FileType.JPG);
    }

    public ImageAssertions asPng() {
        return new ImageAssertions(this, FileType.PNG);
    }

    public WordAssertions asWord() {
        return new WordAssertions(this);
    }

    public ExcelAssertions asExcel() {
        return new ExcelAssertions(this);
    }

    public PdfAssertions asPdf() {
        return new PdfAssertions(this);
    }

    public TextAssertions asText() {
        return new TextAssertions(this);
    }

    public CSVAssertions asCsv() {
        return asCsv(",", true);
    }

    public CSVAssertions asCsv(String delimiter) {
        return asCsv(delimiter, true);
    }

    public CSVAssertions asCsv(String delimiter, boolean hasHeaders) {
        return new CSVAssertions(this, delimiter, hasHeaders);
    }

    public CSVAssertions asCsv(String delimiter, boolean hasHeaders, Charset charset) {
        return new CSVAssertions(this, delimiter, hasHeaders, charset);
    }

    public JSONAssertions asJson() {
        return new JSONAssertions(this);
    }

    public XmlAssertions asXml() {
        return new XmlAssertions(this);
    }

    public ZipAssertions asZip() {
        return new ZipAssertions(this);
    }

    @Override
    public byte[] actual() {
        return data;
    }
}
