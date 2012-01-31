/*
 * Created 2007/02/14
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

/**
 * Web�֘A�̃��[�e�B���e�B�N���X�ł��B
 * @author Naoki Iwami
 */
public final class LimyQalabWebUtils {
    
    /**
     * private constructor
     */
    private LimyQalabWebUtils() { }
    
    /**
     * Checkstyle��source���ɑΉ�����URL��Ԃ��܂��B
     * @param sourceName source��
     * @return source���ɑΉ�����URL
     */
    public static String getCheckstyleUrl(String sourceName) {
        
        // com.puppycrawl.tools.checkstyle.checks.header.RegexpHeaderCheck
        String[] names = sourceName.split("\\.");
        
        String groupName = null;
        if (names.length == 6) {
            // checks�p�b�P�[�W�����̏ꍇ�A�O���[�v����"misc"�Ƃ���
            groupName = "misc";
        }
        if (names.length == 7) {
            groupName = names[5];
        }
        
        if (groupName != null) {
            String checkName = names[names.length - 1].substring(
                    0, names[names.length - 1].length() - 5);
            return "http://checkstyle.sourceforge.net/config_" + groupName + ".html#"
                + checkName;
        }
        return null;
    }

}
