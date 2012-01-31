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

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;
import org.limy.eclipse.common.LimyEclipsePluginUtils;

/**
 * Velocity�G�f�B�^���̃n�C�p�[�����N����������N���X�ł��B
 * @depend - - - VelocityHyperLink
 * @author Naoki Iwami
 */
public class VelocityHyperlinkDetector implements IHyperlinkDetector {
    
    // ------------------------ Fields

    /**
     * �e�L�X�g�G�f�B�^
     */
    private ITextEditor editor;
    
    // ------------------------ Constructors

    /**
     * VelocityHyperlinkDetector�C���X�^���X���\�z���܂��B
     * @param editor �e�L�X�g�G�f�B�^
     */
    public VelocityHyperlinkDetector(ITextEditor editor) {
        this.editor = editor;
    }

    // ------------------------ Implement Methods

    public IHyperlink[] detectHyperlinks(ITextViewer textViewer,
            IRegion region, boolean canShowMulti) {
        
        try {
            IDocument doc = textViewer.getDocument();
            // �J�[�\���ʒu�ɂ���P��̃|�W�V�������擾
            Position position = VelocityUtils.getLiteralPosition(doc, region.getOffset());
            if (position == null) {
                return new IHyperlink[0]; // �P�ꖳ��
            }
            
            int offset = position.getOffset();
            int length = position.getLength();
            String word = doc.get(offset, length); // �P����擾
            return new IHyperlink[] {
                    new VelocityHyperLink(new Region(offset, length), word,
                            ((IFileEditorInput)editor.getEditorInput()).getFile()),
            };
            
        } catch (BadLocationException e) {
            LimyEclipsePluginUtils.log(e);
        }
        
        return null;
    }

}
