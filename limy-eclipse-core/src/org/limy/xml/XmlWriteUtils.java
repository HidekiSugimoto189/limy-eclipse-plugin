/*
 * Created 2007/08/14
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

import java.io.IOException;
import java.util.Arrays;

/**
 * XML�o�͊֘A�̃��[�e�B���e�B�N���X�ł��B
 * @author Naoki Iwami
 * @depend - - - XmlElement
 */
public final class XmlWriteUtils {
    
    /**
     * ���s����
     */
    private static final String BR = "\n";

    /**
     * private constructor
     */
    private XmlWriteUtils() { }

    /**
     * XML�v�f�̓��e���o�͂��܂��B
     * @param out �o�͐�
     * @param root �o�͓��e
     * @throws IOException I/O��O
     */
    public static void writeXml(Appendable out, XmlElement root) throws IOException {
        writeXml(out, (SimpleElement)root, 0);
    }

    /**
     * XML�v�f�̓��e���o�͂��܂��B
     * @param out �o�͐�
     * @param root �o�͓��e
     * @param index �C���f�b�N�X
     * @throws IOException I/O��O
     */
    public static void writeXml(Appendable out, XmlElement root, int index)
            throws IOException {
        
        char[] spaceChar = new char[index * 2];
        Arrays.fill(spaceChar, ' ');
        String space = new String(spaceChar);
        
        out.append(space);
        writeStartTag(out, root);
        
        if (root.getValue() == null && !root.hasChildren()) {
            // �v�f�l�������A�q�v�f�������ꍇ
            out.append("/>").append(BR);
            return;
        }
        
        out.append('>');
        
        if (root.getValue() != null) {
            // �v�f�l������ꍇ�A������o�͂��ďI���i�q�v�f�͏o�͂��Ȃ��j
            out.append(root.getValue());
            out.append("</");
            out.append(root.getName());
            out.append('>');
            out.append(BR);
            return;
        }
        
        out.append(BR);
        
        writeChildren(out, root, index);
        
        out.append(space);
        out.append("</");
        out.append(root.getName());
        out.append('>');
        out.append(BR);
    
    }

    // ------------------------ Private Methods

    /**
     * XML�v�f������ё������o�͂��܂��B
     * <p>
     * �Ō�� &gt; �͏o�͂��܂���B<br>
     * &lt;tag value="abc" ... �����܂�
     * </p>
     * @param out �o�͐�
     * @param el XML�v�f
     * @throws IOException I/O��O
     */
    private static void writeStartTag(Appendable out, XmlElement el)
            throws IOException {
        
        out.append('<');
        out.append(el.getName());
        if (el.hasAttributes()) {
            for (XmlAttribute attr : el.getAttributes()) {
                out.append(' ');
                out.append(attr.getName());
                out.append("=\"");
                out.append(attr.getValue());
                out.append('"');
            }
        }
    }

    /**
     * XML�̎q�v�f���o�͂��܂��B
     * @param out �o�͐�
     * @param el XML�v�f
     * @param tabIndex �^�u�C���f�b�N�X
     * @throws IOException 
     */
    private static void writeChildren(Appendable out, XmlElement el,
            int tabIndex) throws IOException {
        
        if (el.hasChildren()) {
            for (XmlElement child : el.getChildren()) {
                writeXml(out, child, tabIndex + 1);
            }
        }
    }

}
