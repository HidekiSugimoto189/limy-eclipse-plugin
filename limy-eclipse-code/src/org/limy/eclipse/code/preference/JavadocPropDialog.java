/*
 * Created 2005/07/21
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
package org.limy.eclipse.code.preference;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.limy.eclipse.common.LimyEclipsePluginUtils;

/**
 * Javadoc�����x���ݒ�t�@�C����\������_�C�A���O�N���X�ł��B
 * @depend - - - JavadocPropUI
 * @author Naoki Iwami
 */
public class JavadocPropDialog extends ApplicationWindow {
    
    // ------------------------ Fields
    
    /**
     * �i�[��v���p�e�B�t�@�C��
     */
    private File propFile;
    
    /**
     * ��i�e�[�u���r���[�A
     */
    private TableViewer normalViewer;
    
    /**
     * Javadoc�J�X�^�}�C�Y���Bean
     */
    private LimyJavadocBean javadocBean;

    /**
     * �X�V�t���O
     */
    private boolean modified;
    
    // ------------------------ Constructors
    
    /**
     * @param parentShell
     * @param propFile
     */
    public JavadocPropDialog(Shell parentShell, File propFile) {
        super(parentShell);
        this.propFile = propFile;
        modified = false;
    }
    
    // ------------------------ Implement Methods
    
    protected Control createContents(Composite parent) {
        
        getShell().setText("Limy Javadoc Comments Editor");

        Composite comp = new Composite(parent, SWT.NONE);
        comp.setLayout(new FormLayout());
        
        try {
            javadocBean = new LimyJavadocBean(getPropFile());
        } catch (IOException e) {
            javadocBean = new LimyJavadocBean();
            LimyEclipsePluginUtils.log(e);
        }
        
        createTable(comp);
        parent.setSize(700, 500);

        return comp;
    }
    
    // ------------------------ Override Methods
    
    @Override
    public boolean close() {
        
        if (modified) {
            String[] buttons = new String[] {
                    IDialogConstants.YES_LABEL,
                    IDialogConstants.NO_LABEL,
                    IDialogConstants.CANCEL_LABEL,
            };
            MessageDialog d = new MessageDialog(
                getShell(), "Save Resourse",
                null,
                MessageFormat.format(
                        "���e���X�V����Ă��܂��B''{0}'' �t�@�C�����X�V���܂����H",
                        getPropFile().getAbsolutePath()),
                MessageDialog.QUESTION, buttons, 0);
            int ret = d.open();
            
            if (ret == 0/*YES*/) {
                try {
                    javadocBean.save(getPropFile());
                } catch (IOException e) {
                    LimyEclipsePluginUtils.log(e);
                }
            }
            if (ret == 2/*CANCEL*/) {
                return false;
            }
        }
        return super.close();
    }
    
    // ------------------------ Getter/Setter Methods

    /**
     * �i�[��v���p�e�B�t�@�C�����擾���܂��B
     * @return �i�[��v���p�e�B�t�@�C��
     */
    public File getPropFile() {
        return propFile;
    }

    /**
     * �i�[��v���p�e�B�t�@�C����ݒ肵�܂��B
     * @param propFile �i�[��v���p�e�B�t�@�C��
     */
    public void setPropFile(File propFile) {
        this.propFile = propFile;
    }

    /**
     * ��i�e�[�u���r���[�A���擾���܂��B
     * @return ��i�e�[�u���r���[�A
     */
    public TableViewer getNormalViewer() {
        return normalViewer;
    }

    /**
     * ��i�e�[�u���r���[�A��ݒ肵�܂��B
     * @param normalViewer ��i�e�[�u���r���[�A
     */
    public void setNormalViewer(TableViewer normalViewer) {
        this.normalViewer = normalViewer;
    }

    /**
     * Javadoc�J�X�^�}�C�Y���Bean���擾���܂��B
     * @return Javadoc�J�X�^�}�C�Y���Bean
     */
    public LimyJavadocBean getJavadocBean() {
        return javadocBean;
    }

    /**
     * Javadoc�J�X�^�}�C�Y���Bean��ݒ肵�܂��B
     * @param javadocBean Javadoc�J�X�^�}�C�Y���Bean
     */
    public void setJavadocBean(LimyJavadocBean javadocBean) {
        this.javadocBean = javadocBean;
    }

    /**
     * �X�V�t���O���擾���܂��B
     * @return �X�V�t���O
     */
    public boolean isModified() {
        return modified;
    }

    /**
     * �X�V�t���O��ݒ肵�܂��B
     * @param modified �X�V�t���O
     */
    public void setModified(boolean modified) {
        this.modified = modified;
    }

    // ------------------------ Private Methods

    /**
     * SWT�e�[�u���𐶐����܂��B
     * @param comp �e�R���|�[�l���g
     */
    private void createTable(Composite comp) {
        
        new JavadocPropUI().createAllComps(this, comp, javadocBean);
    }

}
