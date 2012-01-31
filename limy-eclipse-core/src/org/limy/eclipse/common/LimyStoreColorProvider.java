/*
 * Created 2004/09/18
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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * Limy Eclipse�Ŏg�p����F�񋟃v���o�C�_�N���X�ł��B
 * <p>�X�g�A�ƘA�����Ēl���擾���邱�Ƃ��\�ł�</p>
 * @author Naoki Iwami
 * @depend - - - LimyEclipseConstants
 */
public class LimyStoreColorProvider {
    
    // ------------------------ Static Fields

    /**
     * �F�����i�[����}�b�v�i�F�L�[ => RGB�j
     */
    private static Map<String, RGB> defaultColor = new HashMap<String, RGB>(10);

    // ------------------------ Fields
    
    /** �X�g�A */
    private IPreferenceStore store;
    
    /**
     * �J���[�e�[�u���̃L���b�V���i�F�L�[ => �F�j
     */
    private Map<String, Color> fColorTable = new HashMap<String, Color>(10);
    
    // ------------------------ Constructors
    
    /**
     * LimyColorProvider�C���X�^���X���\�z���܂��B
     * @param store �X�g�A
     */
    public LimyStoreColorProvider(IPreferenceStore store) {
        this.store = store;
        addDefaultColor("text", new RGB(0, 0, 0));
        addDefaultColor(LimyEclipseConstants.P_BGCOLOR, new RGB(240, 255, 240));
    }
    
    // ------------------------ Public Methods

    /**
     * Release all of the color resources held onto by the receiver.
     */
    public void dispose() {
        for (Color color : fColorTable.values()) {
            color.dispose();
        }
        fColorTable.clear();
    }
    
    /**
     * �F���擾���܂��B
     * <p>�X�g�A�ɐF���ݒ肵�Ă���΂��̐F���A���Ă��Ȃ���΃f�t�H���g�l�Ƃ��Đݒ肵���F��Ԃ��܂��B</p>
     * @param key �F�L�[
     * @return �F
     */
    public Color getColor(String key) {
        
        // �F�L���b�V����������擾
        Color color = fColorTable.get(key);
        
        if (color == null) {
            // �X�g�A����RGB�l���擾
            String rgb = store.getString(key);
            if (rgb == null || rgb.length() == 0) {
                // �擾�ł��Ȃ������ꍇ�A�f�t�H���g�l�ɐݒ�
                color = new Color(Display.getCurrent(), defaultColor.get(key));
            } else {
                // �擾�ł�����A����RGB�l���g���ĐF���쐬
                color = new Color(Display.getCurrent(), StringConverter.asRGB(rgb));
            }
            fColorTable.put(key, color); // �L���b�V���ɕۑ�
        }
        return color;
    }
    
    /**
     * �f�t�H���g�F��ǉ����܂��B
     * @param key �F�L�[
     * @param color �F
     */
    protected final void addDefaultColor(String key, RGB color) {
        defaultColor.put(key, color);
    }
    
}
