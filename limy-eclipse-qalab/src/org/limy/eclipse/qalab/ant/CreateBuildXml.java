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
package org.limy.eclipse.qalab.ant;

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.preference.IPreferenceStore;
import org.limy.eclipse.common.LimyEclipsePluginUtils;
import org.limy.eclipse.common.resource.LimyResourceUtils;
import org.limy.eclipse.qalab.common.LimyQalabConstants;
import org.limy.eclipse.qalab.common.LimyQalabEnvironment;
import org.limy.xml.XmlElement;
import org.limy.xml.XmlWriteUtils;

/**
 * builx.xml�t�@�C�����쐬���܂��B
 * @depend - - - CreateBuildProperties
 * @depend - - - BuildXmlCreator
 * @author Naoki Iwami
 */
public class CreateBuildXml {
    
    /** build.xml�̃t�@�C�����e */
    private String contentsBuildXml;
    
    /** build.properties�̃t�@�C�����e */
    private String contentsBuildProp;
    
    /**
     * build.xml, build.properties �t�@�C���̍쐬�������s���܂��B
     * @param env QALab���ݒ���e
     * @return �쐬���ɔ��������x�����e
     * @throws CoreException Core��O
     */
    public BuildWarning prepareBuildFiles(LimyQalabEnvironment env)
            throws CoreException {

        BuildWarning warningInfo = new BuildWarning();
        
        try {
            
            contentsBuildProp = new CreateBuildProperties().createContents(env);

            StringBuilder out = new StringBuilder();

            XmlElement element = new BuildXmlCreator().createElement(env, warningInfo);
            XmlWriteUtils.writeXml(out, element);
            contentsBuildXml = out.toString();
            
        } catch (IOException e) {
            LimyEclipsePluginUtils.log(e);
            warningInfo.addException(e);
        }
        
        return warningInfo;
    }
    
    /**
     * build.xml, build.properties �t�@�C�����쐬���܂��B
     * @param env QALab���ݒ���e
     * @throws CoreException Core��O
     */
    public void createFiles(LimyQalabEnvironment env) throws CoreException {
        
        IJavaProject project = env.getJavaProject();
        IPreferenceStore store = env.getStore();

        IPath projectPath = project.getProject().getFullPath();

        String charset = project.getProject().getDefaultCharset();
        try {
            IPath path1 = projectPath.append(store.getString(LimyQalabConstants.KEY_BUILD_XML));
            LimyResourceUtils.createFile(path1, contentsBuildXml, charset);
            
            IPath path2 = projectPath.append(store.getString(LimyQalabConstants.KEY_BUILD_PROP));
            LimyResourceUtils.createFile(path2, contentsBuildProp, charset);
            
        } catch (IOException e) {
            LimyEclipsePluginUtils.log(e);
        }

    }

}
