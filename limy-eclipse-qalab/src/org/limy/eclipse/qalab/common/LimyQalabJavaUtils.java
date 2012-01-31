/*
 * Created 2007/08/29
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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;

/**
 * Java�֘A�̃��[�e�B���e�B�N���X�ł��B
 * @author Naoki Iwami
 */
public final class LimyQalabJavaUtils {
    
    /**
     * private constructor
     */
    private LimyQalabJavaUtils() { }

    /**
     * Java�v���W�F�N�g�̑S���C�u�����iJava bin�p�X����юQ�ƃv���W�F�N�g��Export���C�u�������܂ށj���擾���܂��B
     * @param project Java�v���W�F�N�g
     * @return ���C�u�����̐�΃p�X
     * @throws CoreException 
     */
    public static Collection<String> getJavaLibraries(IJavaProject project)
            throws CoreException {
        
        Collection<String> results = new HashSet<String>();
        
        for (IPackageFragmentRoot fragment : project.getAllPackageFragmentRoots()) {
            
            String location = getExternalLocation(fragment);
            if (location != null) {
                results.add(location);
            }
        }
        
        // TODO ������ Eclipse Plugin ���܂Ƃ߂�悤�ȃv���W�F�N�g�̏ꍇ�A
        // �e�v���W�F�N�g��Eclipse�֘Ajar���擾�ł��Ȃ�
        // �����export�Ɋ܂߂Ă��Ȃ��ׂ����A�܂߂�킯�ɂ͂����Ȃ��iAccess�G���[�ɂȂ�j�̂�
        // ����p�̃t���O��p�ӂ��ĕʓr�擾����K�v����
        
        for (IProject refProject : project.getProject().getReferencedProjects()) {
            
            boolean isPlugin = Arrays.asList(refProject.getDescription().getNatureIds())
                    .indexOf("org.eclipse.pde.PluginNature") >= 0;
            if (isPlugin) {
                IPackageFragmentRoot[] roots = JavaCore.create(refProject)
                        .getAllPackageFragmentRoots();
                for (IPackageFragmentRoot fragment : roots) {
                    String location = getExternalLocation(fragment);
                    if (location != null) {
                        results.add(location);
                    }
                }
            }
                    
        }
        
        return results;
    }

    /**
     * PackageFragmentRoot�̐�΃p�X��Ԃ��܂��B
     * @param fragment PackageFragmentRoot
     * @return
     * @throws CoreException 
     */
    public static String getExternalLocation(IPackageFragmentRoot fragment) throws CoreException {
        
        String location = null;
        if (fragment.getKind() == IPackageFragmentRoot.K_SOURCE) {
            // ���v���W�F�N�g����юQ�ƃv���W�F�N�g�̃\�[�X�f�B���N�g��
            IPath outputLocation = fragment.getRawClasspathEntry().getOutputLocation();
            if (outputLocation != null) {
                // �\�[�X�p�X�ŗL�̏o�͐悪�w�肳��Ă���ꍇ
                location = LimyQalabUtils.createFullPath(
                        fragment.getJavaProject(), outputLocation);
            } else {
                // �\�[�X�p�X�ŗL�̏o�͐悪�w�肳��Ă��Ȃ��ꍇ�A�v���W�F�N�g�̃f�t�H���g�o�͐���g�p
                location = LimyQalabUtils.createFullPath(fragment.getJavaProject(),
                        fragment.getJavaProject().getOutputLocation());
            }
        } else {
            // ���v���W�F�N�g��classpath�ꗗ����юQ�ƃv���W�F�N�g��Export���C�u����
            IResource resource = fragment.getResource();
            if (resource != null) {
                location = resource.getLocation().toString();
            } else {
                // Variable�w���jar�t�@�C����resource = null �ƂȂ�
                IPath path = fragment.getRawClasspathEntry().getPath();
                if (!path.toString().startsWith("org.eclipse.jdt.launching.JRE_CONTAINER")) {
                    // JRE�ȊO��JAR�t�@�C���iVariable�w��j���N���X�p�X�ɒǉ�
                    location = fragment.getPath().toString();
                }
            }
        }
        return location;
    }
    
    public static IPackageFragmentRoot getPackageFragmentRoot(IJavaElement el) {
//        if (el.getElementType() == IJavaElement.COMPILATION_UNIT) {
//            return (PackageFragmentRoot)((IPackageFragment)el.getParent()).getParent();
//        }
//        if (el.getElementType() == IJavaElement.PACKAGE_FRAGMENT) {
//            return (PackageFragmentRoot)el.getParent();
//        }
//        if (el.getElementType() == IJavaElement.PACKAGE_FRAGMENT_ROOT) {
//            return (PackageFragmentRoot)el;
//        }
        return el.getJavaProject().getPackageFragmentRoot(el.getResource());
    }

    /**
     * Java�v�f�ɑΉ�����bin�f�B���N�g����Ԃ��܂��B
     * <p>
     * �p�b�P�[�W�v�f : �Ή�����bin�f�B���N�g�����̃T�u�f�B���N�g��<br>
     * �\�[�X�f�B���N�g�� : �Ή�����bin�f�B���N�g��<br>
     * </p>
     * @param el Java�v�f
     * @return bin�f�B���N�g���i��΃p�X�j
     * @throws CoreException 
     */
    public static String getBinDirPath(IJavaElement el) throws CoreException {

        if (el.getElementType() == IJavaElement.PACKAGE_FRAGMENT) {
            IPackageFragmentRoot root = (IPackageFragmentRoot)el.getParent();
            String path = LimyQalabJavaUtils.getExternalLocation(root);
            return new File(path, el.getElementName().replace('.', '/')).getAbsolutePath();
        }
        if (el.getElementType() == IJavaElement.PACKAGE_FRAGMENT_ROOT) {
            return LimyQalabJavaUtils.getExternalLocation((IPackageFragmentRoot)el);
        }
        return null;
    }


}
