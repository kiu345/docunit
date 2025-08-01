package net.qdevzone.docunit.pdf;

import java.awt.geom.Point2D.Float;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.contentstream.PDContentStream;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.contentstream.operator.color.SetNonStrokingColor;
import org.apache.pdfbox.contentstream.operator.color.SetNonStrokingColorN;
import org.apache.pdfbox.contentstream.operator.color.SetNonStrokingColorSpace;
import org.apache.pdfbox.contentstream.operator.color.SetNonStrokingDeviceCMYKColor;
import org.apache.pdfbox.contentstream.operator.color.SetNonStrokingDeviceGrayColor;
import org.apache.pdfbox.contentstream.operator.color.SetNonStrokingDeviceRGBColor;
import org.apache.pdfbox.contentstream.operator.color.SetStrokingColor;
import org.apache.pdfbox.contentstream.operator.color.SetStrokingColorN;
import org.apache.pdfbox.contentstream.operator.color.SetStrokingColorSpace;
import org.apache.pdfbox.contentstream.operator.color.SetStrokingDeviceCMYKColor;
import org.apache.pdfbox.contentstream.operator.color.SetStrokingDeviceGrayColor;
import org.apache.pdfbox.contentstream.operator.color.SetStrokingDeviceRGBColor;
import org.apache.pdfbox.contentstream.operator.markedcontent.BeginMarkedContentSequence;
import org.apache.pdfbox.contentstream.operator.markedcontent.BeginMarkedContentSequenceWithProperties;
import org.apache.pdfbox.contentstream.operator.markedcontent.EndMarkedContentSequence;
import org.apache.pdfbox.contentstream.operator.state.Concatenate;
import org.apache.pdfbox.contentstream.operator.state.Restore;
import org.apache.pdfbox.contentstream.operator.state.Save;
import org.apache.pdfbox.contentstream.operator.state.SetFlatness;
import org.apache.pdfbox.contentstream.operator.state.SetGraphicsStateParameters;
import org.apache.pdfbox.contentstream.operator.state.SetLineCapStyle;
import org.apache.pdfbox.contentstream.operator.state.SetLineDashPattern;
import org.apache.pdfbox.contentstream.operator.state.SetLineJoinStyle;
import org.apache.pdfbox.contentstream.operator.state.SetLineMiterLimit;
import org.apache.pdfbox.contentstream.operator.state.SetLineWidth;
import org.apache.pdfbox.contentstream.operator.state.SetMatrix;
import org.apache.pdfbox.contentstream.operator.state.SetRenderingIntent;
import org.apache.pdfbox.contentstream.operator.text.BeginText;
import org.apache.pdfbox.contentstream.operator.text.EndText;
import org.apache.pdfbox.contentstream.operator.text.MoveText;
import org.apache.pdfbox.contentstream.operator.text.MoveTextSetLeading;
import org.apache.pdfbox.contentstream.operator.text.NextLine;
import org.apache.pdfbox.contentstream.operator.text.SetCharSpacing;
import org.apache.pdfbox.contentstream.operator.text.SetFontAndSize;
import org.apache.pdfbox.contentstream.operator.text.SetTextHorizontalScaling;
import org.apache.pdfbox.contentstream.operator.text.SetTextLeading;
import org.apache.pdfbox.contentstream.operator.text.SetTextRenderingMode;
import org.apache.pdfbox.contentstream.operator.text.SetTextRise;
import org.apache.pdfbox.contentstream.operator.text.SetWordSpacing;
import org.apache.pdfbox.contentstream.operator.text.ShowText;
import org.apache.pdfbox.contentstream.operator.text.ShowTextAdjusted;
import org.apache.pdfbox.contentstream.operator.text.ShowTextLine;
import org.apache.pdfbox.contentstream.operator.text.ShowTextLineAndSpace;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDType3CharProc;
import org.apache.pdfbox.pdmodel.graphics.form.PDTransparencyGroup;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceStream;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import org.apache.pdfbox.util.Matrix;

