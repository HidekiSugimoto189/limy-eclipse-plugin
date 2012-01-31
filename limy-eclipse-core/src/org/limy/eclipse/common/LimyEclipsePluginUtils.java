/*
 * Created 2007/08/14
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
package org.limy.eclipse.common;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.limy.eclipse.common.io.LimyIOUtils;

/**
 * Limy Eclipse Plugin���ʂ̃��[�e�B���e�B�N���X�ł��B
 * @author Naoki Iwami
 * @depend - - - LimyEclipseConstants
 */
public final class LimyEclipsePluginUtils {
    
    /**
     * private constructor
     */
    private LimyEclipsePluginUtils() { }
    
    /**
     * �v���O�C���̃��[�g�f�B���N�g�����擾���܂��B
     * @param plugin �v���O�C��
     * @return �v���O�C���̃��[�g�f�B���N�g��
     */
    public static File getPluginRoot(Plugin plugin) {
        try {
            // 3.2
            URL url = FileLocator.toFileURL(plugin.getBundle().getEntry("/"));
            // 3.1
            // URL url = Platform.asLocalURL(plugin.getBundle().getEntry("/"));
            return new File(url.getFile());
        } catch (IOException e) {
            log(e);
        }
        return null;
    }

    /**
     * �G���[���O���o�͂��܂��B
     * @param e ��O
     */
    public static void log(Throwable e) {
        log(LimyEclipseConstants.PLUGIN_ID, LimyEclipseConstants.INTERNAL_ERROR, e);
    }

    /**
     * �G���[���b�Z�[�W���O���o�͂��܂��B
     * @param message ���O���b�Z�[�W
     */
    public static void log(String message) {
        log(LimyEclipseConstants.PLUGIN_ID, LimyEclipseConstants.INTERNAL_ERROR, message);
    }

    /**
     * Preference�̒l�iBoolean�^�j���擾���܂��B
     * @param store 
     * @param key Preference���ڂ̃L�[������
     * @param defaultValue �f�t�H���g�l
     * @return Preference�l
     */
    public static boolean getPreferenceBoolean(IPreferenceStore store,
            String key, boolean defaultValue) {
        String value = store.getString(key);
        if (value.length() == 0) {
            return defaultValue;
        }
        return Boolean.getBoolean(value);
    }

    /**
     * �t�@�C����ǂݍ���ŕ�������擾���܂��B
     * @param plugin 
     * @param contentFilePath
     * @return �t�@�C���̓��e������
     */
    public static String loadContent(Plugin plugin, String contentFilePath) {
        try {
            // 3.2
            URL url = FileLocator.toFileURL(plugin.getBundle().getEntry("/"));
            return LimyIOUtils.getContent(new URL(url, contentFilePath).openStream());
        } catch (IOException e) {
            log(e);
            return null;
        }
    }
    // ------------------------ Private Methods

    /**
     * ���O���o�͂��܂��B
     * @param pluginId �v���O�C��ID
     * @param statusCode �X�e�[�^�X�R�[�h
     * @param e �o�͂����O
     */
    private static void log(String pluginId, int statusCode, Throwable e) {
    
        ILog log = Platform.getLog(Platform.getBundle(pluginId));
        Status status = new Status(IStatus.ERROR, pluginId, statusCode, e.getMessage(), e);
        log.log(status);
    }

    /**
     * ���O���o�͂��܂��B
     * @param pluginId �v���O�C��ID
     * @param statusCode �X�e�[�^�X�R�[�h
     * @param message �o�͂��郁�b�Z�[�W
     */
    private static void log(String pluginId, int statusCode, String message) {
    
        ILog log = Platform.getLog(Platform.getBundle(pluginId));
        Status status = new Status(IStatus.ERROR, pluginId, statusCode, message, null);
        log.log(status);
    }

}
