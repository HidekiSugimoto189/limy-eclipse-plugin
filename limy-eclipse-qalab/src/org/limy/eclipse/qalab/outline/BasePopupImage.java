/*
 * Created 2007/02/28
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

import java.awt.geom.Point2D.Double;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * �|�b�v�A�b�v�C���[�W�̊��N���X�ł��B
 * @author Naoki Iwami
 */
public class BasePopupImage implements PopupImage {

    // ------------------------ Fields

    /** ���������\���t���O */
    private boolean horizontal;

    /** �摜�t�@�C�� */
    private File imageFile;

    /** SVG�t�@�C�� */
    private File svgImageFile;
    
    /** �N���b�N�\�ȗv�f�ꗗ */
    private Collection<? extends ClickablePointInfo> elements = new ArrayList<ClickablePointInfo>();
    
    /** �p�����[�^�ꗗ */
    private Map<String, Object> params = new HashMap<String, Object>();
    
    // ------------------------ Implement Methods

    public Collection<? extends ClickablePointInfo> getClickableElements() {
        return elements;
    }

    public File getImageFile() {
        return imageFile;
    }

    public File getSvgImageFile() {
        return svgImageFile;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public Object getParam(String key) {
        return params.get(key);
    }
    
    public void setParam(String key, Object value) {
        params.put(key, value);
    }

    public ClickablePointInfo getElement(Double point) {
        for (ClickablePointInfo el : elements) {
            if (el.getRect().contains(point)) {
                return el;
            }
        }
        return null;
    }
    
    // ------------------------ Public Methods

    /**
     * SVG�t�@�C����ݒ肵�܂��B
     * @param svgImageFile SVG�t�@�C��
     */
    public void setSvgImageFile(File svgImageFile) {
        this.svgImageFile = svgImageFile;
    }

    /**
     * �摜�t�@�C����ݒ肵�܂��B
     * @param imageFile �摜�t�@�C��
     */
    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    /**
     * �N���b�N�\�ȗv�f�ꗗ��ݒ肵�܂��B
     * @param elements �N���b�N�\�ȗv�f�ꗗ
     */
    public void setElements(Collection<? extends ClickablePointInfo> elements) {
        this.elements = elements;
    }

    /**
     * ���������\���t���O��ݒ肵�܂��B
     * @param horizontal ���������\���t���O
     */
    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }

}
