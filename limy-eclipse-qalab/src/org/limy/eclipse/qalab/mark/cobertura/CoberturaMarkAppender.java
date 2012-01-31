/*
 * Created 2007/01/23
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
package org.limy.eclipse.qalab.mark.cobertura;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.limy.eclipse.common.LimyEclipsePluginUtils;
import org.limy.eclipse.common.resource.LimyMarkerUtils;
import org.limy.eclipse.qalab.LimyQalabMarker;
import org.limy.eclipse.qalab.common.LimyQalabEnvironment;
import org.limy.eclipse.qalab.common.QalabResourceUtils;
import org.limy.eclipse.qalab.tester.ClassTestResult;
import org.limy.eclipse.qalab.tester.FailureItem;
import org.limy.eclipse.qalab.tester.ProjectTestResult;

/**
 * �e�X�g���ʂ̃}�[�J�[���쐬����⏕�N���X�ł��B
 * @author Naoki Iwami
 */
public class CoberturaMarkAppender {

    /** �e�X�g�w�b�_��� */
    private static final Pattern PATTERN_HEADER = Pattern.compile("(.*)\\((.*)\\)");;

    // ------------------------ Fields

    /** �v���W�F�N�g�P�ʂ̃e�X�g���ʊi�[�� */
    private ProjectTestResult projectResult;

    /** �N���X�P�ʂ̃e�X�g���ʊi�[�� */
    private ClassTestResult classResult;
    
    /** �� */
    private LimyQalabEnvironment env;
    
    /** �h�L�������g�L���b�V�� */
    private Map<String, IDocument> documentMap = new HashMap<String, IDocument>();
    
    /** ���s�����e�X�g���\�b�h�ꗗ */
    private Collection<IJavaElement> failMethods = new HashSet<IJavaElement>();

    // ------------------------ Constructors

    /**
     * CoberturaMarkAppender�C���X�^���X���\�z���܂��B
     * @param projectResult �v���W�F�N�g�P�ʂ̃e�X�g���ʊi�[��
     * @param classResult �N���X�P�ʂ̃e�X�g���ʊi�[��
     */
    public CoberturaMarkAppender(ProjectTestResult projectResult, ClassTestResult classResult) {
        this.projectResult = projectResult;
        this.classResult = classResult;
    }

    // ------------------------ Public Methods

    /**
     * �e�X�g���ʂ����Ƀ��\�[�X�Ƀ}�[�J�[��ǉ����܂��B
     * @param env 
     * @param resources ���\�[�X�ꗗ
     * @param testResults �e�X�g����
     * @throws IOException I/O��O
     * @throws CoreException �R�A��O
     */
    public void addTestResultMarker(LimyQalabEnvironment env,
            IResource[] resources, Collection<Result> testResults)
            throws IOException, CoreException {
        
        this.env = env;
        // ���s�����e�X�g�P�[�X�ꗗ���i�[�������\�[�X��S���[�v
        for (IResource resource : resources) {
            
            // �e�X�g�P�[�X��Java�N���X��
            String testClassName = QalabResourceUtils.getQualifiedTestClassName(env, resource);
    
            // �e�X�g�P�[�X�̃��\�[�X���擾
            IResource testResource = QalabResourceUtils.getJavaResource(
                    env, testClassName, false);
            
            if (testResource != null && testResource.exists()) {
                documentMap.put(testClassName, QalabResourceUtils.parseDocument(testResource));
                // failure�����error�}�[�J�[���폜
                testResource.deleteMarkers(LimyQalabMarker.TEST_ID,
                        true, IResource.DEPTH_INFINITE);
            }
        }
    
        for (Result result : testResults) {
            markFailures(result.getFailures(), false);
        }
        
        // �����������\�b�h���}�[�L���O
        for (IResource resource : resources) {
            
            // �e�X�g�P�[�X��Java�N���X��
            String testClassName = QalabResourceUtils.getQualifiedTestClassName(env, resource);
    
            IType type = env.getJavaProject().findType(testClassName);
            if (type != null) {
                for (IJavaElement el : type.getChildren()) {
                    if (el.getElementType() == IJavaElement.METHOD
                            && !failMethods.contains(el)) {
                        IMethod method = (IMethod)el;
                        IRegion region = getRegion(method);
                        if (region != null) {
                            markSuccess(method, region, documentMap.get(testClassName));
                        }
                    }
                }
            }
        }
    
    }

    // ------------------------ Private Methods

    private IRegion getRegion(IMethod method) throws JavaModelException {
        
        ISourceRange range = method.getSourceRange();
        return new Region(range.getOffset(), range.getLength());
    }

