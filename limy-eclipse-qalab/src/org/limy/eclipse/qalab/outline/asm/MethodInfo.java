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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;


/**
 * ���\�b�h����\���܂��B
 * @author Naoki Iwami
 */
public class MethodInfo {

    // ------------------------ Fields

    /** ���\�b�h�� */
    private final String name;
    
    /** �S�|�C���g��� */
    private Collection<PointInfo> pointInfos;
    
    /** �S���C����� */
    private List<LineInfo> lineInfos;

    // ------------------------ Constructors

    /**
     * MethodInfo�C���X�^���X���\�z���܂��B
     * @param name ���\�b�h��
     * @param pointInfos �S�|�C���g���
     * @param lineInfos �S���C�����
     */
    public MethodInfo(String name, Collection<PointInfo> pointInfos,
            List<LineInfo> lineInfos) {
        super();
        this.name = name;
        this.pointInfos = pointInfos;
        this.lineInfos = lineInfos;
    }

    // ------------------------ Override Methods

    @Override
    public String toString() {
        return Arrays.toString(lineInfos.toArray(new LineInfo[lineInfos.size()]));
    }

    // ------------------------ Getter/Setter Methods

    /**
     * ���\�b�h�����擾���܂��B
     * @return ���\�b�h��
     */
    public String getName() {
        return name;
    }

    /**
     * �S�|�C���g�����擾���܂��B
     * @return �S�|�C���g���
     */
    public Collection<PointInfo> getPointInfos() {
        return pointInfos;
    }

    /**
     * �S���C�������擾���܂��B
     * @return �S���C�����
     */
    public List<LineInfo> getLineInfos() {
        return lineInfos;
    }

}
