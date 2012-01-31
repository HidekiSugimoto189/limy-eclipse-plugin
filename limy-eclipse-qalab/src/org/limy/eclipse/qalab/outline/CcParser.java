/*
 * Created 2007/03/03
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
package org.limy.eclipse.qalab.outline;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.limy.eclipse.qalab.outline.asm.LineInfo;
import org.limy.eclipse.qalab.outline.asm.MethodInfo;

import antlr.collections.AST;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

/**
 * @author Naoki Iwami
 */
public class CcParser {
    
    
    // ------------------------ Fields

    /** ���݂̃J�E���g */
    private int count;

    /** �Ώۃ��\�b�h�� */
    private final String methodName;

    /** �Ώۃ��\�b�h�V�O�j�`�� */
    private final String methodSignature;
    
    // ------------------------ Constructors

    /**
     * CcParser �C���X�^���X���\�z���܂��B
     * @param methodName �Ώۃ��\�b�h��
     * @param methodSignature �Ώۃ��\�b�h�V�O�j�`��
     */
    public CcParser(String methodName, String methodSignature) {
        super();
        this.methodName = methodName;
        this.methodSignature = methodSignature;
    }
    
    // ------------------------ Public Methods

    /**
     * ���\�b�h�����쐬���܂��B
     * @param sourceAst �\�[�X��͏��iCheckstyle��TreeWalker�g�p�j
     * @return ���\�b�h���
     */
    public MethodInfo createMethodInfo(AST sourceAst) {
        
        // �f�o�b�O�o��
        ASTUtils.debugSourceAst(sourceAst);

        // ���\�b�h��������
        AST methodAst = ASTUtils.searchMethodFromSource(sourceAst, 
                methodName, methodSignature);
        
        // �q�v�f���烁�\�b�h���������i����A�K�v�H�j
        AST sibling = sourceAst.getNextSibling();
        while (methodAst == null && sibling != null) {
            methodAst = ASTUtils.searchMethodFromSource(sibling, methodName, methodSignature);
            sibling = sibling.getNextSibling();
        }
        
        // ���\�b�h�v�f����SLIST�v�f������
        AST mainBlockAst = ASTUtils.search(methodAst, TokenTypes.SLIST);
        
        List<LineInfo> lineInfos = new ArrayList<LineInfo>();
        
        // ���C���u���b�N����͂��ă��C�������쐬
        createInfos(mainBlockAst, lineInfos);
        
        if (lineInfos.size() > 1) {
            // ���򂪈�ł��������ꍇ�A����_�ւ̃��C�����͍폜
            for (ListIterator<LineInfo> it = lineInfos.listIterator(); it.hasNext();) {
                LineInfo lineInfo = it.next();
                if (lineInfo.getFrom() == lineInfo.getTo()) {
                    it.remove();
                }
            }
        }
        
        return new MethodInfo(methodName, null, lineInfos);
    }

    // ------------------------ Private Methods

    /**
     * ���[�g�v�f����͂��� lineInfos �Ƀ��C������ǉ����܂��B
     * @param ast SLIST���[�g�v�f
     * @param lineInfos ���C�����i�[��
     */
    private void createInfos(AST ast, List<LineInfo> lineInfos) {
        
        // ���[�g�v�f�̒�����AELSE�ȊO�̗v�f��S�ă��X�g�A�b�v
        AST[] targetAsts = ASTUtils.searchMultiWithSiblingWithEx(ast, TokenTypes.LITERAL_ELSE,
                TokenTypes.LITERAL_IF,
                TokenTypes.LITERAL_WHILE, TokenTypes.LITERAL_FOR, TokenTypes.LITERAL_DO,
                TokenTypes.LITERAL_SWITCH,
                TokenTypes.LITERAL_TRY,
                TokenTypes.LAND, TokenTypes.LOR, TokenTypes.QUESTION);
        
        if (targetAsts.length == 0) {
            // ���������Ȃ�������A���C�����Ɏ��g��ǉ�
            appendLineInfo(lineInfos, count, count, 1);
            ++count;
        } else {
            // ��ȏ㌩��������A���C�����Ɍ��������S�Ă�ǉ�
            for (AST targetAst : targetAsts) {
                int fromCount = count++;
                
                int type = targetAst.getType();
                if (type == TokenTypes.LITERAL_TRY) {
                    taskTry(lineInfos, targetAst, fromCount);
                } else if (type == TokenTypes.LAND || type == TokenTypes.LOR
                        || type == TokenTypes.QUESTION) {
                    taskAnd(lineInfos, targetAst, fromCount);
                } else if (type == TokenTypes.LITERAL_SWITCH) {
                    taskSwitch(lineInfos, targetAst, fromCount);
                } else {
                    // if,for,while
                    taskIf(lineInfos, targetAst, fromCount);
                }
            }
        }
        
    }

