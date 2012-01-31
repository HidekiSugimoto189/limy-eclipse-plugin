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
 * Velocity(#�L��)�̌��o���[�����߂��N���X�ł��B
 * @author Naoki Iwami
 */
public class VelocitySharpRule extends AbstractDollarRule {

    /**
     * ���o�����[�h��\���񋓌^�ł��B
     */
    enum Mode {
        /** �������[�h */
        DEFAULT,
        /** �P�s�R�����g���[�h */
        SINGLE_COMMENT,
        /** �����s�R�����g���[�h */
        MULTI_COMMENT,
        /** �L�[���[�h�������[�h */
        KEYWORD_INNER;
    }

    /**
     * ���ʖ��L�[���[�h�ꗗ
     */
    private static final String[] KEYWORDS = new String[] {
        "else",
        "end",
    };

    // ------------------------ Fields

    /**
     * �R�����g������̑���
     */
    private TextAttribute commentToken;

    /**
     * �L�[���[�h������̑���
     */
    private TextAttribute keywordToken;

    /**
     * �L�[���[�h����������̑���
     */
    private TextAttribute keywordInnerToken;

    // ------------------------ Constructors

    /**
     * VelocitySharpRule�C���X�^���X���\�z���܂��B
     * @param commentToken
     * @param keywordToken
     * @param keywordInnerToken
     * @param propertyToken
     */
    public VelocitySharpRule(TextAttribute commentToken, TextAttribute keywordToken,
            TextAttribute keywordInnerToken, TextAttribute propertyToken) {
        
        super(new VelocityDollarRule(propertyToken));
        
        this.commentToken = commentToken;
        this.keywordToken = keywordToken;
        this.keywordInnerToken = keywordInnerToken;
    }
    
    // ------------------------ Implement Methods

    public IToken evaluate(ICharacterScanner scanner) {
        int c = scanner.read();
        if (c == '#') {
            setScanner(scanner);
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
        StringBuffer buff = new StringBuffer();
        Mode mode = Mode.DEFAULT;

        // �L�[���[�h�J�E���^
        int charCount = 0;

        // ���ʐ�(������ - �E����)
        int braceCount = 0;

        // ���� # ��ǂݏo���Ă���̂ŏ����J�E���g��1
        setCount(1);

        while (true) {
            int c = readScanner();

            mode = getAfterMode(c, mode);

            if (mode == Mode.SINGLE_COMMENT) {
                // 1�s�R�����g
                if (c == ICharacterScanner.EOF || c == '\n' || c == '\r') {
                    attr.add(commentToken, getCount());
                    return new Token(attr);
                }
            } else if (mode == Mode.MULTI_COMMENT) {
                // �����s�R�����g
                switch (charCount) {
                case 0:
                    if (c == '*') {
                        charCount = 1;
                    }
                    break;
                case 1:
                    if (c == '#') {
                        attr.add(commentToken, getCount());
                        return new Token(attr);
                    }
                    charCount = 0;
                    break;
                default:
                    break;
                }
            } else if (mode == Mode.DEFAULT) {
                // �L�[���[�h���O
                if (c == '(') {
                    // ( �̑O�܂ł�o�^
                    attr.add(keywordToken, getCount() - 1);
                    ++braceCount;
                    mode = Mode.KEYWORD_INNER;
                } else {
                    buff.append((char)c);
                    for (String keyword : KEYWORDS) {
                        if (keyword.length() == buff.length()
                            && keyword.equals(buff.toString())) {
                            // ���ʖ��̃L�[���[�h�����o���ꂽ�炻�̏�ŉ�͏I��
                            attr.add(keywordToken, getCount());
                            return new Token(attr);
                        }
                    }
                }
            } else /* if (mode == Mode.KEYWORD_INNER) */ {
                // �L�[���[�h����
                if (c == '(') {
                    ++braceCount;
                } else if (c == ')') {
                    --braceCount;
                    if (braceCount == 0) {
                        // ���ʂ����I��������͏I��
                        attr.add(keywordInnerToken, getCount());
                        return new Token(attr);
                    }
                } else {
                    if (c == '$') {
                        // ��͒���$�L�����������ꂽ��A$RULE�ɏ������ς˂�
                        evaluateDollar(attr);
                    }
                }
            }
            if (c == ICharacterScanner.EOF) {
                unreadAll();
                return Token.UNDEFINED;
            }
        }
    }

    /**
     * �����ɂ���ă��[�h��ύX���ĕԂ��܂��B
     * @param c ����
     * @param mode ���݂̃��[�h
     * @return �ύX��̃��[�h
     */
    private Mode getAfterMode(int c, Mode mode) {
        if (getCount() == 2) {
            if (c == '#') {
                return Mode.SINGLE_COMMENT;
            }
            if (c == '*') {
                return Mode.MULTI_COMMENT;
            }
        }
        return mode;
    }

    /**
     * $���������͂��܂��B
     * @param attr �e�L�X�g����
     */
    private void evaluateDollar(MultiTextAttribute attr) {
        // $ �̑O�܂ł�o�^
        attr.add(keywordInnerToken, getCount() - 1);
        doEvaluateDollar(attr);
    }
    
}
