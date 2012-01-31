/*
 * Created 2006/12/01
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

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.preference.IPreferenceStore;
import org.limy.eclipse.qalab.LimyQalabPlugin;
import org.limy.eclipse.qalab.LimyQalabPluginUtils;
import org.limy.eclipse.qalab.common.LimyQalabConstants;
import org.limy.eclipse.qalab.common.LimyQalabEnvironment;
import org.limy.eclipse.qalab.common.LimyQalabUtils;

import edu.umd.cs.findbugs.Version;

/**
 * build.properties�t�@�C���o�̓N���X�ł��B
 * @author Naoki Iwami
 */
public class CreateBuildProperties {

    /** ���s���� */
    private static final char BR = '\n';
    
    /**
     * build.properties�t�@�C���̓��e���쐬���܂��B
     * @param env 
     * @param store �v���W�F�N�g�pPreferenceStore
     * @return 
     * @throws IOException I/O��O
     * @throws CoreException Core��O
     */
    public String createContents(LimyQalabEnvironment env) throws IOException, CoreException {
        
        IProject project = env.getProject();
        IPreferenceStore store = env.getStore();
        
        File projectDir = project.getLocation().toFile();

        StringBuilder out = new StringBuilder();
            
        File destDir = new File(projectDir, store.getString(LimyQalabConstants.KEY_DEST_DIR));
        out.append("dest.dir = ").append(
                destDir.getAbsolutePath().replace('\\', '/'))
                .append(BR);
//            out.append("dest.dir = ").append(destDir.replace('\\', '/')).append(BR);
        
        out.append("all.src.dir = ${dest.dir}/src").append(BR);
        
        out.append("findbugs.home = ");
        out.append(LimyQalabPluginUtils.getResourcePath("external-lib/findbugs")
                .replace('\\', '/'));
        out.append(BR);
        
        out.append("findbugs.version = ");
        out.append(Version.RELEASE).append(BR);
        
        out.append("src.encoding = ");
        out.append(project.getDefaultCharset()).append(BR);

        out.append("dot.exec = ");
        out.append(store.getString(LimyQalabConstants.KEY_DOT_EXE).replace('\\', '/')).append(BR);
        
        out.append("plugin.version = ");
        String version = (String)LimyQalabPlugin.getDefault().getBundle()
                .getHeaders().get(org.osgi.framework.Constants.BUNDLE_VERSION);
        out.append(version).append(BR);

        appendJdkVersion(out, env.getJavaProject());
        appendCheckstyleConfig(out, env);
        appendPmdConfig(out, env);
        appendQalabXml(out, store);
        
        return out.toString();
    }

    // ------------------------ Private Methods

    /**
     * qalab.xml ���o�͂��܂��B
     * @param out �o�͐�
     * @param store �v���W�F�N�g�pPreferenceStore
     * @throws IOException I/O��O
     */
    private void appendQalabXml(Appendable out, IPreferenceStore store)
            throws IOException {
        String qalabDir = store.getString(LimyQalabConstants.KEY_QALAB_XML);
        if (qalabDir.startsWith("/")) {
            qalabDir = qalabDir.substring(1);
        }
        if (qalabDir.length() > 0 && !"/".equals(qalabDir) && !qalabDir.endsWith("/")) {
            qalabDir += "/";
        }
        out.append("qalab.xml = ${basedir}/");
        out.append(qalabDir.replace('\\', '/'));
        out.append("qalab.xml").append(BR);
    }

    /**
     * jdk.version ���o�͂��܂��B
     * @param out �o�͐�
     * @param project Java�v���W�F�N�g
     * @throws IOException I/O��O
     */
    private void appendJdkVersion(Appendable out, IJavaProject project)
            throws IOException {
        String jdkVersion = LimyQalabUtils.getJdkVersion(project);
        out.append("jdk.version = ").append(jdkVersion).append(BR);
    }

    /**
     * checkstyle.config ���o�͂��܂��B
     * @param out �o�͐�
     * @param env 
     * @throws IOException I/O��O
     */
    private void appendCheckstyleConfig(Appendable out, LimyQalabEnvironment env)
            throws IOException {

        File file = LimyQalabPluginUtils.getConfigFile(env,
                "sun_checks.xml",
                LimyQalabConstants.KEY_CHK_TYPE, LimyQalabConstants.KEY_CHK_CFG);

        out.append("checkstyle.config = ");
        out.append(file.getAbsolutePath().replace('\\', '/'));
//        out.append("${dest.dir}/checkstyle.xml");
        out.append(BR);
    }

    /**
     * pmd.xml ���o�͂��܂��B
     * @param out �o�͐�
     * @param env 
     * @throws IOException I/O��O
     */
    private void appendPmdConfig(Appendable out, LimyQalabEnvironment env)
            throws IOException {
        
        File file = LimyQalabPluginUtils.getConfigFile(env,
                "pmd-ruleset.xml",
                LimyQalabConstants.KEY_PMD_TYPE, LimyQalabConstants.KEY_PMD_CFG);
        
        out.append("pmd.config = ");
        out.append(file.getAbsolutePath().replace('\\', '/'));
        out.append(BR);
    }

}
