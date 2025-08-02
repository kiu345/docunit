package net.qdevzone.docunit.struct;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

import net.qdevzone.docunit.AbstractDocAssert;
import net.qdevzone.docunit.DocumentAssert;

public class XmlAssertions extends AbstractDocAssert<XmlAssertions> {
    private final Document document;
    private final DocumentAssert base;
    private Throwable loadError;

    public XmlAssertions(DocumentAssert base) {
        super(XmlAssertions.class);
        document = parseXml(base.actual());
        this.base = base;
    }

    private Document parseXml(byte[] data) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(new ByteArrayInputStream(data));
        }
        catch (Exception e) {
            loadError = e;
            return null;
        }
    }

    public XmlAssertions hasXPath(String expression, String expectedValue) {
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            XPathExpression expr = xpath.compile(expression);
            String result = expr.evaluate(document).trim();

            if (!expectedValue.equals(result)) {
                throw failure("xpath '%s' expected <%s>, got <%s>.", expression, expectedValue, result);
            }
        }
        catch (Exception e) {
            throw failure("error in xpath expression '%s': %s", expression, e.getMessage());
        }
        return this;
    }

    public XmlAssertions hasRootElement(String expectedName) {
        String actual = document.getDocumentElement().getTagName();
        if (!expectedName.equals(actual)) {
            throw failure("expected root element <%s> does not match <%s>.", expectedName, actual);
        }
        return this;
    }

    @Override
    public XmlAssertions isValid() {
        if (loadError != null) {
            throw failure("unloadable document: %s", loadError.getMessage());
        }
        if (document == null) {
            throw failure("document loader returned null");
        }
        return this;
    }

    @Override
    public XmlAssertions isNotValid() {
        if (loadError == null && document != null) {
            throw failure("document was valid");
        }
        return this;
    }

    public XmlAssertions contains(CharSequence... expected) {
        String fullText = document.getDocumentElement().getTextContent();
        assertThat(fullText).contains(expected);
        return this;
    }

    @Override
    public byte[] actual() {
        return base.actual();
    }
}
