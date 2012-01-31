/*
 * Created 2007/02/27
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

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.limy.eclipse.common.LimyEclipsePluginUtils;
import org.limy.eclipse.common.io.LimyIOUtils;
import org.limy.eclipse.qalab.common.LimyQalabEnvironment;
import org.limy.eclipse.qalab.outline.asm.MethodInfo;

import antlr.ANTLRException;
import antlr.collections.AST;

import com.puppycrawl.tools.checkstyle.TreeWalker;
import com.puppycrawl.tools.checkstyle.api.FileContents;

/**
 * CC�O���t�쐬�N���X�ł��B
 * @author Naoki Iwami
 */
public class CcImageCreator implements ImageCreator, ListenerCreator, DialogSupport {

    // ------------------------ Classes

    /**
     *
     * @author Naoki Iwami
     */
    class KeyImpl extends KeyAdapter {

        /** �e�_�C�A���O */
        private GraphPopupDialog dialog;

        public KeyImpl(GraphPopupDialog dialog) {
            super();
            this.dialog = dialog;
        }
        
        // ------------------------ Override Methods

        @Override
        public void keyPressed(KeyEvent evt) {
            boolean isExec = false;
            if (evt.character == 'c') {
                compact = !compact;
                isExec = true;
            }
            if (evt.character == 's') {
                enableSource = !enableSource;
                isExec = true;
            }
            
            if (isExec) {
                try {
                    image = CcGraphCreator.make(env, methodInfo);
                    dialog.changeImageFile(image.getImageFile());
                    image.setElements(SvgParser.makePointElementInfos(
                            image.getSvgImageFile(),
                            new LineClickPoint.Creator()));
                    dialog.setMouseMoveListener(createMouseMoveListener(dialog));
                } catch (IOException e) {
                    LimyEclipsePluginUtils.log(e);
                }
            }

            super.keyPressed(evt);
        }

    }

    // ------------------------ Fields
    
//    /** �_�C�A���O */
//    private GraphPopupDialog dialog;
    
    /** ���ݒ� */
    private LimyQalabEnvironment env;
    
    /** �Ώۃ��\�b�h */
    private IMethod targetMethod;

    /** �쐬�����C���[�W��� */
    private BasePopupImage image;
    
    /** �R���p�N�g�\���t���O */
    private boolean compact;
    
    /** �\�[�X�\���t���O */
    private boolean enableSource = true;

    /** ���\�b�h��� */
    private MethodInfo methodInfo;

    // ------------------------ Constructors

    /**
     * CcImageCreator�C���X�^���X���\�z���܂��B
     * @param env
     * @param targetElement
     */
    public CcImageCreator(LimyQalabEnvironment env, IMethod targetElement) {
        this.env = env;
        this.targetMethod = targetElement;
    }

    // ------------------------ Implement Methods
    
    public String getDialogTitle() {
        return "cc : " + image.getParam("cc") + "    (Enable key 'c' or 's')";
    }

    public KeyListener createKeyListener(GraphPopupDialog dialog) {
        return new KeyImpl(dialog);
    }

    public MouseListener createMouseListener(GraphPopupDialog dialog) {
        return null;
    }

    public MouseMoveListener createMouseMoveListener(GraphPopupDialog dialog) {
        return new CanvasMouseMoveListener(dialog.getCanvas(), image);
    }

    public PopupImage create() throws CoreException, IOException {
        
        IResource javaResource = targetMethod.getResource();
        
        File file = javaResource.getLocation().toFile();
        try {
            String content = LimyIOUtils.getContent(file);
            String[] lines = content.split("\\n");
            FileContents contents = new FileContents(file.getAbsolutePath(), lines);
            AST ast = TreeWalker.parse(contents);
            CcParser parser = new CcParser(
                    targetMethod.getElementName(), targetMethod.getSignature());
            MethodInfo method = parser.createMethodInfo(ast);
            if (method != null) {
                this.methodInfo = method;
                image = CcGraphCreator.make(env, method);
                image.setElements(SvgParser.makePointElementInfos(
                        image.getSvgImageFile(),
                        new LineClickPoint.Creator()));
                return image;
            }
        } catch (ANTLRException e) {
            LimyEclipsePluginUtils.log(e);
        }
        return null;
    }

    public IJavaElement getTargetElement() {
        return targetMethod;
    }

    public PopupImage changeLocation() throws CoreException, IOException {
        image.setHorizontal(!image.isHorizontal());
        create();
        return image;
    }

    public PopupImage changeHorizontal() throws CoreException, IOException {
        image.setHorizontal(!image.isHorizontal());
        create();
        return image;
    }

}
