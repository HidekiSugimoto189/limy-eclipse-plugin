/*
 * Created 2006/08/11
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
import java.io.FileNotFoundException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.limy.eclipse.core.LimyEclipsePlugin;
import org.limy.eclipse.qalab.LimyQalabPluginUtils;
import org.limy.eclipse.qalab.QalabJarFileFinder;
import org.limy.eclipse.qalab.common.AntCreator;
import org.limy.eclipse.qalab.common.LimyQalabEnvironment;
import org.limy.eclipse.qalab.common.LimyQalabUtils;
import org.limy.xml.XmlElement;
import org.limy.xml.XmlUtils;


/**
 * Ant�v�f�𐶐����钊�ۊ��N���X�ł��B
 * @author Naoki Iwami
 * @version 1.0.0
 */
public abstract class AbstractAntCreator implements AntCreator {
    
    // ------------------------ Protected Methods

    /**
     * �N���X�p�X�Ɏw��̃t�@�C����ǉ����܂��B
     * @param classEl classpath�v�f
     * @param fileName �t�@�C����
     * @throws FileNotFoundException jar�t�@�C����������Ȃ��ꍇ
     */
    protected void addPathElement(XmlElement classEl, final String fileName)
            throws FileNotFoundException {
        
        createPathelement(classEl, getFileLocation(fileName));
    }

    
    /**
     * �N���X�p�X�Ɏw���jar�t�@�C����ǉ����܂��B
     * @param classEl classpath�v�f
     * @param prefix jar�t�@�C�����v���t�B�b�N�X
     * @throws FileNotFoundException jar�t�@�C����������Ȃ��ꍇ
     */
    protected void addPathElementPrefix(XmlElement classEl, final String prefix)
            throws FileNotFoundException {
        
        createPathelement(classEl, getPrefixFileLocation(prefix));
    }

    /**
     * �N���X�p�X��Plugin���N���X�ւ̃p�X��ǉ����܂��B
     * @param classEl classpath�v�f
     */
    protected void addPathElementMyPackage(XmlElement classEl) {
        // �z�z�p
        createPathelement(classEl, LimyQalabPluginUtils.getPath(""));
        // �J���p
        createPathelement(classEl, LimyQalabPluginUtils.getPath("bin"));
    }

    /**
     * �N���X�p�X��Core�p�b�P�[�W�ւ̃p�X��ǉ����܂��B
     * @param classEl classpath�v�f
     */
    protected void addPathElementCorePackage(XmlElement classEl) {
        File root = LimyEclipsePlugin.getDefault().getPluginRoot();
        // �z�z�p
        createPathelement(classEl, root.getAbsolutePath());
        // �J���p
        createPathelement(classEl, new File(root, "bin").getAbsolutePath());
        // velocity-dep.jar
        createPathelement(classEl,
                new File(root, "resource/lib/velocity-dep-1.5.jar").getAbsolutePath());
//        createPathelement(classEl,
//                new File(root, "resource/lib/limy-core.jar").getAbsolutePath());
        
    }

    /**
     * pathelement�v�f���쐬���܂��B
     * @param parent �e�v�f
     * @param location ���P�[�V����
     */
    protected void createPathelement(XmlElement parent, String location) {
        XmlElement pathEl = XmlUtils.createElement(parent, "pathelement");
        pathEl.setAttribute("location", location);
    }
    
    /**
     * VM�pparam�v�f��ǉ����܂��B
     * @param parent �e�v�f�inull�j
     * @param name param��
     * @param value param�l
     */
    protected void addVmParam(XmlElement parent, String name, String value) {
        if (parent != null) {
            XmlElement paramEl = XmlUtils.createElement(parent, "param");
            paramEl.setAttribute("name", name);
            if (value != null) {
                paramEl.setAttribute("expression", value);
            }
        }
    }

