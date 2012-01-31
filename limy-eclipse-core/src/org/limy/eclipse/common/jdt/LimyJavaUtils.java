/*
 * Created 2007/01/15
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
package org.limy.eclipse.common.jdt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IParent;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.dom.TokenScanner;
import org.eclipse.jdt.internal.corext.util.CodeFormatterUtil;
import org.eclipse.jdt.internal.corext.util.MethodOverrideTester;
import org.eclipse.jdt.internal.corext.util.SuperTypeHierarchyCache;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * Java�v�f�֘A�̃��[�e�B���e�B�N���X�ł��B
 * @author Naoki Iwami
 */
public final class LimyJavaUtils {
    
    /**
     * private constructor
     */
    private LimyJavaUtils() { }
    
    /**
     * javaElement �ɑ�����S�Ă�Java�t�@�C���ɑ΂��ď��������s���܂��B
     * @param javaElement ���[�gJava�v�f
     * @param visitor IJavaResourceVisitor
     * @throws CoreException �R�A��O
     */
    public static void executeAllJavas(IJavaElement javaElement,
            IJavaResourceVisitor visitor)
            throws CoreException {
        
        Set<IJavaElement> results = new HashSet<IJavaElement>();
        appendAllJavas(results, javaElement);
        for (IJavaElement el : results) {
            visitor.executeJavaElement(el);
        }
    }

    /**
     * �I�����ꂽ�S�Ă�Java�v�f��Ԃ��܂��B
     * <p>
     * �G�f�B�^�� : IType, IMethod, IField �̃C���X�^���X���<br>
     * �G�������g : IJavaProject, IPackageFragmentRoot, IPackageFragment,
     * ICompilationUnit �̃C���X�^���X��I������������<br>
     * </p>
     * @param window WorkbenchWindow
     * @param selection �I��͈�
     * @return �I�����ꂽ�S�Ă�Java�v�f
     * @throws JavaModelException Java���f����O
     */
    public static List<IJavaElement> getSelectedJavaElements(
            IWorkbenchWindow window, ISelection selection) throws JavaModelException {

        List<IJavaElement> results = new ArrayList<IJavaElement>();

        if (selection instanceof ITextSelection) {
            // �G�f�B�^���ɑI��͈͂��������ꍇ�Aselection �͋�Ȃ̂œn���Ă��Ӗ�������
            
            // �G�f�B�^�ŊJ���Ă���t�@�C�����\������Java�N���X�G�������g
            IEditorPart editor = window.getActivePage().getActiveEditor();
            IJavaElement javaElement = JavaUI.getEditorInputJavaElement(editor.getEditorInput());

            // �G�f�B�^���̑I��͈͂��擾
            ITextSelection textSelection = (ITextSelection)
                    ((ITextEditor)editor).getSelectionProvider().getSelection();

            if (javaElement instanceof ICompilationUnit) {
                ICompilationUnit cunit = (ICompilationUnit)javaElement;
                // ���ݑI�𒆂̃t�B�[���h���擾
                IJavaElement selectedElement = cunit.getElementAt(textSelection.getOffset());
                if (selectedElement == null) {
                    // �ǂ����I������Ă��Ȃ��ꍇ�iclass��`�O�̃R�����g�����Ȃǁj
                    results.add(cunit); // Java�N���X���̂��̂�I���������̂ƌ��Ȃ�
                } else {
                    results.add(selectedElement);
                }
            }
        }

        if (selection instanceof IStructuredSelection) {
            Object[] elements = ((IStructuredSelection)selection).toArray();
            for (Object element : elements) {
                if (element instanceof IJavaElement) {
                    results.add((IJavaElement)element);
                }
            }
        }
        
        return results;
    }

    /**
     * �����o�̐錾�ʒu��Ԃ��܂��B
     * @param member �����o
     * @param document �h�L�������g
     * @return �����o�̐錾�ʒu
     * @throws CoreException �R�A��O
     */
    public static int getMemberStartOffset(IMember member, IDocument document)
            throws CoreException {
        
        int offset = member.getSourceRange().getOffset();
//        TokenScanner scanner = new TokenScanner(document);
        TokenScanner scanner = new TokenScanner(document, member.getJavaProject());
        return scanner.getNextStartOffset(offset, true);
    }
    
    /**
     * Java�N���X�Ɋ܂܂��S�Ẵ����o���擾���܂��B
     * @param cunit Java�N���X
     * @return Java�N���X�Ɋ܂܂��S�Ẵ����o
     * @throws JavaModelException Java���f����O
     */
    public static Collection<IJavaElement> getAllMembers(ICompilationUnit cunit)
            throws JavaModelException {
        
        Collection<IJavaElement> results = new ArrayList<IJavaElement>();
        for (IType type : cunit.getTypes()) {
            for (IJavaElement element : type.getChildren()) {
                results.add(element);
            }
        }
        return results;
    }
    
    /**
     * �G�f�B�^�̃^�u�����擾���܂��B
     * @param project �v���W�F�N�g
     * @return �^�u��
     */
    public static int getTabWidth(IJavaProject project) {
        return CodeFormatterUtil.getTabWidth(project);
    }


    /**
     * @param method
     * @param declaringType
     * @return
     * @throws JavaModelException
     */
    public static IMethod isOverridden(
            IMethod method, IType declaringType) throws JavaModelException {
        
        ITypeHierarchy hierarchy = SuperTypeHierarchyCache.getTypeHierarchy(declaringType);
        MethodOverrideTester tester = new MethodOverrideTester(declaringType, hierarchy);
        return tester.findOverriddenMethod(method, true);
    }

