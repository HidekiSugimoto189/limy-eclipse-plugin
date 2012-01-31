/*
 * Created 2006/08/19
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
package org.limy.eclipse.qalab.common;

import java.io.File;
import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.limy.eclipse.common.resource.LimyResourceUtils;

/**
 * Limy Qalab�p�̃��[�e�B���e�B�N���X�ł��B
 * @author Naoki Iwami
 */
public final class LimyQalabUtils {

    /**
     * private constructor
     */
    private LimyQalabUtils() { }

    /**
     * �I�����ꂽ�A�C�e���̐eJava�v���W�F�N�g��Ԃ��܂��B
     * @param selection �I����e
     * @return �eJava�v���W�F�N�g�i���݂��Ȃ����null�j
     */
    public static IJavaProject getJavaProject(IStructuredSelection selection) {
        
        // �I�����ꂽ�ŏ��̃A�C�e�����擾
        Object element = selection.getFirstElement();
        if (element == null) {
            return null;
        }
        
        // Java�v���W�F�N�g���擾
        IJavaProject project = null;
        if (element instanceof IResource) {
            IResource resource = (IResource)element;
            IProject normalProject = resource.getProject();
            project = JavaCore.create(normalProject);
        }
        if (element instanceof IJavaElement) {
            IJavaElement javaEl = (IJavaElement)element;
            project = javaEl.getJavaProject();
        }
        return project;

    }

    /**
     * Java�t�@�C����QA�ΏۊO���ǂ����𔻒肵�܂��B
     * @param env 
     * @param path Java�t�@�C��
     * @param store �v���W�F�N�gStore
     * @return QA�ΏۊO�\�[�X�f�B���N�g���Ȃ�ΐ^
     */
    public static boolean isIgnoreSource(LimyQalabEnvironment env, IPath path) {
        
        String packages = env.getStore().getString(LimyQalabConstants.IGNORE_PACKAGES);
        String[] ignorePackages = packages.split("\n");
        
        IResource resource = LimyResourceUtils.newFile(path);
        try {
            String className = getQualifiedClassName(env, resource);
            if (className == null) {
                return false;
            }
            for (String ignorePackage : ignorePackages) {
                if (className.startsWith(ignorePackage + ".")) {
                    return true;
                }
            }
        } catch (CoreException e) {
            // do nothing
        }
        return false;
    }

    /**
     * Java�v���W�F�N�g��JDK�o�[�W�������擾���܂��B
     * @param project Java�v���W�F�N�g
     * @return JDK�o�[�W����
     */
    public static String getJdkVersion(IJavaProject project) {
        return project.getOption(JavaCore.COMPILER_SOURCE, true);
    }

    /**
     * IPath���Ό`���̃p�X���A��΃p�X�ɕϊ����ĕԂ��܂��B
     * @param javaProject Java�v���W�F�N�g�inull�j
     * @param path ���΃p�X
     * @return ��΃p�X
     * @throws CoreException �R�A��O
     */
    public static String createFullPath(IJavaProject javaProject, IPath path)
            throws CoreException {
        
        if (javaProject != null
                && javaProject.getProject().getName().equals(path.segment(0))) {
            
            // �v���W�F�N�g������̏ꍇ
            return javaProject.getProject().getLocation()
            .append(path.removeFirstSegments(1)).toString();
        }
        
        IResource[] projects = ResourcesPlugin.getWorkspace().getRoot().members();
        for (IResource resource : projects) {
            IProject targetProject = (IProject)resource;
            if (targetProject.getName().equals(path.segment(0))) {
                return targetProject.getLocation()
                        .append(path.removeFirstSegments(1)).toString();
            }
        }
        return null;
    }
    
    /**
     * Java���\�[�X�̃N���X���i���S���薼�j���擾���܂��B
     * @param env 
     * @param resource ���\�[�X
     * @return �N���X��
     * @throws CoreException 
     */
    public static String getQualifiedClassName(LimyQalabEnvironment env,
            IResource resource) throws CoreException {
        
        // ex. /project/src/pack1/ClassA.java
        String pathStr = resource.getFullPath().toString();
    
        Collection<IPath> paths = env.getSourcePaths(true);
        for (IPath path : paths) {
            
            if (pathStr.equals(path.toString())) {
                return null;
            }
            // ex. "/project/src/pack1/ClassA.java".startsWith("/project/src")
            if (pathStr.startsWith(path.toString())) {
                
                // ex. pack1/ClassA.java
                String relativeName = pathStr.substring(path.toString().length() + 1);
                
                if (relativeName.lastIndexOf('.') <= 0) {
                    return null;
                }
                
                // ex. pack1.ClassA
                return relativeName.substring(0, relativeName.lastIndexOf('.'))
                    .replace('\\', '/').replace('/', '.');
            }
        }
        return null;
    }

    /**
     * ".limy"��Ƀe���|�����t�@�C�����쐬���܂��B
     * @param project �v���W�F�N�g
     * @param relativePath �t�@�C����
     * @return
     */
    public static File createTempFile(IProject project, String relativePath) {
        File baseDir = new File(project.getLocation().toFile(), ".limy");
        File file = new File(baseDir, relativePath);
        file.getParentFile().mkdirs();
        return file;
    }

    /**
     * �v���W�F�N�g�̑S�o�͐�p�X�� results �ɒǉ����܂��B
     * @param project 
     * @param results ���ʊi�[��
     */
    public static void appendProjectBinPaths(IJavaProject project,
            Collection<IPath> results) {
        
        results.add(project.readOutputLocation());
        // �v���W�F�N�g�̃\�[�X�f�B���N�g���ꗗ�����[�v
        for (IClasspathEntry entry : project.readRawClasspath()) {
            IPath location = entry.getOutputLocation();
            if (location != null) {
                // �\�[�X�f�B���N�g�����L�̏o�̓f�B���N�g�����w�肳��Ă���ꍇ�͂�����ʂ�
                results.add(location);
            }
        }
    }

}
