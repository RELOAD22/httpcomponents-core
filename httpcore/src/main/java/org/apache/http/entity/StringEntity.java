/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.http.entity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;

/**
 * A self contained, repeatable entity that obtains its content from
 * a {@link String}.
 *
 * @since 4.0
 */
@NotThreadSafe
public class StringEntity extends AbstractHttpEntity {

    private final byte[] content;

    /**
     * Creates a StringEntity with the specified content and content type.
     *
     * @param string content to be used. Not {@code null}.
     * @param contentType content type to be used. May be {@code null}, in which case the default
     *   MIME type {@link ContentType#TEXT_PLAIN} is assumed.
     *
     * @throws IllegalArgumentException if the string parameter is null
     * this instance of the Java virtual machine
     * @since 4.2
     */
    public StringEntity(final String string, final ContentType contentType) {
        super();
        Args.notNull(string, "Source string");
        Charset charset = contentType != null ? contentType.getCharset() : null;
        if (charset == null) {
            charset = HTTP.DEF_CONTENT_CHARSET;
        }
        this.content = string.getBytes(charset);
        if (contentType != null) {
            setContentType(contentType.toString());
        }
    }

    /**
     * Creates a StringEntity with the specified content and charset. The MIME type defaults
     * to "text/plain".
     *
     * @param string content to be used. Not {@code null}.
     * @param charset character set to be used. May be {@code null}, in which case the default
     *   is {@link HTTP#DEF_CONTENT_CHARSET} is assumed
     *
     * @throws IllegalArgumentException if the string parameter is null
     *
     * @since 4.2
     */
    public StringEntity(final String string, final Charset charset) {
        this(string, ContentType.TEXT_PLAIN.withCharset(charset));
    }

    /**
     * Creates a StringEntity with the specified content. The content type defaults to
     * {@link ContentType#TEXT_PLAIN}.
     *
     * @param string content to be used. Not {@code null}.
     *
     * @throws IllegalArgumentException if the string parameter is null
     */
    public StringEntity(final String string) {
        this(string, ContentType.DEFAULT_TEXT);
    }

    @Override
    public boolean isRepeatable() {
        return true;
    }

    @Override
    public long getContentLength() {
        return this.content.length;
    }

    @Override
    public InputStream getContent() throws IOException {
        return new ByteArrayInputStream(this.content);
    }

    @Override
    public void writeTo(final OutputStream outstream) throws IOException {
        Args.notNull(outstream, "Output stream");
        outstream.write(this.content);
        outstream.flush();
    }

    /**
     * Tells that this entity is not streaming.
     *
     * @return {@code false}
     */
    @Override
    public boolean isStreaming() {
        return false;
    }

} // class StringEntity
