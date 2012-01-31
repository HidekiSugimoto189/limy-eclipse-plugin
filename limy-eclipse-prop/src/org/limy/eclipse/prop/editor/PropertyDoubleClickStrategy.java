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
package org.limy.eclipse.prop.editor;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextViewer;
import org.limy.eclipse.common.LimyEclipsePluginUtils;
import org.limy.eclipse.prop.editor.PropertyDoubleClickStrategy.PosAndType.SelectType;

/**
 * �v���p�e�B�G�f�B�^���Ń_�u���N���b�N�����Ƃ��̏������`�����N���X�ł��B
 * @author Naoki Iwami
 */
public class PropertyDoubleClickStrategy implements ITextDoubleClickStrategy {

    /**
     * �I���J�n�ʒu����ёI����ʂ�\���܂��B
     * @author Naoki Iwami
     */
    static class PosAndType {
        
        /**
         * �I����ʂ�\���܂��B
         * @author Naoki Iwami
         */
        enum SelectType {
            /** name�I�� */
            NAME,
            /** value�I�� */
            VALUE;
        }
        
        /** �Ώە����ʒu */
        private int targetPos;
        
        /** �I����� */
        private SelectType type;

        protected int getTargetPos() {
            return targetPos;
        }

        protected SelectType getType() {
            return type;
        }
        
        protected void setTypeAnd(SelectType type, int targetPos) {
            this.type = type;
            this.targetPos = targetPos;
        }
        
    }
    
    // ------------------------ Fields

    /** ���ݏ������̃e�L�X�g�r���[�A */
    private ITextViewer viewer;
    
    // ------------------------ Implement Methods

    public void doubleClicked(ITextViewer viewer) {

        this.viewer = viewer;

        try {
            
            if (initCheck(viewer)) {
                return;
            }
            
            PosAndType posType = decideSelectType();
            
            if (posType.getType() == SelectType.VALUE) {
                // value�I��
                selectTextValue(posType.getTargetPos());
            } else {
                // name�I��
                selectTextName(posType.getTargetPos());
            }
        
        } catch (BadLocationException e) {
            LimyEclipsePluginUtils.log(e);
        }
    }


    // ------------------------ Private Methods

    /**
     * ���ݏ������̃h�L�������g���擾���܂��B
     * @return ���ݏ������̃h�L�������g
     */
    private IDocument getDocument() {
        return viewer.getDocument();
    }
    
    /**
     * �N���b�N�����J�[�\���ʒu��Ԃ��܂��B
     * @return �N���b�N�����J�[�\���ʒu
     */
    private int getSelectedPos() {
        return viewer.getSelectedRange().x; // �N���b�N�����J�[�\���ʒu
    }

    /**
     * �����`�F�b�N���s���܂��B
     * @param viewer ITextViewer
     * @return ���̌�̏������s��Ȃ��ꍇ�� true
     * @throws BadLocationException
     */
    private boolean initCheck(ITextViewer viewer) throws BadLocationException {
        
        int pos = getSelectedPos();
        if (pos >= getDocument().getLength()) {
            return true; // �A�T�[�V���������˂Ĕ͈̓`�F�b�N
        }
        
        if (getDocument().getChar(pos) == '=') {
            // '=' �������N���b�N�����ꍇ�A'=' �݂̂�I�����ďI��
            viewer.setSelectedRange(pos, 1);
            return true;
        }
        
        return false;
    }

    /**
     * �J�[�\�����O�����ɕ�������͂��āA�I���J�n�ʒu�ƑI����ʂ����肵�܂��B
     * @return �I���J�n�ʒu�ƑI�����
     * @throws BadLocationException 
     */
    private PosAndType decideSelectType() throws BadLocationException {
        
        IDocument doc = getDocument();

        PosAndType posType = new PosAndType();

        int posMark = -1; // �u=�v�L�������������ʒu
        boolean flagPrevLine = false; // ��͂��O�s�ɓ˓���������� true �ɂ���

        // �J�[�\���ʒu����O�����Ɍ���
        for (int offset = getSelectedPos(); offset >= 0; offset--) {
            char c = doc.getChar(offset);
            int line = doc.getLineOfOffset(offset); // �s�ԍ����擾

            if (flagPrevLine) {
                // �O�s�ɉ�͂��˓���������A�����ɗ���
                if (c == '\\') {
                    // �O�s�̍s���� '\' �ŏI�����Ă����ꍇ�A����ɉ�͂𑱂���
                    flagPrevLine = false;

                    // �����ȑO�̉�͂� '=' ���������Ă����Ƃ�����A�����value�̈ꕔ�ƌ��Ȃ�
                    posMark = -1;

                } else if (!Character.isWhitespace(c)) { // �X�y�[�X�����͖���
                    // �O�s�̍s���� '\' ��X�y�[�X�����ȊO�������ꍇ�A������͂�����K�v�͖����̂ŏI��
                    if (posMark >= 0) {
                        // '=' ���������Ă���ꍇ�Avalue�I��
                        posType.setTypeAnd(SelectType.VALUE, posMark + 1);
                    } else {
                        // '=' ���������Ă��Ȃ��ꍇ�Aname�I��
                        posType.setTypeAnd(SelectType.NAME, doc.getLineOffset(line + 1));
                    }
                    break;
                }
            } else {
                if (c == '=') {
                    posMark = offset; // �u=�v�L�������������B���̏ꍇ�A���ݑI�𒆂̈ʒu�́u=�v�ȍ~
                }
            }

            if (doc.getLineOffset(line) == offset) {
                // �s���ɓ��B�����ꍇ�AnewLine�t���O�𗧂Ă�B����ɑO�̍s�܂ŉ�͂͑�����
                flagPrevLine = true;
            }

            checkFirstDocument(offset, posMark, posType);
        }
        return posType;
    }

