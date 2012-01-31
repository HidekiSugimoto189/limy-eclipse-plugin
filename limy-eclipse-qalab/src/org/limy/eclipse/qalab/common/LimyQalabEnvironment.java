/*
 * Created 2007/01/31
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * QALab�p�̊��ݒ�����i�[����N���X�ł��B
 * @author Naoki Iwami
 */
public class LimyQalabEnvironment {

    // ------------------------ Fields

    /** �v���W�F�N�g�p�X�g�A */
    private final IPreferenceStore store;
    
    /** �v���W�F�N�g�ꗗ */
    private IProject[] projects;
    
    /** �v���W�F�N�g�p�N���X���[�_ */
    private ClassLoader projectClassLoader;

    /** �\�[�X�p�X�ꗗ�i�Q�ƃv���W�F�N�g���܂ށj */
    private Collection<IPath> sourcePathsWithRef;

    /** �\�[�X�p�X�ꗗ�i�Q�ƃv���W�F�N�g�������j */
    private Collection<IPath> sourcePathsWithoutRef;

    /** ���C��Java�v���W�F�N�g */
    private IJavaProject javaProject;

    /** bin�p�X�ꗗ */
    private Collection<IPath> binPaths;
    
    // ------------------------ Constructors

    /**
     * LimyQalabEnvironment�C���X�^���X���\�z���܂��B
     * @param projects �v���W�F�N�g�ꗗ
     * @param store �v���W�F�N�g�p�X�g�A
     */
    public LimyQalabEnvironment(IProject[] projects, IPreferenceStore store) {
        super();
        this.projects = projects;
        this.store = store;
    }

    // ------------------------ Public Methods

    /**
     * @return
     */
    public IPreferenceStore getStore() {
        return store;
//        return LimyQalabPluginUtils.createQalabStore(getMainProject());
    }
    
    /**
     * ���C���v���W�F�N�g���擾���܂��B
     * @return ���C���v���W�F�N�g
     */
    public IProject getProject() {
        return projects[0];
    }

    /**
     * ���C��Java�v���W�F�N�g���擾���܂��B
     * @return ���C��Java�v���W�F�N�g
     */
    public IJavaProject getJavaProject() {
        if (javaProject == null) {
            javaProject = JavaCore.create(projects[0]);
        }
        return javaProject;
    }

    /**
     * Java�v���W�F�N�g�̃\�[�X�f�B���N�g���ꗗ��Ԃ��܂��B
     * @param enableRef �Q�ƃv���W�F�N�g���ΏۂɊ܂߂邩
     * @return
     * @throws CoreException 
     */
    public Collection<IPath> getSourcePaths(boolean enableRef) throws CoreException {
        if (sourcePathsWithRef == null) {
            sourcePathsWithRef = privateGetSourcePaths(true);
            sourcePathsWithoutRef = privateGetSourcePaths(false);
        }
        return enableRef ? sourcePathsWithRef : sourcePathsWithoutRef;
    }
    
    /**
     * Java�v���W�F�N�g�̃\�[�X�f�B���N�g���ꗗ�i�e�X�g�f�B���N�g���͏����j��Ԃ��܂��B
     * @return Java�v���W�F�N�g�̃\�[�X�f�B���N�g���ꗗ�i�e�X�g�f�B���N�g���͏����j
     * @throws CoreException
     */
    public Collection<IPath> getMainSourcePaths() throws CoreException {

        return getAllSourcePaths(false);
    }

    /**
     * Java�v���W�F�N�g�̃e�X�g�\�[�X�f�B���N�g���ꗗ��Ԃ��܂��B
     * @return Java�v���W�F�N�g�̃\�[�X�f�B���N�g���ꗗ
     * @throws CoreException
     */
    public Collection<IPath> getTestSourcePaths() throws CoreException {
        
        return getAllSourcePaths(true);
    }

    /**
     * bin�f�B���N�g���ꗗ��Ԃ��܂��B
     * @param enableRef �Q�ƃv���W�F�N�g���ΏۂɊ܂߂邩
     * @return bin�f�B���N�g���ꗗ
     * @throws JavaModelException 
     */
    public Collection<IPath> getBinPaths(boolean enableRef) throws JavaModelException {
        
        if (binPaths == null) {
            Collection<IPath> paths = privateGetBinPaths(this, enableRef);
            String enableProject = store.getString(LimyQalabConstants.SUB_PROJECT_NAMES);

            binPaths = new ArrayList<IPath>();
            for (IPath path : paths) {
                String projectName = path.segment(0);
                
                // ���v���W�F�N�g�������͑Ώۂ̃T�u�v���W�F�N�g�̂�target�Ƃ���
                if (projectName.equals(getMainProject().getName())
                        || enableProject.indexOf(projectName + "\n") >= 0) {
                    binPaths.add(path);
                }
            }
        }
        return binPaths;
    }
    
    /**
     * �L���ȎQ�ƃv���W�F�N�g�ꗗ��Ԃ��܂��B
     * @return �L���ȎQ�ƃv���W�F�N�g�ꗗ
     * @throws CoreException 
     */
    public Collection<IProject> getEnableReferencedProjects() throws CoreException {
        Collection<IProject> results = new ArrayList<IProject>();
        
        String enableProject = store.getString(LimyQalabConstants.SUB_PROJECT_NAMES);

        for (IProject refProject : getMainProject().getReferencedProjects()) {
            if (enableProject.indexOf(refProject.getName() + "\n") >= 0) {
                results.add(refProject);
            }
        }
        return results;
    }

    // ------------------------ Getter/Setter Methods

    /**
     * �v���W�F�N�g�p�N���X���[�_���擾���܂��B
     * @return �v���W�F�N�g�p�N���X���[�_
     */
    public ClassLoader getProjectClassLoader() {
        return projectClassLoader;
    }

