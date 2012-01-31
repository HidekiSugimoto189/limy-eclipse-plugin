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

import java.awt.geom.Rectangle2D.Double;

import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.limy.eclipse.qalab.common.ClickableXmlParser;
import org.w3c.dom.Element;

/**
 * Java�v�f�̃G�������g����\���܂��B
 * @author Naoki Iwami
 */
public class JavaElementClickPoint extends ClickablePointInfo {
    
    /**
     * JavaElementClickPoint�쐬�S���N���X�ł��B
     * @author Naoki Iwami
     */
    public static class Creator implements PointInfoCreator {

        /** Java�v���W�F�N�g */
        private final IJavaProject project;

        /** SVG�p�[�T */
        private final ClickableXmlParser parser;

        /**
         * Creator �C���X�^���X���\�z���܂��B
         * @param project Java�v���W�F�N�g
         * @param parser SVG�p�[�T
         */
        public Creator(IJavaProject project, ClickableXmlParser parser) {
            super();
            this.project = project;
            this.parser = parser;
        }

        public ClickablePointInfo create(Element el, Double rect) {

            IJavaElement javaElement = null;
            
            String qualifiedName = parser.getQualifiedName(el);
            if (qualifiedName != null) {
                javaElement = getPackageElement(project, qualifiedName);
                if (javaElement == null) {
                    javaElement = getTypeElement(project, qualifiedName);
                }
            }
            
            JavaElementClickPoint result = new JavaElementClickPoint(javaElement);
            result.setRect(rect);
            result.setTooltipText(parser.getTooltip(javaElement));

            return result;
        }

    }
    
    // ------------------------ Fields

    /** Java�v�f */
    private final IJavaElement element;

    // ------------------------ Constructors

    /**
     * JavaElementClickPoint �C���X�^���X���\�z���܂��B
     * @param element Java�v�f
     */
    public JavaElementClickPoint(IJavaElement element) {
        super();
        this.element = element;
    }
    
    // ------------------------ Getter/Setter Methods

    /**
     * Java�v�f���擾���܂��B
     * @return Java�v�f
     */
    public IJavaElement getElement() {
        return element;
    }

    // ------------------------ Private Methods
    
    private static IJavaElement getPackageElement(IJavaProject project, String qualifiedName) {
        IJavaElement javaElement = null;
        try {
            javaElement = project.findElement(
                    new Path(qualifiedName.replace('.', '/')));
        } catch (JavaModelException e) {
            // ignore
        }
        return javaElement;
    }

    private static IJavaElement getTypeElement(IJavaProject project, String qualifiedName) {
        try {
            return project.findType(qualifiedName);
        } catch (JavaModelException e) {
            // ignore
        }
        return null;
    }

}
