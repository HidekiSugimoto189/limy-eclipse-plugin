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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.UIJob;
import org.limy.eclipse.common.LimyEclipsePluginUtils;

/**
 * ���\�[�X�ɑΉ������A�N�V�������N���X�ł��B
 * <p>
 * �A�N�V�������s���UI�������s�����Ƃ��ł��܂��B
 * </p>
 * @author Naoki Iwami
 */
public abstract class AbstractResoueceAction implements IWorkbenchWindowActionDelegate {

    // ------------------------ Fields

    /** WorkbenchWindow */
    private IWorkbenchWindow window;

    /**
     * �I��͈�
     */
    private ISelection selection;

    // ------------------------ Implement Methods

    public void run(IAction action) {
        
        final List<IResource> resources = getSelectedResources(getWindow(), selection);
        
        String jobName = getJobName(action);
        Job job = new Job(jobName) {
            @Override
            protected IStatus run(IProgressMonitor monitor) {
                if (execBeforeUIJob()) {
                    try {
                        doActions(resources, monitor);
                    } catch (CoreException e) {
                        LimyEclipsePluginUtils.log(e);
                        return Status.CANCEL_STATUS;
                    }
                    execAfterUIJob();
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
    
    // ------------------------ Public Methods
    
    /**
     * �I�����ꂽ�S�Ẵ��\�[�X�v�f��Ԃ��܂��B
     * <p>
     * �G�f�B�^�� : �ҏW���̃t�@�C��<br>
     * �G�������g : IResource, IFolder, IProject �̃C���X�^���X��I������������<br>
     * </p>
     * @param window WorkbenchWindow
     * @param selection �I��͈�
     * @return �I�����ꂽ�S�Ẵ��\�[�X�v�f
     */
    public static List<IResource> getSelectedResources(
            IWorkbenchWindow window, ISelection selection) {

        List<IResource> results = new ArrayList<IResource>();

        if (selection instanceof ITextSelection) {
            // �G�f�B�^���ɑI��͈͂��������ꍇ�Aselection �͋�Ȃ̂œn���Ă��Ӗ�������
            
            // �G�f�B�^�ŊJ���Ă���t�@�C�����\������Java�N���X�G�������g
            IEditorPart editor = window.getActivePage().getActiveEditor();
            IEditorInput input = editor.getEditorInput();
            if (input instanceof IFileEditorInput) {
                IFileEditorInput fileInput = (IFileEditorInput)input;
                results.add(fileInput.getFile());
            }
        }

        if (selection instanceof IStructuredSelection) {
            Object[] elements = ((IStructuredSelection)selection).toArray();
            for (Object element : elements) {
                if (element instanceof IResource) {
                    results.add((IResource)element);
                }
                if (element instanceof IJavaElement) {
                    results.add(((IJavaElement)element).getResource());
                }
            }
        }
        
        return results;
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
     * �����̃��\�[�X�ɑ΂��ď��������s���܂��B
     * �f�t�H���g�ł� doAction() ���J��Ԃ��Ăяo���܂��B
     * �ꊇ�������s�������Ƃ��ɂ͂��̃��\�b�h���I�[�o�[���C�h����K�v������܂��B
     * @param resources ���\�[�X
     * @param monitor �J�ڃ��j�^
     * @throws CoreException �R�A��O
     */
    protected void doActions(List<IResource> resources, IProgressMonitor monitor)
            throws CoreException {
        
        for (IResource resource : resources) {
            doAction(resource, monitor);
        }
    }
    
    /**
     * Job���s�O��UI�֘A���������s���܂��B
     * <p>
     * �T�u�N���X�ł͕K�v�ɉ����Ă��̃��\�b�h��Override���ĉ������B
     * </p>
     * @param monitor 
     * @return ���̌�̏������p������ꍇ��true
     * @throws CoreException 
     */
    protected boolean execBefore(IProgressMonitor monitor) throws CoreException {
        return true; // do nothing
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
     * �P��̃��\�[�X�ɑ΂��ď��������s���܂��B
     * @param resource ���\�[�X
     * @param monitor �J�ڃ��j�^
     * @throws CoreException �R�A��O
     */
    protected abstract void doAction(IResource resource, IProgressMonitor monitor)
            throws CoreException;

    // ------------------------ Private Methods
    
    private boolean execBeforeUIJob() {
        final AtomicBoolean result = new AtomicBoolean();
        UIJob job = new UIJob("UIJob") {
            @Override
            public IStatus runInUIThread(IProgressMonitor monitor) {
                try {
                    result.set(execBefore(monitor));
                } catch (CoreException e) {
                    LimyEclipsePluginUtils.log(e);
                    return Status.CANCEL_STATUS;
                }
                return Status.OK_STATUS;
            }
        };
        job.schedule();
        try {
            job.join();
        } catch (InterruptedException e) {
            LimyEclipsePluginUtils.log(e);
        }
        return result.get();
    }
    
    private void execAfterUIJob() {
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

}
