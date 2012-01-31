/*
 * Created 2007/02/27
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

import java.awt.geom.Point2D;
import java.io.File;
import java.util.Collection;

/**
 * �|�b�v�A�b�v�C���[�W�p�̃C���^�[�t�F�C�X�ł��B
 * @author Naoki Iwami
 */
public interface PopupImage {

    /**
     * �C���[�W�t�@�C����Ԃ��܂��B
     * @return
     */
    File getImageFile();
    
    /**
     * SVG�t�@�C�����擾���܂��B
     * @return SVG�t�@�C��
     */
    File getSvgImageFile();

    /**
     * �N���b�N�\�ȗv�f�ꗗ��Ԃ��܂��B
     * @return �N���b�N�\�ȗv�f�ꗗ
     */
    Collection<? extends ClickablePointInfo> getClickableElements();
    
    /**
     * �w�肳�ꂽ���W�ɑΉ�����v�f��Ԃ��܂��B
     * @param point ���W
     * @return �v�f
     */
    ClickablePointInfo getElement(Point2D.Double point);

    /**
     * ���������\���t���O���擾���܂��B
     * @return ���������\���t���O
     */
    boolean isHorizontal();
    
    /**
     * �ėp�l���擾���܂��B
     * @param key �L�[
     * @return �l
     */
    Object getParam(String key);

    /**
     * �ėp�l��ݒ肵�܂��B
     * @param key �L�[
     * @param value �l
     */
    void setParam(String key, Object value);
    
    /**
     * ���������\���t���O��ݒ肵�܂��B
     * @param horizontal ���������\���t���O
     */
    void setHorizontal(boolean horizontal);

}
