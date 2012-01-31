/*
 * Created 2005/07/21
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
package org.limy.eclipse.common.jface;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.TextAttribute;

/**
 * �����̕���������\������N���X�ł��B
 * @author Naoki Iwami
 */
public class MultiTextAttribute /*extends TextAttribute*/ {
    
    /**
     * �������X�g
     */
    private List<TextAttribute> textAttributes;
    
    /**
     * �ʒu���X�g
     */
    private List<Position> positions;
    
    /**
     * MultiTextAttribute�C���X�^���X���\�z���܂��B
     */
    public MultiTextAttribute() {
//        super(new Color(null, 0, 0, 0));
        textAttributes = new ArrayList<TextAttribute>();
        positions = new ArrayList<Position>();
    }

    /**
     * �C���X�^���X�ɑ�����ǉ����܂��B
     * @param attribute ����
     * @param length ������
     */
    public void add(TextAttribute attribute, int length) {
        if (positions.isEmpty()) {
            positions.add(new Position(0, length));
        } else {
            Position pos = positions.get(positions.size() - 1);
            int offset = pos.getOffset() + pos.getLength();
            if (offset < 0 || length - offset < 0) {
//                LimyEclipsePluginUtils.log("offset = " + offset + ", length = " + (length - offset));
                return;
            } else {
                positions.add(new Position(offset, length - offset));
            }
        }
        textAttributes.add(attribute);
    }

    /**
     * �C���X�^���X�ɕ���������ǉ����܂��B
     * @param multiAttr ��������
     * @param adjustOffset �����I�t�Z�b�g�l
     */
    public void addMulti(MultiTextAttribute multiAttr, int adjustOffset) {

        int offset;
        if (positions.isEmpty()) {
            offset = 0;
        } else {
            Position pos = positions.get(positions.size() - 1);
            offset = pos.getOffset() + pos.getLength();
        }
        offset += adjustOffset;
        
        for (int i = 0; i < multiAttr.size(); i++) {
            TextAttribute attr = multiAttr.get(i);
            int multiOffset = multiAttr.getOffset(i);
            int multiLength = multiAttr.getLength(i);
            positions.add(new Position(offset + multiOffset, multiLength));
            textAttributes.add(attr);
        }
    }

    /**
     * �������擾���܂��B
     * @param index �C���f�b�N�X�ԍ�
     * @return ����
     */
    public TextAttribute get(int index) {
        return textAttributes.get(index);
    }

    /**
     * �I�t�Z�b�g���擾���܂��B
     * @param index �C���f�b�N�X�ԍ�
     * @return �I�t�Z�b�g
     */
    public int getOffset(int index) {
        return positions.get(index).getOffset();
    }

    /**
     * ���������擾���܂��B
     * @param index �C���f�b�N�X�ԍ�
     * @return ������
     */
    public int getLength(int index) {
        return positions.get(index).getLength();
    }
    
    /**
     * �ێ����鑮���̃T�C�Y���擾���܂��B
     * @return �����̃T�C�Y
     */
    public int size() {
        return textAttributes.size();
    }
}
