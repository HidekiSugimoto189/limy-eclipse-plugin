/*
 * Created 2008/08/23
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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

import antlr.collections.AST;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

/**
 * AST�֘A�̃��[�e�B���e�B�N���X�ł��B
 * @author Naoki Iwami
 */
public final class ASTUtils {

    // ------------------------ Constants

    /** �v���~�e�B�u�^ => �V�O�j�`�������ւ̕ϊ��}�b�v */
    private static final Map<Integer, Character> PRIMITIVE_MAP;
    
    static {
        PRIMITIVE_MAP = new HashMap<Integer, Character>();
        PRIMITIVE_MAP.put(Integer.valueOf(TokenTypes.LITERAL_BOOLEAN), Character.valueOf('Z'));
        PRIMITIVE_MAP.put(Integer.valueOf(TokenTypes.LITERAL_BYTE), Character.valueOf('B'));
        PRIMITIVE_MAP.put(Integer.valueOf(TokenTypes.LITERAL_CHAR), Character.valueOf('C'));
        PRIMITIVE_MAP.put(Integer.valueOf(TokenTypes.LITERAL_DOUBLE), Character.valueOf('D'));
        PRIMITIVE_MAP.put(Integer.valueOf(TokenTypes.LITERAL_FLOAT), Character.valueOf('F'));
        PRIMITIVE_MAP.put(Integer.valueOf(TokenTypes.LITERAL_INT), Character.valueOf('I'));
        PRIMITIVE_MAP.put(Integer.valueOf(TokenTypes.LITERAL_SHORT), Character.valueOf('S'));
        PRIMITIVE_MAP.put(Integer.valueOf(TokenTypes.LITERAL_LONG), Character.valueOf('J'));
        PRIMITIVE_MAP.put(Integer.valueOf(TokenTypes.ARRAY_DECLARATOR), Character.valueOf('['));
    }
    
    /**
     * private constructor
     */
    private ASTUtils() { }

    /**
     * �\�[�X���[�g�v�f���烁�\�b�h�v�f���������ĕԂ��܂��B
     * @param sourceAst �\�[�X���[�g�v�f
     * @param method ���\�b�h
     * @return�@���\�b�h�v�f
     * @throws JavaModelException 
     */
    public static AST searchMethodFromSource(AST sourceAst, IMethod method)
            throws JavaModelException {
        
        String methodName = method.getElementName();
        String methodSignature = method.getSignature();
        
        // ���[�g�v�f����Ώۃ��\�b�h�������B������΂����Ԃ�
        AST result = ASTUtils.searchTargetMethod(sourceAst, methodName, methodSignature);
        if (result != null) {
            return result;
        }
        
        // �S�Ă̎q�v�f����Ώۃ��\�b�h�������B������΂����Ԃ�
        AST sibling = sourceAst.getNextSibling();
        while (sibling != null && result == null) {
            result = ASTUtils.searchTargetMethod(sibling, methodName, methodSignature);
            sibling = sibling.getNextSibling();
        }
        return result;
    }

    /**
     * �\�[�X���[�g�v�f���烁�\�b�h�v�f���������ĕԂ��܂��B
     * @param sourceAst �\�[�X���[�g�v�f
     * @param methodName �������郁�\�b�h��
     * @param methodSignature �������郁�\�b�h�V�O�j�`��
     * @return�@���\�b�h�v�f
     */
    public static AST searchMethodFromSource(AST sourceAst,
            String methodName, String methodSignature) {
        
        // ���[�g�v�f����Ώۃ��\�b�h�������B������΂����Ԃ�
        AST result = ASTUtils.searchTargetMethod(sourceAst, methodName, methodSignature);
        if (result != null) {
            return result;
        }
        
        // �S�Ă̎q�v�f����Ώۃ��\�b�h�������B������΂����Ԃ�
        AST sibling = sourceAst.getNextSibling();
        while (sibling != null && result == null) {
            result = ASTUtils.searchTargetMethod(sibling, methodName, methodSignature);
            sibling = sibling.getNextSibling();
        }
        return result;
    }