import net.qdevzone.docunit.AbstractDocAssert;
import net.qdevzone.docunit.DocumentAssert;
import net.qdevzone.docunit.PagedDocAsserts;

public class PdfAssertions extends AbstractDocAssert<PdfAssertions> implements PagedDocAsserts<PdfAssertions> {

    private class Engine extends PDFTextStripper {

        class TextElement {
            String text;
            Rectangle2D location;

            public TextElement(String text, Rectangle2D location) {
                super();
                this.text = text;
                this.location = location;
            }

        }

        private List<TextElement> foundElements = new ArrayList<>();

        public Engine() {
            addOperator(new BeginText(this));
            addOperator(new Concatenate(this));
            addOperator(new SetStrokingColorSpace(this));
            addOperator(new SetNonStrokingColorSpace(this));
            addOperator(new SetLineDashPattern(this));
            addOperator(new EndText(this));
            addOperator(new SetStrokingDeviceGrayColor(this));
            addOperator(new SetNonStrokingDeviceGrayColor(this));
            addOperator(new SetGraphicsStateParameters(this));
            addOperator(new SetFlatness(this));
            addOperator(new SetLineJoinStyle(this));
            addOperator(new SetLineCapStyle(this));
            addOperator(new SetStrokingDeviceCMYKColor(this));
            addOperator(new SetNonStrokingDeviceCMYKColor(this));
            addOperator(new SetLineMiterLimit(this));
            addOperator(new Save(this));
            addOperator(new Restore(this));
            addOperator(new SetStrokingDeviceRGBColor(this));
            addOperator(new SetNonStrokingDeviceRGBColor(this));
            addOperator(new SetRenderingIntent(this));
            addOperator(new SetStrokingColor(this));
            addOperator(new SetNonStrokingColor(this));
            addOperator(new SetStrokingColorN(this));
            addOperator(new SetNonStrokingColorN(this));
            addOperator(new NextLine(this));
            addOperator(new SetCharSpacing(this));
            addOperator(new MoveText(this));
            addOperator(new MoveTextSetLeading(this));
            addOperator(new SetFontAndSize(this));
            addOperator(new ShowText(this));
            addOperator(new ShowTextAdjusted(this));
            addOperator(new SetTextLeading(this));
            addOperator(new SetMatrix(this));
            addOperator(new SetTextRenderingMode(this));
            addOperator(new SetTextRise(this));
            addOperator(new SetWordSpacing(this));
            addOperator(new SetTextHorizontalScaling(this));
            addOperator(new SetLineWidth(this));
            addOperator(new ShowTextLine(this));
            addOperator(new ShowTextLineAndSpace(this));
            addOperator(new BeginMarkedContentSequence(this));
            addOperator(new BeginMarkedContentSequenceWithProperties(this));
            addOperator(new EndMarkedContentSequence(this));
            output = new StringWriter();
        }

        @Override
        protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
//            System.out.println("PdfAssertions.Engine.writeString(%s, %s)".formatted(text, posString(textPositions)));
//            System.out.println("### " + calculateArea(textPositions).toString());
            foundElements.add(new TextElement(text, calculateArea(textPositions)));
            super.writeString(text, textPositions);
        }

//        protected String posString(List<TextPosition> textPositions) {
//            StringBuilder builder = new StringBuilder();
//            builder.append("[");
//            for (TextPosition pos : textPositions) {
//                builder.append("{");
//                builder.append(pos.getX() + ":" + pos.getY());
//                builder.append("|");
//                builder.append(pos.getEndX() + ":" + pos.getEndY());
//                builder.append("}");
//            }
//            builder.append("](");
//            builder.append(textPositions.size());
//            builder.append(")");
//            return builder.toString();
//        }

