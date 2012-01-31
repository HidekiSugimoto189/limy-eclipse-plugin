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
package org.limy.eclipse.common.ui;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.UIJob;
import org.limy.eclipse.common.LimyEclipsePluginUtils;
import org.limy.eclipse.common.jdt.LimyJavaUtils;

/**
 * Java�v�f�ɑΉ������A�N�V�������N���X�ł��B
 * <p>
 * �A�N�V�������s���UI�������s�����Ƃ��ł��܂��B
 * </p>
 * @author Naoki Iwami
 */
public abstract class AbstractJavaElementAction implements IWorkbenchWindowActionDelegate {

    // ------------------------ Fields

    /** WorkbenchWindow */
    private IWorkbenchWindow window;

    /**
     * �I��͈�
     */
    private ISelection selection;

    // ------------------------ Implement Methods

    public void run(IAction action) {
        
        try {
            init();
            final List<IJavaElement> javaElements = LimyJavaUtils.getSelectedJavaElements(
                    getWindow(), selection);
            
            String jobName = getJobName(action);
            Job job = new Job(jobName) {
                @Override
                protected IStatus run(IProgressMonitor monitor) {
                    try {
                        doActions(javaElements, monitor);
                    } catch (CoreException e) {
                        LimyEclipsePluginUtils.log(e);
                        return Status.CANCEL_STATUS;
                    }
                    execUIJob();
                    return Status.OK_STATUS;
                }
            };
            job.schedule();
        } catch (JavaModelException e) {
            LimyEclipsePluginUtils.log(e);
        }
        
    }

    private void execUIJob() {
        UIJob job = new UIJob("UIJob") {
            @Override
            public IStatus runInUIThread(IProgressMonitor monitor) {
                try {
                    execAfter(monitor);
                } catch (CoreException e) {
                    LimyEclipsePluginUtils.log(e);
                    return Status.CANCEL_STATUS;
                }
                return Status.OK_STATUS;
            }
        };
        job.schedule();
        
    }

    public void selectionChanged(IAction action, ISelection selection) {
        this.selection = selection;
    }

    public void dispose() {
        // no nothing
    }

    public void init(IWorkbenchWindow window) {
        this.window = window;
    }
    
    
    // ------------------------ Protected Methods
    
    protected final IWorkbenchWindow getWindow() {
        if (window == null) {
            IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
            if (windows.length > 0) {
                return windows[0];
            }
        }
        return window;
    }

    /**
     * �I��͈͂��擾���܂��B
     * @return �I��͈�
     */
    protected final ISelection getSelection() {
        return selection;
    }
    
    /**
     * ������Java�G�������g�ɑ΂��ď��������s���܂��B
     * �f�t�H���g�ł� doAction() ���J��Ԃ��Ăяo���܂��B
     * �ꊇ�������s�������Ƃ��ɂ͂��̃��\�b�h���I�[�o�[���C�h����K�v������܂��B
     * @param javaElements Java�G�������g
     * @param monitor �J�ڃ��j�^
     * @throws CoreException �R�A��O
     */
    protected void doActions(List<IJavaElement> javaElements, IProgressMonitor monitor)
            throws CoreException {
        
        for (IJavaElement javaElement : javaElements) {
            doAction(javaElement, monitor);
        }
    }

    protected void init() {
        // do nothing
    }
    
    /**
     * Job���s���UI�֘A���������s���܂��B
     * <p>
     * �T�u�N���X�ł͕K�v�ɉ����Ă��̃��\�b�h��Override���ĉ������B
     * </p>
     * @param monitor 
     * @throws CoreException 
     */
    protected void execAfter(IProgressMonitor monitor) throws CoreException {
        // do nothing
    }
    
    protected String getJobName(IAction action) {
        return action != null ? action.getText() : "";
    }

    // ------------------------ Abstract Methods

    /**
     * �P���Java�G�������g�ɑ΂��ď��������s���܂��B
     * @param javaElement Java�G�������g
     * @param monitor �J�ڃ��j�^
     * @throws CoreException �R�A��O
     */
    protected abstract void doAction(IJavaElement javaElement, IProgressMonitor monitor)
            throws CoreException;


}
