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
import org.eclipse.jface.viewers.ListViewer;

/**
 * �X�g�A�Ɗ֘A�t����ꂽList�R���g���[���p�N���X�i�L�[�ƒl�̗��������j�ł��B
 * @author Naoki Iwami
 */
public class StoredNameValueList extends AbstractStoredControl {

    /** ���X�g�r���[�A */
    private ListViewer listViewer;

    public StoredNameValueList(String storeKey, ListViewer listViewer) {
        super(storeKey);
        this.listViewer = listViewer;
    }

    @Override
    public void doStore(IPreferenceStore store) {
        int count = listViewer.getList().getItemCount();
        StringBuilder value = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (value.length() > 0) {
                value.append('\n');
            }
            NameValue nameValue = (NameValue)listViewer.getElementAt(i);
            value.append(nameValue.getName()).append('\t').append(nameValue.getValue());
        }
        store.setValue(getStoreKey(), value.toString());
    }


}
