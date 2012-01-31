/*
 * Created 2008/08/23
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
package org.limy.eclipse.qalab.outline.sequence;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;

/**
 * �V�[�P���X�}��\���܂��B
 * @author Naoki Iwami
 */
public class SequenceBean {
    
    // ------------------------ Fields
    
    /** �R���e�i�� */
    private String containerName;

    /** �Ăяo���� */
    private String callName;

    /** �C���X�^���X�� */
    private String instanceName;

    /** Java�v�f */
    private IJavaElement javaElement;

    /** �q�v�f */
    private Collection<SequenceBean> children = new ArrayList<SequenceBean>();
    
    // ------------------------ Constructors

    /**
     * SequenceBean �C���X�^���X���\�z���܂��B
     * @param instanceName �C���X�^���X��
     * @param javaElement Java�v�f
     */
    public SequenceBean(String instanceName, IJavaElement javaElement) {
        super();
        this.javaElement = javaElement;
        this.callName = javaElement.getElementName();
        this.instanceName = instanceName;
        if (javaElement instanceof IMember) {
            IMember member = (IMember)javaElement;
            this.containerName = member.getDeclaringType().getElementName();
        }
    }

    /**
     * SequenceBean �C���X�^���X���\�z���܂��B
     * @param instanceName �C���X�^���X��
     * @param javaElement Java�v�f
     * @param containerName �R���e�i��
     */
    public SequenceBean(String instanceName, IJavaElement javaElement, String containerName) {
        super();
        this.javaElement = javaElement;
        this.callName = javaElement.getElementName();
        this.instanceName = instanceName;
        this.containerName = containerName;
    }

    /**
     * SequenceBean �C���X�^���X���\�z���܂��B
     * @param containerName �R���e�i��
     * @param callName �Ăяo����
     */
    public SequenceBean(String containerName, String callName) {
        super();
        this.callName = callName;
        this.containerName = containerName;
    }
    
    // ------------------------ Public Methods
    
    public void addChild(SequenceBean bean) {
        children.add(bean);
    }
    
    public String getResultValue() {
        if (callName.startsWith("get")) {
            return callName.substring(3, 4).toLowerCase() + callName.substring(4);
        }
        return null;
    }
    
    /**
     * �w�肵�����O��Java�v�f��Ԃ��܂��B
     * @param lastBean �Ō�Ɍ�������Bean
     * @param name ����
     * @return Java�v�f
     */
    public IJavaElement searchElement(SequenceBean lastBean, String name) {
        for (SequenceBean child : getChildren()) {
            IJavaElement result = child.searchElement(lastBean, name);
            if (result != null) {
                return result;
            }
        }
        if (getCallName().equals(name)) {
            return javaElement;
        }
        return null;
    }

    // ------------------------ Getter/Setter Methods

    /**
     * �R���e�i�����擾���܂��B
     * @return �R���e�i��
     */
    public String getContainerName() {
        return containerName;
    }
    
    /**
     * �R���e�i����ݒ肵�܂��B
     * @param containerName �R���e�i��
     */
    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    /**
     * �Ăяo�������擾���܂��B
     * @return �Ăяo����
     */
    public String getCallName() {
        return callName;
    }

    /**
     * �Ăяo������ݒ肵�܂��B
     * @param callName �Ăяo����
     */
    public void setCallName(String callName) {
        this.callName = callName;
    }

    /**
     * �C���X�^���X�����擾���܂��B
     * @return �C���X�^���X��
     */
    public String getInstanceName() {
        return instanceName;
    }

    /**
     * Java�v�f���擾���܂��B
     * @return Java�v�f
     */
    public IJavaElement getJavaElement() {
        return javaElement;
    }

    /**
     * �q�v�f���擾���܂��B
     * @return �q�v�f
     */
    public Collection<SequenceBean> getChildren() {
        return children;
    }

    /**
     * �q�v�f��ݒ肵�܂��B
     * @param children �q�v�f
     */
    public void setChildren(Collection<SequenceBean> children) {
        this.children = children;
    }

}
