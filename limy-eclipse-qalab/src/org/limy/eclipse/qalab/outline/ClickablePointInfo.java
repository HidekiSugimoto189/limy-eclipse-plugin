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

import java.awt.geom.Rectangle2D;

/**
 * �N���b�N�\�ȃG�������g����\���܂��B
 * @author Naoki Iwami
 */
public class ClickablePointInfo {
    
    // ------------------------ Fields

    /** �\���ʒu */
    private Rectangle2D.Double rect;
    
    /** �c�[���`�b�v������ */
    private String tooltipText;

    // ------------------------ Getter/Setter Methods

    /**
     * �\���ʒu���擾���܂��B
     * @return �\���ʒu
     */
    public Rectangle2D.Double getRect() {
        return rect;
    }

    /**
     * �\���ʒu��ݒ肵�܂��B
     * @param rect �\���ʒu
     */
    public void setRect(Rectangle2D.Double rect) {
        this.rect = rect;
    }

    /**
     * �c�[���`�b�v��������擾���܂��B
     * @return �c�[���`�b�v������
     */
    public String getTooltipText() {
        return tooltipText;
    }

    /**
     * �c�[���`�b�v�������ݒ肵�܂��B
     * @param tooltipText �c�[���`�b�v������
     */
    public void setTooltipText(String tooltipText) {
        this.tooltipText = tooltipText;
    }

}
