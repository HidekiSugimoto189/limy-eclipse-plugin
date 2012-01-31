/*
 * Created 2007/01/05
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
package org.limy.eclipse.qalab.mark;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.progress.UIJob;
import org.junit.runner.Result;
import org.limy.eclipse.common.LimyEclipsePluginUtils;
import org.limy.eclipse.common.ui.LimyUIUtils;
import org.limy.eclipse.qalab.common.LimyQalabEnvironment;
import org.limy.eclipse.qalab.common.LimyQalabUtils;
import org.limy.eclipse.qalab.common.QalabResourceUtils;
import org.limy.eclipse.qalab.mark.cobertura.CoberturaExecuter;
import org.limy.eclipse.qalab.mark.cobertura.CoberturaMarkAppender;
import org.limy.eclipse.qalab.mark.cobertura.CoberturaSupport;
import org.limy.eclipse.qalab.tester.ClassTestResult;
import org.limy.eclipse.qalab.tester.ProjectTestResult;
import org.limy.eclipse.qalab.ui.TestResultView;

/**
 * �e�X�g���sJob�N���X�ł��B
 * @author Naoki Iwami
 */
public class ExecuteUIJob extends UIJob {

    // ------------------------ Fields (final)

    /** ���ݒ� */
    private final LimyQalabEnvironment env;
    
    /** Cobertura�T�|�[�g */
    private final CoberturaSupport coberturaSupport;

    /** ���\�[�X�ꗗ */
    private final IResource[] resources;
    
    // ------------------------ Fields (variable)
            
    /** �v���W�F�N�g�P�ʂ̃e�X�g���ʊi�[�� */
    private ProjectTestResult projectResult;

    /** �N���X�P�ʂ̃e�X�g���ʊi�[�� */
    private ClassTestResult classResult;

    // ------------------------ Constructors

    public ExecuteUIJob(LimyQalabEnvironment env, IResource resource) {
        
        super("Test Execute");
        this.resources = new IResource[] { resource };
        this.env = env;
        try {
            classResult = new ClassTestResult(
                    QalabResourceUtils.getQualifiedTestClassName(env, resource));
        } catch (CoreException e) {
            LimyEclipsePluginUtils.log(e);
        }
        coberturaSupport = new CoberturaSupport(env, resources);
    }

    public ExecuteUIJob(LimyQalabEnvironment env, IResource[] resources) {
        super("Test Execute");
        this.resources = resources;
        this.env = env;
        projectResult = new ProjectTestResult(env.getProject());
        coberturaSupport = new CoberturaSupport(env, resources);
    }

    @Override
    public IStatus runInUIThread(IProgressMonitor monitor) {
        
        try {
            
            Collection<Result> testResults
                    = new CoberturaExecuter(env).calculateCoverage(resources);

            // �J�o���b�W���ʃ}�[�J�[�o��
            coberturaSupport.addCoverageMarker(getDataFile());
            
            // �e�X�g���ʃ}�[�J�[�o��
            new CoberturaMarkAppender(projectResult, classResult)
                    .addTestResultMarker(env, resources, testResults);
            
        } catch (CoreException e) {
            LimyEclipsePluginUtils.log(e);
        } catch (IOException e) {
            LimyEclipsePluginUtils.log(e);
        }
        
        TestResultView view = (TestResultView)
                LimyUIUtils.findView("org.limy.eclipse.qalab.ui.TestResultView");
        if (view != null) {
            TableViewer viewer = view.getTableViewer();
            if (classResult != null) {
                viewer.setInput(classResult); // setInput �� null ��n���Ă͂����Ȃ��I
            }
            if (projectResult != null) {
                viewer.setInput(projectResult); // setInput �� null ��n���Ă͂����Ȃ��I
            }
        }

        return Status.OK_STATUS;
        
    }

    // ------------------------ Private Methods

    /**
     * Coberura�̃J�o���b�W���ʊi�[�t�@�C���icobertura.ser�j���擾���܂��B
     * @return �J�o���b�W���ʊi�[�t�@�C��
     */
    private File getDataFile() {
        return LimyQalabUtils.createTempFile(env.getProject(), "cobertura.ser");
    }

    
}
