/*
 * Created 2007/06/23
 * Copyright (C) 2003-2007  Naoki Iwami (naoki@limy.org)
 *
 * This file is part of Limy Eclipse Plugin.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.limy.eclipse.qalab.action.part;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.types.FileSet;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.limy.eclipse.common.LimyCompatibleUtils;
import org.limy.eclipse.common.LimyJavaUtils;
import org.limy.eclipse.qalab.FileFinder;
import org.limy.eclipse.qalab.LimyQalabConstants;
import org.limy.eclipse.qalab.LimyQalabEnvUtils;
import org.limy.eclipse.qalab.LimyQalabEnvironment;
import org.limy.eclipse.qalab.LimyQalabUtils;
import org.limy.eclipse.qalab.ProcessUtils;
import org.limy.eclipse.qalab.action.QalabActionUtils;
import org.limy.eclipse.qalab.ant.CreateBuildXml;
import org.limy.eclipse.qalab.tool.CheckstyleTool;
import org.limy.xml.VmParam;
import org.limy.xml.task.VmStyleTask;

/**
 * �e��v�������s�������N���X�ł��B
 * @author Naoki Iwami
 */
public abstract class AbstractPartAction /*extends AbstractHandler*/
        implements IWorkbenchWindowActionDelegate {
    
    // ------------------------ Fields
    
    /** �ݒ� */
    private LimyQalabEnvironment env;
    
    /** �o�͐� */
    private Writer writer;
    
    /** ���݂̃��[�N�x���`�E�B���h�E */
    private IWorkbenchWindow window;
    
    /** ���݂̑I��͈� */
    private ISelection selection;

    // ------------------------ Abstract Methods

    /**
     * @return
     */
    protected abstract String[] getTargetNames();
    
    /**
     * ���|�[�g���쐬���܂��B
     * @param env �ݒ�
     * @throws CoreException �R�A��O
     */
    protected abstract void makeReport() throws CoreException;
    
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
                    innerExecute(true);
                    if (action instanceof ISelectionListener) {
                        LimyCompatibleUtils.openBrowser(getReportHtml().toURI().toURL());
                    }
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
    
    // ------------------------ Public Methods

    public void innerExecute(boolean isInit) throws CoreException, IOException {
        
        writer = new StringWriter();
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
        if (!javaElements.isEmpty()) {
            env = LimyQalabUtils.createEnv(
                    javaElements.iterator().next().getJavaProject().getProject());
            if (isInit) {
                init();
            }
//            makeReport();
            makeReportWithAnt();
        }
    }
   

    /**
     * ���݂̑I��͈͂��擾���܂��B
     * @return ���݂̑I��͈�
     */
    public ISelection getSelection() {
        return selection;
    }
    
    /**
     * ���݂̃��[�N�x���`�E�B���h�E���擾���܂��B
     * @return ���݂̃��[�N�x���`�E�B���h�E
     */
    public IWorkbenchWindow getWindow() {
        return window;
    }
    
    /**
     * �ݒ���擾���܂��B
     * @return �ݒ�
     */
    public LimyQalabEnvironment getEnv() {
        return env;
    }
    
    // ------------------------ Protected Methods
    
    /**
     * �v���W�F�N�g���[�g�f�B���N�g����Ԃ��܂��B
     * @return �v���W�F�N�g���[�g�f�B���N�g��
     */
    protected File getBaseDir() {
        return env.getProject().getLocation().toFile();
    }

    /**
     * ���O�o�͐��Ԃ��܂��B
     * @return ���O�o�͐�
     */
    protected Writer getWriter() {
        return writer;
    }
    
    /**
     * �v�����ʏo�͐�f�B���N�g����Ԃ��܂��B
     * @return �v�����ʏo�͐�f�B���N�g��
     */
    protected File getDestDir() {
        IProject project = env.getProject();
        IPreferenceStore store = env.getStore();
        File projectDir = project.getLocation().toFile();
        return new File(projectDir, store.getString(LimyQalabConstants.KEY_DEST_DIR));
    }

    /**
     * DEST�f�B���N�g�����t�@�C����Ԃ��܂��B
     * @param relativePath ���΃p�X
     * @return DEST�f�B���N�g�����t�@�C��
     */
    protected File getDestFile(String relativePath) {
        return new File(getDestDir(), relativePath);
    }

    /**
     * �v���W�F�N�g�̕����G���R�[�f�B���O��Ԃ��܂��B
     * @return �v���W�F�N�g�̕����G���R�[�f�B���O
     * @throws CoreException �R�A��O
     */
    protected String getEncoding() throws CoreException {
        return env.getProject().getDefaultCharset();
    }

    /**
     * �\�[�X�f�B���N�g���iQA�Ώہj��Ԃ��܂��B
     * @return �\�[�X�f�B���N�g���iQA�Ώہj
     */
    protected File getAllSrcDir() {
        return new File(getDestDir(), "src");
    }

    protected IJavaProject getJavaProject() {
        return env.getJavaProject();
    }
    
    protected String getFile(String file) throws FileNotFoundException {
        return new FileFinder().getFileLocation(file);
    }

    protected String getFilePrefix(String prefix) throws FileNotFoundException {
        return new FileFinder().getPrefixFileLocation(prefix);
    }
    
    protected String createClasspath(String... files) {
        char separator = Platform.OS_WIN32.equals(Platform.getOS()) ? ';' : ':';
        StringBuilder buff = new StringBuilder();

        buff.append(LimyQalabUtils.getPath(""));
        buff.append(separator).append(LimyQalabUtils.getPath("bin"));

        for (String file : files) {
            buff.append(separator);
            buff.append(file);
        }
        return buff.toString();
    }
    
    protected void outputReport(String target, VmParam... params) {
        VmStyleTask task = new VmStyleTask();
        task.setOut(getDestFile(target + "_report.html"));
        task.setStyle(new File(LimyQalabUtils.getResourcePath(target + "/index.vm")));
        task.setIn(getDestFile(target + "_report.xml"));
        executeVmTask(task, params);
    }

    protected void outputReport(String style, String out, VmParam... params) {
        VmStyleTask task = new VmStyleTask();
        task.setOut(getDestFile(out));
        task.setStyle(new File(LimyQalabUtils.getResourcePath(style)));
        executeVmTask(task, params);
    }

    protected void outputReport(String style, File in, String out, VmParam... params) {
        VmStyleTask task = new VmStyleTask();
        task.setIn(in);
        task.setOut(getDestFile(out));
        task.setStyle(new File(LimyQalabUtils.getResourcePath(style)));
        executeVmTask(task, params);
    }

    /**
     * VmStyle�^�X�N�Ƀp�����[�^��ݒ肵�Ď��s���܂��B
     * @param task VmStyle�^�X�N
     * @param params �p�����[�^�ꗗ
     */
    protected void executeVmTask(VmStyleTask task, VmParam... params) {
        task.setToolClass(CheckstyleTool.class.getName());
        for (VmParam param : params) {
            VmParam vmParam = task.createParam();
            vmParam.setName(param.getName());
            vmParam.setExpression(param.getExpression());
        }

        ClassLoader orgLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
        try {
            task.execute();
        } finally {
            Thread.currentThread().setContextClassLoader(orgLoader);
        }
    }

    protected File getQalabFile() {
        return new File(
                getEnv().getProject().getLocation().toFile(), "qalab.xml");
    }

    // ------------------------ Private Methods

    private void init() throws CoreException, IOException {
        
        CreateBuildXml builder = new CreateBuildXml();
        builder.prepareBuildFiles(env);
        builder.createFiles(env);

//        getDestDir().mkdirs();
//        mergeSrc();
//        if (getDestFile("src").list().length == 0) {
//            throw new IOException("����\�[�X�t�@�C�������݂��܂���B");
//        }
//        
//        Copy task = new Copy();
//        task.setProject(new Project());
//        task.setTodir(getDestDir());
//        FileSet fileset = new FileSet();
//        fileset.setDir(new File(LimyQalabUtils.getResourcePath("")));
//        fileset.setIncludes("css/**,images/**,js/**");
//        task.addFileset(fileset);
//        task.execute();

    }

    private void mergeSrc() throws CoreException, IOException {
        
        Copy task = new Copy();
        task.setProject(new Project());
        task.setTodir(new File(getDestDir(), "src"));
        
        for (IPath path : env.getMainSourcePaths(true)) {
            
            FileSet fileset = new FileSet();
            fileset.setDir(new File(LimyQalabUtils.createFullPath(env.getJavaProject(), path)));
            fileset.setExcludes(LimyQalabEnvUtils.createIgnoreStr(env, path.segmentCount() == 1));
            task.addFileset(fileset);
        }
        task.execute();
        
    }

    private void makeReportWithAnt() throws IOException {
        Collection<String> args = new ArrayList<String>();
        args.add("ant.bat");
        args.add("-f");
        args.add("build.xml");
        args.addAll(Arrays.asList(getTargetNames()));
        
        ProcessUtils.execProgram(getBaseDir(), getWriter(),
                args.toArray(new String[args.size()]));
        
    }
    
}
