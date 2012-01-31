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

import org.eclipse.jface.preference.IPreferenceStore;
import org.limy.eclipse.qalab.LimyQalabPluginUtils;
import org.limy.eclipse.qalab.common.AntCreator;
import org.limy.eclipse.qalab.common.LimyQalabConstants;
import org.limy.eclipse.qalab.common.LimyQalabEnvironment;
import org.limy.xml.XmlElement;
import org.limy.xml.XmlUtils;

/**
 * PMD�p��Ant�v�f�𐶐�����N���X�ł��B
 * @author Naoki Iwami
 * @version 1.0.0
 */
public class PmdCreator extends AbstractAntCreator {
    
    /**
     * ���o����ŏ��d���g�[�N����
     */
    private static final String MIN_TOKEN_COUNT = "50"; // TODO �ςɂ�����

    // ------------------------ Implement Methods

    public void exec(XmlElement root, LimyQalabEnvironment env) throws FileNotFoundException {
        appendPmdElement(root);
    }
    
    public String[] getQalabClassNames() {
        return new String[] {
                "net.objectlab.qalab.parser.PMDStatMerge",
//                "net.objectlab.qalab.parser.PMDCPDStatMerge",
                "org.limy.eclipse.qalab.parser.PmdCpdStatMerge",
        };
    }

    public String[] getReportXmlNames() {
        return new String[] {
                "pmd_report.xml",
                "pmd_cpd_report.xml",
        };
    }

    public int getSummaryGroup() {
        return 0;
    }

    public String[] getSummaryTypes() {
        return new String[] { "pmd", "pmd-cpd" };
    }

    public String getTargetName() {
        return "pmd";
    }

    public boolean isEnable(IPreferenceStore store) {
        return store.getBoolean(LimyQalabConstants.ENABLE_PMD);
    }

    public AntCreator newInstance() {
        return new PmdCreator();
    }

    // ------------------------ Private Methods
    
    private void appendPmdElement(XmlElement root)
            throws FileNotFoundException {
        
        createPathElement(root);
        createTaskdef(root);
        createMainTarget(root);
        createReportTarget(root);
        
    }


    /**
     * taskdef�v�f���쐬���܂��B
     * @param root ���[�g�v�f
     */
    private void createTaskdef(XmlElement root) {
        
        createTaskdefElement(root, "cpd",
                "net.sourceforge.pmd.cpd.CPDTask", "pmd.classpath");
        
        
//        createTaskdefElement(root, "pmd",
//                "net.sourceforge.pmd.ant.PMDTask", "pmd.classpath");

        createTaskdefElement(root, "pmd",
                "org.limy.eclipse.qalab.ant.pmd.LimyPmdTask", "pmd.classpath");

    }
    
    /**
     * path�v�f���쐬���܂��B
     * @param root ���[�g�v�f
     * @throws FileNotFoundException �K�v�ȃt�@�C�������݂��Ȃ��ꍇ
     */
    private void createPathElement(XmlElement root) throws FileNotFoundException {
        XmlElement pathEl = XmlUtils.createElement(root, "path");
        pathEl.setAttribute("id", "pmd.classpath");

        addPathElementMyPackage(pathEl);

        // ruleset�p
        createPathelement(pathEl, LimyQalabPluginUtils.getResourcePath(""));

        addPathElementPrefix(pathEl, "jaxen");
        addPathElementPrefix(pathEl, "jakarta-oro");
        addPathElement(pathEl, "asm-3.0.jar");
        addPathElement(pathEl, "backport-util-concurrent.jar");
//        addPathElement(pathEl, "pmd.jar");
        addPathElementPrefix(pathEl, "pmd");
        addPathElement(pathEl, "crimson.jar");
//        addPathElement(pathEl, "xercesImpl.jar");
//        addPathElement(pathEl, "limy-core.jar");
        
    }

    /**
     * PMD�v���^�[�Q�b�g���쐬���܂��B
     * @param root ���[�g�v�f
     */
    private void createMainTarget(XmlElement root) {
        XmlElement targetEl = createTargetElement(root, "pmd", "init");
        
        XmlElement copyEl = createElement(targetEl, "copy");
        copyEl.setAttribute("tofile", "${dest.dir}/pmd.xml");
        copyEl.setAttribute("file", "${pmd.config}");

        XmlElement pmdEl = createElement(targetEl, "pmd");
        pmdEl.setAttribute("shortFilenames", "true");
        pmdEl.setAttribute("targetjdk", "${jdk.version}");
        pmdEl.setAttribute("encoding", "${src.encoding}");
        pmdEl.setAttribute("rulesetfiles", "${pmd.config}");
        pmdEl.setAttribute("supportUTF8", "true");
        
        XmlElement formatEl = createElement(pmdEl, "formatter", "type", "xml");
        formatEl.setAttribute("toFile", "${dest.dir}/pmd_report.xml");
        createFilesetAllSrc(pmdEl);
        
        XmlElement cpdEl = XmlUtils.createElement(targetEl, "cpd");
        cpdEl.setAttribute("minimumTokenCount", MIN_TOKEN_COUNT);
        cpdEl.setAttribute("outputFile", "${dest.dir}/pmd_cpd_report.xml");
        cpdEl.setAttribute("format", "xml");
        cpdEl.setAttribute("encoding", "${src.encoding}");
        
        createFilesetAllSrc(cpdEl);

    }
    
    /**
     * PMD���|�[�g�o�̓^�[�Q�b�g���쐬���܂��B
     * @param root ���[�g�v�f
     */
    private void createReportTarget(XmlElement root) {
        XmlElement targetEl = createTargetElement(root, "pmd-report-only", "init");
        
        createVmstyleElement(targetEl,
                "${dest.dir}/pmd_report.xml",
                "${dest.dir}/pmd_report.html",
                "pmd/index.vm");

        XmlElement styleEl2 = createVmstyleElement(targetEl,
                "${dest.dir}/pmd_cpd_report.xml",
                "${dest.dir}/pmd_cpd_report.html",
                "pmd_cpd/index.vm");
        addVmParam(styleEl2, "minimumTokenCount", MIN_TOKEN_COUNT);

    }


}
