/*
 * Created 2007/01/23
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
package org.limy.eclipse.qalab.propertypage;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.List;

/**
 * �X�g�A�Ɗ֘A�t����ꂽList�R���g���[���p�N���X�ł��B
 * @author Naoki Iwami
 */
public class StoredList extends AbstractStoredControl {

    // ------------------------ Fields

    /** List�R���g���[�� */
    private final List listControl;

    // ------------------------ Constructors

    /**
     * StoredList�C���X�^���X���\�z���܂��B
     * @param storeKey �X�g�A�L�[
     * @param control List�R���g���[��
     */
    public StoredList(String storeKey, List control) {
        super(storeKey);
        this.listControl = control;
    }

    // ------------------------ Implement Methods

    @Override
    public void doStore(IPreferenceStore store) {
        StringBuilder value = new StringBuilder();
        for (String item : listControl.getItems()) {
            if (value.length() > 0) {
                value.append('\n');
            }
            value.append(item);
        }
        store.setValue(getStoreKey(), value.toString());
    }

}