    /**
     * �\�[�X���[�g�v�f���烁�\�b�h�v�f���������ĕԂ��܂��B
     * @param sourceAst �\�[�X���[�g�v�f
     * @param methodName �������郁�\�b�h��
     * @param methodSignature �������郁�\�b�h�V�O�j�`��
     * @return ���\�b�h�v�f
     */
    private static AST searchTargetMethod(AST sourceAst,
            String methodName, String methodSignature) {
        
        // OBJBLOCK�v�f��T���B���ꂪ�N���X�v�f
        AST mainAst = ASTUtils.search(sourceAst, TokenTypes.OBJBLOCK);
        if (mainAst == null) {
            return null;
        }
        
        // METHOD_DEF�v�f�i�����j��T���B���ꂪ�N���X�Ɋ܂܂�郁�\�b�h�v�f
        AST[] targetAsts = ASTUtils.searchMultiAllSibling(mainAst,
                TokenTypes.METHOD_DEF, TokenTypes.CTOR_DEF);
        for (AST targetAst : targetAsts) {
            String name = ASTUtils.getIdent(targetAst); // ���\�b�h���̂��擾
            if (!methodName.equals(name)) {
                continue;
            }
            
            // ���\�b�h���̂���уV�O�j�`�����A�t�B�[���h�Ɋi�[�����l�Ɠ�������΂����Ԃ��B
            String paramSignature = ASTUtils.getMethodParamSignature(targetAst);
            if (methodSignature.startsWith(paramSignature)) {
                return targetAst;
            }
        }
        return null;
    }

    /**
     * ���[�g�v�f�i����юq�E���j�̒�����A�w�肵���^�C�v�̗v�f��T���ĕԂ��܂��B
     * @param ast ���[�g�v�f
     * @param type �����^�C�v�BTokenTypes
     * @return ���������v�f
     */
    public static AST search(AST ast, int type) {
        if (ast.getType() == type) {
            return ast; // ���[�g�v�f���̂��̂��w�肵���^�C�v�������ꍇ
        }
        
        // �S�Ă̎q�i����ё��j�v�f������
        AST child = ast.getFirstChild();
        while (child != null) {
            AST result = search(child, type);
            if (result != null) {
                return result;
            }
            child = child.getNextSibling();
        }
        return null;
    }

    /**
     * ���g����т��̌Z��v�f�̒�����A�w�肵���^�C�v�̗v�f��T���ĕԂ��܂��B
     * @param ast ���[�g�v�f
     * @param type �����^�C�v�BTokenTypes
     * @return ���������v�f
     */
    public static AST searchWithSibling(AST ast, int type) {
        if (ast.getType() == type) {
            return ast; // ���[�g�v�f���̂��̂��w�肵���^�C�v�������ꍇ
        }
        
        // �S�Ă̌Z��v�f������
        AST child = ast.getNextSibling();
        while (child != null) {
            AST result = search(child, type);
            if (result != null) {
                return result;
            }
            child = child.getNextSibling();
        }
        return null;
    }

    /**
     * �����̎q�v�f�̒�����A�w�肵���^�C�v�̗v�f��T���ĕԂ��܂��B
     * @param ast ���[�g�v�f
     * @param type �����^�C�v�BTokenTypes
     * @return
     */
    public static AST searchOnlySibling(AST ast, int type) {
        AST sibling = ast.getFirstChild();
        while (sibling != null) {
            if (sibling.getType() == type) {
                return sibling;
            }
            sibling = sibling.getNextSibling();
        }
        return null;
    }

    /**
     * �����̎q�v�f�̒�����A�w�肵���^�C�v�̗v�f��T���ĕԂ��܂��B
     * @param ast ���[�g�v�f
     * @param type �����^�C�v�BTokenTypes
     * @return
     */
    public static AST[] searchOnlySiblings(AST ast, int type) {
        Collection<AST> results = new ArrayList<AST>();
        AST sibling = ast.getFirstChild();
        while (sibling != null) {
            if (sibling.getType() == type) {
                results.add(sibling);
            }
            sibling = sibling.getNextSibling();
        }
        return results.toArray(new AST[results.size()]);
    }

    /**
     * ���g�Ƃ��̌Z��v�f�̒�����A�w�肵���^�C�v�̗v�f��T���ĕԂ��܂��B
     * @param ast ���[�g�v�f
     * @param type �����^�C�v�BTokenTypes
     * @return
     */
    public static AST searchSelfAndSiblings(AST ast, int type) {
        if (ast.getType() == type) {
            return ast;
        }
        AST sibling = ast.getNextSibling();
        while (sibling != null) {
            if (sibling.getType() == type) {
                return sibling;
            }
            sibling = sibling.getNextSibling();
        }
        return null;
    }