    /**
     * AND���[�g�v�f����͂��ă��C�����ɒǉ����܂��B
     * @param lineInfos ���C�����i�[��
     * @param ast AND���[�g�v�f
     * @param fromCount FROM�ʒu
     */
    private void taskAnd(List<LineInfo> lineInfos, AST ast, int fromCount) {
        
        // ��������C�������쐬
        List<LineInfo> childInfos = new ArrayList<LineInfo>();
        createInfos(ast.getFirstChild(), childInfos);
        
        // FROM -> ������擪�v�f�܂ł̐����쐬
        appendLineInfoWithLineNumber(lineInfos, fromCount, childInfos.get(0).getFrom(), 1, ast);
        lineInfos.addAll(childInfos);
        
        // ������ŏI�v�f -> ���ݗv�f�܂ł̐����쐬
        appendLineInfoWithLineNumber(lineInfos,
                childInfos.get(childInfos.size() - 1).getTo(), count,
                1, ast);
        
        // FROM -> ���ݗv�f�܂ł̐����쐬�B���ꂪ�A���򂪖����ꍇ�̃��C�����[�g
        appendLineInfoWithLineNumber(lineInfos, fromCount, count, 1, ast);
    }

    private void taskTry(List<LineInfo> lineInfos, AST ast, int fromCount) {
        
        AST mainAst = ASTUtils.search(ast, TokenTypes.SLIST);
        AST[] catchAsts = ASTUtils.searchMultiAllSibling(ast, TokenTypes.LITERAL_CATCH);
        
        List<Integer> lastLines = new ArrayList<Integer>();
        
        List<LineInfo> mainLineInfos = new ArrayList<LineInfo>();
        createInfos(mainAst, mainLineInfos);
        lastLines.add(Integer.valueOf(mainLineInfos.get(mainLineInfos.size() - 1).getTo()));
        
        appendLineInfoWithLineNumber(lineInfos, fromCount, mainLineInfos.get(0).getFrom(), 1, ast);
        lineInfos.addAll(mainLineInfos);
        
        for (AST catchAst : catchAsts) {
            List<LineInfo> catchLineInfos = new ArrayList<LineInfo>();
            createInfos(catchAst, catchLineInfos);
            appendLineInfoWithLineNumber(
                    lineInfos, fromCount, catchLineInfos.get(0).getFrom(), 1, ast);
            lastLines.add(Integer.valueOf(catchLineInfos.get(catchLineInfos.size() - 1).getTo()));
            lineInfos.addAll(catchLineInfos);
        }
        
        for (Integer lineNumber : lastLines) {
            appendLineInfoWithLineNumber(lineInfos, lineNumber.intValue(), count, 1, ast);
        }

    }

    private void taskSwitch(List<LineInfo> lineInfos, AST ast, int fromCount) {
        
        AST[] caseGroupAsts = ASTUtils.searchMultiAllSibling(ast, TokenTypes.CASE_GROUP);
        
        List<Integer> lastLines = new ArrayList<Integer>();

        boolean isDefault = false;
        for (AST caseGroupAst : caseGroupAsts) {
            List<LineInfo> caseInfos = new ArrayList<LineInfo>();
            isDefault |= taskCase(caseInfos, caseGroupAst, fromCount);
            lastLines.add(Integer.valueOf(caseInfos.get(caseInfos.size() - 1).getTo()));
            lineInfos.addAll(caseInfos);
        }
        
        if (!isDefault) {
            // �����I��default�߂����݂��Ȃ��ꍇ
            appendLineInfoWithLineNumber(lineInfos, fromCount, count, 100, ast);
            lastLines.add(Integer.valueOf(count));
        }
        
        for (Integer lineNumber : lastLines) {
            appendLineInfo(lineInfos, lineNumber.intValue(), count, 1);
        }
    
    }

