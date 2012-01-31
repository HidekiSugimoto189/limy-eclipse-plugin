/*
 * Created 2005/09/13
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
package org.limy.eclipse.code.accessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IParent;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.codemanipulation.GetterSetterUtil;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.text.edits.TextEdit;
import org.limy.eclipse.code.LimyCodeConstants;
import org.limy.eclipse.code.common.LimyFieldObject;
import org.limy.eclipse.common.LimyEclipseConstants;

/**
 * �A�N�Z�b�T�iGetter/Setter�j���\�b�h�֘A�̃��[�e�B���e�B�N���X�ł��B
 * @depend - - - LimyClassObject
 * @depend - - - LimyFieldObject
 * @depend - - - AccessorCreater
 * @author Naoki Iwami
 */
public final class AccessorUtils {
    
    /**
     * private constructor
     */
    private AccessorUtils() { }
    
    /**
     * �����A�N�Z�b�T���\�b�h��Javadoc���X�V���܂��B
     * @param element Java�N���X
     * @param monitor �J�ڃ��j�^
     * @param edits 
     * @throws JavaModelException Java���f����O
     */
    public static void modifyAccessorJavadoc(IType element,
            IProgressMonitor monitor,
            Map<ICompilationUnit, Collection<TextEdit>> edits) throws JavaModelException {
        
        for (IField javaField : element.getFields()) {
            LimyFieldObject field = new LimyFieldObject(javaField);
            modifyGetterMethod(element, field, monitor, edits);
            modifySetterMethod(element, field, monitor, edits);
        }
    }
    
    /**
     * �w�肵���N���X��public������Getter/Setter���\�b�h��S�������܂��B
     * @param element Java�N���X
     * @param monitor �J�ڃ��j�^
     * @throws JavaModelException Java���f����O
     */
    public static void createAllPublicMethods(IType element,
            IProgressMonitor monitor) throws JavaModelException {
        
        List<LimyFieldObject> fields = createLimyClass(element).getFields();
        
        for (int idx = 0; idx < fields.size(); idx++) {
            LimyFieldObject field = fields.get(idx);
            
            if (Flags.isStatic(field.getField().getFlags())) {
                continue;
            }

            // Getter/Setter���\�b�h��ǉ�����ʒu������
            IJavaElement sibling = decideSibling(fields, idx);

            // Getter/Setter���\�b�h��ǉ�
            appendGetterMethod(element, field, sibling, monitor);
            
            if (!Flags.isFinal(field.getField().getFlags())) {
                appendSetterMethod(element, field, sibling, monitor);
            }

        }
        
    }

    /**
     * �w�肵���t�B�[���h��public������Getter/Setter���\�b�h��S�������܂��B
     * @param javaField Java�t�B�[���h
     * @param sibling �����ӏ�
     * @param monitor �J�ڃ��j�^
     * @throws JavaModelException Java���f����O
     */
    public static void createPublicAccessor(IField javaField,
            IProgressMonitor monitor) throws JavaModelException {

        List<LimyFieldObject> fields = createLimyClass(
                javaField.getDeclaringType()).getFields();

        LimyFieldObject field = new LimyFieldObject(javaField);
        IType javaClass = (IType)javaField.getParent();
        
        if (Flags.isStatic(field.getField().getFlags())) {
            return;
        }

        // Getter/Setter���\�b�h��ǉ�
        for (int i = 0; i < fields.size(); i++) {
            LimyFieldObject nowField = fields.get(i);
            if (nowField.getField().equals(javaField)) {
                IJavaElement sibling = decideSibling(fields, i);
                appendGetterMethod(javaClass, field, sibling, monitor);
                if (!Flags.isFinal(field.getField().getFlags())) {
                    appendSetterMethod(javaClass, field, sibling, monitor);
                }
                continue;
            }
        }
        
    }

