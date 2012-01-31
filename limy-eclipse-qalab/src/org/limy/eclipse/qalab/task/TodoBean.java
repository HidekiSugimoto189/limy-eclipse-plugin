/*
 * Created 2006/11/22
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
package org.limy.eclipse.qalab.task;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Naoki Iwami
 */
public class TodoBean {

    // ------------------------ Classes

    /** �t�@�C���� */
    private String name;
    
    /** �G���[�ꗗ */
    private List<TodoError> errors = new ArrayList<TodoError>();
    
    // ------------------------ Public Methods

    /**
     * todo�G���[��ǉ����܂��B
     * @param error todo�G���[
     */
    public void addError(TodoError error) {
        errors.add(error);
    }
    
    // ------------------------ Getter/Setter Methods

    /**
     * �t�@�C�������擾���܂��B
     * @return �t�@�C����
     */
    public String getName() {
        return name;
    }

    /**
     * �t�@�C������ݒ肵�܂��B
     * @param name �t�@�C����
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * �G���[�ꗗ���擾���܂��B
     * @return �G���[�ꗗ
     */
    public List<TodoError> getErrors() {
        return errors;
    }

}
