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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;



/**
 * Javadoc�����x������\���N���X�ł��B
 * @author Naoki Iwami
 */
public class LimyJavadocBean {
    
    // ------------------------ Fields
    
    /**
     * �N���X������ɕt����Prefix(Eclipse�ŗL����)
     */
    private static final String PREFIX_CLASS_STR = "Q";

    /**
     * ��
     */
    private static final String EMPTY_NAME = "";
    
    /**
     * ��ʃp�����[�^Javadoc���ꗗ
     */
    private List<JavadocTableObject> normalValues = new ArrayList<JavadocTableObject>();
    
    // ------------------------ Constructors

    /**
     * LimyJavadocBean�C���X�^���X���\�z���܂��B
     */
    public LimyJavadocBean() {
        // empty
    }

    /**
     * LimyJavadocBean�C���X�^���X���\�z���܂��B
     * @param propFile �擾���v���p�e�B�t�@�C��
     * @throws IOException I/O��O
     */
    public LimyJavadocBean(File propFile) throws IOException {
        FileInputStream in = null;
        try {
            in = new FileInputStream(propFile);
            ResourceBundle bundle = new PropertyResourceBundle(in);
            init(bundle);
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    // ------------------------ Public Methods
    
    /**
     * Javadoc�x�������t�@�C���ɕۑ����܂��B
     * @param propFile �ۑ���v���p�e�B�t�@�C��
     * @throws IOException I/O��O
     */
    public void save(File propFile) throws IOException {
        Properties props = new Properties();
        
        for (JavadocTableObject obj : normalValues) {
            switch (obj.getType()) {
            case CLAZZ:
                if (obj.getFieldName() == null || obj.getFieldName().length() == 0) {
                    props.put(PREFIX_CLASS_STR + obj.getClassName() + ";", obj.getJavadocComment());
                } else {
                    props.put(PREFIX_CLASS_STR + obj.getClassName() + ";/" + obj.getFieldName(),
                            obj.getJavadocComment());
                }
                break;
            case EXCEPTION:
                props.put(obj.getClassName(), obj.getJavadocComment());
                break;
            default:
                // Primitive
                props.put(obj.getType().getString().substring(0, 1).toUpperCase() + "/"
                        + obj.getFieldName(),
                        obj.getJavadocComment());
                break;
            }
        }
        
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(propFile);
            props.store(out, "Limy Javadoc Comments");
        } finally {
            if (out != null) {
                out.close();
            }
        }
        
    }

    // ------------------------ Getter/Setter Methods
    
    /**
     * ��ʃp�����[�^Javadoc���ꗗ���擾���܂��B
     * @return ��ʃp�����[�^Javadoc���ꗗ
     */
    public List<JavadocTableObject> getNormalValues() {
        return normalValues;
    }
    
    // ------------------------ Private Methods

    /**
     * ���\�[�X�o���h���̓��e�ŃC���X�^���X�����������܂��B
     * @param bundle ���\�[�X�o���h��
     * @throws IOException I/O��O
     */
    private void init(ResourceBundle bundle) throws IOException {
        for (Enumeration<String> en = bundle.getKeys(); en.hasMoreElements();) {
            String key = en.nextElement();
            String value = bundle.getString(key);
            
            if (key.indexOf(';') >= 0) {
                // �ʏ�
                key = key.substring(1);
                int index = key.indexOf('/');
                if (index >= 0) {
                    normalValues.add(new JavadocTableObject(Type.CLAZZ,
                            key.substring(0, index - 1), key.substring(index + 1), value));
                } else {
                    normalValues.add(
                            new JavadocTableObject(Type.CLAZZ,
                                    key.substring(0, key.length() - 1), EMPTY_NAME, value));
                }
            } else if (key.indexOf('/') >= 0) {
                // Primitive Type
                int index = key.indexOf('/');
                normalValues.add(new JavadocTableObject(getPrimitiveType(key.charAt(0)),
                        EMPTY_NAME, key.substring(index + 1), value));
                
            } else {
                // exception
                normalValues.add(new JavadocTableObject(Type.EXCEPTION, key, EMPTY_NAME, value));
            }
        }
//        Collections.sort(normalValues);
    }
    

    /**
     * �L�����N�^�ɑΉ������ʂ�Ԃ��܂��B
     * @param c �L�����N�^
     * @return ���
     */
    private Type getPrimitiveType(char c) {
        Type r = null;
        switch (c) {
        case 'B':
            r = Type.BYTE;
            break;
        case 'S':
            r = Type.SHORT;
            break;
        case 'I':
            r = Type.INT;
            break;
        case 'L':
            r = Type.LONG;
            break;
        case 'C':
            r = Type.CHAR;
            break;
        case 'F':
            r = Type.FLOAT;
            break;
        case 'D':
            r = Type.DOUBLE;
            break;
        default:
            break;
        }
        return r;
    }

}