    /**
     * �w�肵���t�B�[���h��public������Setter���\�b�h��S�������܂��B
     * @param javaField Java�t�B�[���h
     * @param monitor �J�ڃ��j�^
     * @throws JavaModelException Java���f����O
     */
    public static void createPublicSetter(IField javaField,
            IProgressMonitor monitor) throws JavaModelException {
        
        List<LimyFieldObject> fields = createLimyClass(
                javaField.getDeclaringType()).getFields();

        IType javaClass = (IType)javaField.getParent();

        for (int i = 0; i < fields.size(); i++) {
            LimyFieldObject nowField = fields.get(i);
            if (nowField.getField().equals(javaField)) {
                IJavaElement sibling = decideSibling(fields, i);
                appendSetterMethod(javaClass, nowField, sibling, monitor);
            }
        }

    }

    /**
     * �w�肵���t�B�[���h��public������Getter���\�b�h��S�������܂��B
     * @param javaField Java�t�B�[���h
     * @param monitor �J�ڃ��j�^
     * @throws JavaModelException Java���f����O
     */
    public static void createPublicGetter(IField javaField,
            IProgressMonitor monitor) throws JavaModelException {
        
        List<LimyFieldObject> fields = createLimyClass(
                javaField.getDeclaringType()).getFields();

        IType javaClass = (IType)javaField.getParent();

        for (int i = 0; i < fields.size(); i++) {
            LimyFieldObject nowField = fields.get(i);
            if (nowField.getField().equals(javaField)) {
                IJavaElement sibling = decideSibling(fields, i);
                appendGetterMethod(javaClass, nowField, sibling, monitor);
            }
        }

    }

    /**
     * �t���O�l����A�N�Z�X���ʎq�������Ԃ��܂��B
     * @param flags �t���O�l
     * @return �A�N�Z�X���ʎq������
     */
    public static String getFlagString(int flags) {
        String r = LimyEclipseConstants.EMPTY_STR;
        if ((flags & Flags.AccPrivate) != 0) {
            r = "private";
        }
        if ((flags & Flags.AccProtected) != 0) {
            r = "protected";
        }
        if ((flags & Flags.AccPublic) != 0) {
            r = LimyCodeConstants.FLAGSTR_PUBLIC;
        }
        return r;
    }

    /**
     * Java�N���X����LimyClassObject�𐶐����ĕԂ��܂��B
     * @param element Java�N���X
     * @return ��������LimyClassObject
     * @throws JavaModelException Java���f����O
     */
    public static LimyClassObject createLimyClass(IType element) throws JavaModelException {
        
        LimyClassObject limyClass = new LimyClassObject();
        IField[] fields = element.getFields();
        for (IField field : fields) {
            limyClass.addField(new LimyFieldObject(field));
        }
        return limyClass;
    }

    // ------------------------ Private Methods

    /**
     * ������Getter���\�b�h���C�����܂��B
     * @param javaElement Java�N���X
     * @param field �t�B�[���h
     * @param monitor �J�ڃ��j�^
     * @throws JavaModelException Java���f����O
     */
    private static void modifyGetterMethod(IType javaElement,
            LimyFieldObject field, IProgressMonitor monitor,
            Map<ICompilationUnit, Collection<TextEdit>> edits)
            throws JavaModelException {
        
        // ������Getter���\�b�h���擾
        IMethod getterMethod = GetterSetterUtil.getGetter(field.getField());
        if (getterMethod != null) {
            // ����ǉ�����Getter���\�b�h���쐬�i�A�N�Z�X���ʎq�͊����̂��̂ɍ��킹��j
            String getterContents = AccessorCreater.createGetterContents(
                    field.getField(),
                    field.getComment(),
                    field.getAnnotationHint(),
                    AccessorUtils.getFlagString(getterMethod.getFlags()),
                    getterMethod);
            
            // �����̃��\�b�h��`�ƍ���쐬�������\�b�h��`���قȂ��Ă�����A����쐬�������\�b�h��`�ɒu������
            String getterSource = getterMethod.getSource();
            if (!generalizeStr(getterContents).equals(generalizeStr(getterSource))) {
                
                Collection<TextEdit> changes = edits.get(javaElement.getCompilationUnit());
                if (changes == null) {
                    changes = new ArrayList<TextEdit>();
                    edits.put(javaElement.getCompilationUnit(), changes);
                }
                ISourceRange range = getterMethod.getSourceRange();
                changes.add(new ReplaceEdit(range.getOffset(), range.getLength(), getterContents));
                
                javaElement.createMethod(getterContents,
                        getNextSibling(getterMethod), true, monitor);
                getterMethod.delete(true, monitor);
            }
        }
    }

