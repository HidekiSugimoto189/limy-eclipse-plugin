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
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.limy.eclipse.common.LimyEclipsePluginUtils;
import org.limy.eclipse.qalab.common.LimyQalabEnvironment;
import org.limy.eclipse.qalab.common.QalabResourceUtils;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

/**
 * Checkstyle�p�̃}�[�J�[�쐬�N���X�ł��B
 * @author Naoki Iwami
 */
public final class CheckstyleMarkCreator implements MarkCreator {
    
    /** �B��̃C���X�^���X */
    private static CheckstyleMarkCreator instance = new CheckstyleMarkCreator();
    
    /** Checkstyle�R���g���[�� */
    private CheckCreator creator = new CheckCreator();

    /**
     * private constructor
     */
    private CheckstyleMarkCreator() {
        // empty
    }
    
    public static CheckstyleMarkCreator getInstance() {
        return instance;
    }
    
    /**
     * Checkstyle�R���g���[���̃L���b�V�����N���A���܂��B
     */
    public void clearCache() {
        creator.clearCache();
    }
    
    // ------------------------ Implement Methods

    public String getName() {
        return "checkstyle";
    }


    public boolean markJavaElement(LimyQalabEnvironment env,
            Collection<IJavaElement> elements, IProgressMonitor monitor) {
        
        return markResources(env, QalabResourceUtils.getResources(elements), monitor);
    }
    
    public boolean markResource(LimyQalabEnvironment env,
            IResource resource, IProgressMonitor monitor) {
        
        return markAll(env, new File[] { resource.getLocation().toFile() },
                monitor);
    }


    public boolean markResourceTemporary(LimyQalabEnvironment env,
            IResource resource, IProgressMonitor monitor) {
        
        return markAll(env, new File[] { resource.getLocation().toFile() },
                monitor);
    }

    public boolean markResources(LimyQalabEnvironment env,
            Collection<IResource> resources, IProgressMonitor monitor) {

        Collection<File> files = new ArrayList<File>();
        for (IResource resource : resources) {
            files.add(resource.getLocation().toFile());
        }
        return markAll(env, files.toArray(new File[files.size()]),
                monitor);
    }

    // ------------------------ Private Methods

    /**
     * �t�@�C���ꗗ���`�F�b�N���ă}�[�J�[���쐬���܂��B
     * @param env �v���W�F�N�g
     * @param files �t�@�C���ꗗ
     * @param monitor �J�ڃ��j�^
     * @return �����ɐ���������true
     */
    private boolean markAll(LimyQalabEnvironment env, File[] files, IProgressMonitor monitor) {
        
        try {
            Checker checker = creator.getChecker(env, monitor);
            checker.process(files);
            return true;
        } catch (CheckstyleException e) {
            LimyEclipsePluginUtils.log(e);
        } catch (CheckstylePluginException e) {
            LimyEclipsePluginUtils.log(e);
        }
        return false;
    }


}
