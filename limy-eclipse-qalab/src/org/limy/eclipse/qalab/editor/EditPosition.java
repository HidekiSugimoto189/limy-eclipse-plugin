/*
 * Created 2007/09/02
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
package org.limy.eclipse.qalab.editor;

import org.eclipse.jface.text.Position;
import org.eclipse.ui.IEditorInput;

/**
 * �G�f�B�^���|�W�V������\���܂��B
 * @author Naoki Iwami
 */
public class EditPosition {

    /** �G�f�B�^ */
    private final IEditorInput editorInput;
    
    /** �G�f�B�^ID */
    private final String editorId;
    
    /** �e�L�X�g�|�W�V���� */
    private Position position;

    /**
     * EditPosition �C���X�^���X���\�z���܂��B
     * @param editorInput �G�f�B�^
     * @param editorId �G�f�B�^ID
     * @param position �e�L�X�g�|�W�V����
     */
    public EditPosition(IEditorInput editorInput, String editorId,
            Position position) {
        super();
        this.editorInput = editorInput;
        this.editorId = editorId;
        this.position = position;
    }

    /**
     * �G�f�B�^���擾���܂��B
     * @return �G�f�B�^
     */
    public IEditorInput getEditorInput() {
        return editorInput;
    }

    /**
     * �G�f�B�^ID���擾���܂��B
     * @return �G�f�B�^ID
     */
    public String getEditorId() {
        return editorId;
    }

    /**
     * �e�L�X�g�|�W�V�������擾���܂��B
     * @return �e�L�X�g�|�W�V����
     */
    public Position getPosition() {
        return position;
    }

    /**
     * �e�L�X�g�|�W�V������ݒ肵�܂��B
     * @param position �e�L�X�g�|�W�V����
     */
    public void setPosition(Position position) {
        this.position = position;
    }

}
