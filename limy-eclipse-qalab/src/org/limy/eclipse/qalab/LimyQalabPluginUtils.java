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
package org.limy.eclipse.qalab;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.limy.eclipse.core.LimyEclipsePlugin;
import org.limy.eclipse.qalab.common.ClassLoaderCreator;
import org.limy.eclipse.qalab.common.LimyQalabConstants;
import org.limy.eclipse.qalab.common.LimyQalabEnvironment;

/**
 * @author Naoki Iwami
 */
public final class LimyQalabPluginUtils {
    
    /**
     * private constructor
     */
    private LimyQalabPluginUtils() { }

    /**
     * �v���O�C�����t�@�C���̐�΃p�X��Ԃ��܂��B
     * @param relativePath �v���O�C�����[�g����̑��΃p�X
     * @return ���\�[�X�t�@�C���̐�΃p�X
     */
    public static String getPath(String relativePath) {
        return new File(LimyQalabPlugin.getDefault().getPluginRoot(),
                relativePath).getAbsolutePath();
    }

    /**
     * ���\�[�X�t�@�C���̐�΃p�X��Ԃ��܂��B
     * @param relativePath �v���O�C�����[�g����̑��΃p�X
     * @return ���\�[�X�t�@�C���̐�΃p�X
     */
    public static String getResourcePath(String relativePath) {
        return getPath("resource/" + relativePath);
    }

    /**
     * �v���W�F�N�g�p��Store�𐶐����ĕԂ��܂��B
     * @param project �v���W�F�N�g
     * @return �v���W�F�N�g�p��Store
     * @throws CoreException 
     */
    public static IPreferenceStore createQalabStore(IProject project) throws CoreException {
        IPreferenceStore store = new ScopedPreferenceStore(new ProjectScope(project), 
                LimyQalabPlugin.getDefault().getBundle().getSymbolicName());
        
        store.setDefault(LimyQalabConstants.KEY_CHK_TYPE,
                LimyQalabConstants.FILE_TYPE_DEFAULT);
        store.setDefault(LimyQalabConstants.KEY_PMD_TYPE,
                LimyQalabConstants.FILE_TYPE_DEFAULT);
        
        store.setDefault(LimyQalabConstants.KEY_QALAB_XML, "/");
        store.setDefault(LimyQalabConstants.KEY_DEST_DIR, "dest");
        store.setDefault(LimyQalabConstants.ENABLE_CHECKSTYLE, true);
        store.setDefault(LimyQalabConstants.ENABLE_PMD, true);
        store.setDefault(LimyQalabConstants.ENABLE_FINDBUGS, true);
        store.setDefault(LimyQalabConstants.ENABLE_JUNIT, true);
        store.setDefault(LimyQalabConstants.ENABLE_NCSS, true);
        store.setDefault(LimyQalabConstants.ENABLE_TODO, true);
        store.setDefault(LimyQalabConstants.ENABLE_JDEPEND, false);
        store.setDefault(LimyQalabConstants.ENABLE_UMLGRAPH, false);
        store.setDefault(LimyQalabConstants.ENABLE_INDIVISUAL, true);
        store.setDefault(LimyQalabConstants.ENABLE_REFPROJECT, true);
        store.setDefault(LimyQalabConstants.UMLGRAPH_INFERREL, true);
        
        boolean isPlugin = Arrays.asList(project.getDescription().getNatureIds())
                .indexOf("org.eclipse.pde.PluginNature") >= 0;

        if (isPlugin) {
            store.setDefault(LimyQalabConstants.KEY_BUILD_XML, "build_qa.xml");
            store.setDefault(LimyQalabConstants.KEY_BUILD_PROP, "build_qa.properties");
        } else {
            store.setDefault(LimyQalabConstants.KEY_BUILD_XML, "build.xml");
            store.setDefault(LimyQalabConstants.KEY_BUILD_PROP, "build.properties");
        }
        
        // Workspace��Preference����v���W�F�N�g��Preference�ɒl���R�s�[
        IPreferenceStore preferenceStore = LimyEclipsePlugin.getDefault().getPreferenceStore();
        store.setValue(LimyQalabConstants.KEY_DOT_EXE,
                preferenceStore.getString(LimyQalabConstants.KEY_DOT_EXE));
        
        return store;
    }

    /**
     * �ݒ�t�@�C����Ԃ��܂��B
     * @param env ��
     * @param defaultFile �f�t�H���g�t�@�C����
     * @param storeKeyType �J�X�^�}�C�Y�ݒ�t�@�C����ʂ̃X�g�A�L�[
     * @param storeKeyFile �J�X�^�}�C�Y�ݒ�t�@�C�����̃X�g�A�L�[
     * @return �ݒ�t�@�C��
     */
    public static File getConfigFile(LimyQalabEnvironment env,
            String defaultFile,
            String storeKeyType, String storeKeyFile) {
        
        IPreferenceStore store = env.getStore();
    
        int type = store.getInt(storeKeyType);
        String file = store.getString(storeKeyFile).replace('\\', '/');
    
        switch (type) {
        case LimyQalabConstants.FILE_TYPE_DEFAULT:
            return new File(LimyQalabPluginUtils.getResourcePath(defaultFile));
        case LimyQalabConstants.FILE_TYPE_INTERNAL:
            // Linked Resource�ɑΉ�
            IFile targetFile = env.getProject().getFile(new Path(file));
            return targetFile.getRawLocation().toFile();
        case LimyQalabConstants.FILE_TYPE_EXTERNAL:
            return new File(file);
        default:
            throw new IllegalStateException("Not Suppoted type : " + type);
        }
    
    }

    /**
     * @param project
     * @return
     * @throws CoreException 
     */
    public static LimyQalabEnvironment createEnv(IProject project)
            throws CoreException {

        try {
            LimyQalabEnvironment env = new LimyQalabEnvironment(
                    new IProject[] { project }, createQalabStore(project));
            ClassLoader loader = ClassLoaderCreator.createProjectClassLoader(
                    env, ClassLoader.getSystemClassLoader());
            env.setProjectClassLoader(loader);
            return env;
        } catch (IOException e) {
            throw new WorkbenchException(e.getMessage(), e);
        }
    }

}
