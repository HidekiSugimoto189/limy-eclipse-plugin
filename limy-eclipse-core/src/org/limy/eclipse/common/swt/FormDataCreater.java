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
package org.limy.eclipse.common.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Control;

/**
 * FormData�����p�̃��[�e�B���e�B�N���X�ł��B
 * @author Naoki Iwami
 */
public final class FormDataCreater {
    
    /**
     * private constructor
     */
    private FormDataCreater() { }
    
    /**
     * �E�B���h�E�S�̂̉���������FormData���쐬���܂��B
     * @param top Top�ʒu(�p�[�Z���g)
     * @param topOffset Top�ʒu�I�t�Z�b�g(�s�N�Z���P��)
     * @param bottom Bottom�ʒu(�p�[�Z���g)
     * @param bottomOffset Bottom�ʒu�I�t�Z�b�g(�s�N�Z���P��)
     * @return FormData
     */
    public static FormData maxWidth(int top, int topOffset, int bottom, int bottomOffset) {
        FormData formData = new FormData();
        formData.left = new FormAttachment(0, 0);
        formData.right = new FormAttachment(100, 0);
        formData.top = new FormAttachment(top, topOffset);
        formData.bottom = new FormAttachment(bottom, bottomOffset);
        return formData;
    }

    /**
     * �E�B���h�E�S�̂̉���������FormData���쐬���܂��B
     * @param top Top�ʒu(�p�[�Z���g)
     * @param bottom Bottom�ʒu(�p�[�Z���g)
     * @return FormData
     */
    public static FormData maxWidth(int top, int bottom) {
        return maxWidth(top, 0, bottom, 0);
    }

    /**
     * �w�肵���ʒu��FormData���쐬���܂��B
     * @param left Left�ʒu(�p�[�Z���g)
     * @param top Top�ʒu(�p�[�Z���g)
     * @return FormData
     */
    public static FormData leftTop(int left, int top) {
        FormData formData = new FormData();
        formData.left = new FormAttachment(left);
        formData.top = new FormAttachment(top);
        return formData;
    }

    /**
     * �^�[�Q�b�g�̉��ɕt���E�B���h�E�S�̂̉���������FormData���쐬���܂��B
     * @param ctl �^�[�Q�b�g
     * @param topOffset Top�ʒu�I�t�Z�b�g(�s�N�Z���P��)
     * @param bottom Bottom�ʒu(�p�[�Z���g)
     * @param bottomOffset Bottom�ʒu�I�t�Z�b�g(�s�N�Z���P��)
     * @return FormData
     */
    public static FormData maxWidthControlBottom(
            Control ctl, int topOffset, int bottom, int bottomOffset) {
        FormData formData = new FormData();
        formData.left = new FormAttachment(0, 0);
        formData.right = new FormAttachment(100, 0);
        formData.top = new FormAttachment(ctl, topOffset);
        formData.bottom = new FormAttachment(bottom, bottomOffset);
        return formData;
    }

    /**
     * �^�[�Q�b�g�̉��ɕt���E�B���h�E�S�̂̉���������FormData���쐬���܂��B
     * @param ctl �^�[�Q�b�g
     * @param topOffset Top�ʒu�I�t�Z�b�g(�s�N�Z���P��)
     * @param leftOffset Left�ʒu�I�t�Z�b�g(�s�N�Z���P��)
     * @return FormData
     */
    public static FormData maxWidthNoHeightControlBottom(
            Control ctl, int topOffset, int leftOffset) {
        FormData formData = new FormData();
        formData.left = new FormAttachment(0, leftOffset);
        formData.right = new FormAttachment(100, 0);
        formData.top = new FormAttachment(ctl, topOffset);
        return formData;
    }

    /**
     * �^�[�Q�b�g�̉��ɕt���E���ɒ���t��FormData���쐬���܂��B
     * @param ctl �^�[�Q�b�g
     * @param topOffset Top�ʒu�I�t�Z�b�g(�s�N�Z���P��)
     * @param width �����iPixel�P�ʁj
     * @return FormData
     */
    public static FormData rightAttachControlBottom(
            Control ctl, int topOffset, int width) {
        FormData formData = new FormData();
        formData.top = new FormAttachment(ctl, topOffset);
        formData.right = new FormAttachment(100, 0);
        formData.width = width;
        return formData;
    }

