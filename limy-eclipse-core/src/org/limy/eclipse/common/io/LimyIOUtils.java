/*
 * Created 2007/08/15
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
package org.limy.eclipse.common.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Naoki Iwami
 */
public final class LimyIOUtils {
    
    /**
     * private constructor
     */
    private LimyIOUtils() { }
    

    /**
     * ���̓X�g���[����ǂݍ���ŕ�������擾���܂��B
     * @param stream ���̓X�g���[��
     * @param encoding �����G���R�[�f�B���O
     * @return �X�g���[���̓��e������
     * @throws IOException
     */
    public static String getContent(InputStream stream, String encoding) throws IOException {
        byte[] buff = getContentBytes(stream);
        if (encoding == null) {
            return new String(buff);
        }
        return new String(buff, encoding);
    }

    /**
     * ���̓X�g���[����ǂݍ���ŕ�����iOS�̃f�t�H���g�����G���R�[�f�B���O�j���擾���܂��B
     * @param stream ���̓X�g���[��
     * @return �X�g���[���̓��e������
     * @throws IOException
     */
    public static String getContent(InputStream stream) throws IOException {
        return getContent(stream, null);
    }

    /**
     * �t�@�C����ǂݍ���ŕ�����iOS�̃f�t�H���g�����G���R�[�f�B���O�j���擾���܂��B
     * @param file �t�@�C��
     * @return �X�g���[���̓��e������
     * @throws IOException
     */
    public static String getContent(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        try {
            return getContent(in, null);
        } finally {
            in.close();
        }
    }

    /**
     * �t�@�C����ǂݍ���ŕ�������擾���܂��B
     * @param file �t�@�C��
     * @param charset �����G���R�[�f�B���O
     * @return �X�g���[���̓��e������
     * @throws IOException
     */
    public static String getContent(File file, String charset) throws IOException {
        FileInputStream in = new FileInputStream(file);
        try {
            return getContent(in, charset);
        } finally {
            in.close();
        }
    }
    
    /**
     * ���̓X�g���[����ǂݍ���œ��e���擾���܂��B
     * @param stream ���̓X�g���[��
     * @return �X�g���[���̓��e
     * @throws IOException
     */
    public static byte[] getContentBytes(InputStream stream) throws IOException {
        BufferedInputStream in = null;
        if (stream instanceof BufferedInputStream) {
            in = (BufferedInputStream)stream;
        } else {
            in = new BufferedInputStream(stream);
        }
        try {
            int size = in.available();
            byte[] buff = new byte[size];
            int pos = 0;
            while (pos < size) {
                int realSize = in.read(buff, pos, size - pos);
                pos += realSize;
            }
            return buff;
        } finally {
            in.close();
        }
    }

    /**
     * �t�@�C����ۑ����܂��B
     * @param targetFile �ۑ���t�@�C���p�X
     * @param contents �ۑ����e
     * @throws IOException I/O��O
     */
    public static void saveFile(File targetFile, byte[] contents) throws IOException {
        FileOutputStream output = null;
        try {
            File parent = new File(targetFile.getAbsolutePath()).getParentFile();
            if (parent != null) {
                parent.mkdirs();
            }
            output = new FileOutputStream(targetFile);
            output.write(contents);
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }

    /**
     * �t�@�C�����R�s�[���܂��B
     * @param src �R�s�[���t�@�C��
     * @param dest �R�s�[��t�@�C��
     * @throws IOException I/O��O
     */
    public static void copyFile(File src, File dest) throws IOException {
        FileInputStream in = new FileInputStream(src);
        try {
            byte[] bs = getContentBytes(in);
            saveFile(dest, bs);
        } finally {
            in.close();
        }

    }

}
