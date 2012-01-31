/*
 * Created 2007/01/05
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
package net.sourceforge.cobertura.instrument;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

import net.sourceforge.cobertura.coveragedata.CoverageDataFileHandler;
import net.sourceforge.cobertura.coveragedata.ProjectData;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.limy.eclipse.common.LimyEclipsePluginUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

/**
 *
 * @author iwami
 */
public class CoberturaInstrument {
    
    private File dataFile;
    
    private byte[] instrumentedClasses;

    private ProjectData projectData;
    
    /**
     * CoberturaInstrument�C���X�^���X���\�z���܂��B
     * @param dataFile
     */
    public CoberturaInstrument(File dataFile) {
        this.dataFile = dataFile;
    }

    public void makeInstrument(IProject project, IResource[] classResources) {
        
        projectData = new ProjectData();
        
        try {
            analyzeClassFiles(classResources);
        } catch (IOException e) {
            LimyEclipsePluginUtils.log(e);
        }

        // cobertura.ser�o��
        CoverageDataFileHandler.saveCoverageData(projectData, dataFile);

    }

    public void makeInstrument(IResource resource) {
        
        projectData = new ProjectData();

        try {
            
            analyzeClassFiles(new IResource[] { resource });
            
        } catch (IOException e) {
            LimyEclipsePluginUtils.log(e);
        }

        // cobertura.ser�o��
        CoverageDataFileHandler.saveCoverageData(projectData, dataFile);
        
    }
    
    // ------------------------ Private Methods

    /**
     * class�t�@�C������͂��� projectData �Ɍ��ʂ��i�[���܂��B
     * @param resources class�t�@�C���̃��\�[�X
     * @throws IOException I/O��O
     */
    private void analyzeClassFiles(
            IResource[] resources) throws IOException {
                
        for (IResource resource : resources) {
            ClassInstrumenter cv = apeendInstrumentInfo(resource.getLocation().toFile());
            
            // instrument�t�@�C���Q�o��
            writeInstrumentedFiles(cv,
                    new File(resource.getProject().getLocation().toFile(), ".instrument"));

        }
    }

    /**
     * class�t�@�C������͂��� ClassInstrumenter �ɉ�͌��ʂ��i�[���܂��B
     * @param cv ClassInstrumenter
     * @param file class�t�@�C��
     * @return 
     * @throws IOException I/O��O
     */
    private ClassInstrumenter apeendInstrumentInfo(File file) throws IOException {
        
        instrumentedClasses = null;

        Collection<Pattern> ignoreRegexes = new ArrayList<Pattern>();
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassInstrumenter cv = new ClassInstrumenter(projectData, cw, ignoreRegexes, null);

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            ClassReader cr = new ClassReader(inputStream);
            cr.accept(cv, 0);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        
        instrumentedClasses = cw.toByteArray();

        return cv;
    }
    
    /**
     * instrument�t�@�C�����o�͂��܂��B
     * @param cv ClassInstrumenter
     * @param outputDir instrument�t�@�C���o�͐�f�B���N�g��
     * @throws IOException I/O��O
     */
    private void writeInstrumentedFiles(ClassInstrumenter cv, File outputDir)
            throws IOException {
        
        OutputStream outputStream = null;
        try {
            if (cv.isInstrumented()) {
                File outputFile;
                outputFile = new File(outputDir, cv
                        .getClassName().replace('.', File.separatorChar)
                        + ".class");

                File parentFile = outputFile.getParentFile();
                if (parentFile != null) {
                    parentFile.mkdirs();
                }

                outputStream = new FileOutputStream(outputFile);
                outputStream.write(instrumentedClasses);
            }
            
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

}
