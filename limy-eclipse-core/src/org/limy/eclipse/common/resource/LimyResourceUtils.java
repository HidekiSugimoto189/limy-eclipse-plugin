/*
 * Created 2007/08/21
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
package org.limy.eclipse.common.resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

/**
 * @author Naoki Iwami
 */
public final class LimyResourceUtils {
    
    /**
     * private constructor
     */
    private LimyResourceUtils() { }
    
    /**
     * File�C���X�^���X���쐬���܂��B
     * @param path �t�@�C���p�X
     * @return File�C���X�^���X
     */
    public static IFile newFile(IPath path) {
        return ResourcesPlugin.getWorkspace().getRoot().getFile(path);
    }

    /**
     * Folder�C���X�^���X���쐬���܂��B
     * @param path �t�@�C���p�X
     * @return Folder�C���X�^���X
     */
    public static IFolder newFolder(IPath path) {
        return ResourcesPlugin.getWorkspace().getRoot().getFolder(path);
    }

    /**
     * �t�@�C���� contents �̓��e�ŐV�K�쐬���܂��B
     * <p>
     * ���łɃt�@�C�������݂����ꍇ�A�������œ��e���㏑�����܂��B
     * </p>
     * @param path �t�@�C���p�X�i�����p�X�`���j
     * @param contents �t�@�C�����e
     * @param charset �������ݎ��̕����Z�b�g
     * @throws CoreException 
     * @throws IOException 
     */
    public static void createFile(IPath path, String contents, String charset)
            throws CoreException, IOException {
        
        IFile file = newFile(path);
        InputStream in = new ByteArrayInputStream(contents.getBytes(charset));
        if (file.exists()) {
            file.delete(true, null);
        }
        file.create(in, true, null);
        in.close();

    }

}
