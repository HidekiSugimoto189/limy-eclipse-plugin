/*
 * Created 2005/09/13
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
package org.limy.eclipse.code;

/**
 * Code�v���O�C�����Ŏg�p�����萔�N���X�ł��B
 * @author Naoki Iwami
 */
public final class LimyCodeConstants {
    
    // ------------------------ Constants
    
    /**
     * public���ʎq������
     */
    public static final String FLAGSTR_PUBLIC = "public";

    /**
     * Javadoc�J�n������
     */
    public static final String JAVADOC_START = "/**";

    /**
     * Javadoc�p��������
     */
    public static final String JAVADOC_CONT = " * ";

    /**
     * Javadoc�I��������
     */
    public static final String JAVADOC_END = " */";

    /**
     * �E������
     */
    public static final String RIGHT_BRACKET = "}";

    /**
     * �C���f���g������
     */
    public static final String INDENT_STR = "    ";

    // ------------------------ Preference Names

    /**
     * Preference�L�[ : Java�t�@�C���w�b�_������
     */
    public static final String PREF_JAVA_HEADER = "javaHeader";

    /**
     * Preference�L�[ : Javadoc�J�X�^�}�C�Y�ݒ�t�@�C���p�X
     */
    public static final String PREF_PROP_PATH = "JavadocProp";

    /** Preference�L�[ : Getter���\�b�h�ɋL�q���镶�� */
    public static final String PREF_GETTER_DESC = "GetterDesc";

    /** Preference�L�[ : Setter���\�b�h�ɋL�q���镶�� */
    public static final String PREF_SETTER_DESC = "SetterDesc";

    /**
     * Preference�L�[ : GNU�v���W�F�N�g��
     */
    public static final String PREF_PROJECT_NAME = "GnuProjectName";

    // ------------------------ Resource Keys

//    /**
//     * Getter���\�b�h��Javadoc�R�����g
//     */
//    public static final String MES_GETTER = "getter";
//
//    /**
//     * Setter���\�b�h��Javadoc�R�����g
//     */
//    public static final String MES_SETTER = "setter";

    /**
     * Add Java Header�R�}���h���s���̊m�F���b�Z�[�W
     */
    public static final String MES_JAVA_HEADER = "add.java.header";
    
    /**
     * �����R�[�h�ϊ��A�N�V�������s���̃_�C�A���O�^�C�g��
     */
    public static final String MES_CONV_TITLE = "convert.charset.title";
    
    /**
     * �����R�[�h�ϊ��A�N�V�������s���̃_�C�A���O���b�Z�[�W
     */
    public static final String MES_CONV_DIALOG = "convert.charset.dialog";

    /**
     * �����R�[�h�ϊ��A�N�V�������s���̊m�F���b�Z�[�W
     */
    public static final String MES_CONV_CONFIRM = "convert.charset.confirm";

    // ------------------------ Constructors

    /**
     * private constructor
     */
    private LimyCodeConstants() { }

}