    private boolean taskCase(List<LineInfo> lineInfos, AST ast, int fromCount) {
        AST[] caseAsts = ASTUtils.searchMultiAllSibling(ast, TokenTypes.LITERAL_CASE);
        if (caseAsts.length == 0) {
            caseAsts = ASTUtils.searchMultiAllSibling(ast, TokenTypes.LITERAL_DEFAULT);
        }
        
        int mainCount = count + caseAsts.length;
        for (AST caseAst : caseAsts) {
            appendLineInfoWithLineNumber(lineInfos, fromCount, count, 1, ast);
            appendLineInfoWithLineNumber(lineInfos, count, mainCount, 1, ast);
            ++count;
        }
        
        AST slistAst = ASTUtils.search(ast, TokenTypes.SLIST);
        createInfos(slistAst, lineInfos);
        
        return ASTUtils.search(ast, TokenTypes.LITERAL_DEFAULT) != null;
    }

    private void taskIf(List<LineInfo> lineInfos, AST ast, int fromCount) {
        
        // �������̒��𒲂ׂ�i&& �� ||�j
        List<LineInfo> exprInfos = new ArrayList<LineInfo>();
        AST exprAst = ASTUtils.search(ast, TokenTypes.EXPR);
        createInfos(exprAst.getFirstChild(), exprInfos);
        
        // ���g����������̐擪�ւ̃��C���iA->I0�j
        appendLineInfo(lineInfos, fromCount, exprInfos.get(0).getFrom(), 1);
        lineInfos.addAll(exprInfos);
        
        List<LineInfo> mainInfos = new ArrayList<LineInfo>();
        AST innerAst = ASTUtils.search(ast, TokenTypes.RPAREN); // if (...) �̉E���ʂ����o
        if (exprInfos.size() > 1) {
            ++count; // ���������ŕ��򂪂������ꍇ�����J�E���g����
        }
        createInfos(innerAst, mainInfos);
        
        // �������̍Ōォ��if�u���b�N�̐擪�ւ̃��C���iI1->B0�j
        int newFromCount = exprInfos.get(exprInfos.size() - 1).getTo();
        appendLineInfo(lineInfos, newFromCount, mainInfos.get(0).getFrom(), 1);
        lineInfos.addAll(mainInfos);

        // else�u���b�N������
        AST elseAst = ASTUtils.searchSelfAndSiblings(innerAst, TokenTypes.LITERAL_ELSE);
        if (elseAst != null) {
            // else�u���b�N�����݂���ꍇ
            
            ++count;
            List<LineInfo> elseInfos = new ArrayList<LineInfo>();
            createInfos(elseAst.getFirstChild(), elseInfos);

            // �������̍Ōォ��else�u���b�N�̐擪�ւ̃��C���iA->E0�j
            appendLineInfoWithLineNumber(lineInfos,
                    newFromCount, elseInfos.get(0).getFrom(), 1, ast);
            lineInfos.addAll(elseInfos);
            
            // if�u���b�N�̍Ōォ�璼��̃|�C���g�ւ̃��C���iB1->C�j
            appendLineInfo(lineInfos, mainInfos.get(mainInfos.size() - 1).getTo(), count,
                    1);

            // else�u���b�N�̍Ōォ�璼��̃|�C���g�ւ̃��C���iE1->C�j
            appendLineInfo(lineInfos, elseInfos.get(elseInfos.size() - 1).getTo(), count,
                    1);

        } else {
            // if�u���b�N�̍Ōォ�璼��̃|�C���g�ւ̃��C���iB1->C�j
            appendLineInfo(lineInfos, mainInfos.get(mainInfos.size() - 1).getTo(), count,
                    1);

            // ���g���璼��̃|�C���g�ւ�(�Ö�else)���C���iA->C�j
            appendLineInfoWithLineNumber(lineInfos, newFromCount, count, 100, ast);
        }
    }

    /**
     * ���C������ǉ����܂��B�s�ԍ��t��
     * @param lineInfos ���C�����ǉ���
     * @param from FROM�ʒu
     * @param to TO�ʒu
     * @param weight �E�F�C�g
     * @param ast ���[�g�v�f
     */
    private void appendLineInfoWithLineNumber(List<LineInfo> lineInfos, int from,
            int to, int weight, AST ast) {
        
        int lineNumber = ((DetailAST)ast).getLineNo();
        String text = "[L." + lineNumber + "]";
        lineInfos.add(new LineInfo(from, to, weight, lineNumber, text));
    }

    /**
     * ���C������ǉ����܂��B
     * @param lineInfos ���C�����ǉ���
     * @param from FROM�ʒu
     * @param to TO�ʒu
     * @param weight �E�F�C�g
     */
    private void appendLineInfo(List<LineInfo> lineInfos, int from,
            int to, int weight) {
        
        lineInfos.add(new LineInfo(from, to, weight, 0, "."));
    }

}
