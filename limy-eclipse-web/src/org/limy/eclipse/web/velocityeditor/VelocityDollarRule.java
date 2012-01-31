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
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.limy.eclipse.common.jface.MultiTextAttribute;

/**
 * Velocity($�L��)�̌��o���[�����߂��N���X�ł��B
 * @author Naoki Iwami
 */
public class VelocityDollarRule implements IRule {

    // ------------------------ Fields

    /**
     * �v���p�e�B������̑���
     */
    private TextAttribute propertyToken;

    /**
     * �X�L���i
     */
    private ICharacterScanner scanner;
    
    /**
     * �X�L���i���J�E���g
     */
    private int count;

    // ------------------------ Constructors

    /**
     * VelocityDollarRule�C���X�^���X���\�z���܂��B
     * @param propertyToken
     */
    public VelocityDollarRule(TextAttribute propertyToken) {
        this.propertyToken = propertyToken;
    }
    
    // ------------------------ Public Methods

    /**
     * �X�L���i���J�E���g��Ԃ��܂��B
     * @return �X�L���i���J�E���g
     */
    public int getCount() {
        return count;
    }
    
    // ------------------------ Implement Methods

    public IToken evaluate(ICharacterScanner scanner) {
        int c = scanner.read();
        if (c == '$') {
            this.scanner = scanner;
            return doEvaluate();
        }
        scanner.unread();
        return Token.UNDEFINED;
    }
    
    // ------------------------ Private Methods

    /**
     * �v�f�����o���܂��B
     * @param scanner �X�L���i
     * @return ���o���ꂽ�g�[�N��
     */
    private IToken doEvaluate() {
        MultiTextAttribute attr = new MultiTextAttribute();

        // ���ʕ����K�v�ȏꍇ�͐^�ɂ���
        boolean useBrace = false;

        // �ϐ���͂��n�܂�����^�ɂ���
        boolean start = false;

        // ���� $ ��ǂݏo���Ă���̂ŏ����J�E���g��1
        count = 1;

        while (true) {
            int c = readScanner();

            if (!start) {
                if (c == '!') {
                    continue;
                } else if (c == '{') {
                    useBrace = true;
                    start = true;
                    continue;
                } else {
                    start = true;
                }
            }

            if (c == ICharacterScanner.EOF) {
                break;
            }

            if (start) {
                if (isVariableCharacter(c)) {
                    // do nothing
                } else {
                    if (!useBrace) {
                        unreadScanner();
                        attr.add(propertyToken, count);
                        return new Token(attr);
                    } else {
                        if (c == '}') {
                            attr.add(propertyToken, count);
                            return new Token(attr);
                        } else {
                            unreadScanner();
                            attr.add(propertyToken, count);
                            return new Token(attr);
                        }
                    }
                }
            }
            
        }

        for (int i = 0; i < count; i++) {
            scanner.unread();
        }
        return Token.UNDEFINED;
    }

    /**
     * �������ϐ�������Ƃ��ėL�����ǂ�����Ԃ��܂��B
     * @param c ����
     * @return �������ϐ�������Ƃ��ėL���Ȃ�ΐ^
     */
    private boolean isVariableCharacter(int c) {
        return Character.isLetterOrDigit(c) || ("-_.".indexOf(c) >= 0);
    }

    /**
     * �X�L���i����ꕶ���擾���܂��B
     * @param scanner 
     * @return �擾��������
     */
    private int readScanner() {
        ++count;
        return scanner.read();
    }

    /**
     * �X�L���i����ꕶ���߂��܂��B
     * @param scanner 
     */
    private void unreadScanner() {
        --count;
        scanner.unread();
    }

}
