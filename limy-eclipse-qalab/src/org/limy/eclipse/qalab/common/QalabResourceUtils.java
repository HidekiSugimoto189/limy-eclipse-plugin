/*
 * Created 2007/01/06
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

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.limy.eclipse.common.LimyEclipsePluginUtils;
import org.limy.eclipse.common.io.LimyIOUtils;
import org.limy.eclipse.common.resource.LimyResourceUtils;
import org.limy.eclipse.common.resource.ResourceWithBasedir;
import org.limy.qalab.AutoCreated;

/**
 * ���\�[�X�֘A�̃��[�e�B���e�B�N���X�ł��B
 * @author Naoki Iwami
 */
public final class QalabResourceUtils {

    /**
     * private constructor
     */
    private QalabResourceUtils() { }
    
    /**
     * Java�t�@�C���ɑΉ�����class�t�@�C����Ԃ��܂��B
     * @param env 
     * @param javaResource Java�t�@�C��
     * @return �Ή�����class�t�@�C��
     * @throws CoreException 
     */
    public static ResourceWithBasedir getClassResource(LimyQalabEnvironment env,
            IResource javaResource)
            throws CoreException {
        
        // Java�t�@�C���̃p�X�i/project/src/... �`���j
        String pathStr = javaResource.getFullPath().toString();
    
        Collection<IPath> sourcePaths = env.getSourcePaths(false);
        Collection<IPath> binPaths = env.getBinPaths(false);
        
        // �S�\�[�X�p�X�����[�v
        for (IPath path : sourcePaths) {
            if (pathStr.startsWith(path.toString())) {
                // Java�t�@�C�������̃\�[�X�p�X�z���ɑ��݂���ꍇ
                
                // ex. pack1/ClassA.java
                String relativeName = pathStr.substring(path.toString().length());
                
                // ex. pack1/ClassA.class
                Path classfilePath = new Path(
                        relativeName.substring(0, relativeName.lastIndexOf('.'))
                        + ".class");
    
                // �Sbin�p�X�����[�v���đΉ�����class�t�@�C��������
                for (IPath binPath : binPaths) {
                    IResource searchResource = LimyResourceUtils.newFile(
                            binPath.append(classfilePath));
                    if (searchResource.exists()) {
                        return new ResourceWithBasedir(binPath, searchResource);
                    }
                }
            }
        }
        
        return null; // Java Initializer ���������Ă��Ȃ��ꍇ�A�����ɗ���
    }

    /**
     * Java�t�@�C���̊�o�X�t���\�[�X��Ԃ��܂��B
     * @param env 
     * @param javaResource Java�t�@�C��
     * @return ��o�X�t���\�[�X
     * @throws CoreException 
     */
    public static ResourceWithBasedir getResourceWithBasedir(LimyQalabEnvironment env,
            IResource javaResource)
            throws CoreException {
        
        // Java�t�@�C���̃p�X�i/project/src/... �`���j
        String pathStr = javaResource.getFullPath().toString();
        
        Collection<IPath> sourcePaths = env.getSourcePaths(false);
        
        // �S�\�[�X�p�X�����[�v
        for (IPath path : sourcePaths) {
            if (pathStr.startsWith(path.toString())) {
                // Java�t�@�C�������̃\�[�X�p�X�z���ɑ��݂���ꍇ
                return new ResourceWithBasedir(path, javaResource);
            }
        }
        
        LimyEclipsePluginUtils.log("Resource " + javaResource + " is not found.");
        return null;
    }

    /**
     * @param resource
     * @return
     */
    public static boolean isTestResource(IResource resource) {
        String name = resource.getName();
        if (name.endsWith("Test.java")) {
            return true;
        }
        return false;
    }

