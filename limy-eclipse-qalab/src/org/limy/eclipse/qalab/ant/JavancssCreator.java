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

import java.io.FileNotFoundException;

import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.limy.eclipse.qalab.common.AntCreator;
import org.limy.eclipse.qalab.common.LimyQalabConstants;
import org.limy.eclipse.qalab.common.LimyQalabEnvironment;
import org.limy.xml.XmlElement;
import org.limy.xml.XmlUtils;


/**
 * Javancss�p��Ant�v�f�𐶐�����N���X�ł��B
 * @author Naoki Iwami
 */
public class JavancssCreator extends AbstractAntCreator {

    // ------------------------ Implement Methods

    public void exec(XmlElement root, LimyQalabEnvironment env)
            throws FileNotFoundException, JavaModelException {
        
        appendJavancssElement(root);
    }
    
    public String[] getQalabClassNames() {
        return new String[] { "org.limy.eclipse.qalab.parser.JavaNCSSStatMerge" };
    }

    public String[] getReportXmlNames() {
        return new String[] { "javancss_report.xml" };
    }
    
    public int getSummaryGroup() {
        return 2;
    }

    public String[] getSummaryTypes() {
        return new String[] { "javancss" };
    }

    public String getTargetName() {
        return "javancss";
    }
    
    public boolean isEnable(IPreferenceStore store) {
        return store.getBoolean(LimyQalabConstants.ENABLE_NCSS);
    }

    public AntCreator newInstance() {
        return new JavancssCreator();
    }

    // ------------------------ Private Methods

    private void appendJavancssElement(XmlElement root)
            throws FileNotFoundException {

        createTaskdef(root);
        createMainTarget(root);
        createReportTarget(root);
    }

    /**
     * taskdef�v�f���쐬���܂��B
     * @param root ���[�g�v�f
     * @throws FileNotFoundException �K�v�ȃt�@�C�������݂��Ȃ��ꍇ
     */
    private void createTaskdef(XmlElement root) throws FileNotFoundException {
        
        XmlElement taskEl = createTaskdefElement(root,
                "javancss", "javancss.JavancssAntTask", null);

        XmlElement classEl = XmlUtils.createElement(taskEl, "classpath");
        addPathElement(classEl, "javancss.jar");
        addPathElement(classEl, "ccl.jar");
        addPathElement(classEl, "jhbasic.jar");

    }
    
    /**
     * Javancss�v���^�[�Q�b�g���쐬���܂��B
     * @param root ���[�g�v�f
     */
    private void createMainTarget(XmlElement root) {
        XmlElement targetEl = createTargetElement(root, "javancss", "init");

        XmlElement mainEl = XmlUtils.createElement(targetEl, "javancss");
        mainEl.setAttribute("srcdir", "${all.src.dir}");
        mainEl.setAttribute("includes", "**/*.java");
        mainEl.setAttribute("generateReport", "true");
        mainEl.setAttribute("outputfile", "${dest.dir}/javancss_report.xml");
        mainEl.setAttribute("format", "xml");
        mainEl.setAttribute("charset", "${src.encoding}");
        
    }

    /**
     * Javancss���|�[�g�o�̓^�[�Q�b�g���쐬���܂��B
     * @param root ���[�g�v�f
     */
    private void createReportTarget(XmlElement root) {
        XmlElement targetEl = createTargetElement(root, "javancss-report-only", "init");

        createVmstyleElement(targetEl,
                "${dest.dir}/javancss_report.xml",
                "${dest.dir}/javancss_report.html",
                "javancss/index.vm");
        
    }

}
