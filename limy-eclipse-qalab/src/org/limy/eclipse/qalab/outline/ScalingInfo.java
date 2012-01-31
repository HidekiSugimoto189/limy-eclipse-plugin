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

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

/**
 * �X�P�[�����O����\���܂��B
 * @author Naoki Iwami
 */
public class ScalingInfo {

    // ------------------------ Fields

    /** scale��� */
    private Point2D.Double scale;
    
    /** translate��� */
    private Point2D.Double translate;

    // ------------------------ Public Methods

    /**
     * �ʒu�����X�P�[�����O�������܂��B
     * @param point1 �ʒu���1
     * @param point2 �ʒu���2
     */
    public void adjust(Double point1, Double point2) {
        point1.x = (point1.x + translate.x) * scale.x;
        point1.y = (point1.y + translate.y) * scale.y;
        point2.x = (point2.x + translate.x) * scale.x;
        point2.y = (point2.y + translate.y) * scale.y;
    }

    // ------------------------ Getter/Setter Methods

    /**
     * scale�����擾���܂��B
     * @return scale���
     */
    public Point2D.Double getScale() {
        return scale;
    }

    /**
     * scale����ݒ肵�܂��B
     * @param scale scale���
     */
    public void setScale(Point2D.Double scale) {
        this.scale = scale;
    }

    /**
     * translate�����擾���܂��B
     * @return translate���
     */
    public Point2D.Double getTranslate() {
        return translate;
    }

    /**
     * translate����ݒ肵�܂��B
     * @param translate translate���
     */
    public void setTranslate(Point2D.Double translate) {
        this.translate = translate;
    }

}
