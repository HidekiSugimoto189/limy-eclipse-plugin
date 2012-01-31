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
package org.limy.eclipse.code.preference;


import org.limy.eclipse.common.jface.ITableObject;

/**
 * Javadoc�����x������\���e�[�u���I�u�W�F�N�g�N���X�ł��B
 * @author Naoki Iwami
 */
public class JavadocTableObject implements ITableObject, Comparable<JavadocTableObject> {
    
    // ------------------------ Classes
    
    /**
     * ���
     */
    private Type type;
    
    /**
     * �N���X��
     */
    private String className;
    
    /**
     * �t�B�[���h��
     */
    private String fieldName;
    
    /**
     * Javadoc�R�����g
     */
    private String javadocComment;
    
    // ------------------------ Constructors

    /**
     * JavadocTableObject�C���X�^���X���\�z���܂��B
     * @param type ���
     * @param className �N���X��
     * @param fieldName �t�B�[���h��
     * @param javadocComment Javadoc�R�����g
     */
    public JavadocTableObject(
            Type type,
            String className,
            String fieldName,
            String javadocComment) {
        this.type = type;
        this.className = className;
        this.fieldName = fieldName;
        this.javadocComment = javadocComment;
    }
    
   
    // ------------------------ Implement Methods
    
    public Object getValue(int index) {
        Object r = null;
        switch (index) {
        case 0:
            r = Integer.valueOf(getType().getValue());
            break;
        case 1:
            r = getClassName();
            break;
        case 2:
            r = getFieldName();
            break;
        case 3:
            r = getJavadocComment();
            break;
        default:
            break;
        }
        return r;
    }

    public String getViewString(int index) {
        String r = null;
        switch (index) {
        case 0:
            r = getType().getString();
            break;
        case 1:
            r = getClassName();
            break;
        case 2:
            r = getFieldName();
            break;
        case 3:
            r = getJavadocComment();
            break;
        default:
            break;
        }
        return r;
    }
    
    public int getColumnSize() {
        return 10;
    }

    public void setValue(int index, Object value) {
        switch (index) {
        case 0:
            int typeNumber = ((Integer)value).intValue();
            setType(Type.getType(typeNumber));
            if (typeNumber >= 2/*Primitive*/) {
                setClassName("");
            }
            break;
        case 1:
            setClassName((String)value);
            break;
        case 2:
            setFieldName((String)value);
            break;
        case 3:
            setJavadocComment((String)value);
            break;
        default:
            break;
        }
    }

    public int compareTo(JavadocTableObject o) {
        int c1 = getType().getValue() - o.getType().getValue();
        if (c1 != 0) {
            return c1;
        }
        return getClassName().compareTo(o.getClassName());
    }
    
    // ------------------------ Getter/Setter Methods

    /**
     * ��ʂ��擾���܂��B
     * @return ���
     */
    public Type getType() {
        return type;
    }

    /**
     * ��ʂ�ݒ肵�܂��B
     * @param type ���
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * �N���X�����擾���܂��B
     * @return �N���X��
     */
    public String getClassName() {
        return className;
    }

    /**
     * �N���X����ݒ肵�܂��B
     * @param className �N���X��
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * �t�B�[���h�����擾���܂��B
     * @return �t�B�[���h��
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * �t�B�[���h����ݒ肵�܂��B
     * @param fieldName �t�B�[���h��
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * Javadoc�R�����g���擾���܂��B
     * @return Javadoc�R�����g
     */
    public String getJavadocComment() {
        return javadocComment;
    }

    /**
     * Javadoc�R�����g��ݒ肵�܂��B
     * @param javadocComment Javadoc�R�����g
     */
    public void setJavadocComment(String javadocComment) {
        this.javadocComment = javadocComment;
    }

}
