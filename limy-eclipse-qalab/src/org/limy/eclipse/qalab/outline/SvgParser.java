/*
 * Created 2007/08/30
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
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.TransformerException;

import org.eclipse.jface.preference.IPreferenceStore;
import org.limy.eclipse.common.LimyEclipsePluginUtils;
import org.limy.eclipse.core.LimyEclipsePlugin;
import org.limy.eclipse.qalab.common.LimyQalabConstants;
import org.limy.xml.XmlUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xpath.internal.XPathAPI;

/**
 * @author Naoki Iwami
 */
public final class SvgParser {
    
    /**
     * private constructor
     */
    private SvgParser() { }

    /**
     * svg�t�@�C������͂��ėv�f���Ɨv�f�̃}�b�s���O���쐬���܂��B
     * @param svgFile svg�t�@�C��
     * @param creator �|�C���g���쐬�S��
     * @return �v�f���ƈʒu�̃}�b�s���O
     * @throws IOException I/O��O
     */
    public static Collection<ClickablePointInfo> makePointElementInfos(
            File svgFile,
            PointInfoCreator creator)
            throws IOException {
        
        IPreferenceStore store = LimyEclipsePlugin.getDefault().getPreferenceStore();
        boolean adjust = store.getBoolean(LimyQalabConstants.ADJUST_SCALING);
        
        InputStream xmlInput = new FileInputStream(svgFile);
        try {
            Element root = XmlUtils.parse(xmlInput);
            ScalingInfo scalingInfo = createScalingInfo(root, adjust);
    
            PointElementAppender appender = new PointElementAppender(creator);
            
            addPositions(appender, scalingInfo,
                    XPathAPI.selectNodeList(root, "/svg/g/g/text"));
            
            addPositions(appender, scalingInfo,
                    XPathAPI.selectNodeList(root, "/svg/g/g/a/text"));

            return appender.getResults();
        } catch (TransformerException e) {
            LimyEclipsePluginUtils.log(e);
        } finally {
            xmlInput.close();
        }
        
        return null;
    }

    // ------------------------ Private Methods

    /**
     * �e�L�X�g���� appender �ɒǉ����܂��B
     * @param appender �|�W�V�����i�[��
     * @param scalingInfo �X�P�[�����O���
     * @param texts �e�L�X�g���
     * @throws TransformerException
     */
    private static void addPositions(SvgElementAppender appender,
            ScalingInfo scalingInfo, NodeList texts)
            throws TransformerException {
        
        for (int i = 0; i < texts.getLength(); i++) {
            Element el = (Element)texts.item(i);
            
            Node pointNode = XPathAPI.selectSingleNode(el.getParentNode(), "polygon/@points");
            if (pointNode != null) {
                String pointValue = pointNode.getNodeValue();
                String[] points = pointValue.split(" ");
                Point2D.Double leftTop = createPoint(points[1]);
                Point2D.Double rightBottom = createPoint(points[3]);
                
                scalingInfo.adjust(leftTop, rightBottom);
                
                Rectangle2D.Double rect = new Rectangle2D.Double(
                        leftTop.x, leftTop.y,
                        rightBottom.x - leftTop.x, rightBottom.y - leftTop.y);
                
                appender.append(el, rect);
            }
            
        }
    }

    /**
     * svg����X�P�[�����O�����擾���܂��B
     * @param root svg���[�g�v�f
     * @param adjust �X�P�[�����O�����t���O
     * @return �X�P�[�����O���
     * @throws TransformerException
     */
    private static ScalingInfo createScalingInfo(Element root, boolean adjust)
            throws TransformerException {
        ScalingInfo scalingInfo = new ScalingInfo();
        Node transform = XPathAPI.selectSingleNode(root, "/svg/g/@transform");
        Matcher matcher = Pattern.compile(
                "scale\\(([^ ]*) ([^)]*)\\).*translate\\(([^ ]*) ([^)]*).*").matcher(
                        transform.getNodeValue());
        if (matcher.matches()) {
            
            if (adjust) {
                scalingInfo.setScale(new Point2D.Double(
                        Double.parseDouble(matcher.group(1)) * 4 / 3,
                        Double.parseDouble(matcher.group(2)) * 4 / 3));
                scalingInfo.setTranslate(new Point2D.Double(
                        Double.parseDouble(matcher.group(3)),
                        Double.parseDouble(matcher.group(4))));
            } else {
                scalingInfo.setScale(new Point2D.Double(
                        Double.parseDouble(matcher.group(1)),
                        Double.parseDouble(matcher.group(2))));
                scalingInfo.setTranslate(new Point2D.Double(
                        Double.parseDouble(matcher.group(3)) + 1/*adjust*/,
                        Double.parseDouble(matcher.group(4)) + 1/*adjust*/));
            }
        }
        return scalingInfo;
    }

    /**
     * ������i"1.0,2.5"�`���j��Point2D�`���ɕϊ����܂��B
     * @param str ������
     * @return Point2D�`��
     */
    private static Point2D.Double createPoint(String str) {
        String[] strs = str.split(",");
        return new Point2D.Double(Double.parseDouble(strs[0]), Double.parseDouble(strs[1]));
    }
    
}
