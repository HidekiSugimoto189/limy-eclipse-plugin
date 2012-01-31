/*
 * Created 2005/09/17
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
package org.limy.eclipse.core;

import java.io.File;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.limy.eclipse.common.LimyEclipsePluginUtils;
import org.limy.eclipse.common.LimyStoreColorProvider;
import org.limy.eclipse.common.LimyEclipseConstants;

/**
 * @author Naoki Iwami
 * @depend - - - LimyEclipseConstants
 */
public class LimyEclipsePlugin extends AbstractUIPlugin {

    /**
     * �B���LimyEclipsePlugin�C���X�^���X
     */
    private static LimyEclipsePlugin plugin;

    // ------------------------ Fields

    /** Plugin���[�g�f�B���N�g�� */
    private File rootDir;
    
    /**
     * ���\�[�X�o���h��
     */
    private ResourceBundle resourceBundle;
    
    /** �J���[�v���o�C�_ */
    private LimyStoreColorProvider colorProvider;
    
    // ------------------------ Constructors
    
    /**
     * The constructor.
     */
    public LimyEclipsePlugin() {
        super();
        plugin = this;
        try {
            resourceBundle = ResourceBundle.getBundle(
                    "org.limy.eclipse.core.LimyEclipsePluginResources");
        } catch (MissingResourceException x) {
            log(x);
//            resourceBundle = null;
        }
    }
    
    // ------------------------ Public Methods

    /**
     * Returns the shared instance.
     * @return �B���LimyEclipsePlugin�C���X�^���X
     */
    public static LimyEclipsePlugin getDefault() {
        return plugin;
    }

    /**
     * Returns an image descriptor for the image file at the given
     * plug-in relative path.
     *
     * @param path the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path) {
        return AbstractUIPlugin.imageDescriptorFromPlugin("org.limy.eclipse.core", path);
    }
    
    // ------------------------ Public Methods
    
    /**
     * ���\�[�X�������Ԃ��܂��B
     * @param key ���\�[�X�L�[
     * @return ���\�[�X������
     */
    public static String getResourceString(String key) {
        ResourceBundle bundle = LimyEclipsePlugin.getDefault().getResourceBundle();
        try {
            return (bundle != null) ? bundle.getString(key) : key;
        } catch (MissingResourceException e) {
            return key;
        }
    }
    

    /**
     * ���[�N�X�y�[�X���擾���܂��B
     * @return ���[�N�X�y�[�X
     */
    public static IWorkspace getWorkspace() {
        return ResourcesPlugin.getWorkspace();
    }
    
    /**
     * ���O���o�͂��܂��B
     * @param status �o�͂���X�e�[�^�X
     */
    private static void log(IStatus status) {
        getDefault().getLog().log(status);
    }

    /**
     * ���O���o�͂��܂��B
     * @param e �o�͂����O
     */
    private static void log(Throwable e) {
        log(new Status(IStatus.ERROR, getUniqueIdentifier(),
                LimyEclipseConstants.INTERNAL_ERROR, "error by limy-eclipse", e));
    }
    
    /**
     * �J���[�v���o�C�_���擾���܂��B
     * @return �J���[�v���o�C�_
     */
    public LimyStoreColorProvider getColorProvider() {
        if (colorProvider == null) {
            colorProvider = new LimyStoreColorProvider(getDefault().getPreferenceStore());
        }
        return colorProvider;
    }

    /**
     * �v���o�C�_�Q���X�V�i�������j���܂��B
     */
    public void updateProviders() {
        colorProvider = new LimyStoreColorProvider(getDefault().getPreferenceStore());
    }

    /**
     * �v���O�C���̃��[�g�f�B���N�g�����擾���܂��B
     * @return �v���O�C���̃��[�g�f�B���N�g��
     */
    public File getPluginRoot() {
        if (rootDir == null) {
            rootDir = LimyEclipsePluginUtils.getPluginRoot(plugin);
        }
        return rootDir;
    }

    // ------------------------ Private Methods

    /**
     * ���ʎq���擾���܂��B
     * @return �v���O�C�����ʎq
     */
    private static String getUniqueIdentifier() {
        return getDefault().getBundle().getSymbolicName();
    }

    /**
     * ���\�[�X�o���h����Ԃ��܂��B
     * @return ���\�[�X�o���h��
     */
    private ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

}
