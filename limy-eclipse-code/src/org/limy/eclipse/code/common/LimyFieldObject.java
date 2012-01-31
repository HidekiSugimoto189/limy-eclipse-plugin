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
package org.limy.eclipse.code.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;
import org.limy.eclipse.common.LimyEclipseConstants;

/**
 * �t�B�[���h�I�u�W�F�N�g��\���܂��B
 * @author Naoki Iwami
 */
public class LimyFieldObject {
    
    // ------------------------ Constants

    /**
     * ����(Annotation)���擾�p�^�[��
     */
    private static final Pattern PAT_ANNOTATION = Pattern.compile("//\\s*(@\\w+)$");
    
    /**
     * Javadoc�R�����g�擾�p�^�[��
     */
    private static final Pattern PAT_COMMENT = Pattern.compile("/\\*\\*(.*)\\*/");
    

    /**
     * Javadoc�R�����g�擾�p�^�[��(�����s�Ή�)
     */
    private static final Pattern PAT_COMMENT_MULTI
            = Pattern.compile("/\\*\\*\n\\s*\\*\\s*([^\n]*)", Pattern.MULTILINE);
    
    // ------------------------ Fields

    /**
     * JDT�t�B�[���h
     */
    private IField field;
    
    /**
     * �t�B�[���h�R�����g
     */
    private String comment;

    /**
     * ����(Annotation)��
     */
    private String annotationHint;

    // ------------------------ Constructors

    /**
     * LimyFieldObject�C���X�^���X���\�z���܂��B
     * @param field JDT�t�B�[���h���
     * @throws JavaModelException Java���f����O
     */
    public LimyFieldObject(IField field) throws JavaModelException {
        
        this.field = field;
        
        // �t�B�[���h�R�����g���擾
        this.comment = getFieldComment(field);
        
        // ����(Annotation)�����擾
        this.annotationHint = getFieldAnnotationHint(field);

    }
    
    // ------------------------ Private Methods

    /**
     * �t�B�[���h�R�����g���擾���܂��B
     * @param field �t�B�[���h�I�u�W�F�N�g
     * @return �t�B�[���h�R�����g
     * @throws JavaModelException
     */
    private String getFieldComment(IField field) throws JavaModelException {
        String fieldComment = LimyEclipseConstants.EMPTY_STR;
        Matcher matcher = PAT_COMMENT.matcher(field.getSource());
        if (matcher.find()) {
            fieldComment = matcher.group(1).trim();
        } else {
            matcher = PAT_COMMENT_MULTI.matcher(
                    field.getSource().replaceAll("\r\n", "\n"));
            if (matcher.find()) {
                fieldComment = matcher.group(1).trim();
            }
        }
        return fieldComment;
    }

    /**
     * ����(Annotation)�����擾���܂��B
     * @param field �t�B�[���h�I�u�W�F�N�g
     * @return ����(Annotation)��
     * @throws JavaModelException Java���f����O
     */
    private String getFieldAnnotationHint(IField field) throws JavaModelException {
        
        // type name = ...; // @fdsaf

        String result = LimyEclipseConstants.EMPTY_STR;
        Matcher matcher = PAT_ANNOTATION.matcher(field.getSource());
        if (matcher.find()) {
            result = matcher.group(1).trim();
        }
        return result;
    }

    // ------------------------ Getter/Setter Methods

    /**
     * JDT�t�B�[���h���擾���܂��B
     * @return JDT�t�B�[���h
     */
    public IField getField() {
        return field;
    }

    /**
     * JDT�t�B�[���h��ݒ肵�܂��B
     * @param field JDT�t�B�[���h
     */
    public void setField(IField field) {
        this.field = field;
    }

    /**
     * �t�B�[���h�R�����g���擾���܂��B
     * @return �t�B�[���h�R�����g
     */
    public String getComment() {
        return comment;
    }

    /**
     * �t�B�[���h�R�����g��ݒ肵�܂��B
     * @param comment �t�B�[���h�R�����g
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * ����(Annotation)�����擾���܂��B
     * @return ����(Annotation)��
     */
    public String getAnnotationHint() {
        return annotationHint;
    }

    /**
     * ����(Annotation)����ݒ肵�܂��B
     * @param annotationHint ����(Annotation)��
     */
    public void setAnnotationHint(String annotationHint) {
        this.annotationHint = annotationHint;
    }

}
