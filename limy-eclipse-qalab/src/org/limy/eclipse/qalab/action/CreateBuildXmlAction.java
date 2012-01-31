/*
 * Created 2006/08/19
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

import java.io.CharArrayWriter;
import java.io.File;
import java.io.IOException;
import java.io.Writer;

import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.ui.progress.WorkbenchJob;
import org.limy.common.ProcessUtils;
import org.limy.eclipse.common.LimyEclipsePluginUtils;
import org.limy.eclipse.common.io.LimyIOUtils;
import org.limy.eclipse.common.ui.AbstractJavaElementAction;
import org.limy.eclipse.common.ui.LimyUIUtils;
import org.limy.eclipse.qalab.LimyQalabPluginUtils;
import org.limy.eclipse.qalab.ant.BuildWarning;
import org.limy.eclipse.qalab.ant.CreateBuildXml;
import org.limy.eclipse.qalab.common.LimyQalabConstants;
import org.limy.eclipse.qalab.common.LimyQalabEnvironment;

/**
 * build.xml���쐬����A�N�V�����ł��B
 * @depend - - - ProcessUtils
 * @depend - - - CreateBuildXml
 * @author Naoki Iwami
 */
public class CreateBuildXmlAction extends AbstractJavaElementAction {
    
    @Override
    protected void doAction(IJavaElement javaElement, IProgressMonitor monitor)
            throws CoreException {

        CreateBuildXml builder = new CreateBuildXml();
        
        final LimyQalabEnvironment env = LimyQalabPluginUtils.createEnv(
                javaElement.getJavaProject().getProject());
        
        final BuildWarning warningInfo = builder.prepareBuildFiles(env);
        builder.createFiles(env);
        
        new WorkbenchJob("info") {
            @Override
            public IStatus runInUIThread(IProgressMonitor monitor) {
                QalabActionUtils.showConfirmDialog(warningInfo,
                        "build.xml���쐬���܂����B\n"
                        + "Ant�̎��s�̓R�}���h�v�����v�g����s���ĉ������B\n"
                        + "Eclipse������Ant�����s����ƁA�������ł܂����肷��ꍇ������܂��B");
                checkAntEnvironment(env);
                return Status.OK_STATUS;
            }
        }
        .schedule();
    }

    // ------------------------ Private Methods

    /**
     * Ant�̃C���X�g�[�������`�F�b�N���܂��B
     * @param env ���ݒ�
     */
    private void checkAntEnvironment(LimyQalabEnvironment env) {

        String antExe = "ant";
        if (Platform.OS_WIN32.equals(Platform.getOS())) {
            antExe = "ant.bat";
        }

        Writer out = new CharArrayWriter();

        try {
            ProcessUtils.execProgram(new File("."), out, antExe, "-version");
        } catch (IOException e) {
            // ant��������Ȃ�
            LimyUIUtils.showConfirmDialog(
                    "���s�p�X���� ant.bat ��������܂���B\n"
                    + "�����炭���̂܂܂ł̓R�}���h�v�����v�g����Ant�����s�ł��܂���B\n"
                    + "ant.bat �̂���f�B���N�g���Ƀp�X��ʂ��ĉ������B");
            return;
        }
        
        String antHome = System.getenv("ANT_HOME");
        if (antHome == null) {
            LimyUIUtils.showConfirmDialog(
                    "���ϐ�ANT_HOME���w�肳��Ă��܂���B\n"
                    + "�w�肵�Ă����ƁA�v���O�C������Ant�̏ꏊ�𔻕ʂ���\n"
                    + "�K�v��jar�t�@�C���������ŃR�s�[���铙�̕⏕�@�\���L���ɂȂ�܂��B");
            return;
        }
        
        File antLibDir = new File(antHome, "lib");
        if (env.getStore().getBoolean(LimyQalabConstants.ENABLE_JDEPEND)) {
            if (antLibDir.list(new PrefixFileFilter("jdepend-")).length == 0) {
                // jdepend-XX.jar ��������Ȃ��ꍇ�A������ANT_HOME/lib�փR�s�[
                String jdependFileName = "jdepend-2.9.jar";
                String path = LimyQalabPluginUtils.getResourcePath(
                        "external-lib/" + jdependFileName);
                try {
                    LimyIOUtils.copyFile(new File(path), new File(antLibDir, jdependFileName));
                    LimyUIUtils.showConfirmDialog(
                            jdependFileName + " �� ANT_HOME/lib �ɃR�s�[���܂����B");
                } catch (IOException e) {
                    LimyEclipsePluginUtils.log(e);
                }
            }
        }

        if (env.getStore().getBoolean(LimyQalabConstants.ENABLE_JUNIT)) {
            if (antLibDir.list(new PrefixFileFilter("junit-")).length == 0) {
                // junit-XX.jar ��������Ȃ��ꍇ�A������ANT_HOME/lib�փR�s�[
                String junitFileName = "junit-4.1.jar";
                String path = LimyQalabPluginUtils.getResourcePath(
                        "external-lib/" + junitFileName);
                try {
                    LimyIOUtils.copyFile(new File(path), new File(antLibDir, junitFileName));
                    LimyUIUtils.showConfirmDialog(
                            junitFileName + " �� ANT_HOME/lib �ɃR�s�[���܂����B");
                } catch (IOException e) {
                    LimyEclipsePluginUtils.log(e);
                }
            }
        }

    }

}
