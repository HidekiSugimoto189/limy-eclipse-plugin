/*
 * Created 2006/08/19
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * XML�v�f��\���܂��B
 * @author Naoki Iwami
 * @version 1.0.0
 */
public class SimpleElement implements XmlElement, Cloneable {
    
    // ------------------------ Fields

    /**
     * �e�v�f
     */
    private XmlElement parent;
    
    /**
     * �v�f��
     */
    private String name;

    /**
     * �v�f�l
     * <p>
     * ���̒l���ݒ肳��Ă���Ƃ��A�q�v�f�ꗗ�͖�������܂��B
     * </p>
     */
    private String value;
    
    /**
     * �����ꗗ
     */
    private Set<XmlAttribute> attributes = new LinkedHashSet<XmlAttribute>();
    
    /**
     * �q�v�f�ꗗ
     */
    private List<XmlElement> elements = new ArrayList<XmlElement>();

    // ------------------------ Constructors

    /**
     * SimpleElement�C���X�^���X���\�z���܂��B
     * @param name �v�f��
     */
    public SimpleElement(String name) {
        super();
        this.name = name;
    }

    /**
     * SimpleElement�C���X�^���X���\�z���܂��B
     * @param parent �e�v�f
     * @param name �v�f��
     */
    public SimpleElement(XmlElement parent, String name) {
        super();
        this.parent = parent;
        this.name = name;
        parent.addChild(this);
    }

    /**
     * SimpleElement�C���X�^���X���\�z���܂��B
     * @param parent �e�v�f
     * @param name �v�f��
     * @param afterEl �}���ʒu
     */
    public SimpleElement(XmlElement parent, String name, XmlElement afterEl) {
        super();
        this.parent = parent;
        this.name = name;
        for (int i = 0; i < parent.getChildren().size(); i++) {
            if (parent.getChildren().get(i).equals(afterEl)) {
                parent.addChild(i, this);
                break;
            }
        }
    }

    /**
     * SimpleElement�C���X�^���X���\�z���܂��B
     * @param parent �e�v�f
     * @param name �v�f��
     * @param value �v�f�l
     */
    public SimpleElement(SimpleElement parent, String name, String value) {
        super();
        this.parent = parent;
        this.name = name;
        this.value = value;
        parent.addChild(this);
    }
    
    // ------------------------ Override Methods

    @Override
    public String toString() {
        StringBuilder buff = new StringBuilder();
        buff.append(name).append(" : ");
        for (XmlAttribute attr : attributes) {
            buff.append(attr).append(',');
        }
        for (XmlElement el : elements) {
            buff.append('\n');
            buff.append(el);
        }
        return buff.toString();
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        SimpleElement obj = (SimpleElement)super.clone();
        List<XmlElement> orgElements = new ArrayList<XmlElement>();
        orgElements.addAll(obj.getChildren());
        obj.setElements(orgElements);
        return obj;
    }

    // ------------------------ Implement Methods
    
    public XmlElement cloneSelf() {
        try {
            return (XmlElement)clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e);
        }
    }

    public XmlElement copyBeforeSelf() {
        SimpleElement result = new SimpleElement(parent, name, this);
        for (XmlAttribute attr : getAttributes()) {
            result.setAttribute(attr);
        }
        for (XmlElement child : getChildren()) {
            result.addChild(child);
        }
        return result;
    }
    
    /**
     * ������ǉ����܂��B
     * @param attr ����
     */
    public void setAttribute(XmlAttribute attr) {
        attributes.remove(attr);
        attributes.add(attr);
    }

    /**
     * ������ǉ����܂��B
     * @param name ������
     * @param value �����l
     */
    public void setAttribute(String name, String value) {
        setAttribute(new SimpleAttribute(name, value));
    }

    /**
     * �q�v�f���폜���܂��B
     * @param child �q�v�f
     */
    public void removeChild(XmlElement child) {
        elements.remove(child);
    }

    /**
     * �����������Ă��邩�ǂ�����Ԃ��܂��B
     * @return �����������Ă���ΐ^
     */
    public boolean hasAttributes() {
        return attributes != null && !attributes.isEmpty();
    }
    
    /**
     * �q�v�f�������Ă��邩�ǂ�����Ԃ��܂��B
     * @return �q�v�f�������Ă���ΐ^
     */
    public boolean hasChildren() {
        return elements != null && !elements.isEmpty();
    }
    
    /**
     * �q�v�f�ꗗ��Ԃ��܂��B
     * @return �q�v�f�ꗗ
     */
    public List<XmlElement> getChildren() {
        return elements;
    }

    public String getAttribute(String name) {
        for (XmlAttribute attr : attributes) {
            if (attr.getName().equals(name)) {
                return attr.getValue();
            }
        }
        return null;
    }
    
    /**
     * �q�v�f��ǉ����܂��B
     * @param child �q�v�f
     */
    public void addChild(XmlElement child) {
        elements.add(child);
    }
    
    /**
     * �q�v�f��ǉ����܂��B
     * @param index �}���ʒu
     * @param child �q�v�f
     */
    public void addChild(int index, XmlElement child) {
        elements.add(index, child);
    }

    // ------------------------ Getter/Setter Methods

    /**
     * �v�f�����擾���܂��B
     * @return �v�f��
     */
    public String getName() {
        return name;
    }

    /**
     * �v�f�l���擾���܂��B
     * @return �v�f�l
     */
    public String getValue() {
        return value;
    }

    /**
     * �����ꗗ���擾���܂��B
     * @return �����ꗗ
     */
    public Collection<XmlAttribute> getAttributes() {
        return attributes;
    }
    
    // ------------------------ Private Methods
    
    /**
     * �q�v�f�ꗗ��ݒ肵�܂��B
     * @param elements �q�v�f�ꗗ
     */
    private void setElements(List<XmlElement> elements) {
        this.elements = elements;
    }
    
}
