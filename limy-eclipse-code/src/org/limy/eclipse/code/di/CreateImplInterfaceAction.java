/*
 * Created 2006/11/17
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
package org.limy.eclipse.code.di;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.limy.eclipse.code.LimyCodePlugin;
import org.limy.eclipse.common.LimyEclipsePluginUtils;
import org.limy.eclipse.common.io.LimyIOUtils;
import org.limy.eclipse.common.jdt.LimyJavaUtils;
import org.limy.eclipse.common.swt.LimySwtUtils;
import org.limy.eclipse.common.ui.AbstractJavaElementAction;

/**
 * �����N���X�ɑΉ�����C���^�[�t�F�C�X��������������A�N�V�����N���X�ł��B
 * @author Naoki Iwami
 */
public class CreateImplInterfaceAction extends AbstractJavaElementAction {

    // ------------------------ Constants

    /** ���s���� */
    private static final String BR = "\n";
    
    // ------------------------ Implement Methods

    @Override
    protected void doAction(IJavaElement javaElement, IProgressMonitor monitor)
            throws CoreException {
        
        action(javaElement);
    }

    // ------------------------ Private Methods

    /**
     * Java�v�f�ɑΉ�����C���^�[�t�F�C�X�������������܂��B
     * @param javaElement Java�v�f
     * @throws CoreException �R�A��O
     */
    private void action(IJavaElement javaElement) throws CoreException {
        createClass(LimyJavaUtils.getPrimaryType(javaElement));
    }

    /**
     * �����N���X�ɑΉ�����C���^�[�t�F�C�X���쐬���܂��B
     * @param type �����N���X(IType)
     * @throws CoreException �R�A��O
     */
    private void createClass(IType type) throws CoreException {
        
        try {
            IResource resource = LimyDIUtils.getInterfaceResource(type);
            if (resource != null) {
                String buffString = createInterfaceCode(type, resource);
                
                File file = resource.getLocation().toFile();
                OutputStreamWriter out = new OutputStreamWriter(
                        new FileOutputStream(file), ((IFile)resource).getCharset());
                out.write(buffString);
                out.close();
                resource.refreshLocal(IResource.DEPTH_INFINITE, null);
            } else {
                LimySwtUtils.showAlertDialog(
                        LimyCodePlugin.getResourceString("not.found.implement.class"));
            }
        } catch (IOException e) {
            LimyEclipsePluginUtils.log(e);
        }
        
    }

    /**
     * Java�����N���X�ɑΉ�����C���^�[�t�F�C�X��Java�R�[�h���쐬���܂��B
     * @param type Java�����N���X
     * @param resource �����C���^�[�t�F�C�X�t�@�C�����\�[�X
     * @return �C���^�[�t�F�C�X��Java�R�[�h
     * @throws IOException I/O��O
     * @throws CoreException �R�A��O
     */
    private String createInterfaceCode(IType type, IResource resource)
            throws IOException, CoreException {
        
        StringBuilder buff = new StringBuilder();
        buff.append(getHeader(resource, type));

        for (IMethod method : type.getMethods()) {
            
            int flags = method.getFlags();
            if (!Flags.isPublic(flags)) {
                continue;
            }
            
            String source = method.getSource();
            int index = source.indexOf('{');
            if (index >= 0) {
                String methodSource = source.substring(0, index);
                if (methodSource.endsWith(" ")) {
                    methodSource = methodSource.substring(0, methodSource.length() - 1);
                }
                methodSource = methodSource.replaceAll("public ", "");
                
                buff.append("    ");
                buff.append(methodSource).append(";").append(BR).append(BR);
            }
        }

        buff.append("}").append(BR);
        return buff.toString();
    }

    /**
     * �C���^�[�t�F�C�X�̃w�b�_���擾���܂��B
     * @param resource �C���^�[�t�F�C�X�̃��\�[�X
     * @param type �����N���X
     * @return �w�b�_������i"public interface X {"�܂Łj
     * @throws IOException I/O��O
     * @throws CoreException �R�A��O
     */
    private String getHeader(IResource resource, IType type)
            throws IOException, CoreException {
        
        File file = resource.getLocation().toFile();
        StringBuilder buff = new StringBuilder();
        if (file.exists()) {
            // �����C���^�[�t�F�C�X�����݂���Ƃ��̓t�@�C������w�b�_���擾
            FileInputStream in = new FileInputStream(file);
            try {
                String contents = LimyIOUtils.getContent(in, ((IFile)resource).getCharset());
                int index = contents.indexOf('{');
                if (index >= 0) {
                    buff.append(contents.substring(0, index + 1)).append(BR).append(BR);
                } else {
                    throw new IOException("�����C���^�[�t�F�C�X�t�@�C�����s���ł��B");
                }
            } finally {
                in.close();
            }
        } else {
            // �����t�@�C�������݂��Ȃ��Ƃ��̓f�t�H���g�̃w�b�_�𐶐�
            String name = type.getFullyQualifiedName();
            if (name.indexOf('.') >= 0) {
                String packageName = name.substring(0, name.lastIndexOf('.'));
                buff.append("package ").append(packageName).append(";").append(BR).append(BR);
            }
            String interfaceName = name.substring(name.lastIndexOf('.') + 1);
            interfaceName = interfaceName.substring(0, interfaceName.length() - 4);
            buff.append("/**").append(BR);
            buff.append(" * ").append(BR);
            buff.append(" */").append(BR);
            buff.append("public interface ").append(interfaceName).append(" {").append(BR);
        }
        return buff.toString();
    }

}
