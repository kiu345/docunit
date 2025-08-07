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
package net.qdevzone.docunit.text;

import static org.assertj.core.api.Assertions.assertThat;

import net.qdevzone.docunit.AbstractDocAssert;
import net.qdevzone.docunit.DocumentAssert;

public class TextAssertions extends AbstractDocAssert<TextAssertions> {
    private final String document;
    private final DocumentAssert base;

    public TextAssertions(DocumentAssert base) {
        super(TextAssertions.class);
        this.document = (base.actual() != null) ? new String(base.actual()) : null;
        this.base = base;
    }

    @Override
    public TextAssertions isValid() {
        if (document == null) {
            throw failure("document is null");
        }
        return this;
    }

    @Override
    public TextAssertions isNotValid() {
        if (document != null) {
            throw failure("document was valid");
        }
        return this;
    }

    public TextAssertions contains(CharSequence... expected) {
        assertThat(document).contains(expected);
        return this;
    }

    @Override
    public byte[] actual() {
        return base.actual();
    }
}