    /**
     * �E�B���h�E�E���ɒ���t��FormData���쐬���܂��B
     * @param ctl �^�[�Q�b�g
     * @param topOffset Top�ʒu�I�t�Z�b�g(�s�N�Z���P��)
     * @param width �����iPixel�P�ʁj
     * @return FormData
     */
    public static FormData bottomAttach(int width) {
        
        FormData formData = new FormData();
        formData.bottom = new FormAttachment(100, 0);
        formData.right = new FormAttachment(100, 0);
        formData.width = width;
        return formData;
    }

    /**
     * �^�[�Q�b�g�̉��ɕt�������������Ȃ�FormData���쐬���܂��B
     * @param ctl �^�[�Q�b�g
     * @param topOffset Top�ʒu�I�t�Z�b�g(�s�N�Z���P��)
     * @param bottom Bottom�ʒu(�p�[�Z���g)
     * @param bottomOffset Bottom�ʒu�I�t�Z�b�g(�s�N�Z���P��)
     * @return FormData
     */
    public static FormData noWidthControlBottom(
            Control ctl, int topOffset, int bottom, int bottomOffset) {
        FormData formData = new FormData();
        formData.left = new FormAttachment(0, 0);
        formData.top = new FormAttachment(ctl, topOffset);
        formData.bottom = new FormAttachment(bottom, bottomOffset);
        return formData;
    }

    /**
     * �^�[�Q�b�g�̉E�ɕt���E�B���h�E�S�̂̉���������FormData���쐬���܂��B
     * @param ctl �^�[�Q�b�g
     * @param leftOffset Left�ʒu�I�t�Z�b�g(�s�N�Z���P��)
     * @return FormData
     */
    public static FormData maxWidthControlRight(
            Control ctl, int leftOffset) {
        FormData formData = new FormData();
        formData.left = new FormAttachment(ctl, leftOffset);
        formData.top = new FormAttachment(ctl, 0, SWT.TOP);
        formData.right = new FormAttachment(100, 0);
        return formData;
    }

    /**
     * �^�[�Q�b�g�̉E�ɕt���E�B���h�E�S�̂̉���������FormData���쐬���܂��B
     * @param ctl �^�[�Q�b�g
     * @param leftOffset Left�ʒu�I�t�Z�b�g(�s�N�Z���P��)
     * @param topOffset Top�ʒu�I�t�Z�b�g(�s�N�Z���P��)
     * @return FormData
     */
    public static FormData maxWidthControlRight(
            Control ctl, int leftOffset, int topOffset) {
        FormData formData = new FormData();
        formData.left = new FormAttachment(ctl, leftOffset);
        formData.top = new FormAttachment(ctl, topOffset, SWT.TOP);
        formData.right = new FormAttachment(100, 0);
        return formData;
    }

    /**
     * �^�[�Q�b�g�̍��ɕt���E�B���h�E�S�̂̉���������FormData���쐬���܂��B
     * @param ctl �^�[�Q�b�g
     * @param leftOffset Left�ʒu�I�t�Z�b�g(�s�N�Z���P��)
     * @return FormData
     */
    public static FormData maxWidthControlLeft(
            Control ctl, int leftOffset) {
        FormData formData = new FormData();
        formData.left = new FormAttachment(0, 0);
        formData.right = new FormAttachment(ctl, leftOffset);
        formData.top = new FormAttachment(ctl, 0, SWT.TOP);
        return formData;
    }

    /**
     * �^�[�Q�b�g�̉E�ɕt��FormData���쐬���܂��B
     * @param ctl �^�[�Q�b�g
     * @param leftOffset Left�ʒu�I�t�Z�b�g(�s�N�Z���P��)
     * @param width ����(Pixel�P��)
     * @return FormData
     */
    public static FormData controlRight(
            Control ctl, int leftOffset, int width) {
        FormData formData = new FormData();
        formData.left = new FormAttachment(ctl, leftOffset);
        formData.top = new FormAttachment(ctl, 0, SWT.TOP);
        formData.width = width;
        return formData;
    }

    /**
     * �^�[�Q�b�g�̍��ɕt��FormData���쐬���܂��B
     * @param ctl �^�[�Q�b�g
     * @param leftOffset Left�ʒu�I�t�Z�b�g(�s�N�Z���P��)
     * @param width ����(Pixel�P��)
     * @return FormData
     */
    public static FormData controlLeft(
            Control ctl, int leftOffset, int width) {
        FormData formData = new FormData();
        formData.right = new FormAttachment(ctl, -leftOffset, SWT.LEFT);
        formData.top = new FormAttachment(ctl, 0, SWT.TOP);
        formData.width = width;
        return formData;
    }

