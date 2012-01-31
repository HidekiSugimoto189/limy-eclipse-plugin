/*
 * Created 2007/01/08
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

import java.util.Collection;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.limy.eclipse.qalab.common.LimyQalabEnvironment;

/**
 * �}�[�J�[�쐬�p�C���^�[�t�F�C�X�ł��B
 * @author Naoki Iwami
 */
public interface MarkCreator {

    /**
     * Creator����Ԃ��܂��B
     * @return Creator��
     */
    String getName();
    
    /**
     * �P�ꃊ�\�[�X���v�����ă}�[�J�[�����܂��B
     * @param env 
     * @param resource ���\�[�X
     * @param monitor �J�ڃ��j�^
     * @return �����ɐ��������� true
     */
    boolean markResource(LimyQalabEnvironment env,
            IResource resource, IProgressMonitor monitor);

    /**
     * �P�ꃊ�\�[�X���v�����Ĉꎞ�}�[�J�[�����܂��B
     * @param env 
     * @param resource ���\�[�X
     * @param monitor �J�ڃ��j�^
     * @return �����ɐ��������� true
     */
    boolean markResourceTemporary(LimyQalabEnvironment env,
            IResource resource, IProgressMonitor monitor);

    /**
     * �������\�[�X���v�����ă}�[�J�[�����܂��B
     * @param env 
     * @param resources ���\�[�X
     * @param monitor �J�ڃ��j�^
     * @return �����ɐ��������� true
     */
    boolean markResources(LimyQalabEnvironment env,
            Collection<IResource> resources, IProgressMonitor monitor);
    
    boolean markJavaElement(LimyQalabEnvironment env,
            Collection<IJavaElement> elements, IProgressMonitor monitor);

}
