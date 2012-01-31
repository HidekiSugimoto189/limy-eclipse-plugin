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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.limy.eclipse.common.LimyEclipsePluginUtils;
import org.limy.eclipse.web.LimyWebConstants;
import org.limy.eclipse.web.LimyWebPlugin;

/**
 * Velocity�G�f�B�^��Folding�@�\���T�|�[�g����p�[�T�N���X�ł��B
 * @author Naoki Iwami
 */
public class VelocityFoldingParser {
    
    /**
     * �h�L�������g
     */
    private IDocument doc;
    
    /**
     * �����Ŏg�p����|�C���^�l
     */
    private int pos;

    /**
     * �h�L�������g������
     */
    private int max;
    
    /**
     * �֐���folding�L���t���O
     */
    private boolean foldFunctions;

    /**
     * VelocityFoldingParser�C���X�^���X���\�z���܂��B
     * @param doc ��͂���h�L�������g
     */
    public VelocityFoldingParser(IDocument doc) {
        this.doc = doc;
        this.pos = 0;
        this.max = doc.getLength();
        
        foldFunctions = LimyEclipsePluginUtils.getPreferenceBoolean(
                LimyWebPlugin.getDefault().getPreferenceStore(),
                LimyWebConstants.P_FOLDING, true);

    }
    
    /**
     * �h�L�������g����͂���Folding�A�m�e�[�V�����}�b�v��Ԃ��܂��B
     * @return Folding�A�m�e�[�V�����}�b�v
     * @throws BadLocationException �����|�W�V������O
     */
    public Map<ProjectionAnnotation, Position> parseAnnotations() throws BadLocationException {

        // �A�m�e�[�V�����ꗗ(�ԋp�l)
        Map<ProjectionAnnotation, Position> annotations
                = new LinkedHashMap<ProjectionAnnotation, Position>();

        // �u���b�N�̊J�n�ʒu���X�g
        Stack<Integer> startPositions = new Stack<Integer>();

        while (pos < max) {
            char c = doc.getChar(pos);
            switch (c) {
            case '{':
                startPositions.push(Integer.valueOf(pos));
                break;
            case '}':
                if (startPositions.size() == 1) {
                    int startPos = startPositions.pop().intValue();
                    int lineDiff = doc.getLineOfOffset(pos - 1) - doc.getLineOfOffset(startPos);
                    // ����s�̊��ʂ�Folding���Ȃ�
                    if (lineDiff > 0) {
                        ProjectionAnnotation annotation = new ProjectionAnnotation(foldFunctions);
                        Position position = new Position(startPos, pos - startPos);
                        annotations.put(annotation, position);
                    }
                } else if (startPositions.size() == 0) {
                    // �����ʂ��o�����Ă��Ȃ��̂ɉE���ʂ��o��������A��������
                } else {
                    // ���O�̍����ʏ���p��
                    startPositions.pop();
                }
                break;
            default:
                break;
            }
            ++pos;
        }
        return groupAnnotations(annotations);
    }

    /**
     * �A������A�m�e�[�V��������ɂ܂Ƃ߂܂��B
     * @param annotations �A�m�e�[�V�����ꗗ�i�����t�j
     * @return �܂Ƃ߂��A�m�e�[�V�����ꗗ
     * @throws BadLocationException �����|�W�V������O
     */
    private Map<ProjectionAnnotation, Position> groupAnnotations(
            Map<ProjectionAnnotation, Position> annotations) throws BadLocationException {
        
        // �ԋp�l
        Map<ProjectionAnnotation, Position> results
                = new LinkedHashMap<ProjectionAnnotation, Position>();
        
        ProjectionAnnotation lastAnnotation = null;
        for (Map.Entry<ProjectionAnnotation, Position> entry : annotations.entrySet()) {
            ProjectionAnnotation entryKey = entry.getKey();
            Position entryValue = entry.getValue();
            
            if (results.isEmpty()) {
                results.put(entryKey, entryValue);
                lastAnnotation = entryKey;
            } else {
                Position position = annotations.get(lastAnnotation);
                int endPos = position.offset + position.length;
                int lastLine = doc.getLineOfOffset(endPos) + 1;
                int nowLine = doc.getLineOfOffset(entryValue.offset);
                while (lastLine < nowLine) {
                    int offset = doc.getLineOffset(lastLine);
                    char c = doc.getChar(offset);
                    if (" \t/\n\r".indexOf(c) < 0) {
                        break;
                    }
                    ++lastLine;
                }
                if (lastLine == nowLine) {
                    // �A�������A�m�e�[�V�����͂܂Ƃ߂�
                    position.length = entryValue.offset + entryValue.length - position.offset;
                    annotations.put(lastAnnotation, position);
                } else {
                    results.put(entryKey, entryValue);
                    lastAnnotation = entryKey;
                }
            }
        }
        
        return results;
    }
    
}
