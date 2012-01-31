/*
 * Created 2007/07/01
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
package org.limy.eclipse.qalab.qaview;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.limy.eclipse.common.jface.ITableObject;

/**
 * QALab�r���[�\���p�̃A�C�e���ł��B
 * @author Naoki Iwami
 */
public class QalabItem implements ITableObject {
    
    // ------------------------ Private Methods

    /** ���t */
    private final Date date;
    
    /** �A�C�e�������� */
    private final List<String> itemNames;
    
    /** �e��A�C�e�� */
    private String[] items;

    // ------------------------ Constructors

    /**
     * QalabItem �C���X�^���X���\�z���܂��B
     * @param date ���t
     * @param itemNames �A�C�e��������
     */
    public QalabItem(Date date, List<String> itemNames) {
        super();
        this.date = date;
        this.itemNames = itemNames;
        items = new String[itemNames.size()];
    }

    // ------------------------ Public Methods

    public void setItem(String name, String value) {
        int index = itemNames.indexOf(name);
        if (index >= 0) {
            items[index] = value;
        }
    }
    
    // ------------------------ Getter/Setter Methods

    /**
     * ���t���擾���܂��B
     * @return ���t
     */
    public Date getDate() {
        return date;
    }

    // ------------------------ Implement Methods

    public String getViewString(int index) {
        return (String)getValue(index);
    }

    public Object getValue(int index) {
        if (index == 0) {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        }
        return items[index - 1];
    }

    public void setValue(int index, Object value) {
        // not support
    }

}