        protected Rectangle2D calculateArea(List<TextPosition> textPositions) {
            float minX, minY, maxX, maxY;
            TextPosition first = textPositions.getFirst();
            minX = first.getX();
            minY = first.getY();
            maxX = minX;
            maxY = maxX;

            for (TextPosition pos : textPositions) {
                if (pos.getX() < minX) {
                    minX = pos.getX();
                }
                if (pos.getY() < minY) {
                    minX = pos.getY();
                }
                float endX = pos.getX() + pos.getWidth();
                float endY = pos.getY() + pos.getHeight();

                if (endX > maxX) {
                    maxX = endX;
                }
                if (pos.getEndY() > maxY) {
                    maxY = endY;
                }
            }

            return new Rectangle2D.Float(minX, minY, maxX - minX, maxY - minY);
        }

        @Override
        protected void writeString(String text) throws IOException {
//            System.out.println("PdfAssertions.Engine.writeString(%s)".formatted(text));
            super.writeString(text);
        }

        @Override
        protected void processTextPosition(TextPosition text) {
//            System.out.println("PdfAssertions.Engine.processTextPosition()");
            super.processTextPosition(text);
        }

        @Override
        public void beginText() throws IOException {
//            System.out.println("PdfAssertions.Engine.beginText()");
            super.beginText();
        }

        @Override
        public void endText() throws IOException {
//            System.out.println("PdfAssertions.Engine.endText()");
            super.endText();
        }

        @Override
        public void showTextString(byte[] string) throws IOException {
//            System.out.println("PdfAssertions.Engine.showTextString(%s)".formatted(new String(string)));
            super.showTextString(string);
        }

        @Override
        public void showTextStrings(COSArray array) throws IOException {
//            System.out.println("PdfAssertions.Engine.showTextStrings()");
            super.showTextStrings(array);
        }

        @Override
        protected void showText(byte[] string) throws IOException {
//            System.out.println("PdfAssertions.Engine.showText(" + Arrays.toString(string) + ")");
            super.showText(string);
        }

        @Override
        public void processPage(PDPage page) throws IOException {
//            System.out.println("PdfAssertions.Engine.processPage()");
            super.processPage(page);
        }

        @Override
        protected void processSoftMask(PDTransparencyGroup group) throws IOException {
//            System.out.println("PdfAssertions.Engine.processSoftMask()");
            super.processSoftMask(group);
        }

        @Override
        protected void processTransparencyGroup(PDTransparencyGroup group) throws IOException {
//            System.out.println("PdfAssertions.Engine.processTransparencyGroup()");
            super.processTransparencyGroup(group);
        }

        @Override
        protected void processType3Stream(PDType3CharProc charProc, Matrix textRenderingMatrix) throws IOException {
//            System.out.println("PdfAssertions.Engine.processType3Stream()");
            super.processType3Stream(charProc, textRenderingMatrix);
        }

        @Override
        protected void processAnnotation(PDAnnotation annotation, PDAppearanceStream appearance) throws IOException {
//            System.out.println("PdfAssertions.Engine.processAnnotation()");
            super.processAnnotation(annotation, appearance);
        }

        @Override
        protected void processChildStream(PDContentStream contentStream, PDPage page) throws IOException {
//            System.out.println("PdfAssertions.Engine.processChildStream()");
            super.processChildStream(contentStream, page);
        }

        @Override
        protected void applyTextAdjustment(float tx, float ty) {
//            System.out.println("PdfAssertions.Engine.applyTextAdjustment()");
            super.applyTextAdjustment(tx, ty);
        }

        @Override
        public void processOperator(String operation, List<COSBase> arguments) throws IOException {
//            System.out.println("PdfAssertions.Engine.processOperator(%s)".formatted(operation));
            super.processOperator(operation, arguments);
        }

        @Override
        protected void processOperator(Operator operator, List<COSBase> operands) throws IOException {
//            System.out.println("PdfAssertions.Engine.processOperator(%s)".formatted(operator.getName()));
            super.processOperator(operator, operands);
        }

        @Override
        public PDPage getCurrentPage() {
//            System.out.println("PdfAssertions.Engine.getCurrentPage()");
            return super.getCurrentPage();
        }

