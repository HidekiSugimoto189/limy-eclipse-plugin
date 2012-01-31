/*
 * Created 2007/01/05
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
package org.limy.eclipse.qalab.mark;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

import net.sourceforge.cobertura.instrument.CoberturaInstrument;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.limy.eclipse.common.LimyEclipsePluginUtils;
import org.limy.eclipse.common.jdt.LimyJavaUtils;
import org.limy.eclipse.common.resource.LimyResourceUtils;
import org.limy.eclipse.qalab.common.LimyQalabEnvironment;
import org.limy.eclipse.qalab.common.LimyQalabUtils;
import org.limy.eclipse.qalab.common.QalabResourceUtils;

/**
 * Cobertura�p�̃}�[�J�[�쐬�N���X�ł��B
 * @author Naoki Iwami
 */
public final class CoberturaMarkCreator implements MarkCreator {
    
    /** �B��̃C���X�^���X */
    private static CoberturaMarkCreator instance = new CoberturaMarkCreator();
    
    /**
     * private constructor
     */
    private CoberturaMarkCreator() {
    }
    
    public static CoberturaMarkCreator getInstance() {
        return instance;
    }
    
    // ------------------------ Implement Methods

    public String getName() {
        return "cobertura";
    }

    public boolean markJavaElement(LimyQalabEnvironment env,
            Collection<IJavaElement> elements, IProgressMonitor monitor) {
        
        File dataFile = LimyQalabUtils.createTempFile(env.getProject(), "cobertura.ser");
        System.setProperty("net.sourceforge.cobertura.datafile",
                dataFile.getAbsolutePath());
        dataFile.delete();
        
        try {
            
            IProject project = env.getProject();
            CoberturaInstrument obj = new CoberturaInstrument(dataFile);
            
            // Instrument�ΏۂƂȂ�Java�t�@�C���ꗗ���擾
            IResource[] resources = getTargetJavaFiles(env, elements);
            
            // Class�t�@�C���p�̃��\�[�X�ɕϊ�
            IResource[] classResources = new IResource[resources.length];
            for (int i = 0; i < resources.length; i++) {
                classResources[i] = QalabResourceUtils.getClassResource(env,
                        resources[i]).getResource();
            }

            // Instrument�t�@�C���i�����cobertura.ser�j���쐬
            obj.makeInstrument(project, classResources);
            
            // �e�X�g�̎��s
            new ExecuteUIJob(env, resources).schedule();
            
            return true;
        } catch (CoreException e) {
            LimyEclipsePluginUtils.log(e);
        }
        return false;
    }
    
    public boolean markResource(LimyQalabEnvironment env,
            IResource resource, IProgressMonitor monitor) {

        throw new UnsupportedOperationException("markResource");
//        return executeTest(env, resource);
    }

    public boolean markResourceTemporary(LimyQalabEnvironment env,
            IResource resource,
            IProgressMonitor monitor) {

        throw new UnsupportedOperationException("markResourceTemporary");
//        return executeTest(env, resource);
    }

    public boolean markResources(LimyQalabEnvironment env,
            Collection<IResource> allResources, IProgressMonitor monitor) {
        
        throw new UnsupportedOperationException("markResourceTemporary");
//        File dataFile = LimyQalabUtils.createTempFile(env.getProject(), "cobertura.ser");
//        System.setProperty("net.sourceforge.cobertura.datafile",
//                dataFile.getAbsolutePath());
//        dataFile.delete();
//        
//        try {
//            
//            IProject project = env.getProject();
//            CoberturaInstrument obj = new CoberturaInstrument(dataFile);
//            
//            // Instrument�ΏۂƂȂ�Java�t�@�C���ꗗ���擾
//            IResource[] resources = getTargetJavaFiles(env, allResources);
//            
//            // Class�t�@�C���p�̃��\�[�X�ɕϊ�
//            IResource[] classResources = new IResource[resources.length];
//            for (int i = 0; i < resources.length; i++) {
//                classResources[i] = QalabResourceUtils.getClassResource(env,
//                        resources[i]).getResource();
//            }
//
//            // Instrument�t�@�C���i�����cobertura.ser�j���쐬
//            obj.makeInstrument(project, classResources);
//            
//            // �e�X�g�̎��s
//            new ExecuteUIJob(env, resources).schedule();
//            
//            return true;
//        } catch (CoreException e) {
//            LimyEclipsePluginUtils.log(e);
//        }
//        return false;
    }
    
