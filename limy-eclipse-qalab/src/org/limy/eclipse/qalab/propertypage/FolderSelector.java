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
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.limy.eclipse.common.resource.LimyResourceUtils;
import org.limy.eclipse.qalab.Messages;

/**
 * �t�H���_�I�����������������N���X�ł��B
 * @author Naoki Iwami
 */
public class FolderSelector extends SelectionAdapter {

    // ------------------------ Fields

    /** �v���W�F�N�g�ꗗ */
    private final Collection<IProject> projects;
    
    /** �����l�̒l���i�[���ꂽ�R���g���[�� */
    private Control control;
    
    /** �t�H���_�I�����ɌĂяo����郊�X�i�[ */
    private PropertyChangeListener listener;

    // ------------------------ Constructors

    /**
     * FolderSelector�C���X�^���X���\�z���܂��B
     * @param control �����l�̒l���i�[���ꂽ�R���g���[��
     * @param project �v���W�F�N�g
     * @param listener �t�H���_�I�����ɌĂяo����郊�X�i�[�inull�j
     */
    public FolderSelector(Control control, IProject project, PropertyChangeListener listener) {
        super();
        this.control = control;
        this.projects = new ArrayList<IProject>();
        projects.add(project);
        this.listener = listener;
    }

    /**
     * FolderSelector�C���X�^���X���\�z���܂��B
     * @param control �����l�̒l���i�[���ꂽ�R���g���[��
     * @param projects �v���W�F�N�g�ꗗ
     * @param listener �t�H���_�I�����ɌĂяo����郊�X�i�[
     */
    public FolderSelector(Control control, Collection<IProject> projects,
            PropertyChangeListener listener) {
        
        super();
        this.control = control;
        this.projects = projects;
        this.listener = listener;
    }

    // ------------------------ Override Methods

    @Override
    public void widgetSelected(SelectionEvent e) {
        setFromProject();
    }

    // ------------------------ Private Methods

    /**
     * �v���W�F�N�g���̃t�@�C������I�����܂��B
     */
    private void setFromProject() {
        
        ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(
                new Shell(), new WorkbenchLabelProvider(), new WorkbenchContentProvider());
  
        dialog.setTitle(Messages.TITLE_FOLDER_SELECT);
        dialog.setMessage(Messages.LABEL_FOLDER_SELECT);
        ViewerFilter filter = new ViewerFilter() {
            @Override
            public boolean select(Viewer viewer, Object parentElement, Object element) {
                if (element instanceof IFile) {
                    return false;
                }
                if (element instanceof IFolder) {
                    return true;
                }
                if (element instanceof IProject) {
                    IProject tempTroject = (IProject)element;
                    return projects.contains(tempTroject);
                }
                return true;
            }
        };
        dialog.addFilter(filter);
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        dialog.setInput(root);
        
        if (control instanceof Text) {
            String value = ((Text)control).getText();
            IProject firstProject = projects.iterator().next();
            IPath path = firstProject.getFullPath().append(value);
            if (path.segmentCount() > 1) {
                // �v���W�F�N�g�����͎w��ł��Ȃ��i���[�g�Ȃ̂Ŏw�肷��K�v�������j
                dialog.setInitialSelection(LimyResourceUtils.newFolder(path));
            }
        }
        
        if (dialog.open() == Dialog.OK) {
            
            Object[] results = dialog.getResult();
            for (Object result : results) {
                PropertyChangeEvent evt = new PropertyChangeEvent(
                        control, "resultPath", null, ((IResource)result).getFullPath()
                );
                listener.propertyChange(evt);
            }
        }
    }

}
