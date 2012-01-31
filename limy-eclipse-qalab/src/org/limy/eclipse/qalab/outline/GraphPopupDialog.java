/*
 * Created 2007/02/14
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

import java.io.File;
import java.io.IOException;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.limy.eclipse.common.LimyEclipsePluginUtils;
import org.limy.eclipse.common.io.LimyIOUtils;
import org.limy.eclipse.common.swt.DragScrolledComposite;
import org.limy.eclipse.common.swt.HighlightCanvas;
import org.limy.eclipse.core.LimyEclipsePlugin;

/**
 * �C���[�W��\������|�b�v�A�b�v�_�C�A���O�ł��B
 * @author Naoki Iwami
 */
public class GraphPopupDialog extends PopupDialog {

    /**
     * �C���[�W�ۑ��A�N�V����
     * @author Naoki Iwami
     */
    class SaveAction extends Action {

        public SaveAction() {
            super("&Save image...");
        }

        @Override
        public void run() {
            FileDialog dialog = new FileDialog(new Shell(), SWT.SAVE);
            dialog.setFilterExtensions(new String[] { "*.png" });
            String filePath = dialog.open();
            if (filePath != null) {
                try {
                    LimyIOUtils.copyFile(imageFile, new File(filePath));
                } catch (IOException e) {
                    LimyEclipsePluginUtils.log(e);
                }
            }
        }

    }
    
    // ------------------------ Fields

    /** �C���[�W�t�@�C�� */
    private File imageFile;
    
    /** �C���[�W�f�[�^ */
    private ImageData imageData;
    
    /** �n�C���C�g�Ή��L�����o�X */
    private HighlightCanvas canvas;

    /** ���݂̃}�E�X�ړ��n���h�� */
    private MouseMoveListener mouseMoveListener;

    /** ���݂̃}�E�X�N���b�N�n���h�� */
    private MouseListener mouseListener;

    /** �Ώ�Java�v�f */
    private IJavaElement targetElement;

    // ------------------------ Constructors

    /**
     * GraphPopupDialog�C���X�^���X���\�z���܂��B
     * @param parent �V�F��
     * @param title �^�C�g��
     * @param info �C���t�H���[�V����������
     * @param imageFile �C���[�W�t�@�C��
     * @param targetElement �Ώ�Java�v�f
     */
    public GraphPopupDialog(Shell parent,
            String title, String info, File imageFile,
            IJavaElement targetElement) {
        
        super(parent, SWT.NONE, true, true, true, false, title, info);
        this.imageFile = imageFile;
        this.targetElement = targetElement;
        
    }

    // ------------------------ Override Methods

    @Override
    protected IDialogSettings getDialogSettings() {
        String sectionName = "limyEclipseQalabGraph";
        IDialogSettings section = LimyEclipsePlugin.getDefault()
                .getDialogSettings().getSection(sectionName);
        if (section == null) {
            section = LimyEclipsePlugin.getDefault()
                    .getDialogSettings().addNewSection(sectionName);
//            section.put(getClass().getName() + "DIALOG_USE_PERSISTED_BOUNDS", true);
        }
        return section;
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        
        DragScrolledComposite comp = new DragScrolledComposite(parent);
        canvas = new HighlightCanvas(comp, SWT.NONE);
        comp.setMainComposite(canvas);
        
        imageData = new ImageData(imageFile.getAbsolutePath());
        canvas.setSize(imageData.width, imageData.height);
        canvas.setBackgroundImage(new Image(comp.getDisplay(), imageData));
        
        return comp;
    }
    
    @Override
    protected Point getInitialSize() {
        Point size = new Point(imageData.width + 4, imageData.height + 44 - 14/*info*/);
        if (size.x > 1000) {
            size.x = 1000;
            size.y += 18;
        }
        if (size.y > 800) {
            size.y = 800;
            size.x += 18;
        }
        return size;
    }
    
    @Override
    protected void fillDialogMenu(IMenuManager dialogMenu) {
        super.fillDialogMenu(dialogMenu);
        dialogMenu.add(new SaveAction());
    }
    
    // ------------------------ Public Methods

    /**
     * �C���[�W�t�@�C����ύX���܂��B
     * @param newImageFile
     */
    public void changeImageFile(File newImageFile) {
        imageData = new ImageData(imageFile.getAbsolutePath());
        canvas.setSize(imageData.width, imageData.height);
        canvas.setBackgroundImage(new Image(canvas.getDisplay(), imageData));
        initializeBounds();
    }

    /**
     * �}�E�X�ړ��n���h����ݒ肵�܂��B�����Ăяo���ƁA�ȑO�ɐݒ肵���n���h���͉�������܂��B
     * @param mouseMoveListener 
     */
    public void setMouseMoveListener(MouseMoveListener mouseMoveListener) {
        if (this.mouseMoveListener != null) {
            canvas.removeMouseMoveListener(this.mouseMoveListener);
        }
        this.mouseMoveListener = mouseMoveListener;
        canvas.addMouseMoveListener(mouseMoveListener);
    }

    /**
     * �}�E�X�N���b�N�n���h����ݒ肵�܂��B�����Ăяo���ƁA�ȑO�ɐݒ肵���n���h���͉�������܂��B
     * @param mouseListener 
     */
    public void setMouseListener(MouseListener mouseListener) {
        if (this.mouseListener != null) {
            canvas.removeMouseListener(this.mouseListener);
        }
        this.mouseListener = mouseListener;
        canvas.addMouseListener(mouseListener);
    }

    // ------------------------ Getter/Setter Methods

    /**
     * �C���[�W�f�[�^���擾���܂��B
     * @return �C���[�W�f�[�^
     */
    public ImageData getImageData() {
        return imageData;
    }

    /**
     * �n�C���C�g�Ή��L�����o�X���擾���܂��B
     * @return �n�C���C�g�Ή��L�����o�X
     */
    public HighlightCanvas getCanvas() {
        return canvas;
    }

    /**
     * ���݂̃}�E�X�N���b�N�n���h�����擾���܂��B
     * @return ���݂̃}�E�X�N���b�N�n���h��
     */
    public MouseListener getMouseListener() {
        return mouseListener;
    }

    /**
     * �Ώ�Java�v�f���擾���܂��B
     * @return �Ώ�Java�v�f
     */
    public IJavaElement getTargetElement() {
        return targetElement;
    }

}
