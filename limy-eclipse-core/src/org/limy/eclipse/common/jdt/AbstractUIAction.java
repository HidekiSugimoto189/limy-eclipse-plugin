/*
 * Created 2007/01/11
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
package org.limy.eclipse.common.jdt;

import java.util.Collection;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.UIJob;
import org.limy.eclipse.common.LimyEclipsePluginUtils;

/**
 * UI�����𔺂����A�N�V�����N���X�ł��B
 * <p>
 * ���̃N���X���p������ doRun() ���\�b�h���������܂��B
 * </p>
 * @author Naoki Iwami
 */
public abstract class AbstractUIAction implements IWorkbenchWindowActionDelegate {

    // ------------------------ Classes

    /**
     * �����I�Ɏg�p����UIJob�N���X�ł��B
     * @author Naoki Iwami
     */
    private class LimyUIActionJob extends UIJob {

        /**
         * LimyUIActionJob�C���X�^���X���\�z���܂��B
         * @param name
         */
        public LimyUIActionJob(String name) {
            super(name);
        }

        @Override
        public IStatus runInUIThread(IProgressMonitor monitor) {
            try {
                doRun(selection, monitor);
            } catch (CoreException e) {
                LimyEclipsePluginUtils.log(e);
            }
            return Status.OK_STATUS;
        }
        
    }

    // ------------------------ Fields

    /** WorkbenchWindow */
    private IWorkbenchWindow window;
    
    /**
     * �I��͈�
     * <p>
     * �G�������g�ł���� IStructuredSelection,
     * �G�f�B�^���ł���΁@ITextSelection �i�������I�t�Z�b�g���͖����j
     * </p>
     */
    private ISelection selection;
    
    // ------------------------ Abstract Methods

    /**
     * ���������s���܂��B
     * @param selection 
     * @param monitor 
     * @throws CoreException 
     */
    public abstract void doRun(ISelection selection, IProgressMonitor monitor)
            throws CoreException;

    // ------------------------ Implement Methods

    public void init(IWorkbenchWindow window) {
        this.window = window;
    }

    public void dispose() {
        // do nothing
    }

    public void run(IAction action) {
        String jobName = action != null ? action.getText() : "";
        LimyUIActionJob job = new LimyUIActionJob(jobName);
        job.schedule();
    }

    public void selectionChanged(IAction action, ISelection selection) {
        this.selection = selection;
    }
    
    // ------------------------ Protected Methods

    protected IWorkbenchWindow getWindow() {
        if (window == null) {
            // �|�b�v�A�b�v���j���[���̃A�N�V�����Ή�
            IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
            if (windows.length > 0) {
                return windows[0];
            }
        }
        return window;
    }
    
    /**
     * �I�����ꂽ�S�Ă�Java�v�f��Ԃ��܂��B
     * @return �I�����ꂽ�S�Ă�Java�v�f
     * @throws JavaModelException Java���f����O
     */
    protected Collection<IJavaElement> getSelectedJavaElements() throws JavaModelException {
        return LimyJavaUtils.getSelectedJavaElements(getWindow(), getSelection());
    }
    
    protected ISelection getSelection() {
        return selection;
    }
    
}
