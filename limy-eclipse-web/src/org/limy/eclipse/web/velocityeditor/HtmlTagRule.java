/*
 * Created 2006/01/14
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
package org.limy.eclipse.web.velocityeditor;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.limy.eclipse.common.jface.MultiTextAttribute;

/**
 * HTML�^�O�̌��o���[�����߂��N���X�ł��B
 * @author Naoki Iwami
 */
public class HtmlTagRule extends AbstractDollarRule {

    // ------------------------ Constants

    /**
     * �X�L�b�v����
     */
    private static final String SKIP_CHARACTER = " \t\n\r";

    // ------------------------ Fields

    /**
     * �^�O�̑���
     */
    private TextAttribute tagToken;

    /**
     * �^�O������̑���
     */
    private TextAttribute keywordToken;
    
    // ------------------------ Constructors

    /**
     * HtmlTagRule�C���X�^���X���\�z���܂��B
     * @param tagToken
     * @param keywordToken
     * @param propertyToken 
     */
    public HtmlTagRule(
            TextAttribute tagToken, TextAttribute keywordToken,
            TextAttribute propertyToken) {
        
        super(new VelocityDollarRule(propertyToken));

        this.tagToken = tagToken;
        this.keywordToken = keywordToken;
    }
    
    // ------------------------ Implement Methods

    public IToken evaluate(ICharacterScanner scanner) {
        int c = scanner.read();
        if (c == '<') {
            setScanner(scanner);
            return doEvaluate();
        }
        scanner.unread();
        return Token.UNDEFINED;
    }
    
    // ------------------------ Private Methods

    /**
     * �v�f�����o���܂��B
     * @return ���o���ꂽ�g�[�N��
     */
    private IToken doEvaluate() {
        MultiTextAttribute attr = new MultiTextAttribute();

        // �^�O������͂��n�܂�����^�ɂ���i��{�I�ɂ̓��[�v����ɂ����ɐ^�ɂȂ邪�A</abc>�`���̏ꍇ��a�̎��_�Ő^�ɂȂ�j
        boolean start = false;

        // �^�O����͂��I�������^�ɂ���
        boolean inner = false;

        // ���� < ��ǂݏo���Ă���̂ŏ����J�E���g��1
        setCount(1);

        while (true) {
            int c = readScanner();

            // �^�O��͏���̂ݒʂ郋�[�`��
            if (!start) {
                if (c == '/') {
                    // do nothing
                } else {
                    start = true;
                    attr.add(tagToken, getCount() - 1); // < ���^�O�����Ƃ��ēo�^
                }
            }

            // ���񃋁[�v���ȊO�͑S�Ēʂ郋�[�`��
            if (start) {
                if (c == '>') {
                    // �^�O���I�������ꍇ
                    if (!inner) {
                        // <a> �Ȃǂ̏ꍇ�͂�����ʂ�B�܂�a���L�[���[�h�Ƃ��ēo�^
                        attr.add(keywordToken, getCount() - 1);
                    } else {
                        // <a href> �Ȃǂ̏ꍇ�͂�����ʂ�B�܂� href �͒ʏ핶���Ƃ��ēo�^
                        attr.add((TextAttribute)null, getCount() - 1);
                    }
                    // > ���^�O�����Ƃ��ēo�^���ďI��
                    attr.add(tagToken, getCount());
                    return new Token(attr);
                } else if (!inner && isSkipCharacter(c)) {
                    // �^�O����͒��ɃX�L�b�v�����������ꍇ�A���O�܂ł��^�O���Ƃ��ēo�^
                    attr.add(keywordToken, getCount() - 1);
                    inner = true; // �^�O��͏I���t���O��ON��
                } else {
                    // �^�O������͒��A����у^�O�ȍ~�̕������͒��͂�����ʂ�
                    if (c == '$') {
                        evaluateDollar(attr, inner);
                    }
                    
                }
            }
            
            // EOF��������S�Ă��L�����Z�����ďI��
            if (c == ICharacterScanner.EOF) {
                unreadAll();
                return Token.UNDEFINED;
            }
        }
    }

    /**
     * �������X�L�b�v����(�X�y�[�X�A�^�u�A���s)���ǂ����𔻒肵�܂��B
     * @param c ����
     * @return �������X�L�b�v�����Ȃ�ΐ^
     */
    private boolean isSkipCharacter(int c) {
        return SKIP_CHARACTER.indexOf(c) >= 0;
    }

    /**
     * $��������͂��܂��B
     * @param attr ��������
     * @param inner �^�O����͒��Ȃ�ΐ^
     */
    private void evaluateDollar(MultiTextAttribute attr, boolean inner) {
        // ��͒���$�L�����������ꂽ��A$RULE�ɏ������ς˂�
        
        if (!inner) {
            // <href$.. �Ȃǂ̏ꍇ�Ahref���L�[���[�h�Ƃ��ēo�^
            attr.add(keywordToken, getCount() - 1);
        } else {
            // <href aaa $.. �Ȃǂ̏ꍇ " aaa " ��ʏ핶���Ƃ��ēo�^
            attr.add((TextAttribute)null, getCount() - 1);
        }

        doEvaluateDollar(attr);
    }

}
