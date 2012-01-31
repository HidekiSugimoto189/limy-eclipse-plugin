/*
 * Created 2006/08/20
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
package org.limy.velocity;

/**
 * vmstyle�^�X�N�Ɏg�p�����p�����[�^�N���X�ł��B
 * @author Naoki Iwami
 */
public class VmParam {
    
    /**
     * �p�����[�^��
     */
    private String name;
    
    /**
     * �p�����[�^�l
     */
    private Object expression;

    // ------------------------ Constructors

    /**
     * VmParam �C���X�^���X���\�z���܂��B
     */
    public VmParam() {
        super();
    }

    /**
     * VmParam �C���X�^���X���\�z���܂��B
     * @param name �p�����[�^��
     * @param expression �p�����[�^�l
     */
    public VmParam(String name, Object expression) {
        super();
        this.name = name;
        this.expression = expression;
    }
    
    /**
     * �p�����[�^�l��ݒ肵�܂��B
     * @param expression �p�����[�^�l
     */
    public void setObjectExpression(Object expression) {
        this.expression = expression;
    }

    // ------------------------ Getter/Setter Methods

    /**
     * �p�����[�^�����擾���܂��B
     * @return �p�����[�^��
     */
    public String getName() {
        return name;
    }

    /**
     * �p�����[�^����ݒ肵�܂��B
     * @param name �p�����[�^��
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * �p�����[�^�l���擾���܂��B
     * @return �p�����[�^�l
     */
    public Object getExpression() {
        return expression;
    }

    /**
     * �p�����[�^�l��ݒ肵�܂��B
     * @param expression �p�����[�^�l
     */
    public void setExpression(String expression) {
        this.expression = expression;
    }

}
