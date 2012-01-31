/*
 * Created 2006/05/26
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
package org.limy.common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Writer;

import org.limy.eclipse.common.LimyEclipsePluginUtils;

/**
 * �v���Z�X�֘A�̃��[�e�B���e�B�N���X�ł��B
 * @author Naoki Iwami
 */
public final class ProcessUtils {

    /**
     * private constructor
     */
    private ProcessUtils() {
        // empty
    }

    /**
     * �v���O���������s���܂��B
     * @param execDir ���s�f�B���N�g��
     * @param out �o�͐�
     * @param args ���s�p�����[�^
     * @return ���s
     * @throws IOException I/O��O
     */
    public static int execProgram(File execDir, Writer out, String... args)
            throws IOException {
        
        ProcessBuilder builder = new ProcessBuilder(args);
        builder.directory(execDir);
        builder.environment().put("ANT_OPTS", "-Xmx256M"); // pmd-cpd�΍�
        
        builder.redirectErrorStream(true);
//        for (String arg : args) {
//            System.out.println("ARG = " + arg);
//        }
        Process process = builder.start();
        
//        BufferedReader reader = new BufferedReader(
//                new InputStreamReader(process.getInputStream()));
//        try {
//            return waitEndProcess(out, process, reader);
//        } finally {
//            reader.close();
//        }
        
        BufferedInputStream stream = new BufferedInputStream(process.getInputStream());
        try {
            return waitEndProcess(out, process, stream);
        } finally {
            stream.close();
        }
    }

    /**
     * �v���O���������s���܂��B�v���O�����̏I����҂����ɂ����ɏ�����Ԃ��܂��B
     * @param execDir ���s�f�B���N�g��
     * @param args ���s�p�����[�^
     * @throws IOException I/O��O
     */
    public static void execProgramNoWait(File execDir, String... args)
            throws IOException {
        
        ProcessBuilder builder = new ProcessBuilder(args);
        builder.directory(execDir);
        builder.environment().put("ANT_OPTS", "-Xmx256M"); // pmd-cpd�΍�
        
        builder.redirectErrorStream(true);
        builder.start();
    }

    // ------------------------ Private Methods

    /**
     * �v���Z�X���I������܂őҋ@���܂��B
     * @param out �v���Z�X�̎��s���ʁi�W���o�͂���уG���[�o�́j�̏o�͐�
     * @param process �v���Z�X
     * @param stream �v���Z�X�̓��̓X�g���[���i�W���o�͂���уG���[�o�́j
     * @return �v���Z�X�̎��s����
     * @throws IOException I/O��O
     */
    private static int waitEndProcess(Writer out, Process process, BufferedInputStream stream)
            throws IOException {
        
        int exitValue = -1;

        while (true) {
            try {
                Thread.sleep(100);
                
                while (true) {
                    int size = stream.available();
                    if (size <= 0) {
                        break;
                    }
                    byte[] bs = new byte[size];
                    stream.read(bs, 0, size);
                    
                    if (out != null) {
                        out.write(new String(bs));
                        out.flush();
                    }
                }
                exitValue = process.exitValue();
                break;
            } catch (IllegalThreadStateException e) {
                // empty
//                System.out.println("IllegalThreadStateException");
            } catch (InterruptedException e) {
                LimyEclipsePluginUtils.log(e);
            }
        }
        return exitValue;
    }

//    /**
//     * �v���Z�X���I������܂őҋ@���܂��B
//     * @param out �v���Z�X�̎��s���ʁi�W���o�͂���уG���[�o�́j�̏o�͐�
//     * @param process �v���Z�X
//     * @param reader 
//     * @return �v���Z�X�̎��s����
//     * @throws IOException I/O��O
//     */
//    private static int waitEndProcess(Writer out, Process process, BufferedReader reader)
//            throws IOException {
//        
//        int exitValue = -1;
//
//        while (true) {
//            try {
//                Thread.sleep(100);
//                
//                String line = reader.readLine();
//                if (line == null) {
//                    break;
//                }
//                if (out != null) {
//                    System.out.println(line);
//                    out.write(line);
//                    out.flush();
//                }
//                
//                exitValue = process.exitValue();
//                break;
//            } catch (IllegalThreadStateException e) {
//                // empty
//            } catch (InterruptedException e) {
//                LimyEclipsePluginUtils.log(e);
//            }
//        }
//        return exitValue;
//    }

}
