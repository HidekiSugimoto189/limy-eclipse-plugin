/*
 * Created 2005/09/13
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
package org.limy.eclipse.code.accessor;

import java.util.ArrayList;
import java.util.List;

import org.limy.eclipse.code.common.LimyFieldObject;

/**
 *�@�N���X�I�u�W�F�N�g��\���܂��B
 * @opt inferrel
 * @opt collpackages java.util.*
 * @opt inferdep
 * @author Naoki Iwami
 */
public class LimyClassObject {
    
    // ------------------------ Fields

    /**
     * �t�B�[���h�ꗗ
     */
    private List<LimyFieldObject> fields = new ArrayList<LimyFieldObject>();
    
//    // ------------------------ Constructors
//
//    /**
//     * LimyClassObject�C���X�^���X���\�z���܂��B
//     */
//    public LimyClassObject() {
//        
//    }
    
    // ------------------------ Public Methods

    /**
     * �t�B�[���h��ǉ����܂��B
     * @param field �ǉ�����t�B�[���h
     */
    public void addField(LimyFieldObject field) {
        fields.add(field);
    }
    
    /**
     * �t�B�[���h�����q��Ԃ��܂��B
     * @return �t�B�[���h�����q
     */
    public List<LimyFieldObject> getFields() {
        return fields;
    }
    
    // ------------------------ Getter/Setter Methods

    

}
