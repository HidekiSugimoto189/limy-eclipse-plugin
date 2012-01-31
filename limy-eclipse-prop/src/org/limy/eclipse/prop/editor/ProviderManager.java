/*
 * Created 2007/02/06
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
package org.limy.eclipse.prop.editor;

import org.limy.eclipse.common.LimyStoreColorProvider;
import org.limy.eclipse.core.LimyEclipsePlugin;
import org.limy.eclipse.prop.LimyPropColorProvider;

/**
 * �v���p�e�B�G�f�B�^�Ŏg�p����e��v���o�C�_���Ǘ�����N���X�ł��B
 * @author Naoki Iwami
 */
public final class ProviderManager {
    
    /** �B��̃C���X�^���X */
    private static ProviderManager instance = new ProviderManager();
    
    /** �J���[�v���o�C�_ */
    private LimyStoreColorProvider colorProvider;

    /** �R�[�h�X�L���i */
    private PropertyCodeScanner propertyScanner;

    /**
     * private constructor
     */
    private ProviderManager() { }
    
    public static ProviderManager getInstance() {
        return instance;
    }
    
    /**
     * �J���[�v���o�C�_���擾���܂��B
     * @return �J���[�v���o�C�_
     */
    public LimyStoreColorProvider getColorProvider() {
        if (colorProvider == null) {
            colorProvider = new LimyPropColorProvider(
                    LimyEclipsePlugin.getDefault().getPreferenceStore());
        }
        return colorProvider;
    }

    /**
     * �R�[�h�X�L���i���擾���܂��B
     * @return �R�[�h�X�L���i
     */
    public PropertyCodeScanner getPropertyScanner() {
        if (propertyScanner == null) {
            propertyScanner = new PropertyCodeScanner(getColorProvider());
        }
        return propertyScanner;
    }

    /**
     * �v���o�C�_�Q���X�V�i�������j���܂��B
     */
    public void updateProviders() {
        colorProvider = new LimyPropColorProvider(
                LimyEclipsePlugin.getDefault().getPreferenceStore());
        propertyScanner = new PropertyCodeScanner(getColorProvider());
//      colorProvider = null;
//      propertyCodeScanner = null;
    }

}