        @Override
        public Float transformedPoint(float x, float y) {
//            System.out.println("PdfAssertions.Engine.transformedPoint()");
            return super.transformedPoint(x, y);
        }

        @Override
        protected float transformWidth(float width) {
//            System.out.println("PdfAssertions.Engine.transformWidth()");
            return super.transformWidth(width);
        }

        @Override
        public void markedContentPoint(COSName tag, COSDictionary properties) {
//            System.out.println("PdfAssertions.Engine.markedContentPoint()");
            super.markedContentPoint(tag, properties);
        }

        @Override
        public void beginMarkedContentSequence(COSName tag, COSDictionary properties) {
//            System.out.println("PdfAssertions.Engine.beginMarkedContentSequence()");
            super.beginMarkedContentSequence(tag, properties);
        }

        @Override
        public void endMarkedContentSequence() {
//            System.out.println("PdfAssertions.Engine.beginMarkedContentSequence()");
            super.endMarkedContentSequence();
        }

        public String getContent() {
            return output.toString();
        }
    }

    private final PDDocument document;
    private final DocumentAssert base;
    private Throwable loadError;

    public PdfAssertions(DocumentAssert base) {
        super(PdfAssertions.class);
        PDDocument loadadDoc = null;
        try {
            loadadDoc = Loader.loadPDF(base.actual());
        }
        catch (IOException | NullPointerException ex) {
            loadError = ex;
        }
        this.document = loadadDoc;
        this.base = base;

    }

    @Override
    public PdfAssertions isValid() {
        if (loadError != null) {
            throw failure("unloadable document: %s", loadError.getMessage());
        }
        if (document == null) {
            throw failure("document loader returned null");
        }
        return this;
    }

    @Override
    public PdfAssertions isNotValid() {
        if (loadError == null && document != null) {
            throw failure("document was valid");
        }
        return this;
    }

    @Override
    public byte[] actual() {
        return base.actual();
    }

    public PdfAssertions hasPages() {
        if (document.getNumberOfPages() == 0) {
            throw failure("no pages found");
        }
        return this;
    }

    public PdfAssertions hasPageCount(int count) {
        int actualPages = document.getNumberOfPages();
        if (actualPages != count) {
            throw failure("page count %d differs from expected %d", actualPages, count);
        }
        return this;
    }

    public PdfAssertions hasPageCount(int min, int max) {
        int actualPages = document.getNumberOfPages();
        if (actualPages < min || actualPages > max) {
            throw failure("page count not in range %d >[ %d ]> %d", min, actualPages, max);
        }
        return this;
    }

    public PdfAssertions hasTextInPage(int pageNum, CharSequence search) {
        PDPage page = document.getPage(pageNum - 1);

        var pdfEngine = new Engine();
        try {
            pdfEngine.processPage(page);
        }
        catch (IOException e) {
            throw failure("page processing error: %s", e.getMessage());
        }
        String pageText = pdfEngine.getContent();
        if (!pageText.contains(search)) {
            throw failure("search string '%s' not found", search);
        }

        return this;
    }

    public PdfAssertions hasTextInPage(int pageNum, CharSequence search, Rectangle2D area) {
        return hasTextInPage(pageNum, search, area, false);
    }

    public PdfAssertions hasTextInPage(int pageNum, CharSequence search, Rectangle2D area, boolean fullyCovertArea) {
        PDPage page = document.getPage(pageNum - 1);

        var pdfEngine = new Engine();
        try {
            pdfEngine.processPage(page);
        }
        catch (IOException e) {
            throw failure("page processing error: %s", e.getMessage());
        }
        for (var element : pdfEngine.foundElements) {
            if (element.text.contains(search)) {
                if (Logger.getGlobal().isLoggable(Level.INFO)) {
                    Logger.getGlobal().info("found text at " + element.location);
                }
                if (fullyCovertArea) {
                    if (area.contains(element.location)) {
                        return this;
                    }
                }
                else {
                    if (area.intersects(element.location)) {
                        return this;
                    }

                }
            }
        }
        throw failure("search string '%s' not found", search);
    }

}
