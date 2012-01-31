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
package org.limy.eclipse.code;

import java.io.File;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.limy.eclipse.common.LimyEclipsePluginUtils;

/**
 * 
 * @author Naoki Iwami
 */
public class LimyCodePlugin extends AbstractUIPlugin {
    
    // ------------------------ Static Fields

    /**
     * �B���LimyCodePlugin�C���X�^���X
     */
    private static LimyCodePlugin plugin;

    // ------------------------ Fields
    
    /** Plugin���[�g�f�B���N�g�� */
    private File rootDir;

    /**
     * ���\�[�X�o���h��
     */
    private ResourceBundle resourceBundle;
    
    // ------------------------ Constructors
    
    /**
     * The constructor.
     */
    public LimyCodePlugin() {
        super();
        plugin = this;
        try {
            resourceBundle = ResourceBundle.getBundle(
                    "org.limy.eclipse.code.LimyCodePluginResources");
        } catch (MissingResourceException e) {
            LimyEclipsePluginUtils.log(e);
        }
    }

    // ------------------------ Override Methods

    @Override
    public IPreferenceStore getPreferenceStore() {
        IPreferenceStore store = super.getPreferenceStore();
        store.setDefault(LimyCodeConstants.PREF_GETTER_DESC, "���擾���܂��B");
        store.setDefault(LimyCodeConstants.PREF_SETTER_DESC, "��ݒ肵�܂��B");
        return store;
    }
    
    // ------------------------ Public Methods
    
    /**
     * Returns the shared instance.
     * @return �B���LimyCodePlugin�C���X�^���X
     */
    public static LimyCodePlugin getDefault() {
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
        return AbstractUIPlugin.imageDescriptorFromPlugin("org.limy.eclipse.code", path);
    }
    
    /**
     * ���\�[�X�������Ԃ��܂��B
     * @param key ���\�[�X�L�[
     * @return ���\�[�X������
     */
    public static String getResourceString(String key) {
        ResourceBundle bundle = getDefault().getResourceBundle();
        try {
            return (bundle != null) ? bundle.getString(key) : key;
        } catch (MissingResourceException e) {
            return key;
        }
    }
    
    public File getPluginRoot() {
        if (rootDir == null) {
            rootDir = LimyEclipsePluginUtils.getPluginRoot(plugin); // ���̏�����4�b���炢�|����
        }
        return rootDir;
    }

    // ------------------------ Private Methods
    
    /**
     * ���\�[�X�o���h����Ԃ��܂��B
     * @return ���\�[�X�o���h��
     */
    private ResourceBundle getResourceBundle() {
        return resourceBundle;
    }
}
