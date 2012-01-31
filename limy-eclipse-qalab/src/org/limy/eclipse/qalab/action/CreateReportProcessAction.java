/*
 * Created 2006/08/15
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
package org.limy.eclipse.qalab.action;

import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.progress.WorkbenchJob;
import org.limy.common.ProcessUtils;
import org.limy.eclipse.common.LimyEclipsePluginUtils;
import org.limy.eclipse.common.ui.AbstractJavaElementAction;
import org.limy.eclipse.qalab.LimyQalabPluginUtils;
import org.limy.eclipse.qalab.ant.BuildWarning;
import org.limy.eclipse.qalab.ant.CreateBuildXml;
import org.limy.eclipse.qalab.common.LimyQalabConstants;
import org.limy.eclipse.qalab.common.LimyQalabEnvironment;
import org.limy.eclipse.qalab.common.LimyQalabUtils;

/**
 * QA���|�[�g�쐬�A�N�V�����N���X�ł��B
 * @depend - - - CreateReportJob
 * @depend - - - CreateBuildXml
 * @author Naoki Iwami
 */
public class CreateReportProcessAction extends AbstractJavaElementAction {

    @Override
    protected void doAction(IJavaElement javaElement, IProgressMonitor monitor)
            throws CoreException {
        
        IProject project = javaElement.getJavaProject().getProject();
        LimyQalabEnvironment env = LimyQalabPluginUtils.createEnv(project);
        CreateBuildXml builder = new CreateBuildXml();
        
        final BuildWarning warningInfo = builder.prepareBuildFiles(env);
        builder.createFiles(env);

        IPath projectPath = project.getProject().getLocation();
        IPreferenceStore store = env.getStore();
        IPath path = projectPath.append(store.getString(LimyQalabConstants.KEY_BUILD_XML));
        try {
            ProcessUtils.execProgramNoWait(projectPath.toFile(),
                    "ant.bat", "-f", path.toFile().getAbsolutePath(),
                    "-l", LimyQalabUtils.createTempFile(project, "ant.log").getAbsolutePath()
            );
        } catch (IOException e) {
            LimyEclipsePluginUtils.log(e);
        }

        new WorkbenchJob("info") {
            @Override
            public IStatus runInUIThread(IProgressMonitor monitor) {
                QalabActionUtils.showConfirmDialog(warningInfo,
                        "Ant���g�p���ă��|�[�g���쐬���܂��B\n"
                        + "Ant�o�͌��ʂ�RPOJECT_ROOT/.limy/ant.log �t�@�C���ɏo�͂���܂��B\n"
                        + "���̏����͊O���v���Z�X�Ŏ��s�����ׁA���s�o�߂�Eclipse��Ŋm�F�ł��܂���B\n"
                        + "����������ɏI�������ꍇ�A���O�t�@�C�����r���œr�؂�邱�Ƃ�����܂��B");
                return Status.OK_STATUS;
            }
        }
        .schedule();
    }
    
}
