/*
 * Created 2007/08/31
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
package org.limy.eclipse.qalab.outline.jdepend;

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.limy.eclipse.qalab.outline.DialogSupport;
import org.limy.eclipse.qalab.outline.ImageCreator;
import org.limy.eclipse.qalab.outline.PopupImage;

/**
 * @author Naoki Iwami
 */
public class JDependDialogSupport implements DialogSupport {

    /** �Ώۗv�f */
    private final IJavaElement targetElement;

    /** �C���[�W�쐬�S�� */
    private final ImageCreator creator;
    
    // ------------------------ Constructors

    /**
     * JDependDialogSupport �C���X�^���X���\�z���܂��B
     * @param targetElement �Ώۗv�f
     * @param creator �C���[�W�쐬�S��
     */
    public JDependDialogSupport(IJavaElement targetElement,
            ImageCreator creator) {
        super();
        this.targetElement = targetElement;
        this.creator = creator;
    }

    // ------------------------ Implement Methods

    public String getDialogTitle() {
        return "�p�b�P�[�W�ˑ��֌W   (Press 'c' to change alignment, Press 'v' to show view)";
    }

    public IJavaElement getTargetElement() {
        return targetElement;
    }

    /**
     * �摜�̕�����]�������čĕ`�悵�܂��B
     * @return
     * @throws CoreException 
     * @throws IOException 
     */
    public PopupImage changeHorizontal() throws CoreException, IOException {
        return creator.changeLocation();
    }
}