    /**
     * javaElement �ɑ�����S�Ă�Java�t�@�C������������ results �Ɋi�[���܂��B
     * @param results ���ʊi�[��
     * @param javaElement ���[�gJava�v�f
     * @param visitor IJavaResourceVisitor
     * @throws CoreException �R�A��O
     */
    public static void appendAllJavas(Collection<IJavaElement> results,
            IJavaElement javaElement) throws CoreException {
        
        if (javaElement == null || javaElement.getResource() == null) {
            // Jar�G�������g�̏ꍇ�Aresource = null �ƂȂ�
            return;
        }
        
        // Java�v���W�F�N�g�A�\�[�X�p�X�AJava�p�b�P�[�W
        appendForIParent(results, javaElement/*, visitor*/);
        
        // Java�p�b�P�[�W�̃T�u�p�b�P�[�W�͂����Ŏ擾
        if (javaElement instanceof IPackageFragment) {
            appendForIPackageFragment(results, (IPackageFragment)javaElement/*, visitor*/);
        }

        // Java�N���X�A���\�b�h��`�A�t�B�[���h��`
        int type = javaElement.getElementType();
        if (type == IJavaElement.IMPORT_DECLARATION
                || type == IJavaElement.PACKAGE_DECLARATION
                || type == IJavaElement.COMPILATION_UNIT
                || type == IJavaElement.TYPE
                || type == IJavaElement.METHOD
                || type == IJavaElement.FIELD) {
            
            results.add(javaElement);
        }
    }

    /**
     * �Ώۗv�f�Ɋ܂܂��SIType��Ԃ��܂��B
     * @param targetElement �Ώۗv�f
     * @return �Ώۗv�f�Ɋ܂܂��SIType
     * @throws JavaModelException Java���f����O
     */
    public static IType[] getAllTypes(IJavaElement targetElement)
            throws JavaModelException {

        Collection<ICompilationUnit> units = new ArrayList<ICompilationUnit>();

        if (targetElement instanceof IPackageFragmentRoot) {
            IJavaElement[] elements = ((IPackageFragmentRoot)targetElement).getChildren();
            for (IJavaElement element : elements) {
                units.addAll(Arrays.asList(
                        ((IPackageFragment)element).getCompilationUnits()));
            }
        } else if (targetElement instanceof IPackageFragment) {
            units.addAll(Arrays.asList(
                    ((IPackageFragment)targetElement).getCompilationUnits()));
        } else if (targetElement instanceof ICompilationUnit) {
            units.add((ICompilationUnit)targetElement);
        } else if (targetElement instanceof IMember) {
            units.add(((IMember)targetElement).getCompilationUnit());
        } else if (targetElement instanceof IType) {
            units.add(((IType)targetElement).getCompilationUnit());
        }

        Collection<IType> types = new ArrayList<IType>();
        for (ICompilationUnit unit : units) {
            for (IType type : unit.getTypes()) {
                types.add(type);
            }
        }
        return types.toArray(new IType[types.size()]);
    }
    
    /**
     * Java�v�f���ōŏ��Ɍ��������N���X�iIType�j��Ԃ��܂��B
     * @param javaElement 
     * @return
     * @throws JavaModelException 
     */
    public static IType getPrimaryType(IJavaElement javaElement) throws JavaModelException {
        IType[] types = getAllTypes(javaElement);
        if (types.length > 0) {
            return types[0];
        }
        return null;
    }

    // ------------------------ Private Methods

    /**
     * IPackageFragment�ɑ�����S�Ă�Java�t�@�C������������ results �Ɋi�[���܂��B
     * @param results ���ʊi�[��
     * @param packageFragment IPackageFragment�v�f
     * @param visitor IJavaResourceVisitor
     * @throws CoreException �R�A��O
     */
    private static void appendForIPackageFragment(Collection<IJavaElement> results,
            IPackageFragment packageFragment/*, IJavaResourceVisitor visitor*/)
            throws CoreException {
        
        IPackageFragmentRoot parent = (IPackageFragmentRoot)packageFragment.getParent();
        
        String parentName = packageFragment.getElementName();
        if (parentName.length() > 0) {
            for (IJavaElement child : parent.getChildren()) {
                String childName = child.getElementName();
                
                if (childName.startsWith(parentName)
                        && childName.lastIndexOf('.') == parentName.length()) {
                    // �����̃T�u�p�b�P�[�W�����T��
                    appendAllJavas(results, child/*, visitor*/);
                }
            }
        }
    }

    /**
     * Java�v�f�ɑ�����S�Ă�Java�t�@�C������������ results �Ɋi�[���܂��B
     * @param results ���ʊi�[��
     * @param javaElement Java�v�f
     * @throws CoreException �R�A��O
     */
    private static void appendForIParent(Collection<IJavaElement> results,
            IJavaElement javaElement) throws CoreException {
        
        int type = javaElement.getElementType();
        if (type == IJavaElement.JAVA_PROJECT
                || type == IJavaElement.PACKAGE_FRAGMENT_ROOT
                || type == IJavaElement.PACKAGE_FRAGMENT) {

            IParent parent = (IParent)javaElement;
            for (IJavaElement child : parent.getChildren()) {
                // Java�p�b�P�[�W�̏ꍇ�A�T�u�p�b�P�[�W�܂ł͎擾�ł��Ȃ��̂Ō�ɕʓr�擾���Ă���
                appendAllJavas(results, child);
            }
        }
    }

}
