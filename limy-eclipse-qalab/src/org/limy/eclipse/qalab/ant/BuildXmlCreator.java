/*
 * Created 2007/01/27
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
package org.limy.eclipse.qalab.ant;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.preference.IPreferenceStore;
import org.limy.eclipse.common.LimyEclipsePluginUtils;
import org.limy.eclipse.qalab.common.AntCreator;
import org.limy.eclipse.qalab.common.LimyQalabConstants;
import org.limy.eclipse.qalab.common.LimyQalabEnvUtils;
import org.limy.eclipse.qalab.common.LimyQalabEnvironment;
import org.limy.eclipse.qalab.common.LimyQalabUtils;
import org.limy.xml.XmlElement;
import org.limy.xml.XmlUtils;

/**
 * build.xml�p��XML�v�f�𐶐�����N���X�ł��B
 * @author Naoki Iwami
 */
public class BuildXmlCreator {

    /**
     * build.xml�p��XML�v�f���쐬���܂��B
     * @param env 
     * @param warningInfo �쐬���ɔ��������x���̊i�[��
     * @return XML�v�f
     * @throws FileNotFoundException �K�v�ȃt�@�C�������݂��Ȃ��ꍇ
     * @throws CoreException �R�A��O
     */
    public XmlElement createElement(LimyQalabEnvironment env,
            BuildWarning warningInfo) throws FileNotFoundException, CoreException {
        
        IJavaProject project = env.getJavaProject();
        IPreferenceStore store = env.getStore();
        
        XmlElement root = XmlUtils.createElement("project");
        root.setAttribute("name", project.getElementName());
        root.setAttribute("basedir", ".");
        root.setAttribute("default", "all");

        XmlElement propEl = XmlUtils.createElement(root, "property");
        propEl.setAttribute("file", "${basedir}/"
                + store.getString(LimyQalabConstants.KEY_BUILD_PROP));
        
        XmlElement cleanEl = createTargetElement(root, "clean");
        XmlElement cleanSubEl = XmlUtils.createElement(cleanEl, "delete");
        cleanSubEl.setAttribute("dir", "${dest.dir}");

        createMergesrcTarget(env, root);

        AntCreator[] creators = LimyCreatorUtils.decideCreators(env);
        
        List<AntCreator> okCreators = new ArrayList<AntCreator>();
        for (AntCreator creator : creators) {
            XmlElement orgRoot = root.cloneSelf();
            try {

                creator.exec(root, env);
                okCreators.add(creator);
            } catch (IOException e) {
                warningInfo.addException(e);
                root = orgRoot;
            }
        }

        AntCreator[] enableCreators = okCreators.toArray(
                new AntCreator[okCreators.size()]);

        XmlElement orgRoot;
        orgRoot = root.cloneSelf();
        try {
            QalabCreator creator = new QalabCreator();
            creator.exec(root, env, enableCreators);
            okCreators.add(creator);
            enableCreators = okCreators.toArray(new AntCreator[okCreators.size()]);
        } catch (FileNotFoundException e) {
            warningInfo.addException(e);
            root = orgRoot;
        }
        
        createExecTarget(root, enableCreators);
        
        XmlElement finishEl = createTargetElement(root, "finish");
        XmlElement touchEl = XmlUtils.createElement(finishEl, "touch");
        touchEl.setAttribute("file", "${dest.dir}/.finish");
        
        return root;
    }

    // ------------------------ Private Methods

    /**
     * mergesrc�^�[�Q�b�g���쐬���܂��B
     * @param env 
     * @param parent �e�v�f
     * @throws CoreException �R�A��O
     */
    private void createMergesrcTarget(LimyQalabEnvironment env,
            XmlElement parent)
            throws CoreException {

        IJavaProject project = env.getJavaProject();
        
        XmlElement mergeEl = createTargetElement(parent, "mergesrc");
        XmlElement copyEl = XmlUtils.createElement(mergeEl, "copy");
        copyEl.setAttribute("todir", "${all.src.dir}");
        
        for (IPath path : env.getMainSourcePaths()) {
            
            XmlElement fileEl = XmlUtils.createElement(copyEl, "fileset");
            fileEl.setAttribute("dir", LimyQalabUtils.createFullPath(project, path));

            try {
                addExcludes(env, fileEl, path.segmentCount() == 1);
            } catch (IOException e) {
                LimyEclipsePluginUtils.log(e);
            }
            
        }
        
    }

    /**
     * fileset�v�f��excludes������ݒ肵�܂��B
     * @param env 
     * @param fileEl fileset�v�f
     * @param isRoot ���[�g�v�f�i�v���W�F�N�g�j�̏ꍇtrue
     * @throws IOException 
     * @throws CoreException 
     */
    private void addExcludes(LimyQalabEnvironment env, XmlElement fileEl, boolean isRoot)
            throws CoreException, IOException {

        String ignoreStr = LimyQalabEnvUtils.createIgnoreStr(env, isRoot);
        
        fileEl.setAttribute("excludes", ignoreStr);
    }

    
    /**
     * ���s�p�^�[�Q�b�g���쐬���܂��B
     * @param root XML���[�g�v�f
     * @param enableCreators �L����QA�c�[���ꗗ
     */
    private void createExecTarget(XmlElement root, AntCreator[] enableCreators) {
        
        XmlElement targetEl1 = createTargetElement(root, "a1-prepare");
        String target = LimyCreatorUtils.createTargetString(enableCreators, null, 0, 1, 2);
        if (target.length() > 0) {
            targetEl1.setAttribute("depends", target);
        }

        XmlElement targetEl2 = createTargetElement(root, "a2-calc");
        targetEl2.setAttribute("depends", "qalab");

        XmlElement targetEl3 = createTargetElement(root, "a3-report");
        addDepends(enableCreators, targetEl3);

        XmlElement targetEl4 = createTargetElement(root, "all");
        targetEl4.setAttribute("depends", "clean, a1-prepare, a2-calc, a3-report");

    }

    /**
     * �^�[�Q�b�g�v�f�ia3-report�j��depends���Z�b�g���܂��B
     * @param enableCreators �L����QA�c�[���ꗗ
     * @param targetEl �^�[�Q�b�g�v�f
     */
    private void addDepends(AntCreator[] enableCreators, XmlElement targetEl) {
        
        StringBuilder depends = new StringBuilder();
        
        // ���|�[�g�o�݂͂̂�QA�c�[��
        depends.append(LimyCreatorUtils.createTargetString(enableCreators, null, 3));
        
        // �v������у��|�[�g�o�͂̂���QA�c�[��
        if (depends.length() > 0) {
            depends.append(',');
        }
        depends.append(LimyCreatorUtils.createTargetString(
                enableCreators, "-report-only", 0, 1, 2, 4));
        
        if (depends.length() == 0) {
            // depends����������ꍇ
            return;
        }
        
        if (LimyCreatorUtils.createTargetString(enableCreators, null, 0).length() > 0) {
            depends.append(",qalab-chart");
        }
        if (LimyCreatorUtils.createTargetString(enableCreators, null, 1).length() > 0) {
            depends.append(",qalab-chart-coverage");
        }

        targetEl.setAttribute("depends", depends.toString());
    }
    
    /**
     * target�v�f���쐬���܂��B
     * @param parent �e�v�f
     * @param targetName �^�[�Q�b�g��
     * @return �쐬���ꂽtarget�v�f
     */
    private XmlElement createTargetElement(XmlElement parent,
            String targetName) {
        
        XmlElement el = XmlUtils.createElement(parent, "target");
        el.setAttribute("name", targetName);
        return el;
    }


}
