/*
 * Created 2007/08/30
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
package org.limy.eclipse.qalab.outline;

import java.awt.geom.Rectangle2D.Double;

import org.w3c.dom.Element;

/**
 * �s�ԍ������������N���b�J�u���ʒu����\���܂��B
 * @author Naoki Iwami
 */
public class LineClickPoint extends ClickablePointInfo {
    
    /**
     * �쐬�S���N���X�ł��B
     * @author Naoki Iwami
     */
    static class Creator implements PointInfoCreator {

        public ClickablePointInfo create(Element el, Double rect) {
            String elementName = el.getFirstChild().getNodeValue().trim();
            LineClickPoint info = new LineClickPoint();
            info.setRect(rect);
            if (elementName.startsWith("[L.")) {
                info.setLineNumber(
                        Integer.parseInt(elementName.substring(3, elementName.indexOf(']'))));
            }
            return info;
        }
    }

    // ------------------------ Fields

    /** �s�ԍ� */
    private int lineNumber;

    // ------------------------ Getter/Setter Methods

    /**
     * �s�ԍ����擾���܂��B
     * @return �s�ԍ�
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * �s�ԍ���ݒ肵�܂��B
     * @param lineNumber �s�ԍ�
     */
    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }
    
}