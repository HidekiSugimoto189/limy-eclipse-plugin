/*
 * Created 2004/12/17
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
package org.limy.eclipse.common.jface;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Preference�y�[�W�̊��N���X�ł��B
 * @author Naoki Iwami
 */
public abstract class AbstractLimyPreferencePage extends FieldEditorPreferencePage
        implements IWorkbenchPreferencePage {
    
    // ------------------------ Fields

    /** �֘A�t����ꂽ�\�[�X�r���[�A */
    private SourceViewer sourceViewer;

    /** �t�B�[���h�ꗗ */
    private List<FieldEditor> fields = new ArrayList<FieldEditor>();

    // ------------------------ Constructors

    /**
     * BasePreferencePage�C���X�^���X���\�z���܂��B
     * @param store 
     */
    public AbstractLimyPreferencePage(IPreferenceStore store) {
        super(GRID);
        setPreferenceStore(store);
    }
    
    // ------------------------ Override Methods

    @Override
    public boolean performOk() {
        for (Iterator<FieldEditor> it = fields.iterator(); it.hasNext();) {
            FieldEditor editor = it.next();
            editor.store();
        }
        return super.performOk();
    }
    
    protected void performDefaults() {
        for (Iterator<FieldEditor> it = fields.iterator(); it.hasNext();) {
            FieldEditor editor = it.next();
            editor.loadDefault();
        }
        super.performDefaults();
    }

    @Override
    protected void createFieldEditors() {
        // empty
    }

    public void init(IWorkbench workbench) {
        // empty
    }

    // ------------------------ Protected Methods

    /**
     * �t�B�[���h�����������܂��B
     * @param field �t�B�[���h
     */
    protected void initField(FieldEditor field) {
        field.setPreferenceStore(getPreferenceStore());
        setPreferencePage(this, field);
        field.load();
        fields.add(field);
    }

    /**
     * �F�t�B�[���h�����������܂��B
     * @param field �F�t�B�[���h
     */
    protected void initColorField(LimyColorFieldEditor field) {
        field.setPreferenceStore(getPreferenceStore());
        setPreferencePage(this, field);
        field.load();
        field.setSourceViewer(sourceViewer);
        fields.add(field);
    }

    /**
     * �F�t�B�[���h�̔z����擾���܂��B
     * @return �F�t�B�[���h�̔z��
     */
    protected LimyColorFieldEditor[] getColorFields() {
        
        List<LimyColorFieldEditor> results = new ArrayList<LimyColorFieldEditor>();
        for (Iterator<FieldEditor> it = fields.iterator(); it.hasNext();) {
            Object field = it.next();
            if (field instanceof LimyColorFieldEditor) {
                results.add((LimyColorFieldEditor)field);
            }
        }
        return results.toArray(new LimyColorFieldEditor[results.size()]);
    }

    // ------------------------ Getter/Setter Methods
    
    /**
     * �֘A�t����ꂽ�\�[�X�r���[�A���擾���܂��B
     * @return �֘A�t����ꂽ�\�[�X�r���[�A
     */
    protected SourceViewer getSourceViewer() {
        return sourceViewer;
    }

    /**
     * �֘A�t����ꂽ�\�[�X�r���[�A��ݒ肵�܂��B
     * @param sourceViewer �֘A�t����ꂽ�\�[�X�r���[�A
     */
    protected void setSourceViewer(SourceViewer sourceViewer) {
        this.sourceViewer = sourceViewer;
    }

    
    // ------------------------ Private Methods

    /**
     * FieldEditor �� PreferencePage �Ɋ֘A�t���܂��B
     * @param page PreferencePage
     * @param field FieldEditor
     */
    private void setPreferencePage(PreferencePage page, FieldEditor field) {
        field.setPage(page);
    }

}
