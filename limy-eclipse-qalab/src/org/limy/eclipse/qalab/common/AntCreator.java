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
package org.limy.eclipse.qalab.common;

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.limy.xml.XmlElement;


/**
 * Ant�����\�ȃC���^�[�t�F�C�X�ł��B
 * @author Naoki Iwami
 */
public interface AntCreator {

    // ------------------------ Abstract Methods

    /**
     * Ant�v�f���쐬���܂��B
     * @param root XML���[�g�v�f
     * @param env 
     * @throws IOException I/O��O
     * @throws CoreException �R�A��O
     */
    void exec(XmlElement root, LimyQalabEnvironment env)
            throws IOException, CoreException;

    /**
     * ���|�[�g�o�͐��XML�t�@�C�������擾���܂��B
     * @return ���|�[�g�o�͐��XML�t�@�C����
     */
    String[] getReportXmlNames();
    
    /**
     * Qalab��StatMerge�N���X���ꗗ���擾���܂��B
     * @return Qalab��StatMerge�N���X���ꗗ
     */
    String[] getQalabClassNames();
    
    /**
     * Qalab�T�}���p�O���[�v�ԍ����擾���܂��B
     * <p>
     * -1 : ����
     * 0 : QALab�v������
     * 1 : QALab�v������(Covertura)
     * 2 : QALab�v���Ȃ��B�m�[�}��&���|�[�g
     * 3 : QALab�v���Ȃ��B���|�[�gonly�i�^�[�Q�b�g���̓m�[�}���j
     * 4 : QALab�v���Ȃ��B���|�[�gonly�i�^�[�Q�b�g���̓��|�[�g�j QALab��p
     * </p>
     * @return Qalab�T�}���p�O���[�v�ԍ�
     */
    int getSummaryGroup(); // TODO Enum�����ׂ�
    
    /**
     * Qalab�T�}�����̈ꗗ���擾���܂��B
     * @return Qalab�T�}�����̈ꗗ
     */
    String[] getSummaryTypes();
    
    /**
     * Ant�^�[�Q�b�g�����擾���܂��B
     * @return Ant�^�[�Q�b�g��
     */
    String getTargetName();
    
    /**
     * ���g���v���W�F�N�g���ŗL�����ǂ�����Ԃ��܂��B
     * @param store �v���W�F�N�g�X�g�A
     * @return �v���W�F�N�g���ŗL���Ȃ��true
     */
    boolean isEnable(IPreferenceStore store);

    /**
     * ���g�̏��������ꂽ�C���X�^���X���쐬���܂��B
     * @return ���g�̏��������ꂽ�C���X�^���X
     */
    AntCreator newInstance();

}