    /**
     * ���\�[�X��QA�ΏۊO���ǂ����𔻒肵�܂��B
     * @param env QALab���ݒ���e
     * @param resource ���\�[�X
     * @return ���\�[�X��QA�ΏۊO�Ȃ�� true
     */
    public static boolean isIgnoreResource(LimyQalabEnvironment env, IResource resource) {
        if (LimyQalabUtils.isIgnoreSource(env, resource.getFullPath())) {
            return true;
        }
        
        // �e�X�g�N���X�́AQA�ΏۊO�Ƃ���i����ύX�̗]�n����j
        if (isTestResource(resource)) {
            return true;
        }
        
        return false;
    }

    /**
     * �N���X������Java���\�[�X���������ĕԂ��܂��B
     * @param env 
     * @param project �v���W�F�N�g
     * @param className �N���X���i���S���薼�j
     * @param enableRefProject �Q�ƃv���W�F�N�g�������ΏۂɊ܂߂邩�ǂ���
     * @return Java���\�[�X�i������Ȃ�������null�j
     * @throws CoreException 
     */
    public static IResource getJavaResource(LimyQalabEnvironment env,
            String className,
            boolean enableRefProject)
            throws CoreException {
        
        Collection<IPath> sourcePaths;
        sourcePaths = env.getSourcePaths(enableRefProject);
    
        // �N���X������̏ꍇ�A�ǂ��̃\�[�X���͂킩��Ȃ��̂Ń\�[�X�p�X�̐擪��Ԃ�
        if (className.length() == 0) {
            if (sourcePaths.isEmpty()) {
                // �\�[�X�p�X��������݂��Ȃ��ꍇ�A�d�������̂Ńv���W�F�N�g��Ԃ�
                return env.getProject();
            }
            IPath firstPath = sourcePaths.iterator().next();
            // �擪�̃\�[�X�p�X��Ԃ�
            return LimyResourceUtils.newFolder(firstPath);
        }
        
        // �N���X�����w�肳��Ă���ꍇ�A�S�\�[�X�p�X���猟��
        String javaFileName = className.replace('.', '/') + ".java";
        if (className.indexOf('$') >= 0) {
            javaFileName = className.substring(0, className.indexOf('$'))
                    .replace('.', '/') + ".java";
        }
            
        for (IPath path : sourcePaths) {
            IResource resource = LimyResourceUtils.newFile(
                    path.append(javaFileName));
            if (resource.exists()) {
                return resource;
            }
        }
        return null;
    }

    /**
     * ����\�[�X�ȉ��̑SJava�t�@�C���� results �ɒǉ����܂��i�ċA�j�B
     * @param results Java���\�[�X�i�[��
     * @param resource ����\�[�X�i�t�H���_�܂��̓t�@�C���j
     * @throws CoreException 
     */
    public static void appendJavaResources(
            Collection<IResource> results, IResource resource)
            throws CoreException {
        
        if (resource.getType() == IResource.FOLDER) {
            IResource[] children = ((IContainer)resource).members();
            for (IResource child : children) {
                appendJavaResources(results, child);
            }
        }
        if (resource.getType() == IResource.FILE) {
            String fileName = resource.getName().replace('\\', '/');
            if (fileName.endsWith(".java")) {
                results.add(resource);
            }
        }
    }

    /**
     * �v���W�F�N�g�̑S�\�[�X�t�@�C���ꗗ��Ԃ��܂��B
     * @param env 
     * @param enableRef �Q�ƃv���W�F�N�g���L���ɂ��邩
     * @param ignoreSource false�ɂ���ƁAQA�ΏۊO�f�B���N�g�����ꗗ�Ƃ��ĕԂ�
     * @return �v���W�F�N�g�̑S�\�[�X�t�@�C���ꗗ
     * @throws CoreException �R�A��O
     */
    public static List<IResource> getAllSourceFiles(
            LimyQalabEnvironment env, boolean enableRef, boolean ignoreSource)
            
            throws CoreException {
        
        Collection<IPath> sourcePaths = env.getSourcePaths(enableRef);
        List<IResource> results = new ArrayList<IResource>();
        for (IPath path : sourcePaths) {
            IResource resource = LimyResourceUtils.newFolder(path);
            appendJavaResources(results, resource);
        }
        
        if (!ignoreSource) {
            return results;
        }
        
        for (ListIterator<IResource> it = results.listIterator(); it.hasNext();) {
            IResource resource = it.next();
            if (LimyQalabUtils.isIgnoreSource(env, resource.getFullPath())) {
                it.remove();
            }
        }
        return results;
    }

