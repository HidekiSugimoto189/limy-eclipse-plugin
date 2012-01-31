/*
 * Created 2007/06/23
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
package org.limy.eclipse.qalab.action.toolbar;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.limy.common.ProcessUtils;
import org.limy.eclipse.common.io.LimyIOUtils;
import org.limy.eclipse.common.jdt.LimyJavaUtils;
import org.limy.eclipse.common.ui.LimyUIUtils;
import org.limy.eclipse.qalab.LimyQalabPluginUtils;
import org.limy.eclipse.qalab.action.QalabActionUtils;
import org.limy.eclipse.qalab.ant.CreateBuildXml;
import org.limy.eclipse.qalab.common.LimyQalabConstants;
import org.limy.eclipse.qalab.common.LimyQalabEnvironment;
import org.limy.eclipse.qalab.common.LimyQalabUtils;

/**
 * �e��v�������s�������N���X�ł��B
 * @author Naoki Iwami
 */
public abstract class AbstractPartAction
        implements IWorkbenchWindowActionDelegate, ToolbarAction {
    
    // ------------------------ Fields
    
    /** �ݒ� */
    private LimyQalabEnvironment env;
    
    /** �o�͐� */
    private Writer writer;
    
    /** ���݂̃��[�N�x���`�E�B���h�E */
    private IWorkbenchWindow window;
    
    /** ���݂̑I��͈� */
    private ISelection selection;
    
    /** �J�ڃ��j�^ */
    private IProgressMonitor progressMonitor;

    // ------------------------ Abstract Methods

    /**
     * @return
     * @throws IOException 
     */
    public abstract String[] getTargetNames() throws IOException;
    
    /**
     * ���|�[�g�t�@�C����URL��Ԃ��܂��B
     * @return ���|�[�g�t�@�C����URL
     */
    protected abstract File getReportHtml();

    // ------------------------ Implement Methods

    public void init(IWorkbenchWindow window) {
        this.window = window;
    }

    public void selectionChanged(IAction action, ISelection selection) {
        this.selection = selection;
    }

    public void run(final IAction action) {
        Job job = new Job(getClass().getSimpleName()) {

            @Override
            protected IStatus run(IProgressMonitor monitor) {
                try {
                    progressMonitor = monitor;
                    makeReport();
                    LimyUIUtils.openBrowser(getReportHtml().toURI().toURL());
//                    if (action instanceof ISelectionListener) {
//                        LimyCompatibleUtils.openBrowser(getReportHtml().toURI().toURL());
//                    }
                } catch (final CoreException e) {
                    window.getShell().getDisplay().syncExec(new Runnable() {
                        public void run() {
                            QalabActionUtils.showConfirmDialog(null, e.getMessage());
                        }
                    });
                    return Status.CANCEL_STATUS;
                } catch (final IOException e) {
                    window.getShell().getDisplay().syncExec(new Runnable() {
                        public void run() {
                            QalabActionUtils.showConfirmDialog(null, e.getMessage());
                        }
                    });
                    return Status.CANCEL_STATUS;
                } finally {
                    try {
                        FileUtils.writeByteArrayToFile(getDestFile(".eclipse.log"),
                                getWriter().toString().getBytes());
                    } catch (IOException e) {
                        return Status.CANCEL_STATUS;
                    }
                }
                return Status.OK_STATUS;
            }
        };
        job.schedule();
    }

    public void dispose() {
        // do nothing
    }
    
    // ------------------------ Protected Methods
    
    /**
     * ���̃A�N�V�������L�����ǂ������`�F�b�N���܂��B
     * <p>
     * �����N���X���ŁA�K�v�Ȃ�΂��̃��\�b�h���I�[�o�[���C�h���ĉ������B
     * </p>
     * @return �L���Ȃ��true
     */
    protected boolean checkActionEnabled() {
        return true;
    }

    // ------------------------ Protected Methods (final)

    /**
     * �ݒ���擾���܂��B
     * @return �ݒ�
     */
    protected final LimyQalabEnvironment getEnv() {
        return env;
    }
    
    /**
     * �v���W�F�N�g���[�g�f�B���N�g����Ԃ��܂��B
     * @return �v���W�F�N�g���[�g�f�B���N�g��
     */
    protected final File getBaseDir() {
        return env.getProject().getLocation().toFile();
    }

    /**
     * ���O�o�͐��Ԃ��܂��B
     * @return ���O�o�͐�
     */
    protected final Writer getWriter() {
        return writer;
    }
    
    /**
     * DEST�f�B���N�g�����t�@�C����Ԃ��܂��B
     * @param relativePath ���΃p�X
     * @return DEST�f�B���N�g�����t�@�C��
     */
    protected final File getDestFile(String relativePath) {
        return new File(getDestDir(), relativePath);
    }

    /**
     * �J�ڃ��j�^���擾���܂��B
     * @return �J�ڃ��j�^
     */
    protected final IProgressMonitor getProgressMonitor() {
        return progressMonitor;
    }

    protected final String getAntPath() {
        
        String antExe = "ant";
        if (Platform.OS_WIN32.equals(Platform.getOS())) {
            antExe = "ant.bat";
        }

        String antHome = System.getenv("ANT_HOME");
        if (antHome != null) {
            antExe = new File(new File(antHome, "bin"), antExe).getAbsolutePath();
        }

        return antExe;
    }
    
    protected final String getBuildXml() {
        return getEnv().getStore().getString(LimyQalabConstants.KEY_BUILD_XML);
    }
    
    // ------------------------ Private Methods

    /**
     * �v�����ʏo�͐�f�B���N�g����Ԃ��܂��B
     * @return �v�����ʏo�͐�f�B���N�g��
     */
    private File getDestDir() {
        IProject project = env.getProject();
        IPreferenceStore store = env.getStore();
        File projectDir = project.getLocation().toFile();
        return new File(projectDir, store.getString(LimyQalabConstants.KEY_DEST_DIR));
    }

    /**
     * ���|�[�g�o�͏��������s���܂��B
     * @throws CoreException
     * @throws IOException
     */
    private void makeReport() throws CoreException, IOException {
        
        // TODO env��disable�Ȃ�Ώ��������Ȃ�
        writer = new StringWriter();
        List<IJavaElement> javaElements = getSelectedJavaElements();
        if (!javaElements.isEmpty()) {
            env = LimyQalabPluginUtils.createEnv(
                    javaElements.iterator().next().getJavaProject().getProject());
            createBuildXml();
            makeReportWithAnt();
            File file = LimyQalabUtils.createTempFile(env.getProject(), "report.log");
            LimyIOUtils.saveFile(file, writer.toString().getBytes());
        }
    }

    /**
     * �I�����ꂽ�SJava�v�f��Ԃ��܂��B
     * @return �I�����ꂽ�SJava�v�f
     */
    private List<IJavaElement> getSelectedJavaElements() {
        final List<IJavaElement> javaElements = new ArrayList<IJavaElement>();
        window.getShell().getDisplay().syncExec(new Runnable() {
            public void run() {
                try {
                    javaElements.addAll(LimyJavaUtils.getSelectedJavaElements(window, selection));
                } catch (JavaModelException e) {
                    QalabActionUtils.showConfirmDialog(null, e.getMessage());
                }
            }
        });
        return javaElements;
    }
    
    /**
     * Ant���s�p��build.xml�t�@�C�����쐬���܂��B
     * @throws CoreException
     * @throws IOException
     */
    private void createBuildXml() throws CoreException, IOException {
        CreateBuildXml builder = new CreateBuildXml();
        builder.prepareBuildFiles(env);
        builder.createFiles(env);
    }
    
    /**
     * Ant�����s���܂��Bbuild.xml �͊��ɑ��݂��Ă���K�v������܂��B
     * <p>
     * �^�[�Q�b�g���� getTargetNames() ���\�b�h����擾���܂��B
     * </p>
     * @throws IOException
     * @see {@link AbstractPartAction#getTargetNames()}
     */
    private void makeReportWithAnt() throws IOException {
        Collection<String> args = new ArrayList<String>();
        args.add(getAntPath());
        args.add("-f");
        args.add(getBuildXml());

        args.addAll(Arrays.asList(getTargetNames()));
        
        ProcessUtils.execProgram(getBaseDir(), getWriter(),
                args.toArray(new String[args.size()]));
        
    }
    
}
