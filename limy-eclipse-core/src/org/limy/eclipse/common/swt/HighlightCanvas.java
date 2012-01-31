/*
 * Created 2007/02/19
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

import java.awt.geom.Rectangle2D;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

/**
 * ����̗̈���n�C���C�g�\���\�ȃL�����o�X�N���X�ł��B
 * @author Naoki Iwami
 */
public class HighlightCanvas extends Canvas {

    /** �n�C���C�g�F */
    private RGB highlightRGB = new RGB(255, 0, 0);

    /** �e�̐F */
    private RGB shadowRGB = new RGB(255, 255, 255);

    /** ���ݕ`�撆��rect */
    private Rectangle2D.Double targetRect;
    
    /**
     * HighlightCanvas�C���X�^���X���\�z���܂��B
     * @param parent
     * @param style
     */
    public HighlightCanvas(Composite parent, int style) {
        super(parent, style);
        addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
                
                if (targetRect != null) {
                    e.gc.setForeground(getColor(e, highlightRGB));
                    e.gc.drawRectangle((int)targetRect.x, (int)targetRect.y,
                            (int)targetRect.width, (int)targetRect.height);
                    e.gc.setForeground(getColor(e, shadowRGB));
                    e.gc.drawRectangle((int)targetRect.x + 1, (int)targetRect.y + 1,
                            (int)targetRect.width - 2, (int)targetRect.height - 2);
                }
            }

        });

    }

    // ------------------------ Getter/Setter Methods

    /**
     * �n�C���C�g�F��ݒ肵�܂��B
     * @param highlightRGB �n�C���C�g�F
     */
    public void setHighlightRGB(RGB highlightRGB) {
        this.highlightRGB = highlightRGB;
    }

    /**
     * �e�̐F��ݒ肵�܂��B
     * @param shadowRGB �e�̐F
     */
    public void setShadowRGB(RGB shadowRGB) {
        this.shadowRGB = shadowRGB;
    }

    /**
     * ���ݕ`�撆��rect���擾���܂��B
     * @return ���ݕ`�撆��rect
     */
    public Rectangle2D.Double getTargetRect() {
        return targetRect;
    }

    /**
     * ���ݕ`�撆��rect��ݒ肵�܂��B
     * @param targetRect ���ݕ`�撆��rect
     */
    public void setTargetRect(Rectangle2D.Double targetRect) {
        this.targetRect = targetRect;
    }
    
    // ------------------------ Private Methods

    private Color getColor(PaintEvent e, RGB rgb) {
        return ColorProvider.getColor(e.gc.getDevice(), rgb);
    }

}