    /**
     * Java���C���N���X�ɑΉ�����e�X�g�P�[�X�����擾���܂��B
     * @param env 
     * @param javaResource Java���\�[�X�i���C���N���X�j
     * @return �e�X�g�P�[�X��
     * @throws CoreException 
     */
    public static String getQualifiedTestClassName(LimyQalabEnvironment env,
            IResource javaResource)
            throws CoreException {
        return LimyQalabUtils.getQualifiedClassName(env, javaResource) + "Test";
    }

    /**
     * Java�e�X�g�N���X�ɑΉ����郁�C���N���X�����擾���܂��B
     * @param env 
     * @param javaTestResource Java���\�[�X�i�e�X�g�N���X�j
     * @return ���C���N���X��
     * @throws CoreException 
     */
    public static String getQualifiedMainClassName(LimyQalabEnvironment env,
            IResource javaTestResource) throws CoreException {
        
        String testName = LimyQalabUtils.getQualifiedClassName(env, javaTestResource);
        if (testName.endsWith("Test")) {
            return testName.substring(0, testName.length() - 4);
        }
        return testName;
    }

    /**
     * Java�t�@�C�����p�[�X����Document�𐶐����܂��B
     * @param resource 
     * @return 
     * @throws CoreException 
     * @throws IOException 
     */
    public static IDocument parseDocument(IResource resource) throws IOException, CoreException {
        
        IProject project = resource.getProject();
        String source = LimyIOUtils.getContent(resource.getLocation().toFile(),
                project.getDefaultCharset());
        return new Document(source);
    }

    /**
     * AutoCreated���߂������N���X�ꗗ���擾���܂��B
     * @param env QALab���ݒ���e
     * @param project Java�v���W�F�N�g
     * @return AutoCreated���߂������N���X�ꗗ���擾
     * @throws CoreException �R�A��O
     * @throws IOException I/O��O
     */
    public static Collection<IFile> getAutoCreatedFiles(
            LimyQalabEnvironment env)
            throws CoreException, IOException {
        
        Collection<IFile> results = new ArrayList<IFile>();
        
        List<IResource> souceFiles = QalabResourceUtils.getAllSourceFiles(
                env, true, true);
        for (IResource file : souceFiles) {
            if (isAutoCreated(env, file)) {
                results.add((IFile)file);
            }
        }
        
        return results;
    }
    
    /**
     * Java���\�[�X�������������ꂽ���̂��ǂ����𔻒肵�܂��B
     * @param env QALab���ݒ���e
     * @param file Java���\�[�X
     * @return Java���\�[�X��������������Ă����� true
     * @throws CoreException 
     */
    public static boolean isAutoCreated(LimyQalabEnvironment env, IResource file)
            throws CoreException {
        
        String qualifiedClassName = LimyQalabUtils.getQualifiedClassName(env, file);
        try {
            Annotation[] annotations = env.getProjectClassLoader()
                    .loadClass(qualifiedClassName).getAnnotations();
            for (Annotation annotation : annotations) {
                if (AutoCreated.class.getName().equals(annotation.annotationType().getName())) {
                    return true;
                }
            }
        } catch (ClassNotFoundException e) {
            // do nothing
        } catch (UnsupportedClassVersionError e) {
            LimyEclipsePluginUtils.log("UnsupportedClassVersionError : " + qualifiedClassName);
        } catch (NoClassDefFoundError e) {
            LimyEclipsePluginUtils.log("NoClassDefFoundError : " + qualifiedClassName);
            throw e;
        }
        return false;
    }

    public static Collection<IResource> getResources(Collection<IJavaElement> elements) {
        Collection<IResource> results = new ArrayList<IResource>();
        for (IJavaElement el : elements) {
            results.add(el.getResource());
        }
        return results;
    }


}