    /**
     * ������Setter���\�b�h���C�����܂��B
     * @param javaElement Java�N���X
     * @param field �t�B�[���h
     * @param monitor �J�ڃ��j�^
     * @throws JavaModelException Java���f����O
     */
    private static void modifySetterMethod(IType javaElement,
            LimyFieldObject field, IProgressMonitor monitor,
            Map<ICompilationUnit, Collection<TextEdit>> edits)
            throws JavaModelException {
        
        // ������Setter���\�b�h���擾
        IMethod setterMethod = GetterSetterUtil.getSetter(field.getField());
        if (setterMethod != null) {
            // ����ǉ�����Setter���\�b�h���쐬�i�A�N�Z�X���ʎq�͊����̂��̂ɍ��킹��j
            String setterContents = AccessorCreater.createSetterContents(
                    field.getField(),
                    field.getComment(),
                    AccessorUtils.getFlagString(setterMethod.getFlags()),
                    setterMethod);
            
            // �����̃��\�b�h��`�ƍ���쐬�������\�b�h��`���قȂ��Ă�����A����쐬�������\�b�h��`�ɒu������
            String setterSource = setterMethod.getSource();
            if (!generalizeStr(setterContents).equals(generalizeStr(setterSource))) {
                
                Collection<TextEdit> changes = edits.get(javaElement.getCompilationUnit());
                if (changes == null) {
                    changes = new ArrayList<TextEdit>();
                    edits.put(javaElement.getCompilationUnit(), changes);
                }
                ISourceRange range = setterMethod.getSourceRange();
                changes.add(new ReplaceEdit(range.getOffset(), range.getLength(), setterContents));

                javaElement.createMethod(setterContents,
                        getNextSibling(setterMethod), true, monitor);
                setterMethod.delete(true, monitor);
            }
        }
    }

    /**
     * Getter���\�b�h���N���X�ɒǉ����܂��B
     * @param javaElement Java�N���X
     * @param field �t�B�[���h
     * @param sibling ���\�b�h��}������ꏊ
     * @param monitor �J�ڃ��j�^
     * @throws JavaModelException Java���f����O
     */
    private static void appendGetterMethod(
            IType javaElement, LimyFieldObject field, IJavaElement sibling,
            IProgressMonitor monitor)
            throws JavaModelException {
        
        // ������Getter���\�b�h���擾
        IMethod getterMethod = GetterSetterUtil.getGetter(field.getField());
        
        // ����ǉ�����Getter���\�b�h���쐬
        String getterContents = AccessorCreater.createGetterContents(
                field.getField(),
                field.getComment(),
                field.getAnnotationHint(),
                LimyCodeConstants.FLAGSTR_PUBLIC,
                getterMethod);
        
        // �����̃��\�b�h��`�ƍ���쐬�������\�b�h��`���قȂ��Ă�����A����쐬�������\�b�h��`�ɒu������
        String getterSource = (getterMethod != null) ? getterMethod.getSource() : null;
        if (!generalizeStr(getterContents).equals(generalizeStr(getterSource))) {
            javaElement.createMethod(getterContents, sibling, true, monitor);
            if (getterMethod != null) {
                getterMethod.delete(true, monitor);
            }
        }
    }
    
