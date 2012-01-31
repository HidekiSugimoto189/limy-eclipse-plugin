/*
 * Created 2007/01/27
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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

/**
 * Checkstyle�ݒ�t�@�C���I���{�^�����N���b�N�����Ƃ��̓�����`���܂��B
 * @author Naoki Iwami
 */
public final class FileButtonSelection extends SelectionAdapter {

    // ------------------------ Fields

    /** �v���W�F�N�g */
    private final IProject project;

    /** �e�R���|�[�l���g */
    private final Composite comp;
    
    /** �I���t�@�C���p�X���i�[���镶����R���|�[�l���g */
    private final StringFieldEditor fieldEditor;
    
    /** �t�@�C���ꏊ��ʂ�I�����郉�W�I�{�^���O���[�v */
    private final RadioGroupFieldEditor radioField;

    // ------------------------ Constructors

    /**
     * FileButtonSelection �C���X�^���X���\�z���܂��B
     * @param project �v���W�F�N�g
     * @param comp �e�R���|�[�l���g
     * @param fieldEditor �I���t�@�C���p�X���i�[���镶����R���|�[�l���g
     * @param radioField �t�@�C���ꏊ��ʂ�I�����郉�W�I�{�^���O���[�v
     */
    public FileButtonSelection(IProject project, Composite comp, StringFieldEditor fieldEditor,
            RadioGroupFieldEditor radioField) {
        super();
        this.project = project;
        this.comp = comp;
        this.fieldEditor = fieldEditor;
        this.radioField = radioField;
    }

    // ------------------------ Implement Methods

    @Override
    public void widgetSelected(SelectionEvent e) {
    
        Composite control = radioField.getRadioBoxControl(comp);
        if (((Button)control.getChildren()[1]).getSelection()) {
            // �����t�@�C��
            setFromProject();
        }
        if (((Button)control.getChildren()[2]).getSelection()) {
            // �O���t�@�C��
            setFromExternal();
        }
        
    }

    // ------------------------ Private Methods

    /**
     * �v���W�F�N�g���̃t�@�C������I�����܂��B
     */
    private void setFromProject() {
        
        ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(
                comp.getShell(), new WorkbenchLabelProvider(), new WorkbenchContentProvider());

        dialog.setTitle("File Selection");
        dialog.setMessage("Checkstyle�ݒ�t�@�C����I�����ĉ������B");
        ViewerFilter filter = new ViewerFilter() {
            @Override
            public boolean select(Viewer viewer, Object parentElement, Object element) {
                if (element instanceof IFile) {
                    IFile file = (IFile)element;
                    return "xml".equals(file.getFileExtension());
                }
                if (element instanceof IProject) {
                    IProject tempTroject = (IProject)element;
                    return tempTroject.equals(project);
                }
                return true;
            }
        };
        dialog.addFilter(filter);
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        dialog.setInput(root);
        if (dialog.open() == Dialog.OK) {
            IFile file = (IFile)dialog.getFirstResult();
            fieldEditor.setStringValue(
                    file.getFullPath().removeFirstSegments(1).toString());
        }
    }
    
    /**
     * �O�����\�[�X����t�@�C����I�����܂��B
     */
    private void setFromExternal() {
        
        FileDialog dialog = new FileDialog(comp.getShell(), SWT.OPEN);
        dialog.setFilterExtensions(new String[] { "*.xml" });
        String file = dialog.open();
        if (file != null) {
            fieldEditor.setStringValue(file);
        }
    }

}