/*
 * Created 2007/01/06
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
package org.limy.eclipse.qalab.tester;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.limy.eclipse.common.jface.ITableObjectImage;
import org.limy.eclipse.qalab.LimyQalabPlugin;
import org.limy.eclipse.qalab.LimyQalabPluginImages;

/**
 * �e�X�g���s���ʂ�\��Bean�N���X�ł��B
 * @author Naoki Iwami
 */
public class FailureItem implements ITableObjectImage {

    // ------------------------ Fields

    /** �e�X�g�N���X�� */
    private String testClassName;
    
    /** �e�X�g���\�b�h�� */
    private String testName;
    
    /** �e�X�g���\�[�X */
    private IResource resource;
    
    /** �G���[�t���O�iFailure : false, Error : true�j */
    private boolean isError;
    
    /** �s�ԍ� */
    private int lineNumber;
    
    /** ���b�Z�[�W */
    private String message;
    
    // ------------------------ Constructors

    /**
     * FailureItem�C���X�^���X���\�z���܂��B
     * @param testClassName �e�X�g�N���X��
     * @param testName �e�X�g���\�b�h��
     * @param resource �e�X�g���\�[�X
     * @param isError �G���[�t���O�iFailure : false, Error : true�j
     * @param lineNumber �s�ԍ�
     * @param message ���b�Z�[�W
     */
    public FailureItem(String testClassName,
            String testName,
            IResource resource,
            boolean isError,
            int lineNumber, String message) {
        
        this.testClassName = testClassName;
        this.testName = testName;
        this.resource = resource;
        this.isError = isError;
        this.lineNumber = lineNumber;
        this.message = message;
    }
    
    // ------------------------ Implement Methods

    public int getColumnSize() {
        return 100;
    }

    public String getViewString(int index) {
        
        String result;
        switch (index) {
        case 1:
            result = message.replaceAll("[\\n\\r\\t]+", " ");
            break;
        case 2:
            int pos = testClassName.lastIndexOf('.');
            if (pos >= 0) {
                result = testClassName.substring(pos + 1) + "." + testName;
            } else {
                result = testClassName + "." + testName;
            }
            break;
        case 3:
            result = resource.getFullPath().removeLastSegments(1).toString().substring(1);
            break;
        case 4:
            result = "line " + Integer.toString(lineNumber);
            break;
        default:
            result = "";
            break;
        }
        return result;
    }

    public Image getImage(int index) {
        
        if (index == 0) {
            
            ImageRegistry registry = LimyQalabPlugin.getDefault().getImageRegistry();
            if (isError) {
                return registry.get(LimyQalabPluginImages.ERROR);
            } else {
                return registry.get(LimyQalabPluginImages.FAILURE);
            }
        }
        return null;
    }
    
    public Object getValue(int index) {
        return Integer.valueOf(index);
    }

    public void setValue(int index, Object value) {
        // do nothing
    }

    // ------------------------ Public Methods

    /**
     * �e�X�g�N���X�����擾���܂��B
     * @return �e�X�g�N���X��
     */
    public String getQualifiedClassName() {
        return testClassName;
    }
    
    /**
     * �e�X�g���\�[�X���擾���܂��B
     * @return �e�X�g���\�[�X
     */
    public IResource getResource() {
        return resource;
    }

    /**
     * �s�ԍ����擾���܂��B
     * @return �s�ԍ�
     */
    public int getLineNumber() {
        return lineNumber;
    }

}
