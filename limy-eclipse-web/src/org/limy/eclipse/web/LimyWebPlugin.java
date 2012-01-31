/*
 * Created 2007/08/30
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
package org.limy.eclipse.web;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.limy.eclipse.common.LimyEclipsePluginUtils;

/**
 * @author Naoki Iwami
 */
public class LimyWebPlugin extends AbstractUIPlugin {
    /**
     * �B���LimyWebPlugin�C���X�^���X
     */
    private static LimyWebPlugin plugin;

    // ------------------------ Fields
    
    /**
     * ���\�[�X�o���h��
     */
    private ResourceBundle resourceBundle;
    
    // ------------------------ Constructors
    
    /**
     * The constructor.
     */
    public LimyWebPlugin() {
        super();
        plugin = this;
        try {
            resourceBundle = ResourceBundle.getBundle(
                    "org.limy.eclipse.web.LimyWebPluginResources");
        } catch (MissingResourceException e) {
            LimyEclipsePluginUtils.log(e);
        }
    }
    
    /**
     * Returns the shared instance.
     * @return �B���LimyWebPlugin�C���X�^���X
     */
    public static LimyWebPlugin getDefault() {
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
        return AbstractUIPlugin.imageDescriptorFromPlugin("org.limy.eclipse.web", path);
    }
    
    // ------------------------ Public Methods
    
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
    
    /**
     * ���\�[�X�o���h����Ԃ��܂��B
     * @return ���\�[�X�o���h��
     */
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

}
