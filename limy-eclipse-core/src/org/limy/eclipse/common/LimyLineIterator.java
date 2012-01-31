/*
 * Created 2003/11/01
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
package org.limy.eclipse.common;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * �S�����s���Ƃɏ�������C�e���[�^�N���X�ł��B
 * @author Naoki Iwami
 */
public class LimyLineIterator implements Iterator<String> {

    // ------------------------ Fields

    /**
     * �S�����
     */
    private String lines;
    
    /**
     * ���݃|�W�V����
     */
    private int pos;
    
    // ------------------------ Constructors

    /**
     * LimyLineIterator�C���X�^���X���\�z���܂��B
     * @param lines �S�����
     */
    public LimyLineIterator(String lines) {
        this.lines = lines;
    }
    
    // ------------------------ Implement Methods

    public void remove() {
        // empty
    }

    public boolean hasNext() {
        return pos < lines.length();
    }

    public String next() {
        if (pos >= lines.length()) {
            throw new NoSuchElementException();
        }
        return nextLine();
    }
    
    // ------------------------ Public Methods

    /**
     * ���̍s���擾���܂��B
     * @return 1�s������
     */
    public String nextLine() {
        String r;
        int index = lines.indexOf(10, pos);
        if (index < 0) {
            r = lines.substring(pos);
            pos = lines.length();
        } else {
            if (index > 0 && lines.charAt(index - 1) == 13) {
                r = lines.substring(pos, index - 1);
                pos = index + 1;
            } else {
                r = lines.substring(pos, index);
                pos = index + 1;
            }
        }
        return r;
    }

    /**
     * ���݃|�W�V�������擾���܂��B
     * @return ���݃|�W�V����
     */
    public int getPos() {
        return pos;
    }

}
