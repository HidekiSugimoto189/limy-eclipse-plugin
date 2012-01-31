/*
 * Created 2007/02/26
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
package org.limy.eclipse.qalab.outline.asm;

import java.util.ArrayList;
import java.util.Collection;

import org.objectweb.asm.Label;

/**
 * �C���X�g���N�V�����|�C���^�̏���\���܂��B
 * @author Naoki Iwami
 */
public class PointInfo {

    /** �J�E���^ */
    private final int count;
    
    /** �W�����v�̂ݒ�` */
    private boolean jumpOnly;
    
    /** ���g�̃��x�� */
    private Label self;
    
    /** �s�ԍ� */
    private int lineNumber;
    
    /** info������ */
    private String info;

    /** �J�ڐ�̃��x�� */
    private Collection<Label> jumpTos = new ArrayList<Label>();

    // ------------------------ Constructors

    /**
     * PointInfo�C���X�^���X���\�z���܂��B
     * @param count �J�E���^
     * @param self ���g�̃��x��
     * @param lineNumber �s�ԍ�
     * @param info info������
     */
    public PointInfo(int count, Label self, int lineNumber, String info) {
        super();
        this.count = count;
        this.self = self;
        this.info = info;
        this.lineNumber = lineNumber;
    }

    /**
     * PointInfo�C���X�^���X���\�z���܂��B
     * @param count �J�E���^
     * @param self ���g�̃��x��
     * @param jumpTo �J�ڐ�̃��x��
     * @param jumpOnly 
     * @param lineNumber �s�ԍ�
     * @param info info������
     */
    public PointInfo(int count, Label self, Label jumpTo, boolean jumpOnly,
            int lineNumber, String info) {
        super();
        this.count = count;
        this.self = self;
        this.jumpOnly = jumpOnly;
        this.info = info;
        this.lineNumber = lineNumber;
        jumpTos.add(jumpTo);
    }
    
    /**
     * PointInfo�C���X�^���X���\�z���܂��B
     * @param count �J�E���^
     * @param self ���g�̃��x��
     * @param jumpTos �J�ڐ�̃��x���ꗗ
     * @param lineNumber �s�ԍ�
     * @param info info������
     */
    public PointInfo(int count, Label self, Collection<Label> jumpTos,
            int lineNumber, String info) {
        super();
        this.count = count;
        this.self = self;
        this.jumpTos = jumpTos;
        this.jumpOnly = true;
        this.info = info;
        this.lineNumber = lineNumber;
    }

    // ------------------------ Getter/Setter Methods

    /**
     * �J�E���^���擾���܂��B
     * @return �J�E���^
     */
    public int getCount() {
        return count;
    }
    
    /**
     * ���g�̃��x�����擾���܂��B
     * @return ���g�̃��x��
     */
    public Label getSelf() {
        return self;
    }

    /**
     * �W�����v�̂ݒ�`���擾���܂��B
     * @return �W�����v�̂ݒ�`
     */
    public boolean isJumpOnly() {
        return jumpOnly;
    }
    
    /**
     * �J�ڐ�̃��x���ꗗ���擾���܂��B
     * @return �J�ڐ�̃��x���ꗗ
     */
    public Collection<Label> getJumpTos() {
        return jumpTos;
    }

    /**
     * info��������擾���܂��B
     * @return info������
     */
    public String getInfo() {
        return info;
    }

    /**
     * �s�ԍ����擾���܂��B
     * @return �s�ԍ�
     */
    public int getLineNumber() {
        return lineNumber;
    }

}
