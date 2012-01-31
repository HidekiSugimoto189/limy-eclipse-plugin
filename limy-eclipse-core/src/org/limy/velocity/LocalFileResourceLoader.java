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
package org.limy.velocity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

/**
 * ���[�J���t�@�C���p��Velocity���\�[�X���[�_�ł��B
 * <pre>
 * fol1/main.vm
 * fol1/sub.vm
 * 
 * ���̂悤�ȍ\���̃��\�[�X���������Ƃ��A
 *   getTempalte("fol1/main.vm");
 * �ɂ���ăe���v���[�g���擾���܂��B
 *   template.merge(context, writer);
 * main.vm �̒��g���ȉ��̂悤�Ɏw��ł��܂��B
 * 
 * ...
 * #include "sub"
 * ...
 * 
 * ���̂悤�ɁA���C���e���v���[�g�t�@�C������̑��΃p�X�`����include�ł��܂��B
 * �܂� ".vm" ���ȗ��\�ł��B
 * </pre>
 * @author Naoki Iwami
 */
public class LocalFileResourceLoader extends ResourceLoader {

    /**
     * ��f�B���N�g��
     */
    private File baseDir;
    
    // ------------------------ Override Methods

    @Override
    public void init(ExtendedProperties configuration) {
        // do nothing
    }

    @Override
    public InputStream getResourceStream(String source) throws ResourceNotFoundException {
        
        File sourceFile = new File(source);
        
        if (baseDir == null && (source.indexOf('/') >= 0 || source.indexOf('\\') >= 0)) {
            // include�t�@�C���𑊑΃p�X�Ŏw��ł���悤�ɂ��邽�߂̎d�g��
            baseDir = sourceFile.getParentFile();
        }
        
        if (baseDir != null && !sourceFile.exists()) {
            if (source.endsWith(".vm")) {
                sourceFile = new File(baseDir, source);
            } else {
                sourceFile = new File(baseDir, source + ".vm");
            }
        }
        
        return getInputStream(sourceFile);
    }

    @Override
    public long getLastModified(Resource resource) {
        return 0;
    }

    @Override
    public boolean isSourceModified(Resource resource) {
        return false;
    }

    // ------------------------ Public Methods

    /**
     * ��f�B���N�g�����擾���܂��B
     * @return ��f�B���N�g��
     */
    public File getBaseDir() {
        return baseDir;
    }

    // ------------------------ Private Methods
    
    /**
     * �t�@�C����InputStream��Ԃ��܂��B
     * @param sourceFile �t�@�C��
     * @return InputStream
     */
    private InputStream getInputStream(File sourceFile) {
        try {
            return new FileInputStream(sourceFile);
        } catch (final FileNotFoundException e) {
            throw new ResourceNotFoundException(e);
        }
    }


    
}
