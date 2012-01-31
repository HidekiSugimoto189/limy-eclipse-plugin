/*
 * Created 2005/09/13
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
package org.limy.eclipse.code.accessor;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.limy.eclipse.common.jdt.LimyJavaUtils;
import org.limy.eclipse.common.ui.AbstractJavaElementAction;

/**
 * ���݃A�N�e�B�u�ȃN���X�̃t�B�[���h�ɑ΂���Getter/Setter���\�b�h�𐶐�����A�N�V�����N���X�ł��B
 * @author Naoki Iwami
 */
public class CreateAllPublicAction extends AbstractJavaElementAction {

    @Override
    protected void doAction(IJavaElement javaElement, IProgressMonitor monitor)
            throws CoreException {

        IType type = LimyJavaUtils.getPrimaryType(javaElement);
        
        ICompilationUnit workingCopy = type.getCompilationUnit().getWorkingCopy(monitor);
        AccessorUtils.createAllPublicMethods(workingCopy.findPrimaryType(), monitor);
        workingCopy.commitWorkingCopy(true, monitor);
        workingCopy.discardWorkingCopy();
    }

}
