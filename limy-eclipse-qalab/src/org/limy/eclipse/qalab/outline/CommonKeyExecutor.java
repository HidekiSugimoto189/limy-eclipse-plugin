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

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.limy.eclipse.common.LimyEclipsePluginUtils;
import org.limy.eclipse.common.ui.LimyUIUtils;

/**
 * ��ʓI�ȃL�[�A�N�V�������s�N���X�ł��B
 * @author Naoki Iwami
 */
public class CommonKeyExecutor implements KeyExecutor {
    
    /** �_�C�A���O�����T�|�[�g */
    private DialogSupport support;
    
    /** �|�b�v�A�b�v�_�C�A���O */
    private GraphPopupDialog dialog;

    /** �C���[�W�쐬�S�� */
    private ImageCreator creator;
    
    // ------------------------ Constructors

    /**
     * CommonKeyExecutor �C���X�^���X���\�z���܂��B
     * @param creator �C���[�W�쐬�S��
     * @param support �_�C�A���O�����T�|�[�g
     * @param dialog �|�b�v�A�b�v�_�C�A���O
     */
    public CommonKeyExecutor(ImageCreator creator, DialogSupport support, GraphPopupDialog dialog) {
        super();
        this.creator = creator;
        this.support = support;
        this.dialog = dialog;
    }
    
    // ------------------------ Implement Methods

    public void execute(char character) throws CoreException {
        switch (character) {
        case 'v':
            pushImageToView();
            break;
        case 'c':
            changeImageDirection();
            break;
        default:
            break;
        }
    }
    
    // ------------------------ Getter/Setter Methods

    /**
     * �|�b�v�A�b�v�_�C�A���O���擾���܂��B
     * @return �|�b�v�A�b�v�_�C�A���O
     */
    public GraphPopupDialog getDialog() {
        return dialog;
    }

    /**
     * �|�b�v�A�b�v�_�C�A���O��ݒ肵�܂��B
     * @param dialog �|�b�v�A�b�v�_�C�A���O
     */
    public void setDialog(GraphPopupDialog dialog) {
        this.dialog = dialog;
    }

    // ------------------------ Private Methods

    /**
     * ���݂̃C���[�W��Qalab Graph�r���[�ɕ\�����܂��B
     * @throws CoreException 
     */
    private void pushImageToView() throws CoreException {
        dialog.close();
        String viewId = "org.limy.eclipse.qalab.graphview.QalabGraphView";
        IGraphView view = (IGraphView)LimyUIUtils.showView(viewId);
        try {
            view.setImageData(creator, dialog);
        } catch (IOException e) {
            LimyEclipsePluginUtils.log(e);
        }
    }

    private void changeImageDirection() throws CoreException {
        try {
            PopupImage image = support.changeHorizontal();
            dialog.changeImageFile(image.getImageFile());
        } catch (IOException e) {
            LimyEclipsePluginUtils.log(e);
        }

    }
}