    /**
     * ���[�g�v�f����A�w�肵���^�C�v�i���������O�^�C�v�łȂ����́j��S�ĒT���܂��B
     * @param ast ���[�g�v�f
     * @param excludeType ���O�^�C�v�i���[�g�����̎q�v�f�ɂ̂ݓK�p�j
     * @param types �Ώۃ^�C�v
     * @return ���������v�f�ꗗ
     */
    public static AST[] searchMultiWithSiblingWithEx(AST ast, int excludeType, int... types) {
        Collection<AST> results = new ArrayList<AST>();
        
        // ���[�g�v�f����T��
        results.addAll(Arrays.asList(searchMultiAllSibling(ast, types)));
        
        // �q�v�f����T��
        AST sibling = ast.getNextSibling();
        while (sibling != null) {
            if (sibling.getType() == excludeType) {
                break;
            }
            results.addAll(Arrays.asList(searchMultiAllSibling(sibling, types)));
            sibling = sibling.getNextSibling();
        }
        
        return results.toArray(new AST[results.size()]);
    }

    /**
     * ���[�g�v�f�̒�����A�w�肵���^�C�v�̗v�f�i�����j��T���ĕԂ��܂��B
     * @param ast ���[�g�v�f
     * @param types �^�C�v�i�����j�BTokenTypes
     * @return ���������v�f
     */
    public static AST[] searchMultiAllSibling(AST ast, int... types) {
        Collection<AST> results = new ArrayList<AST>();
        for (int type : types) {
            // ���[�g�v�f���w�肵���^�C�v�̂����̂����ꂩ��������A���[�g�v�f�����̂܂ܕԂ�
            if (ast.getType() == type) {
                return new AST[] { ast };
            }
        }
        
        // �S�Ă̎q�i����ё��j�v�f������
        AST child = ast.getFirstChild();
        while (child != null) {
            searchMultiAndStore(results, child, types);
            child = child.getNextSibling();
        }
        return results.toArray(new AST[results.size()]);
    }

    /**
     * ���[�g�v�f�̒�����A�w�肵���^�C�v�̗v�f�i�����j��T���ĕԂ��܂��B���������O�^�C�v������
     * @param ast ���[�g�v�f
     * @param excludeTypes ���O�^�C�v�ꗗ�B�K���\�[�g�ςł��邱��
     * @param types �^�C�v�i�����j�BTokenTypes
     * @return ���������v�f
     */
    public static AST[] searchMultiAllSiblingEx(AST ast,
            int[] excludeTypes, int... types) {
        Collection<AST> results = new ArrayList<AST>();
        for (int type : types) {
            // ���[�g�v�f���w�肵���^�C�v�̂����̂����ꂩ��������A���[�g�v�f�����̂܂ܕԂ�
            if (ast.getType() == type) {
                return new AST[] { ast };
            }
        }
        
        // �S�Ă̎q�i����ё��j�v�f������
        AST child = ast.getFirstChild();
        while (child != null) {
            if (Arrays.binarySearch(excludeTypes, child.getType()) < 0) {
                searchMultiAndStoreEx(results, child, excludeTypes, types);
            }
            child = child.getNextSibling();
        }
        return results.toArray(new AST[results.size()]);
    }

    /**
     * ���[�g�v�f�̒�����A�w�肵���^�C�v�̗v�f�i�����j��T���� results �ɒǉ����܂��B
     * @param results ���ʊi�[��
     * @param ast ���[�g�v�f
     * @param types �^�C�v�i�����j�BTokenTypes
     */
    private static void searchMultiAndStore(Collection<AST> results, AST ast, int... types) {
        for (int type : types) {
            // ���[�g�v�f���w�肵���^�C�v�̂����̂����ꂩ��������A���[�g�v�f�� results �ɒǉ�
            if (ast.getType() == type) {
                results.add(ast);
                return;
            }
        }
        
        // �S�Ă̎q�i����ё��j�v�f������
        AST child = ast.getFirstChild();
        while (child != null) {
            searchMultiAndStore(results, child, types);
            child = child.getNextSibling();
        }
    }

