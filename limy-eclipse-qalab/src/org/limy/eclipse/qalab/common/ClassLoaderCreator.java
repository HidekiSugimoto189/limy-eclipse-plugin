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
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

/**
 * �N���X���[�_�쐬��S�����܂��B
 * @author Naoki Iwami
 */
public final class ClassLoaderCreator {
    
    /**
     * private constructor
     */
    private ClassLoaderCreator() { }
    
    /**
     * Java�v���W�F�N�g�̑S���C�u�������i�[�����N���X���[�_���쐬���ĕԂ��܂��B
     * @param env 
     * @param parentLoader �e�N���X���[�_
     * @return Java�v���W�F�N�g�p�N���X���[�_
     * @throws CoreException �R�A��O
     * @throws IOException I/O��O
     */
    public static ClassLoader createProjectClassLoader(LimyQalabEnvironment env,
            final ClassLoader parentLoader)
            throws CoreException, IOException {
        
        IJavaProject project = env.getJavaProject();
        
        Collection<URI> uris = new ArrayList<URI>();
        for (String libraryPath : LimyQalabJavaUtils.getJavaLibraries(project)) {
            uris.add(new File(libraryPath).toURI());
        }
        // �Q�ƃv���W�F�N�g��Java���C�u�����iExport����Ă��Ȃ����́j���ǉ�
        for (IProject refProject : project.getProject().getReferencedProjects()) {
            IJavaProject refJavaProject = JavaCore.create(refProject);
            for (String libraryPath : LimyQalabJavaUtils.getJavaLibraries(refJavaProject)) {
                uris.add(new File(libraryPath).toURI());
            }
        }
    
        // �v���W�F�N�g�p�̃N���X���[�_���쐬
        final URL[] urls = new URL[uris.size()];
        Iterator<URI> it = uris.iterator();
        for (int i = 0; i < uris.size(); i++) {
            urls[i] = it.next().toURL();
        }
        
        return AccessController.doPrivileged(
                new PrivilegedAction<URLClassLoader>() {
                    public URLClassLoader run() {
                        return new URLClassLoader(urls, parentLoader);
                    }
                });
    }


}