    /**
     * �h�L�������g�̐擪���`�F�b�N���܂��B
     * @param offset ���݉�͒��̕����ʒu
     * @param posMark '='���������������ʒu
     * @param posType �`�F�b�N���e�i�[�C���X�^���X
     */
    private void checkFirstDocument(int offset, int posMark, PosAndType posType) {
        if (offset == 0) {
            // �h�L�������g�̐擪�܂ŉ�͂��B�����ꍇ
            if (posMark >= 0) {
                posType.setTypeAnd(SelectType.VALUE, posMark + 1);
            } else {
                posType.setTypeAnd(SelectType.NAME, 0);
            }
        }
    }
    
    /**
     * value�������I�����܂��B
     * @param start '='�����̒���
     * @throws BadLocationException 
     */
    private void selectTextValue(int start) throws BadLocationException {
        
        IDocument doc = getDocument();

        boolean isCont = false;
        // �J�[�\���ʒu���������Ɍ���
        for (int offset = getSelectedPos(); offset <= doc.getLength(); offset++) {
            
            if (offset == doc.getLength()) {
                // �h�L�������g�̍Ō�܂ŉ�͂��B�����ꍇ�A�Ō�܂ł�I�����Ċ���
                selectText(start, doc.getLength());
                break;
            }

            char c = doc.getChar(offset);
            int line = doc.getLineOfOffset(offset); // �s�ԍ����擾
            
            if (c == '\\') {
                // '\'���������������ꍇ�AisCont�t���O�𗧂Ă�
                isCont = true;
            } else if (doc.getLineOffset(line) == offset) {
                // �s���̏ꍇ�A�����ɗ���
                if (isCont) {
                    // ���O�̕����� '\' �������ꍇ�A�����s value �Ƃ��ĉ�͂𑱂���
                    isCont = false;
                } else {
                    // ���O�̕����� '\' �ȊO�������ꍇ�A��͏I���Bvalue��I��
                    selectText(start, offset);
                    return;
                }
            } else if (!Character.isWhitespace(c)) {
                // �s���ȊO�� '\' �� isCont �t���O�����ɖ߂�
                isCont = false;
            }
        }
    }

    /**
     * name�������I�����܂��B
     * @param start �s�������ʒu
     * @throws BadLocationException 
     */
    private void selectTextName(int start) throws BadLocationException {
        
        IDocument doc = getDocument();

        // �J�[�\���ʒu���������Ɍ���
        for (int offset = getSelectedPos(); offset <= doc.getLength(); offset++) {
            
            char c = doc.getChar(offset);
            int line = doc.getLineOfOffset(offset); // �s�ԍ����擾
            
            if (c == '=') {
                // '=' �������������͏I���Bname��I��
                selectText(start, offset);
                return;
            } else if (doc.getLineOffset(line) == offset) {
                return;
            }
        }
    }

    /**
     * �e�L�X�g��I�����܂��B
     * @param start �I���J�n�ʒu
     * @param end �I���I���ʒu
     * @throws BadLocationException �|�W�V������O
     */
    private void selectText(int start, int end) throws BadLocationException {
        IDocument doc = getDocument();
        int startPos = start;
        for (; startPos <= end; startPos++) {
            if (!Character.isWhitespace(doc.getChar(startPos))) {
                break;
            }
        }
        int endPos = end;
        for (; endPos > startPos; endPos--) {
            if (!Character.isWhitespace(doc.getChar(endPos - 1))) {
                break;
            }
        }
        viewer.setSelectedRange(startPos, endPos - startPos);
    }

}
