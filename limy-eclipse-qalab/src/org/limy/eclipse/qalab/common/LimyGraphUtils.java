/*
 * Created 2007/02/22
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
package org.limy.eclipse.qalab.common;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jfree.chart.JFreeChart;

import com.keypoint.PngEncoder;

/**
 * �O���t�iJFreeChart�j�֘A�̃��[�e�B���e�B�N���X�ł��B
 * @author Naoki Iwami
 */
public final class LimyGraphUtils {
    
    /**
     * private constructor
     */
    private LimyGraphUtils() { }
    
    /**
     * PNG�`���Ń`���[�g���t�@�C���o�͂��܂��B
     * @param chart �`���[�g
     * @param pngFile �o��png�t�@�C��
     * @param width �C���[�Wwidth
     * @param height �C���[�Wheight
     * @throws IOException I/O��O
     */
    public static void writeImagePng(JFreeChart chart, File pngFile, int width, int height)
            throws IOException {

        BufferedImage image = chart.createBufferedImage(width, height);
        FileOutputStream outStream = new FileOutputStream(pngFile);
        try {
            PngEncoder pngEncoder = new PngEncoder(image);
            pngEncoder.setCompressionLevel(9);
            outStream.write(pngEncoder.pngEncode());
        } finally {
            outStream.close();
        }
    }

}
