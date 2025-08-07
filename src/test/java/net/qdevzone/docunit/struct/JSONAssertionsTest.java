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
package net.qdevzone.docunit.struct;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jayway.jsonpath.PathNotFoundException;

import net.qdevzone.docunit.DocAssertions;

class JSONAssertionsTest {

    public class Meta {
        private String name;
        private String version;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

    }

    private byte[] filedata;

    @BeforeEach
    void setUp() throws Exception {
        filedata = Files.readAllBytes(Path.of("src", "test", "resources", "files", "test.json"));
    }

    @Test
    void testDocumentLoad() {
        DocAssertions.assertDoc(filedata)
            .asJson()
            .isValid();
    }

    @Test
    void testDocumentLoadFailNull() {
        Throwable ex = assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc((byte[]) null)
                .asJson()
                .isValid();
        });
        Logger.getGlobal().fine(ex.getMessage());

        DocAssertions.assertDoc((byte[]) null)
            .asJson()
            .isNotValid();
    }

    @Test
    void testDocumentContainsText() {
        DocAssertions.assertDoc(filedata)
            .asJson()
            .contains("countries");
    }

    @Test
    void testDocumentHasKey() {
        DocAssertions.assertDoc(filedata)
            .asJson()
            .hasKey("$.countries[4].name");
    }

    @Test
    void testDocumentHasKeyFail() {
        Throwable ex = assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc(filedata)
                .asJson()
                .hasKey("$.countries[4].nope");
        });
        assertThat(ex.getCause()).isOfAnyClassIn(PathNotFoundException.class);

        Logger.getGlobal().fine(ex.getMessage());
    }

    @Test
    void testDocumentHasStringValue() {
        DocAssertions.assertDoc(filedata)
            .asJson()
            .hasValue("$.countries[4].name", "Andorra");
    }

    @Test
    void testDocumentHasStringValueFail() {
        Throwable ex = assertThrows(AssertionError.class, () -> {
            DocAssertions.assertDoc(filedata)
                .asJson()
                .hasValue("$.countries[4].name", "Nope");
        });
        Logger.getGlobal().fine(ex.getMessage());
    }

    @Test
    void testDocumentContainsObjectValue() {
        Meta test = new Meta();
        test.setName("CountryInfo");
        test.setVersion("1.0");

        DocAssertions.assertDoc(filedata)
            .asJson()
            .hasValue("$.meta", test);
    }

    @Test
    void testDocumentArrayCount() {
        DocAssertions.assertDoc(filedata)
            .asJson()
            .hasArraySize("$.countries", 246);
    }

}