    // ------------------------ Private Methods

//    /**
//     * ���\�[�X�ɑΉ�����e�X�g�����s���ă}�[�J�[���쐬���܂��B
//     * @param env 
//     * @param resource ���\�[�X
//     * @return �����ɐ��������� true
//     */
//    private boolean executeTest(LimyQalabEnvironment env,
//            IResource resource) {
//        
//        File rootDir = LimyQalabPlugin.getDefault().getPluginRoot();
//        File dataFile = new File(rootDir, "cobertura.ser");
//        System.setProperty("net.sourceforge.cobertura.datafile",
//                dataFile.getAbsolutePath());
//        dataFile.delete();
//
//        if (QalabResourceUtils.isTestResource(resource)) {
//            // resource ���e�X�g�t�@�C���������ꍇ�A��������s����
//            try {
//                String className = LimyQalabUtils.getQualifiedClassName(env, resource);
//                // �e�X�g�t�@�C���ɑΉ����郁�C��Java�t�@�C�����擾
//                IResource mainJavaResource = QalabResourceUtils.getJavaResource(
//                        env, className.substring(0, className.length() - 4),
//                        false);
//                
//                ResourceWithBasedir classResource = QalabResourceUtils.getClassResource(
//                        env, mainJavaResource);
//                if (classResource != null) {
//                    // �P��Java�t�@�C����instrument�����cobertura.ser���쐬
//                    CoberturaInstrument obj = new CoberturaInstrument(dataFile);
//                    obj.makeInstrument(classResource.getResource());
//                }
//                
//                // ���C��Java�t�@�C���̃}�[�J�[���폜����
//                mainJavaResource.deleteMarkers(
//                        LimyQalabMarker.DEFAULT_ID, true, IResource.DEPTH_ZERO);
//                addMarker(env, mainJavaResource);
//                return true;
//
//            } catch (CoreException e) {
//                LimyEclipsePluginUtils.log(e);
//            }
//            return false;
//        }
//
//        try {
//            ResourceWithBasedir classResource = QalabResourceUtils.getClassResource(
//                    env, resource);
//            if (classResource != null) {
//                // �P��Java�t�@�C����instrument�����cobertura.ser���쐬
//                CoberturaInstrument obj = new CoberturaInstrument(dataFile);
//                obj.makeInstrument(classResource.getResource());
//            }
//
//            addMarker(env, resource);
//            return true;
//            
//        } catch (CoreException e) {
//            LimyEclipsePluginUtils.log(e);
//        }
//        return false;
//    }
//
//    /**
//     * Java�N���X�̃e�X�g�P�[�X�����s���ă}�[�J�[���쐬���܂��B
//     * @param env 
//     * @param mainJavaResource Java�N���X�i���C���N���X�j
//     */
//    private void addMarker(LimyQalabEnvironment env,
//            IResource mainJavaResource) {
//        
//        new ExecuteUIJob(env, mainJavaResource).schedule();
//    }
    
//    /**
//     * Instrument�ΏۂƂȂ�Java�t�@�C���ꗗ��Ԃ��܂��B
//     * @param env 
//     * @param allResources ���\�[�X�ꗗ
//     * @return Instrument�ΏۂƂȂ�Java�t�@�C���ꗗ
//     * @throws CoreException �R�A��O
//     */
//    private IResource[] getTargetJavaFiles(LimyQalabEnvironment env,
//            Collection<IResource> allResources)
//            throws CoreException {
//        
//        Collection<IResource> results = new HashSet<IResource>();
//        
//        for (IResource resource : allResources) {
//            if (QalabResourceUtils.isTestResource(resource)) {
//                // �e�X�g�t�@�C���̏ꍇ�A�Ή����郁�C���t�@�C�����擾
//                if (resource.getType() == IResource.FILE) {
//                    String name = QalabResourceUtils.getQualifiedMainClassName(env, resource);
//                    IType type = env.getJavaProject().findType(name);
//                    if (type != null) {
//                        results.add(type.getResource());
//                    }
//                } else {
//                    env.getJavaProject().findPackageFragment(resource.getFullPath());
////                    LimyQalabJavaUtils.getBinDirPath(el)
//                }
//            } else if (LimyQalabUtils.isIgnoreSource(env, resource.getFullPath())) {
//                // do nothing
//            } else {
//                results.add(resource);
//            }
//        }
//        
//        return results.toArray(new IResource[results.size()]);
//    }

    /**
     * Instrument�ΏۂƂȂ�Java�t�@�C���ꗗ��Ԃ��܂��B
     * @param env 
     * @param javaElements Java�v�f�ꗗ
     * @return Instrument�ΏۂƂȂ�Java�t�@�C���ꗗ
     * @throws CoreException �R�A��O
     */
    private IResource[] getTargetJavaFiles(LimyQalabEnvironment env,
            Collection<IJavaElement> javaElements)
            throws CoreException {
        
        Collection<IResource> results = new HashSet<IResource>();
        
        for (IJavaElement javaElement : javaElements) {
            IResource resource = javaElement.getResource();
            if (QalabResourceUtils.isTestResource(resource)) {
                // �e�X�g�t�@�C���̏ꍇ�A�Ή����郁�C���t�@�C�����擾
                if (resource.getType() == IResource.FILE) {
                    String name = QalabResourceUtils.getQualifiedMainClassName(env, resource);
                    IType type = env.getJavaProject().findType(name);
                    if (type != null) {
                        results.add(type.getResource());
                    }
                } else if (javaElement.getElementType() == IJavaElement.PACKAGE_FRAGMENT) {
                    Collection<IPath> sourcePaths = env.getSourcePaths(true);
                    for (IPath path : sourcePaths) {
                        IFolder folder = LimyResourceUtils.newFolder(
                                path.append(javaElement.getElementName().replace('.', '/'))
                        );
                        if (folder.exists()) {
                            results.add(folder);
                        }
                    }
                }
            } else if (LimyQalabUtils.isIgnoreSource(env, resource.getFullPath())) {
                // QA�ΏۊO���\�[�X
            } else {
                IType[] types = LimyJavaUtils.getAllTypes(javaElement);
                for (IType type : types) {
                    results.add(type.getResource());
                }
            }
        }
        
        return results.toArray(new IResource[results.size()]);
    }

}
