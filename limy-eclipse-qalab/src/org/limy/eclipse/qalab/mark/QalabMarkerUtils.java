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
package org.limy.eclipse.qalab.mark;

import org.limy.eclipse.qalab.common.AntCreator;

/**
 * �}�[�J�[�֘A�̃��[�e�B���e�B�N���X�ł��B
 * @author Naoki Iwami
 */
public final class QalabMarkerUtils {
    
    /**
     * private constructor
     */
    private QalabMarkerUtils() { }

    /**
     * Creator�ɑΉ�����}�[�J�[�쐬�C���X�^���X��Ԃ��܂��B
     * @param creator Creator
     * @return �}�[�J�[�쐬�C���X�^���X�i���݂��Ȃ��ꍇ��null�j
     */
    public static MarkCreator getMarkCreator(AntCreator creator) {
        String name = creator.getTargetName();
        if ("checkstyle".equals(name)) {
            return CheckstyleMarkCreator.getInstance();
        }
        if ("pmd".equals(name)) {
            return PmdMarkCreator.getInstance();
        }
        if ("findbugs".equals(name)) {
            return FindbugsMarkCreator.getInstance();
        }
        if ("cobertura".equals(name)) {
            return CoberturaMarkCreator.getInstance();
        }
        return null;
    }

}