    /**
     * �v���W�F�N�g�p�N���X���[�_��ݒ肵�܂��B
     * @param projectClassLoader �v���W�F�N�g�p�N���X���[�_
     */
    public void setProjectClassLoader(ClassLoader projectClassLoader) {
        this.projectClassLoader = projectClassLoader;
    }
    
    public File getTempFile(String relativePath) {
        return LimyQalabUtils.createTempFile(getMainProject(), relativePath);
    }

    // ------------------------ Private Methods

    /**
     * ���C���v���W�F�N�g��Ԃ��܂��B
     * @return ���C���v���W�F�N�g
     */
    private IProject getMainProject() {
        return projects[0];
    }
   
    /**
     * �\�[�X�f�B���N�g���ꗗ���擾���܂��B
     * @param enableRef �Q�ƃv���W�F�N�g���ΏۂɊ܂߂邩
     * @return �\�[�X�f�B���N�g���ꗗ
     * @throws JavaModelException Java���f����O
     */
    private Collection<IPath> privateGetSourcePaths(boolean enableRef)
            throws JavaModelException {
        
        Collection<IPath> results = new ArrayList<IPath>();
        IJavaProject project = getJavaProject();
        
        for (IJavaElement element : getAllJavaElements(project)) {
            if (enableRef
                    && getStore().getBoolean(LimyQalabConstants.ENABLE_REFPROJECT)) {
                results.add(element.getPath());
            } else {
                // ���v���W�F�N�g�̂ݗL���ȏꍇ
                if (element.getJavaProject().equals(project)) {
                    results.add(element.getPath());
                }
            }
        }
        return results;
    }

    /**
     * Java�v���W�F�N�g�̑S�\�[�X�p�X�i�Q�ƃv���W�F�N�g�܁j��Ԃ��܂��B
     * @param project Java�v���W�F�N�g
     * @return �S�\�[�X�p�X
     * @throws JavaModelException Java���f����O
     */
    private Collection<IJavaElement> getAllJavaElements(IJavaProject project)
            throws JavaModelException {
        
        Collection<IJavaElement> results = new ArrayList<IJavaElement>();
        
        IPackageFragmentRoot[] roots = project.getAllPackageFragmentRoots();
        
        // �v���W�F�N�g�̑S�\�[�X�p�X����юQ�ƃv���W�F�N�g�����[�v
        for (IPackageFragmentRoot root : roots) {
            if (root.getKind() == IPackageFragmentRoot.K_SOURCE) {
                results.add(root);
            }
        }
        return results;
    }

    /**
     * �p�X���e�X�g�p�f�B���N�g���Ȃ̂��ǂ����𔻒肵�܂��B
     * @param location �p�X
     * @return �e�X�g�p�f�B���N�g���Ȃ�ΐ^
     */
    private boolean isTestPath(IPath location) {
        for (String segment : location.removeFirstSegments(1).segments()) {
            if ("test".equals(segment)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Java�v���W�F�N�g�̏o�͐�f�B���N�g���ꗗ��Ԃ��܂��B
     * @param env 
     * @param enableRef �Q�ƃv���W�F�N�g���ΏۂɊ܂߂邩
     * @return Java�v���W�F�N�g�̃\�[�X�f�B���N�g���ꗗ
     * @throws JavaModelException Java���f����O
     */
    private Collection<IPath> privateGetBinPaths(LimyQalabEnvironment env,
            boolean enableRef) throws JavaModelException {
        
        IJavaProject project = env.getJavaProject();

        Set<IPath> results = new HashSet<IPath>();
        
        if (enableRef
                && store.getBoolean(LimyQalabConstants.ENABLE_REFPROJECT)) {
            
            // �Q�ƃv���W�F�N�g���L���ȏꍇ
            IPackageFragmentRoot[] roots = project.getAllPackageFragmentRoots();
            for (IPackageFragmentRoot refRoot : roots) {
                if (refRoot.getKind() == IPackageFragmentRoot.K_SOURCE) {
                    // �Q�ƃv���W�F�N�g���Ƀ��[�v
                    IJavaProject refProject = refRoot.getJavaProject();
                    LimyQalabUtils.appendProjectBinPaths(refProject, results);
                }
            }
        } else {
            // �f�t�H���g�̃v���W�F�N�g�o�̓f�B���N�g����ǉ�
            LimyQalabUtils.appendProjectBinPaths(project, results);
        }
        
        return results;
    }

    /**
     * Java�v���W�F�N�g�̃\�[�X�f�B���N�g���ꗗ��Ԃ��܂��B
     * @param enableTestPath true�ɂ���ƃe�X�g�p�X�݂̂��擾�Afalse�ɂ���ƃ��C���p�X�݂̂��擾
     * @return Java�v���W�F�N�g�̃\�[�X�f�B���N�g���ꗗ
     * @throws CoreException
     */
    private Collection<IPath> getAllSourcePaths(boolean enableTestPath)
            throws CoreException {
        
        String enableProject = store.getString(LimyQalabConstants.SUB_PROJECT_NAMES);

        Collection<IPath> results = new ArrayList<IPath>();
        Collection<IPath> paths = getSourcePaths(true);
        for (IPath path : paths) {
            if (isTestPath(path) == enableTestPath) {
                String projectName = path.segment(0);
                // ���v���W�F�N�g�������͑Ώۂ̃T�u�v���W�F�N�g�̂�copy����
                if (projectName.equals(getMainProject().getName())
                        || enableProject.indexOf(projectName + "\n") >= 0) {

                    results.add(path);
                }
            }
        }
        return results;
    }

}
