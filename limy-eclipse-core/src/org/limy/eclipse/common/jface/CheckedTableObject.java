/*
 * Created 2007/12/14
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
package org.limy.eclipse.common.jface;

/**
 * �`�F�b�N�t�̃e�[�u���Z����\���܂��B
 * @author Naoki Iwami
 */
public class CheckedTableObject implements ITableObject {

    /** �\���p������ */
    private String viewStr;

    /** �����l */
    private Object value;
    
    /** �I���� */
    private boolean checked;
    
    // ------------------------ Constructors

    /**
     * CheckedTableObject �C���X�^���X���\�z���܂��B
     * @param viewStr �\���p������
     * @param value �����l
     */
    public CheckedTableObject(String viewStr, Object value) {
        super();
        this.viewStr = viewStr;
        this.value = value;
    }
    
    // ------------------------ Implement Methods

    public String getViewString(int index) {
        return viewStr;
    }

    public Object getValue(int index) {
        return value;
    }

    public void setValue(int index, Object value) {
        this.value = value;
    }

    // ------------------------ Getter/Setter Methods

    /**
     * �I���󋵂��擾���܂��B
     * @return �I����
     */
    public boolean isChecked() {
        return checked;
    }

    /**
     * �I���󋵂�ݒ肵�܂��B
     * @param checked �I����
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

}
