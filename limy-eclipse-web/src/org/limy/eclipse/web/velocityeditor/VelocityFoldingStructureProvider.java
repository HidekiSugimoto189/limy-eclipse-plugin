/*
 * Created 2006/01/14
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
package org.limy.eclipse.web.velocityeditor;

import java.util.Map;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.projection.IProjectionListener;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.limy.eclipse.common.LimyEclipsePluginUtils;

/**
 * Velocity�G�f�B�^����Folding�p�v���o�C�_�N���X�ł��B
 * @author Naoki Iwami
 */
public class VelocityFoldingStructureProvider implements IProjectionListener {

    // ------------------------ Fields

    /**
     * XML�G�f�B�^
     */
    private ITextEditor editor;
    
    /**
     * Folding�p�r���[�A
     */
    private ProjectionViewer viewer;

//    /**
//     * Folding�p�h�L�������g
//     */
//    private IDocument cachedDocument;
    
    // ------------------------ Constructors
    
    /**
     * XML�G�f�B�^��Folding�@�\���C���X�g�[�����܂��B
     * @param editor
     * @param viewer
     */
    public void install(ITextEditor editor, ProjectionViewer viewer) {
        if (editor instanceof VelocityEditor) {
            this.editor = editor;
            this.viewer = viewer;
            this.viewer.addProjectionListener(this);
        }
    }
    
    // ------------------------ Implement Methods
    
    public void projectionEnabled() {
        initialize();
        
    }

    public void projectionDisabled() {
//        cachedDocument = null;
    }
    
    // ------------------------ Private Methods

    /**
     * Folding�@�\�����������܂��B
     */
    private void initialize() {
        
        if (!isInstalled()) {
            return;
        }
        
        try {
            
            IDocumentProvider provider = editor.getDocumentProvider();
            IDocument cachedDocument = provider.getDocument(editor.getEditorInput());
            
            ProjectionAnnotationModel model = (ProjectionAnnotationModel)
                    editor.getAdapter(ProjectionAnnotationModel.class);
            if (model != null) {
                
                model.removeAllAnnotations();
                Map<ProjectionAnnotation, Position> annotations
                        = VelocityUtils.getFoldingAnnotations(cachedDocument);
                model.replaceAnnotations(null, annotations);
            }
            
        } catch (BadLocationException e) {
            LimyEclipsePluginUtils.log(e);
        }
    }

    /**
     * �C���X�g�[������Ă��邩�ǂ������肵�܂��B
     * @return �C���X�g�[������Ă����true
     */
    private boolean isInstalled() {
        return editor != null;
    }

}