    /**
     * �^�[�Q�b�g�̉E�ɕt��FormData���쐬���܂��B
     * @param ctl �^�[�Q�b�g
     * @param left Left�ʒu�i�p�[�Z���g�j
     * @param right Right�ʒu�i�p�[�Z���g�j
     * @return FormData
     */
    public static FormData controlRightPercentage(
            Control ctl, int left, int right) {
        FormData formData = new FormData();
        formData.left = new FormAttachment(left);
        formData.top = new FormAttachment(ctl, 0, SWT.TOP);
        formData.right = new FormAttachment(right);
        return formData;
    }

    /**
     * �^�[�Q�b�g�̉E�ɕt��FormData���쐬���܂��B
     * @param ctl �^�[�Q�b�g
     * @param leftOffset Left�ʒu�I�t�Z�b�g(�s�N�Z���P��)
     * @param width ����(Pixel�P��)
     * @param height �c��(Pixel�P��)
     * @return FormData
     */
    public static FormData controlRight(
            Control ctl, int leftOffset, int width, int height) {
        FormData formData = new FormData();
        formData.left = new FormAttachment(ctl, leftOffset);
        formData.top = new FormAttachment(ctl, 0, SWT.TOP);
        formData.width = width;
        formData.height = height;
        return formData;
    }

    /**
     * �^�[�Q�b�g�̉E�ɕt�������������Ȃ�FormData���쐬���܂��B
     * @param ctl �^�[�Q�b�g
     * @param leftOffset Left�ʒu�I�t�Z�b�g(�s�N�Z���P��)
     * @return FormData
     */
    public static FormData noWidthControlRight(
            Control ctl, int leftOffset) {
        FormData formData = new FormData();
        formData.left = new FormAttachment(ctl, leftOffset);
        formData.top = new FormAttachment(ctl, 0, SWT.TOP);
        return formData;
    }

    /**
     * �^�[�Q�b�g�̉��ɕt���E�B���h�E�S�̂̉���������FormData���쐬���܂��B
     * @param ctl �^�[�Q�b�g
     * @param bottom Bottom�ʒu(�p�[�Z���g)
     * @return FormData
     */
    public static FormData maxWidthControlBottom(Control ctl, int bottom) {
        return maxWidthControlBottom(ctl, 0, bottom, 0);
    }

    /**
     * �^�[�Q�b�g�̉��ɕt���E�B���h�E�S�̂̉���������FormData���쐬���܂��B
     * @param ctl �^�[�Q�b�g
     * @param topOffset Top�ʒu�I�t�Z�b�g(�s�N�Z���P��)
     * @return FormData
     */
    public static FormData maxWidthControlDown(Control ctl, int topOffset) {
        FormData formData = new FormData();
        formData.left = new FormAttachment(0, 0);
        formData.right = new FormAttachment(100, 0);
        formData.top = new FormAttachment(ctl, topOffset);
        return formData;
    }

    /**
     * �^�[�Q�b�g�̉��ɕt���E�B���h�E�S�̂̉���������FormData���쐬���܂��B
     * @param ctl �^�[�Q�b�g
     * @param topOffset Top�ʒu�I�t�Z�b�g(�s�N�Z���P��)
     * @param left Left�ʒu�I�t�Z�b�g(�s�N�Z���P��)
     * @return FormData
     */
    public static FormData maxWidthControlDown(Control ctl, int topOffset, int left) {
        FormData formData = new FormData();
        formData.left = new FormAttachment(0, left);
        formData.right = new FormAttachment(100, 0);
        formData.top = new FormAttachment(ctl, topOffset);
        return formData;
    }

    /**
     * �^�[�Q�b�g�̉��ɕt���E�B���h�E�S�̂̉���������FormData���쐬���܂��B
     * @param ctl �^�[�Q�b�g
     * @param topOffset Top�ʒu�I�t�Z�b�g(�s�N�Z���P��)
     * @param left Left�ʒu�I�t�Z�b�g(�s�N�Z���P��)
     * @param height ����(�s�N�Z���P��)
     * @return FormData
     */
    public static FormData maxWidthControlDown(Control ctl, int topOffset, int left, int height) {
        FormData formData = new FormData();
        formData.left = new FormAttachment(0, left);
        formData.right = new FormAttachment(100, 0);
        formData.top = new FormAttachment(ctl, topOffset);
        formData.height = height;
        return formData;
    }

