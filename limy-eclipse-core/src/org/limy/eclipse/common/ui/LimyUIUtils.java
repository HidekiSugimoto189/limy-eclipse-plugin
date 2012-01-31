/*
 * Created 2006/12/01
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

import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.ITextEditor;
import org.limy.eclipse.common.LimyEclipsePluginUtils;

/**
 * UI�֘A�̃��[�e�B���e�B�N���X�ł��B
 * @author Naoki Iwami
 */
public final class LimyUIUtils {
    
    /**
     * private constructor
     */
    private LimyUIUtils() { }

    /**
     * �r���[��Ԃ��܂��B�J���Ă��Ȃ��ꍇ��nul��Ԃ��܂��B
     * @param id �r���[ID
     * @return �r���[
     */
    public static IViewPart findView(String id) {
        IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
        
        for (IWorkbenchWindow window : windows) {
            IViewPart view = window.getActivePage().findView(id);
            if (view != null) {
                return view;
            }
        }
        return null;
    }

    /**
     * �r���[���J���ĕԂ��܂��B
     * @param id �r���[ID
     * @return �r���[
     * @throws PartInitException 
     */
    public static IViewPart showView(String id) throws PartInitException {
        IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
        
        for (IWorkbenchWindow window : windows) {
            IViewPart view = window.getActivePage().showView(id);
            if (view != null) {
                return view;
            }
        }
        return null;
    }

    /**
     * ���݃A�N�e�B�u�ȃG�f�B�^���擾���܂��B
     * @return ���݃A�N�e�B�u�ȃG�f�B�^
     */
    public static IEditorPart getActiveEditor() {
        return LimyUIUtils.getWorkbenchPage().getActiveEditor();
    }

    /**
     * ���݃A�N�e�B�u�ȃe�L�X�g�G�f�B�^���擾���܂��B
     * @return ���݃A�N�e�B�u�ȃe�L�X�g�G�f�B�^
     */
    public static ITextEditor getActiveTextEditor() {
        IEditorPart editorPart = getActiveEditor();
        if (editorPart instanceof AbstractTextEditor) {
            return (AbstractTextEditor)editorPart;
        }
        return null;
    }

    /**
     * IWorkbenchPage���擾���܂��B
     * @return IWorkbenchPage
     */
    public static IWorkbenchPage getWorkbenchPage() {
        return PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow().getActivePage();
    }

    /**
     * �t�@�C�����G�f�B�^�ŊJ���܂��B
     * @param file �t�@�C��
     * @return �J�����e�L�X�g�G�f�B�^
     */
    public static ITextEditor openFile(IFile file) {
        IWorkbenchPage workbenchPage = getWorkbenchPage();
        try {
            IEditorPart editor = workbenchPage.openEditor(new FileEditorInput(file),
                        IDE.getEditorDescriptor(file).getId());
            if (editor instanceof ITextEditor) {
                return (ITextEditor)editor;
            }
        } catch (PartInitException e) {
            LimyEclipsePluginUtils.log(e);
        }
        return null;
    }
    
    /**
     * Java�v�f���G�f�B�^�ŊJ���܂��B
     * @param element Java�v�f
     * @return �J�����G�f�B�^
     * @throws CoreException �R�A��O
     */
    public static IEditorPart openInEditor(IJavaElement element) throws CoreException {
        return JavaUI.openInEditor(element, true, true);
    }
    
    /**
     * �t�@�C�����G�f�B�^�ŊJ���܂��B
     * @param file �t�@�C��
     * @param lineNumber �s�ԍ�
     */
    public static void openFile(IFile file, int lineNumber) {
        gotoLineForEditor(openFile(file), lineNumber);
    }

    /**
     * �t�@�C�����G�f�B�^�ŊJ���܂��B
     * @param editorInput 
     * @param editorId 
     * @param offset 
     * @return �J�����e�L�X�g�G�f�B�^
     */
    public static ITextEditor openFile(IEditorInput editorInput, String editorId, int offset) {
        IWorkbenchPage workbenchPage = getWorkbenchPage();
        try {
            IEditorPart editor = workbenchPage.openEditor(editorInput, editorId);
            if (editor instanceof ITextEditor) {
                ITextEditor textEditor = (ITextEditor)editor;
                textEditor.selectAndReveal(offset, 0);
                return textEditor;
            }
        } catch (PartInitException e) {
            LimyEclipsePluginUtils.log(e);
        }
        return null;
    }

    /**
     * �m�F�_�C�A���O��\�����܂��B
     * @param message �\�����b�Z�[�W
     */
    public static void showConfirmDialog(String message) {
        MessageBox dialog = new MessageBox(new Shell(), SWT.OK);
        dialog.setText("�m�F");
        dialog.setMessage(message);
        dialog.open();
    }

    /**
     * �u���E�U�œ����URL���J���܂��B
     * @param url URL
     */
    public static void openBrowser(URL url) {
        IWorkbenchBrowserSupport support = PlatformUI.getWorkbench().getBrowserSupport();
        
        try {
            support.getExternalBrowser().openURL(url);
        } catch (PartInitException e) {
            LimyEclipsePluginUtils.log(e);
        }

    }

    // ------------------------ Private Methods

    /**
     * �G�f�B�^���Ŏw�肵���s�ԍ��ɃW�����v���܂��B
     * @param editor �G�f�B�^
     * @param lineNumber �s�ԍ�
     */
    private static void gotoLineForEditor(ITextEditor editor, int lineNumber) {
        
        if (editor == null) {
            return;
        }
        
        IDocument document = editor.getDocumentProvider().getDocument(editor.getEditorInput());
        try {
            int offset = document.getLineOffset(lineNumber - 1);
            editor.selectAndReveal(offset, 0);
        } catch (BadLocationException e) {
            LimyEclipsePluginUtils.log(e);
        }
    }

}