    /**
     * VM�pparam�v�f�i�Œ�l"on"�j��ǉ����܂��B
     * @param parent �e�v�f�inull�j
     * @param name param��
     */
    protected void addVmParam(XmlElement parent, String name) {
        if (parent != null) {
            XmlElement paramEl = XmlUtils.createElement(parent, "param");
            paramEl.setAttribute("name", name);
            paramEl.setAttribute("expression", "on");
        }
    }

    /**
     * param�v�f��ǉ����܂��B
     * @param parent �e�v�f�inull�j
     * @param name param��
     * @param value param�l
     */
    protected void addParam(XmlElement parent, String name, String value) {
        if (parent != null) {
            XmlElement paramEl = XmlUtils.createElement(parent, "param");
            paramEl.setAttribute("name", name);
            if (value != null) {
                paramEl.setAttribute("value", value);
            }
        }
    }

    /**
     * target�v�f�𐶐����܂��B
     * @param parent �e�v�f
     * @param name �^�[�Q�b�g��
     * @param depends �ˑ��^�[�Q�b�g
     * @return ���������^�[�Q�b�g�v�f
     */
    protected XmlElement createTargetElement(
            XmlElement parent, String name, String depends) {
        
        XmlElement targetEl = XmlUtils.createElement(parent, "target");
        targetEl.setAttribute("name", name);
        targetEl.setAttribute("depends", depends);
        return targetEl;
    }

    /**
     * sysproperty�v�f�𐶐����܂��B
     * @param parent �e�v�f
     * @param key key�l
     * @param value value�l
     */
    protected void createSyspropertyElement(
            XmlElement parent, String key, String value) {
        
        XmlElement el = XmlUtils.createElement(parent, "sysproperty");
        el.setAttribute("key", key);
        el.setAttribute("value", value);
    }

//    /**
//     * classpath�v�f�𐶐����܂��B
//     * @param antVersion Ant�o�[�W����
//     * @param parent �e�v�f
//     * @param paths �p�X������ꗗ
//     */
//    protected void createClasspathElement(int antVersion,
//            XmlElement parent, Collection<String> paths) {
//        
//        switch (antVersion) {
//        case LimyQalabConstants.ANT_VERSION_16:
//            for (String path : paths) {
//                XmlElement classEl = XmlUtils.createElement(parent, "classpath");
//                classEl.addAttribute("location", path);
//            }
//            break;
//        case LimyQalabConstants.ANT_VERSION_17:
//            XmlElement classEl = XmlUtils.createElement(parent, "classpath");
//            for (String path : paths) {
//                createPathelement(classEl, path);
//            }
//            break;
//        default:
//            break;
//        }
//    }
    
    /**
     * classpath�v�f�𐶐����܂��B
     * @param parent �e�v�f
     * @param refid �Q��ID
     */
    protected void createClasspathElement(XmlElement parent, String refid) {
        XmlElement el = XmlUtils.createElement(parent, "classpath");
        el.setAttribute("refid", refid);

    }

    /**
     * taskdef�v�f�𐶐����܂��B
     * @param root �e�v�f
     * @param name taskdef��
     * @param className Task�N���X��
     * @param classpathRef �N���X�p�XID
     * @return ���������v�f
     */
    protected XmlElement createTaskdefElement(XmlElement root, String name,
            String className, String classpathRef) {
        
        XmlElement taskEl = XmlUtils.createElement(root, "taskdef");
        taskEl.setAttribute("name", name);
        taskEl.setAttribute("classname", className);
        if (classpathRef != null) {
            taskEl.setAttribute("classpathref", classpathRef);
        }
        return taskEl;
    }

