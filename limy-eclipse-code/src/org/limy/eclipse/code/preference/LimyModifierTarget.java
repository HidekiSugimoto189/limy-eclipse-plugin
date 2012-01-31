/*
 * Created 2005/07/21
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
package org.limy.eclipse.code.preference;

/**
 * ���ʂ̃Z���ύX�^�[�Q�b�g�C���^�[�t�F�C�X�ł��B
 * @author Naoki Iwami
 */
public interface LimyModifierTarget {

    /**
     * �Z�����e���X�V���܂��B
     * @param element �X�V�v�f
     * @param properties �X�V�v���p�e�B�i�z��j
     */
    void update(Object element, String[] properties);

    /**
     * �X�V�t���O��ݒ肵�܂��B
     * @param b �X�V�t���O
     */
    void setModified(boolean b);

}
