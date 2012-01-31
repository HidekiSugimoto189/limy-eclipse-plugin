/*
 * Created 2007/08/21
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
package org.limy.eclipse.code.preference;

import java.util.ArrayList;
import java.util.List;

/**
 * �p�����[�^��ʂ�\���܂��B
 * @author Naoki Iwami
 */
/* package */ enum Type {
    
    /** �N���X */
    CLAZZ("Class", 0),
    /** ��O */
    EXCEPTION("Exception", 1),
    /** byte */
    BYTE("byte", 2),
    /** short */
    SHORT("short", 3),
    /** int */
    INT("int", 4),
    /** long */
    LONG("long", 5),
    /** char */
    CHAR("char", 6),
    /** float */
    FLOAT("float", 7),
    /** double */
    DOUBLE("double", 8);
    
    // ------------------------ Fields
    
    /**
     * �\��������
     */
    private final String str;
    
    /**
     * ���l�i�R���{�{�b�N�X�p�j
     */
    private final int number;
    
    /**
     * @param str �\��������
     * @param number ���l�i�R���{�{�b�N�X�p�j
     */
    private Type(String str, int number) {
        this.str = str;
        this.number = number;
    }
    
    /**
     * �p�����[�^��ʂ��擾���܂��B
     * @param number ���l�i�R���{�{�b�N�X�p�j
     * @return �p�����[�^���
     */
    public static Type getType(int number) {
        for (Type type : Type.values()) {
            if (type.getValue() == number) {
                return type;
            }
        }
        return null;
    }
    
    /**
     * ���l���擾���܂��B
     * @return ���l
     */
    public int getValue() {
        return number;
    }
    
    /**
     * �\����������擾���܂��B
     * @return �\��������
     */
    public String getString() {
        return str;
    }
    
    /**
     * �\��������ꗗ���擾���܂��B
     * @return �\��������ꗗ
     */
    public static String[] getStrings() {
        List<String> list = new ArrayList<String>();
        for (Type type : Type.values()) {
            list.add(type.getString());
        }
        return list.toArray(new String[list.size()]);
    }
    
}