    /**
     * vmstyle�v�f�𐶐����܂��B
     * @param root �e�v�f
     * @param inFiles ����XML�t�@�C���ꗗ
     * @param out �o�̓t�@�C��
     * @param style �p�[�X����vm�t�@�C���iresource����̑��΃p�X�j
     * @param toolClass �c�[���N���X��
     * @return ���������v�f
     */
    protected XmlElement createVmstyleElement(XmlElement root,
            String[] inFiles, String out, String style, String toolClass) {
        
        XmlElement styleEl = XmlUtils.createElement(root, "vmstyle");
        for (String file : inFiles) {
            createElement(styleEl, "infile", "value", file);
//            styleEl.setAttribute("in", file);
        }
        styleEl.setAttribute("out", out);
        styleEl.setAttribute("style", LimyQalabPluginUtils.getResourcePath(style));
        if (toolClass != null) {
            styleEl.setAttribute("toolClass", toolClass);
        }
        return styleEl;
    }

    /**
     * vmstyle�v�f�𐶐����܂��B
     * @param root �e�v�f
     * @param in ����XML�t�@�C��
     * @param out �o�̓t�@�C��
     * @param style �p�[�X����vm�t�@�C���iresource����̑��΃p�X�j
     * @return ���������v�f
     */
    protected XmlElement createVmstyleElement(XmlElement root,
            String in, String out, String style) {
        return createVmstyleElement(root, new String[] { in }, out, style, null);
    }

    /**
     * vmstyle�v�f�𐶐����܂��B
     * @param root �e�v�f
     * @param out �o�̓t�@�C��
     * @param style �p�[�X����vm�t�@�C���iresource����̑��΃p�X�j
     * @return ���������v�f
     */
    protected XmlElement createVmstyleElement(XmlElement root,
            String out, String style) {
        return createVmstyleElement(root, new String[0], out, style, null);
    }

    /**
     * vmstyle�v�f�𐶐����܂��B
     * @param root �e�v�f
     * @param inFiles ����XML�t�@�C���ꗗ
     * @param out �o�̓t�@�C��
     * @param style �p�[�X����vm�t�@�C���iresource����̑��΃p�X�j
     * @return ���������v�f
     */
    protected XmlElement createVmstyleElement(XmlElement root,
            String[] inFiles, String out, String style) {
        return createVmstyleElement(root, inFiles, out, style, null);
    }

    /**
     * @param parent
     * @param elementName
     * @return 
     */
    protected XmlElement createElement(XmlElement parent, String elementName) {
        
        return XmlUtils.createElement(parent, elementName);
    }
    
    /**
     * @param parent
     * @param elementName
     * @param attrName
     * @param attrValue
     * @return 
     */
    protected XmlElement createElement(XmlElement parent, String elementName,
            String attrName, String attrValue) {
        
        XmlElement element = XmlUtils.createElement(parent, elementName);
        element.setAttribute(attrName, attrValue);
        return element;
    }
    
    /**
     * �SJava�t�@�C��������filset�v�f���쐬���܂��B
     * @param parent �e�v�f
     */
    protected void createFilesetAllSrc(XmlElement parent) {
        XmlElement filesetEl = XmlUtils.createElement(parent, "fileset");
        filesetEl.setAttribute("dir", "${all.src.dir}");
        filesetEl.setAttribute("includes", "**/*.java");
    }

    /**
     * �SJava�t�@�C��������filset�v�f���쐬���܂��BQA�ΏۊO�\�[�X���܂߂܂��B
     * @param env ��
     * @param parent �e�v�f
     * @throws CoreException �R�A��O
     */
    protected void createFilesetFullSrc(LimyQalabEnvironment env, XmlElement parent)
            throws CoreException {
        
        IJavaProject project = env.getJavaProject();
        for (IPath path : env.getMainSourcePaths()) {
            XmlElement filesetEl = XmlUtils.createElement(parent, "fileset");
            filesetEl.setAttribute("dir", LimyQalabUtils.createFullPath(project, path));
            filesetEl.setAttribute("includes", "**/*.java");
        }
    }

    protected String getPrefixFileLocation(String prefix) throws FileNotFoundException {
        return new QalabJarFileFinder().getPrefixFileLocation(prefix);
    }

    protected String getFileLocation(String fileName) throws FileNotFoundException {
        return new QalabJarFileFinder().getFileLocation(fileName);
    }

    // ------------------------ Private Methods


}