    /**
     * @param failures �G���[�ꗗ
     * @param isError true: error, false: failure
     * @throws CoreException �R�A��O
     */
    private void markFailures(List<Failure> failures,
            boolean isError) throws CoreException {
        
        for (Failure failure : failures) {

            // fail�����e�X�g�̃N���X���ƃ��\�b�h�����擾
            String header = failure.getTestHeader();
            Matcher matcher = PATTERN_HEADER.matcher(header);
            
            String testClassName = header;
            String testMethodName = header;

            if (matcher.matches()) {
                testClassName = matcher.group(2);
                testMethodName = matcher.group(1);
            }

            // ���̃e�X�g���\�b�h��failMathods�ɒǉ�
            IMethod targetMethod = null;
            IType type = env.getJavaProject().findType(testClassName);
            for (IJavaElement el : type.getChildren()) {
                if (el.getElementType() == IJavaElement.METHOD
                        && testMethodName.equals(el.getElementName())) {
                    targetMethod = (IMethod)el;
                    failMethods.add(el);
                }
            }
            
            if (targetMethod != null) {
                IRegion region = getRegion(targetMethod);
                if (region != null) {
                    markFailure(targetMethod.getResource(), region,
                            documentMap.get(testClassName), isError, failure);
                }
            }
        }
    }
    
    /**
     * ���\�[�X�Ƀ}�[�J�[��ǉ����܂��B
     * @param resource �Ώۃ��\�[�X
     * @param region �}�[�N�͈�
     * @param document �h�L�������g
     * @param isError true: error, false: failure
     * @param failure �e�X�g����
     * @throws CoreException �R�A��O
     */
    private void markFailure(IResource resource, IRegion region, IDocument document,
            boolean isError, Failure failure)
            throws CoreException {
        
        try {
            int lineNumber = document.getLineOfOffset(region.getOffset());
            
            Map<String, Object> attrs = new HashMap<String, Object>();
            attrs.put(IMarker.CHAR_START, Integer.valueOf(
                    document.getLineOffset(lineNumber)));
            attrs.put(IMarker.CHAR_END, Integer.valueOf(
                    document.getLineOffset(lineNumber + 1) - 1));
            attrs.put(LimyQalabMarker.TEST_NAME, failure.getTestHeader());
            
            String markerId;
            String message;
            if (isError) {
                markerId = LimyQalabMarker.ERROR_ID;
                String trace = failure.getTrace();
                int index = trace.indexOf('\n');
                message = trace.substring(0, trace.indexOf('\n', index + 1));
            } else {
                markerId = LimyQalabMarker.FAILURE_ID;
                message = failure.getMessage() != null ? failure.getMessage()
                        : failure.getException().toString();
            }
            
            LimyMarkerUtils.addMarker(markerId,
                    resource, lineNumber + 1, message, attrs);
            addItemToResult(failure, resource,
                    isError, lineNumber, message);
    
        } catch (BadLocationException e) {
            LimyEclipsePluginUtils.log(e);
        }
        
    }

    /**
     * ���\�[�X�Ƀ}�[�J�[��ǉ����܂��B
     * @param method �Ώۃ��\�b�h
     * @param resource �Ώۃ��\�[�X
     * @param region �}�[�N�͈�
     * @param document �h�L�������g
     * @throws CoreException �R�A��O
     */
    private void markSuccess(IMethod method,
            IRegion region, IDocument document)
            throws CoreException {
        
        try {
            int lineNumber = document.getLineOfOffset(region.getOffset());
            
            Map<String, Object> attrs = new HashMap<String, Object>();
            attrs.put(IMarker.CHAR_START, Integer.valueOf(
                    document.getLineOffset(lineNumber)));
            attrs.put(IMarker.CHAR_END, Integer.valueOf(
                    document.getLineOffset(lineNumber + 1) - 1));
            attrs.put(LimyQalabMarker.TEST_NAME, method.getElementName());
            
            IResource resource = method.getResource();
            LimyMarkerUtils.addMarker(LimyQalabMarker.SUCCESS_ID,
                    resource, lineNumber + 1, "Test Success", attrs);
    
        } catch (BadLocationException e) {
            LimyEclipsePluginUtils.log(e);
        }
        
    }

    /**
     * Error�܂���Failure���ʂ��r���[�i�[�p�t�B�[���h�ɒǉ����܂��B
     * @param failure Error�܂���Failure����
     * @param resource ���\�[�X
     * @param isError �G���[���ǂ���
     * @param lineNumber �s�ԍ�
     * @param message ���b�Z�[�W
     */
    private void addItemToResult(Failure failure, IResource resource,
            boolean isError, int lineNumber, String message) {
        
        FailureItem item = new FailureItem(
                failure.getTestHeader(),
                failure.getDescription().getDisplayName(),
                resource,
                isError, lineNumber, message);
        if (classResult != null) {
            classResult.addItem(item);
        }
        if (projectResult != null) {
            projectResult.addItem(item);
        }
        
    }
    
}
