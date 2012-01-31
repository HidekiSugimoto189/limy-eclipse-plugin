/*
 * Created 2007/08/29
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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.junit.internal.runners.TestClassRunner;
import org.junit.runner.Result;
import org.junit.runner.notification.RunNotifier;
import org.limy.eclipse.common.LimyEclipsePluginUtils;
import org.limy.eclipse.qalab.QalabJarFileFinder;
import org.limy.eclipse.qalab.common.LimyQalabEnvironment;
import org.limy.eclipse.qalab.common.LimyQalabJavaUtils;
import org.limy.eclipse.qalab.common.QalabResourceUtils;

/**
 * Cobertura���s��S�����܂��B
 * @author Naoki Iwami
 */
public class CoberturaExecuter {

    /** ���ݒ� */
    private final LimyQalabEnvironment env;

    /** �e�X�g���ʊi�[�� */
    private Collection<Result> testResults = new ArrayList<Result>();

    // ------------------------ Constructors

    public CoberturaExecuter(LimyQalabEnvironment env) {
        super();
        this.env = env;
    }

    /**
     * Java�N���X�̃e�X�g�����s���ăJ�o���b�W���v�����܂��B
     * @param javaResources Java�N���X�ꗗ
     * @return 
     * @throws IOException I/O��O
     * @throws CoreException �R�A��O
     */
    public Collection<Result> calculateCoverage(IResource... javaResources)
            throws IOException, CoreException {
        
        IProject project = env.getProject();
        IJavaProject javaProject = env.getJavaProject();
        
        // �N���X�p�X��instrument��ǉ�
        Collection<URL> priorityUrls = new ArrayList<URL>();
        priorityUrls.add(new File(project.getLocation().toFile(), ".instrument").toURI().toURL());
        priorityUrls.add(new File(
                new QalabJarFileFinder().getFileLocation("cobertura.jar")).toURI().toURL());

        Collection<URL> urls = new ArrayList<URL>();
        Collection<String> libraries = LimyQalabJavaUtils.getJavaLibraries(javaProject);
        for (String libraryPath : libraries) {
            urls.add(new File(libraryPath).toURI().toURL());
        }
        
        // Eclipse��ClassLoader(ContextFinder)�͎g��Ȃ�
//        ClassLoader orgClassLoader = Thread.currentThread().getContextClassLoader().getParent();      
//        ClassLoader loader = new LimyClassLoader(
//                priorityUrls.toArray(new URL[priorityUrls.size()]),
//                urls.toArray(new URL[urls.size()]),
//                orgClassLoader);
        
        // �����̃N���X���[�_��ޔ�
        ClassLoader orgClassLoader = Thread.currentThread().getContextClassLoader();
        
        Collection<URL> combineUrls = new ArrayList<URL>();
        combineUrls.addAll(priorityUrls);
        combineUrls.addAll(urls);
        ClassLoader loader = new URLClassLoader(
                combineUrls.toArray(new URL[combineUrls.size()]),
                orgClassLoader);
        
        // ���X���b�h�̃N���X���[�_���㏑��
        Thread.currentThread().setContextClassLoader(loader);

        // �e�X�g�����s�i�J�o���b�W�v���j
        boolean isExecuteTest = false;
        for (IResource javaResource : javaResources) {
            String testClassName = QalabResourceUtils.getQualifiedTestClassName(
                    env, javaResource);
//            System.out.println(testClassName + " exec...");

            try {
                
                TestClassRunner runner = new TestClassRunner(loader.loadClass(testClassName));
                Result result = new Result();
                RunNotifier notifier = new RunNotifier();
                notifier.addListener(result.createListener());
                runner.run(notifier);
//                Class<? extends TestCase> testClass
//                        = (Class<? extends TestCase>)loader.loadClass(testClassName);
//                
//                TestResult testResult = TestRunner.run(new TestSuite(testClass));
                testResults.add(result);
                isExecuteTest = true;
            } catch (ClassNotFoundException e) {
                // �e�X�g�P�[�X�����݂��Ȃ��ꍇ
            } catch (Throwable e) {
                // �e�X�g���ɃG���[�����������ꍇ
                LimyEclipsePluginUtils.log(e);
            }
        }
        
        // �����̃N���X���[�_�𕜋A
        Thread.currentThread().setContextClassLoader(orgClassLoader);
        
        if (isExecuteTest) {
            saveProjectData();
        }
        
        return testResults;
    }


    /**
     * Cobertura�ɏI���̍��}���o���Acobertura.ser �ɃJ�o���b�W���ʂ��o�͂����܂��B
     */
    private void saveProjectData() {

        try {
            String className = "net.sourceforge.cobertura.coveragedata.ProjectData";
            Class<?> saveClass = Class.forName(className);
            Method saveMethod = saveClass.getDeclaredMethod("saveGlobalProjectData", new Class[0]);
            saveMethod.invoke(null, new Object[0]);
            
        } catch (Exception e) {
            LimyEclipsePluginUtils.log(e);
        }
    }

}