    /**
     * �^�[�Q�b�g�̉��ɕt�������������Ȃ�FormData���쐬���܂��B
     * @param ctl �^�[�Q�b�g
     * @param topOffset Top�ʒu�I�t�Z�b�g(�s�N�Z���P��)
     * @return FormData
     */
    public static FormData noWidthControlDown(Control ctl, int topOffset) {
        FormData formData = new FormData();
        formData.left = new FormAttachment(0, 0);
        formData.top = new FormAttachment(ctl, topOffset);
        return formData;
    }

    /**
     * �^�[�Q�b�g�̉��ɕt��FormData���쐬���܂��B
     * @param ctl �^�[�Q�b�g
     * @param topOffset Top�ʒu�I�t�Z�b�g(�s�N�Z���P��)
     * @param width ����(Pixel�P��)
     * @return FormData
     */
    public static FormData controlDown(Control ctl, int topOffset, int width) {
        FormData formData = new FormData();
        formData.left = new FormAttachment(0, 0);
        formData.top = new FormAttachment(ctl, topOffset);
        formData.width = width;
        return formData;
    }

    /**
     * �^�[�Q�b�g�̉��ɕt��FormData���쐬���܂��B
     * @param ctl �^�[�Q�b�g
     * @param topOffset Top�ʒu�I�t�Z�b�g(�s�N�Z���P��)
     * @param left Left�ʒu�I�t�Z�b�g(�s�N�Z���P��)
     * @param height �c��(Pixel�P��)
     * @return FormData
     */
    public static FormData controlDownWithHeight(Control ctl, int topOffset, int left,
            int height) {
        FormData formData = new FormData();
        formData.left = new FormAttachment(0, left);
        formData.top = new FormAttachment(ctl, topOffset);
        formData.height = height;
        return formData;
    }

    /**
     * �^�[�Q�b�g�̉��ɕt��FormData���쐬���܂��B
     * @param ctl �^�[�Q�b�g
     * @param topOffset Top�ʒu�I�t�Z�b�g(�s�N�Z���P��)
     * @param left Left�ʒu�I�t�Z�b�g(�s�N�Z���P��)
     * @param width ����(Pixel�P��)
     * @return FormData
     */
    public static FormData controlDownWithWidth(Control ctl, int topOffset, int left, int width) {
        FormData formData = new FormData();
        formData.left = new FormAttachment(0, left);
        formData.top = new FormAttachment(ctl, topOffset);
        formData.width = width;
        return formData;
    }

    /**
     * �^�[�Q�b�g�̉��ɕt��FormData���쐬���܂��B
     * @param ctl �^�[�Q�b�g
     * @param topOffset Top�ʒu�I�t�Z�b�g(�s�N�Z���P��)
     * @param left Left�ʒu�I�t�Z�b�g(�s�N�Z���P��)
     * @param width ����(Pixel�P��)
     * @param height �c��(Pixel�P��)
     * @return FormData
     */
    public static FormData controlDownWithWidth(Control ctl, int topOffset, int left,
            int width, int height) {
        FormData formData = new FormData();
        formData.left = new FormAttachment(0, left);
        formData.top = new FormAttachment(ctl, topOffset);
        formData.width = width;
        formData.height = height;
        return formData;
    }

    /**
     * �^�[�Q�b�g�̉��ɕt���E�B���h�E�S�̂̉���������FormData���쐬���܂��B
     * @param left Left�ʒu(�p�[�Z���g)
     * @param right Right�ʒu(�p�[�Z���g)
     * @param ctl �^�[�Q�b�g
     * @param topOffset Top�ʒu�I�t�Z�b�g(�s�N�Z���P��)
     * @param bottom Bottom�ʒu(�p�[�Z���g)
     * @param bottomOffset Bottom�ʒu�I�t�Z�b�g(�s�N�Z���P��)
     * @return FormData
     */
    public static FormData controlBottom(int left, int right,
            Control ctl, int topOffset, int bottom, int bottomOffset) {
        FormData formData = new FormData();
        formData.left = new FormAttachment(left, 0);
        formData.right = new FormAttachment(right, 0);
        formData.top = new FormAttachment(ctl, topOffset);
        formData.bottom = new FormAttachment(bottom, bottomOffset);
        return formData;
    }

    /**
     * @param marginWidth �������}�[�W��
     * @param marginHeight �c�����}�[�W��
     * @return
     */
    public static FormLayout createLayout(int marginWidth, int marginHeight) {
        FormLayout layout = new FormLayout();
        layout.marginWidth = marginWidth;
        layout.marginHeight = marginHeight;
        return layout;
    }

}
