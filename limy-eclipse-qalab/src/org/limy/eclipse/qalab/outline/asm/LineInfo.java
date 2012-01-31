/*
 * Created 2007/02/26
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
package org.limy.eclipse.qalab.outline.asm;

/**
 * �O���t�Ɏg�p������̏���\���܂��B
 * @author Naoki Iwami
 */
public class LineInfo {
    
    /** from�ʒu */
    private int from;
    
    /** to�ʒu */
    private int to;

    /** ��d */
    private int weight;

    /** �s�ԍ� */
    private int lineNumber;
    
    /** ���x������ */
    private String text;

    // ------------------------ Constructors

    /**
     * LineInfo�C���X�^���X���\�z���܂��B
     * @param from from�ʒu
     * @param to to�ʒu
     * @param weight ��d
     * @param lineNumber �s�ԍ�
     * @param text ���x������
     */
    public LineInfo(int from, int to, int weight, int lineNumber, String text) {
        super();
        this.from = from;
        this.to = to;
        this.weight = weight;
        this.lineNumber = lineNumber;
        this.text = text;
    }

    // ------------------------ Override Methods

    @Override
    public String toString() {
        return "[" + from + " -> " + to + "] " + text; 
    }
    
    // ------------------------ Getter/Setter Methods

    /**
     * from�ʒu���擾���܂��B
     * @return from�ʒu
     */
    public int getFrom() {
        return from;
    }

    /**
     * from�ʒu��ݒ肵�܂��B
     * @param from from�ʒu
     */
    public void setFrom(int from) {
        this.from = from;
    }

    /**
     * to�ʒu���擾���܂��B
     * @return to�ʒu
     */
    public int getTo() {
        return to;
    }

    /**
     * to�ʒu��ݒ肵�܂��B
     * @param to to�ʒu
     */
    public void setTo(int to) {
        this.to = to;
    }

    /**
     * ��d���擾���܂��B
     * @return ��d
     */
    public int getWeight() {
        return weight;
    }

    /**
     * ��d��ݒ肵�܂��B
     * @param weight ��d
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * ���x���������擾���܂��B
     * @return ���x������
     */
    public String getText() {
        if (text == null) {
            return ".";
//            if (lineNumber == 0) {
//                return "";
//            }
//            return "L." + Integer.toString(lineNumber);
        }
        return text;
    }

    /**
     * ���x��������ݒ肵�܂��B
     * @param text ���x������
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * �s�ԍ����擾���܂��B
     * @return �s�ԍ�
     */
    public int getLineNumber() {
        return lineNumber;
    }
}
