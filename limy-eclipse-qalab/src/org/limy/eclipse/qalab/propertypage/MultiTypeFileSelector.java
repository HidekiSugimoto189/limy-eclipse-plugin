/*
 * Created 2007/01/08
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
package org.limy.eclipse.qalab.propertypage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.limy.eclipse.common.resource.LimyResourceUtils;
import org.limy.eclipse.qalab.Messages;

/**
 * �t�@�C���I�����������������N���X�ł��B
 * <p>
 * Workspace / External ��2��ނɑΉ����Ă��܂��B
 * </p>
 * @author Naoki Iwami
 */
public class MultiTypeFileSelector extends SelectionAdapter {

    // ------------------------ Fields

    /** �v���W�F�N�g */
    private final IProject project;
    
    /** ���W�I�{�^�� */
    private Button innerRadioButton;

    /** �����l�̒l���i�[���ꂽ�R���g���[�� */
    private Text control;
    
    /** �t�@�C���I�����ɌĂяo����郊�X�i�[ */
    private PropertyChangeListener listener;
    
    /** �ΏۂƂȂ�t�@�C���g���q */
    private String targetExt;

    // ------------------------ Constructors

    /**
     * FileSelector�C���X�^���X���\�z���܂��B
     * @param innerRadioButton 
     * @param control �����l�̒l���i�[���ꂽ�R���g���[��
     * @param project �v���W�F�N�g
     * @param targetExt �ΏۂƂȂ�t�@�C���g���q
     * @param listener �t�@�C���I�����ɌĂяo����郊�X�i�[
     */
    public MultiTypeFileSelector(Button innerRadioButton,
            Text control,
            IProject project, String targetExt,
            PropertyChangeListener listener) {
        
        super();
        this.innerRadioButton = innerRadioButton;
        this.control = control;
        this.project = project;
        this.targetExt = targetExt;
        this.listener = listener;
    }

    // ------------------------ Override Methods

    @Override
    public void widgetSelected(SelectionEvent e) {

        if (innerRadioButton.getSelection()) {
            // �����t�@�C��
            setFromProject();
        } else {
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
                new Shell(), new WorkbenchLabelProvider(), new WorkbenchContentProvider());
  
        dialog.setTitle(Messages.TITLE_FILE_SELECT);
        dialog.setMessage(Messages.LABEL_FILE_SELECT);
        ViewerFilter filter = new ViewerFilter() {
            @Override
            public boolean select(Viewer viewer, Object parentElement, Object element) {
                if (element instanceof IFile) {
                    IFile file = (IFile)element;
                    return targetExt.equals(file.getFileExtension());
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
        
        // Text�R���g���[���̒l����_�C�A���O�̏����l������
        String value = control.getText();
        IPath path = project.getFullPath().append(value);
        if (path.segmentCount() > 1) {
            // �v���W�F�N�g�����͎w��ł��Ȃ��i���[�g�Ȃ̂Ŏw�肷��K�v�������j
            dialog.setInitialSelection(LimyResourceUtils.newFile(path));
        }
        
        if (dialog.open() == Dialog.OK) {
            
            Object result = dialog.getFirstResult();
            IPath resultPath = ((IResource)result).getFullPath();
            // /project1/fol1/txt ���I�����ꂽ�ꍇ�A�e�L�X�g�{�b�N�X�ɂ� "fol1/txt" ���i�[
            control.setText(resultPath.removeFirstSegments(1).toString());
            if (listener != null) {
                PropertyChangeEvent evt = new PropertyChangeEvent(
                        control, "resultPath", null, path
                );
                listener.propertyChange(evt);
            }
        }
    }

    /**
     * �O�����\�[�X����t�@�C����I�����܂��B
     */
    private void setFromExternal() {
        
        FileDialog dialog = new FileDialog(new Shell(), SWT.OPEN);
        dialog.setFilterExtensions(new String[] { "*." + targetExt });
        String file = dialog.open();
        if (file != null) {
            control.setText(file);
            if (listener != null) {
                PropertyChangeEvent evt = new PropertyChangeEvent(
                        control, "result", null, file
                );
                listener.propertyChange(evt);
            }
        }
    }

}
