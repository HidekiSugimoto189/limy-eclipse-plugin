/*
 * Created 2007/02/07
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
package org.limy.xml;

import java.util.Collection;
import java.util.List;

/**
 * XML�v�f��\���C���^�[�t�F�C�X�ł��B
 * @author Naoki Iwami
 */
public interface XmlElement {

    /**
     * �v�f����Ԃ��܂��B
     * @return �v�f��
     */
    String getName();

    /**
     * �v�f�l��Ԃ��܂��B
     * @return �v�f�l
     */
    String getValue();
    
    /**
     * �������g�̃R�s�[���쐬���܂��B
     * @return �R�s�[���ꂽ�v�f
     */
    XmlElement cloneSelf();

    /**
     * �������g�̒��O�i���n��j�Ɏ��g�̃R�s�[���쐬���܂��B
     * @return �R�s�[���ꂽ�v�f
     */
    XmlElement copyBeforeSelf();

    /**
     * �����ꗗ��Ԃ��܂��B
     * @return �����ꗗ
     */
    Collection<XmlAttribute> getAttributes();
    
    /**
     * ������ǉ����܂��B
     * @param attr ����
     */
    void setAttribute(XmlAttribute attr);
    
    /**
     * ������ǉ����܂��B
     * @param name ������
     * @param value �����l
     */
    void setAttribute(String name, String value);

    /**
     * �q�v�f���폜���܂��B
     * @param child �q�v�f
     */
    void removeChild(XmlElement child);

    /**
     * �����������Ă��邩�ǂ�����Ԃ��܂��B
     * @return �����������Ă���ΐ^
     */
    boolean hasAttributes();
    
    /**
     * �q�v�f�������Ă��邩�ǂ�����Ԃ��܂��B
     * @return �q�v�f�������Ă���ΐ^
     */
    boolean hasChildren();
    
    /**
     * �q�v�f�ꗗ��Ԃ��܂��B
     * @return �q�v�f�ꗗ
     */
    List<XmlElement> getChildren();

    /**
     * �����l��Ԃ��܂��B
     * @param name ������
     * @return �����l
     */
    String getAttribute(String name);
    
    /**
     * �q�v�f��ǉ����܂��B
     * @param child �q�v�f
     */
    void addChild(XmlElement child);
    
    /**
     * �q�v�f��ǉ����܂��B
     * @param index �}���ʒu
     * @param child �q�v�f
     */
    void addChild(int index, XmlElement child);




}