    /**
     * Setter���\�b�h���N���X�ɒǉ����܂��B
     * @param javaElement Java�N���X
     * @param field �t�B�[���h
     * @param sibling ���\�b�h��}������ꏊ
     * @param monitor �J�ڃ��j�^
     * @throws JavaModelException Java���f����O
     */
    private static void appendSetterMethod(
            IType javaElement, LimyFieldObject field, IJavaElement sibling,
            IProgressMonitor monitor)
            throws JavaModelException {
        
        // ������Setter���\�b�h���擾
        IMethod setterMethod = GetterSetterUtil.getSetter(field.getField());
        
        // ����ǉ�����Setter���\�b�h���쐬
        String setterContents = AccessorCreater.createSetterContents(
                field.getField(),
                field.getComment(),
                LimyCodeConstants.FLAGSTR_PUBLIC,
                setterMethod);
        
        // �����̃��\�b�h��`�ƍ���쐬�������\�b�h��`���قȂ��Ă�����A����쐬�������\�b�h��`�ɒu������
        String setterSource = (setterMethod != null) ? setterMethod.getSource() : null;
        if (!generalizeStr(setterContents).equals(generalizeStr(setterSource))) {
            javaElement.createMethod(setterContents, sibling, true, monitor);
            if (setterMethod != null) {
                setterMethod.delete(true, monitor);
            }
        }
    }

    /**
     * �w�肵���t�B�[���h�ɑΉ�����Getter�܂���Setter���\�b�h��Ԃ��܂��B
     * <p>
     * �Ή����郁�\�b�h�������ꍇ�A���̒���ɒ�`���ꂽGetter�܂���Setter���\�b�h��Ԃ��܂��B
     * </p>
     * @param fields �t�B�[���h�ꗗ
     * @param idx �ΏۂƂ���t�B�[���h�̈ꗗ���X�g���C���f�b�N�X
     * @return
     * @throws JavaModelException Java���f����O
     */
    private static IJavaElement decideSibling(List<LimyFieldObject> fields, int idx)
            throws JavaModelException {
        
        IJavaElement sibling = null;
        
        // fields[(idx + 1) ... last]�܂Ń��[�v
        for (int insIdx = idx + 1; insIdx < fields.size(); insIdx++) {
            LimyFieldObject field = fields.get(insIdx);
            
            // �t�B�[���h�ɑΉ�����Getter������΂����Ԃ�
            IMethod getterMethod = GetterSetterUtil.getGetter(field.getField());
            if (getterMethod != null && getterMethod.exists()) {
                sibling = getterMethod;
                break;
            }
            
            // �t�B�[���h�ɑΉ�����Setter������΂����Ԃ�
            IMethod setterMethod = GetterSetterUtil.getSetter(field.getField());
            if (setterMethod != null && setterMethod.exists()) {
                sibling = setterMethod;
                break;
            }
        }
        return sibling;
    }

    /**
     * �w�肵���G�������g�̒���̃G�������g��Ԃ��܂��B
     * @param element �G�������g
     * @return ����̃G�������g�i�w�肵���G�������g���Ōゾ�����ꍇ�Anull�j
     * @throws JavaModelException Java���f����O
     */
    private static IJavaElement getNextSibling(IJavaElement element)
            throws JavaModelException {
        IJavaElement parent = element.getParent();
        if (parent instanceof IParent) {
            IJavaElement[] children = ((IParent)parent).getChildren();
            for (int i = 0; i < children.length; i++) {
                if (children[i] == element) {
                    if (i < children.length - 1) {
                        return children[i + 1];
                    }
                    return null;
                }
            }
        }
        return null;
    }
    /**
     * ���������ʉ����ĕԂ��܂��B
     * @param contents �R���e���c������
     * @return ��ʉ����ꂽ������
     */
    private static String generalizeStr(String contents) {
        if (contents == null) {
            return null;
        }
        
        String r = contents;
        r = r.replaceAll("//.*", LimyEclipseConstants.EMPTY_STR);
        r = r.trim().replaceAll("\r\n", "\n").replaceAll("\\t", "    ");
        return r;
    }

}
