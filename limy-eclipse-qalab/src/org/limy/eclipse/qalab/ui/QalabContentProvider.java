/*
 * Created 2007/01/06
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
package org.limy.eclipse.qalab.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.limy.eclipse.common.jface.ITableObject;
import org.limy.eclipse.qalab.tester.ClassTestResult;
import org.limy.eclipse.qalab.tester.FailureItem;
import org.limy.eclipse.qalab.tester.ProjectTestResult;


/**
 * �e�X�g���ʕ\���p�̃R���e���c�v���o�C�_�ł��B
 * @author Naoki Iwami
 */
public class QalabContentProvider implements IStructuredContentProvider {

    // ------------------------ Fields

    /** �A�C�e���ꗗ */
    private List<ITableObject> items = new ArrayList<ITableObject>();

    // ------------------------ Implement Methods

    public Object[] getElements(Object inputElement) {
        return items.toArray(new ITableObject[items.size()]);
    }

    public void dispose() {
        // do nothing
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        
        if (newInput instanceof ITableObject) {
            items.add((ITableObject)newInput);
        }

        if (newInput instanceof ClassTestResult) {
            // �N���X�P�ʂ̃e�X�g����
            ClassTestResult result = (ClassTestResult)newInput;
            for (ListIterator<ITableObject> it = items.listIterator(); it.hasNext();) {
                FailureItem item = (FailureItem)it.next();
                String name = item.getQualifiedClassName();
                if (name.equals(result.getTargetClassName())) {
                    // �Ώۂ̃N���X�������X�g����폜
                    it.remove();
                }
            }
            items.addAll(result.getItems());
        }

        if (newInput instanceof ProjectTestResult) {
            // �v���W�F�N�g�P�ʂ̃e�X�g����
            ProjectTestResult result = (ProjectTestResult)newInput;
            for (ListIterator<ITableObject> it = items.listIterator(); it.hasNext();) {
                FailureItem item = (FailureItem)it.next();
                IProject project = item.getResource().getProject();
                if (project.equals(result.getProject())) {
                    // �Ώۂ̃v���W�F�N�g�������X�g����폜
                    it.remove();
                }
            }
            items.addAll(result.getItems());
        }

    }

}
