/*
 * Created 2006/11/22
 * Copyright (C) 2003-2009  Naoki Iwami (naoki@limy.org)
 *
 * This file is part of Limy Eclipse Plugin.
 *
 * Limy Eclipse Plugin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Limy Eclipse Plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Limy Eclipse Plugin.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.limy.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * XML�֘A�̃��[�e�B���e�B�N���X�ł��B
 * @author Naoki Iwami
 */
public final class XmlUtils {

    // ------------------------ Constants

    /** XML�h�L�������g�r���_ */
    private static DocumentBuilder builder;

    // ------------------------ Constructors

    /**
     * private constructor
     */
    private XmlUtils() { }

    // ------------------------ Static Inititalizer
    
    static {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringComments(false);
        factory.setCoalescing(true);
        factory.setValidating(false);
        factory.setNamespaceAware(false);
        try {
            builder = factory.newDocumentBuilder();
            builder.setEntityResolver(new EntityResolver() {

                public InputSource resolveEntity(String publicId,
                        String systemId) throws SAXException, IOException {
                    return new InputSource(new StringReader(""));
                }
                
            });
        } catch (ParserConfigurationException e) {
            e.printStackTrace(); // ���O���g���Ȃ��̂ŕW���G���[�o�͂ɓf��
        }
        
    }
    
    // ------------------------ Public Methods
    
    /**
     * XML���p�[�X����DOM Element��Ԃ��܂��B
     * @param xmlInput XML���̓X�g���[��
     * @return DOM Element
     * @throws IOException I/O��O
     */
    public static Element parse(InputStream xmlInput) throws IOException {
        try {
            Document doc = builder.parse(xmlInput);
            return doc.getDocumentElement();
        } catch (SAXException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * XML���p�[�X����DOM Element��Ԃ��܂��B
     * @param xmlFile XML�t�@�C��
     * @return DOM Element
     * @throws IOException I/O��O
     */
    public static Element parse(File xmlFile) throws IOException {
        try {
            Document doc = builder.parse(xmlFile);
            return doc.getDocumentElement();
        } catch (SAXException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * XML�v�f���쐬���܂��B
     * @param parent �e�v�f
     * @param name �v�f��
     * @return �쐬���ꂽ�v�f
     */
    public static XmlElement createElement(XmlElement parent, String name) {
        return new SimpleElement(parent, name);
    }

    /**
     * XML�v�f���쐬���܂��B
     * @param name �v�f��
     * @return �쐬���ꂽ�v�f
     */
    public static XmlElement createElement(String name) {
        return new SimpleElement(name);
    }

    /**
     * XML�̑����l�Ɏg�p���镶������G�X�P�[�v���܂��B
     * @param str ������
     * @return �G�X�P�[�v����������
     */
    public static String escapeAttributeValue(String str) {
        if (str == null) {
            return null;
        }
        return str.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;")
                .replaceAll("\"", "&quot;").replaceAll("'", "&apos;");
        
    }

}
