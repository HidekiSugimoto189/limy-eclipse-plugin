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

import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;

/**
 * Velocity�֘A�̃��[�e�B���e�B�N���X�ł��B
 * @author Naoki Iwami
 */
public final class VelocityUtils {
    
    /** ���e�������� */
    private static final String LITERAL_CHARS = "_/\\";
    
    /**
     * private constructor
     */
    private VelocityUtils() { }

    /**
     * �h�L�������g����Folding�ΏۂƂȂ�v�f���擾���܂��B
     * @param doc �h�L�������g
     * @return Folding�Ώۃ}�b�v
     * @throws BadLocationException �����|�W�V������O
     */
    public static Map<ProjectionAnnotation, Position> getFoldingAnnotations(IDocument doc)
            throws BadLocationException {
        
        Map<ProjectionAnnotation, Position> results
                = new VelocityFoldingParser(doc).parseAnnotations();
        if (!results.isEmpty()) {
            int offset = results.entrySet().iterator().next().getValue().getOffset();
            if (doc.getLineOfOffset(offset) == 0) {
                // �t�@�C���擪����Folding�̏ꍇ�AJS��p�t�@�C���ł���Ƃ݂Ȃ�Folging��OFF�ɂ���
                for (Entry<ProjectionAnnotation, Position> entry : results.entrySet()) {
                    entry.getKey().markExpanded();
                }
            }
        }
        return results;
    }

    /**
     * �w�肵���ʒu�ɂ��郊�e�����iURL�Ȃǁj�̈ʒu���擾���܂��B
     * @param doc �h�L�������g
     * @param offset �h�L�������g���̕����I�t�Z�b�g
     * @return �P��̈ʒu
     * @throws BadLocationException �����|�W�V������O
     */
    public static Position getLiteralPosition(IDocument doc, int offset)
            throws BadLocationException {
        
        int pos = offset;
        char c = doc.getChar(pos);
        if (!isLiteral(c)) {
            return null;
        }

        Position r = new Position(0);
        for (pos = pos - 1; pos >= 0 && isLiteral(doc.getChar(pos)); --pos) {
            // empty
        }
        r.setOffset(++pos);

        for (pos = offset + 1; pos < doc.getLength(); ++pos) {
            if (!isLiteral(doc.getChar(pos))) {
                break;
            }
        }
        r.setLength(pos - r.getOffset());
        return r;
    }

    /**
     * �w�肵���ʒu�ɂ���P��i�p��������уA���_�[���C���j�̈ʒu���擾���܂��B
     * @param doc �h�L�������g
     * @param offset �h�L�������g���̕����I�t�Z�b�g
     * @return �P��̈ʒu
     * @throws BadLocationException �����|�W�V������O
     */
    public static Position getWordPosition(IDocument doc, int offset) throws BadLocationException {
        int pos = offset;
        char c = doc.getChar(pos);
        if (!isFunctionLiteral(c)) {
            return null;
        }

        Position r = new Position(0);
        for (pos = pos - 1; pos >= 0 && isFunctionLiteral(doc.getChar(pos)); --pos) {
            // empty
        }
        r.setOffset(++pos);

        for (pos = offset + 1; pos < doc.getLength(); ++pos) {
            if (!isFunctionLiteral(doc.getChar(pos))) {
                break;
            }
        }
        r.setLength(pos - r.getOffset());
        return r;
    }

    /**
     * ������Velocity�֐����Ƃ��ėL�����ǂ�����Ԃ��܂��B
     * @param c ����
     * @return �֐����Ƃ��ėL���Ȃ�ΐ^
     */
    private static boolean isFunctionLiteral(char c) {
        return Character.isLetterOrDigit(c) || c == '_';
    }

    /**
     * ���������e�����Ƃ��ėL�����ǂ�����Ԃ��܂��B
     * @param c ����
     * @return ���e�����Ƃ��ėL���Ȃ�ΐ^
     */
    private static boolean isLiteral(char c) {
        return Character.isLetterOrDigit(c) || (LITERAL_CHARS.indexOf(c) >= 0);
    }

}
