/*
 * Created 2006/08/05
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
package org.limy.velocity.task;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.limy.velocity.VelocitySupport;
import org.limy.velocity.VmParam;
import org.limy.velocity.XmlToHtml;
import org.limy.velocity.XmlToHtmlImpl;

/**
 * xml -> text �̃t�H�[�}�b�g�pAntTask�ł��B
 * @author Naoki Iwami
 */
public class VmStyleTask extends Task {
    
    // ------------------------ Fields

    /**
     * ���͌�xml�t�@�C��
     */
    private File in;

    /**
     * ���͌�xml�t�@�C���ꗗ
     */
    private List<SimpleParam> inFiles = new ArrayList<SimpleParam>();

    /**
     * �o�͐�t�@�C��
     */
    private File out;

    /**
     * �X�^�C��(VM)�t�@�C��
     */
    private File style;
    
    /**
     * �c�[���N���X
     */
    private String toolClass;
    
    /**
     * �p�����[�^�ꗗ
     */
    private Collection<VmParam> params = new ArrayList<VmParam>();

    // ------------------------ Fields (inner)
    
    /** HTML�o�͒S�� */
    private XmlToHtml xmlWriter = new XmlToHtmlImpl();

    // ------------------------ Override Methods
    
    @Override
    public void execute() {
        
        VelocitySupport.cleanEngine();
        try {
            
            supportToolClass();
            
            if (!inFiles.isEmpty()) {
                // ���̓t�@�C������������ꍇ
                File[] xmlFiles = new File[inFiles.size()];
                for (int i = 0; i < xmlFiles.length; i++) {
                    xmlFiles[i] = new File(inFiles.get(i).getValue());
                }
                xmlWriter.createHtml(Arrays.asList(xmlFiles), style, out, params);
            } else if (in != null) {
                xmlWriter.createHtml(Arrays.asList(in), style, out, params);
            } else {
                xmlWriter.createHtml(new ArrayList<File>(), style, out, params);
            }
        } catch (Exception e) {
            throw new BuildException(e);
        }
        
    }
    

    // ------------------------ Public Methods

    /**
     * param�v�f��ǉ����܂��B
     * @return param�v�f
     */
    public VmParam createParam() {
        VmParam p = new VmParam();
        params.add(p);
        return p;
    }

    /**
     * infile�v�f��ǉ����܂��B
     * @return infile�v�f
     */
    public SimpleParam createInfile() {
        SimpleParam p = new SimpleParam();
        inFiles.add(p);
        return p;
    }

    // ------------------------ Getter/Setter Methods

    /**
     * ���͌�xml�t�@�C����ݒ肵�܂��B
     * @param in ���͌�xml�t�@�C��
     */
    public void setIn(File in) {
        this.in = in;
    }

    /**
     * �o�͐�t�@�C����ݒ肵�܂��B
     * @param out �o�͐�t�@�C��
     */
    public void setOut(File out) {
        this.out = out;
    }
    
    /**
     * �X�^�C��(VM)�t�@�C����ݒ肵�܂��B
     * @param style �X�^�C��(VM)�t�@�C��
     */
    public void setStyle(File style) {
        this.style = style;
    }
    
    /**
     * �c�[���N���X��ݒ肵�܂��B
     * @param toolClass �c�[���N���X
     */
    public void setToolClass(String toolClass) {
        this.toolClass = toolClass;
    }

    // ------------------------ Private Methods

    /**
     * �c�[���N���X���p�����[�^�ɒǉ����܂��B
     * @throws Exception �C���X�^���X��O
     */
    private void supportToolClass() throws Exception {
        params.add(new VmParam("Util", new VmParseMacro(xmlWriter)));
        if (toolClass != null) {
            Object instance = Class.forName(toolClass).newInstance();
            params.add(new VmParam("Tool", instance));
        }
        Map<String, Object> innerValues = new HashMap<String, Object>();
        for (VmParam param : params) {
            innerValues.put(param.getName(), param.getExpression());
        }
        params.add(new VmParam("__INNER_VALUES__", innerValues));
        
        params.add(new VmParam("__BASE_PATH__", out.getParent()));
    }

}
