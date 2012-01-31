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
package org.limy.eclipse.qalab.common;

import org.eclipse.jdt.core.IJavaElement;
import org.w3c.dom.Element;

/**
 * SVG���������͂���C���^�[�t�F�C�X�ł��B
 * @author Naoki Iwami
 */
public interface ClickableXmlParser {

    /**
     * XML�v�f����AJava���S���薼���擾���܂��B
     * @param el XML�v�f
     * @return Java���S���薼
     */
    String getQualifiedName(Element el);

    /**
     * XML�v�f�ɑΉ�����c�[���`�b�v������𐶐����܂��B
     * @param javaElement XML�v�f
     * @return �c�[���`�b�v������
     */
    String getTooltip(IJavaElement javaElement);

}