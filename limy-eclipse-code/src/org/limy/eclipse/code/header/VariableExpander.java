/*
 * Created 2007/08/21
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
package org.limy.eclipse.code.header;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * �ϐ��̎��W�J����������N���X�ł��B
 * @author Naoki Iwami
 */
public final class VariableExpander {

    // ------------------------ Constants

    /**
     * �t�@�C���̐擪�R�����g�����ʂ���p�^�[��
     */
    // �u/*�v����n�܂��āu */�v�ŏI��镔���i�ŒZ��v�j
    private static final Pattern PATTERN_COMMENT = Pattern.compile("^\\s*(/\\*.*?^ \\*/\\s*)(.*)",
            Pattern.DOTALL | Pattern.MULTILINE);
    
    /**
     * �R�����g���̓��t�����ʂ���p�^�[��
     */
    // yyyy/mm/dd�`������t�Ƃ��ĔF��
    private static final Pattern PATTERN_DATE = Pattern.compile(".*[^\\d](\\d+/\\d+/\\d+).*",
            Pattern.DOTALL | Pattern.MULTILINE);

    /**
     * �R�����g���̎��������ʂ���p�^�[��
     */
    // yyyy/mm/dd hh:mm:ss�`������t�Ƃ��ĔF��
    private static final Pattern PATTERN_TIME = Pattern.compile(
            ".*[^\\d](\\d+/\\d+/\\d+\\s+\\d+:\\d+:\\d+).*",
            Pattern.DOTALL | Pattern.MULTILINE);

    // ------------------------ Constructors

    /**
     * private constructor
     */
    private VariableExpander() { }
    
    // ------------------------ Public Methods

    /**
     * Java�\�[�X������Ƀw�b�_��ǉ����ĕԂ��܂��B
     * @param content Java�\�[�X������
     * @param header �w�b�_������
     * @param options �u�������}�b�s���O
     * @return �w�b�_��ǉ�����Java�\�[�X������
     */
    public static String convertContent(
            String content, String header, Map<String, String> options) {
        
        String tmpContent = content;
        Date date = new Date();
        String dateStr = new SimpleDateFormat("yyyy/MM/dd").format(date);
        String timeStr = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date);
        
        // �擪�� /* �` */ �������i�ŒZ��v�j
        Matcher matcherComment = PATTERN_COMMENT.matcher(content);
        if (matcherComment.matches()) {
            
            String group1 = matcherComment.group(1);
            if (!group1.startsWith("/**")) {
                // �����Ȃ� /** �Ŏn�܂�p�^�[���ipackage-info.java�Ȃǁj�͏��O
                Matcher matcherDate = PATTERN_DATE.matcher(group1);
                if (matcherDate.matches()) {
                    // ���t�`���̕����񂪌���������A�������̒u�����������Ŏg���i�܂茻����ێ�����j
                    dateStr = matcherDate.group(1);
                }

                Matcher matcherTime = PATTERN_TIME.matcher(group1);
                if (matcherTime.matches()) {
                    // �����`���̕����񂪌���������A�������̒u�����������Ŏg���i�܂茻����ێ�����j
                    timeStr = matcherTime.group(1);
                }

                tmpContent = matcherComment.group(2);
            }
        }
        
        String tmpHeader = header;
        for (Entry<String, String> entry : options.entrySet()) {
            tmpHeader = tmpHeader.replaceAll("\\$\\{" + entry.getKey() + "\\}", entry.getValue());
        }
        tmpHeader = tmpHeader.replaceAll("\\$\\$", "\\$");
        tmpHeader = tmpHeader.replaceAll("\\$\\{date\\}", dateStr);
        tmpHeader = tmpHeader.replaceAll("\\$\\{time\\}", timeStr);
        tmpHeader = tmpHeader.replaceAll("\\$\\{year\\}",
                new SimpleDateFormat("yyyy").format(date));
        
        return tmpHeader + tmpContent;
    }

}