    /**
     * ���[�g�v�f�̒�����A�w�肵���^�C�v�̗v�f�i�����j��T���� results �ɒǉ����܂��B
     * @param results ���ʊi�[��
     * @param ast ���[�g�v�f
     * @param excludeTypes ���O�^�C�v�ꗗ�B�K���\�[�g�ςł��邱��
     * @param types �^�C�v�i�����j�BTokenTypes
     */
    private static void searchMultiAndStoreEx(Collection<AST> results, AST ast,
            int[] excludeTypes, int... types) {
        
        if (Arrays.binarySearch(excludeTypes, ast.getType()) >= 0) {
            return;
        }
        for (int type : types) {
            // ���[�g�v�f���w�肵���^�C�v�̂����̂����ꂩ��������A���[�g�v�f�� results �ɒǉ�
            if (ast.getType() == type) {
                results.add(ast);
                return;
            }
        }
        
        // �S�Ă̎q�i����ё��j�v�f������
        AST child = ast.getFirstChild();
        while (child != null) {
            if (Arrays.binarySearch(excludeTypes, child.getType()) < 0) {
                searchMultiAndStoreEx(results, child, excludeTypes, types);
            }
            child = child.getNextSibling();
        }
    }

    /**
     * ���[�g�v�f��ID��Ԃ��܂��B
     * @param ast ���[�g�v�f
     * @return ID
     */
    public static String getIdent(AST ast) {
        // �q�v�f�S�Ă����[�v���āATokenTypes.IDENT �̂��̂�T��
        AST child = ast.getFirstChild();
        while (child != null) {
            if (child.getType() == TokenTypes.IDENT) {
                return child.getText();
            }
            child = child.getNextSibling();
        }
        return null;
    }

    /**
     * ���\�b�h�v�f�̈����̃V�O�j�`����Ԃ��܂��B
     * @param targetAst ���[�g�v�f�i���\�b�h�j
     * @return �V�O�j�`��
     */
    public static String getMethodParamSignature(AST targetAst) {
        AST paramsAst = search(targetAst, TokenTypes.PARAMETERS);
        AST[] params = searchMultiAllSibling(paramsAst, TokenTypes.PARAMETER_DEF);
        
        StringBuilder buff = new StringBuilder();
        buff.append('(');
        for (AST paramAst : params) {
            AST typeAst = search(paramAst, TokenTypes.TYPE);
            AST child = typeAst.getFirstChild();
            while (child != null) {
                if (child.getType() == TokenTypes.IDENT) {
                    buff.append('Q');
                    buff.append(child.getText());
                    buff.append(';');
                } else {
                    buff.append(PRIMITIVE_MAP.get(Integer.valueOf(child.getType())).charValue());
                }
                child = child.getFirstChild();
            }
        }
        buff.append(')');
        return buff.toString();
    }
    
    /**
     * �\�[�X�v�f�̑S�Ă̎q���f�o�b�O�o�͂��܂��B
     * @param sourceAst �\�[�X�v�f
     */
    public static void debugSourceAst(AST sourceAst) {
        debugAst(sourceAst, 0); // ���\�b�h�̃��[�g����S�ď����o��
        // ���\�b�h�̌Z�����S�ď����o��
        AST sibling = sourceAst.getNextSibling();
        while (sibling != null) {
            debugAst(sibling, 0);
            sibling = sibling.getNextSibling();
        }
    }
    
    /**
     * �\�[�X�v�f�̑S�Ă̎q���f�o�b�O�o�͂��܂��B
     * @param ast �\�[�X�v�f
     * @param index �[��
     */
    private static void debugAst(AST ast, int index) {
        AST child = ast.getFirstChild();
        while (child != null) {
            for (int i = 0; i < index; i++) {
                System.out.print("  ");
            }
            int lineNo = 0;
            if (child instanceof DetailAST) {
                lineNo = ((DetailAST)child).getLineNo();
            } 
            System.out.println(child + " " + child.getType()
                    + " " + TokenTypes.getTokenName(child.getType())
                    + " <" + lineNo + ">");
            debugAst(child, index + 1);
            child = child.getNextSibling();
        }
    }


}
