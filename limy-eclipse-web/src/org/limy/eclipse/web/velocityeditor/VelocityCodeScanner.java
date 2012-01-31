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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.limy.eclipse.common.LimyStoreColorProvider;
import org.limy.eclipse.common.jface.AbstractLimyCodeScanner;
import org.limy.eclipse.common.jface.LimyColorFieldEditor;
import org.limy.eclipse.core.LimyEclipsePlugin;
import org.limy.eclipse.web.LimyWebConstants;

/**
 * Velocity�G�f�B�^�p�̐F�t�����[�����쐬����N���X�ł��B
 * @depend - - - VelocityDollarRule
 * @depend - - - VelocitySharpRule
 * @depend - - - HtmlTagRule
 * @author Naoki Iwami
 */
public class VelocityCodeScanner extends AbstractLimyCodeScanner {
    
    // constructors

    /**
     * VelocityCodeScanner�C���X�^���X���\�z���܂��B
     * @param provider
     */
    public VelocityCodeScanner(LimyStoreColorProvider provider) {
        super();
        Map<String, Color> colors = getColorsFromProvider(provider);
        setRules(createRules(colors));
    }
    
    // methods in BaseCodeScanner
    
    /**
     * @param sourceViewer
     * @param colorFieldEditors
     */
    public void refreshViewer(SourceViewer sourceViewer, LimyColorFieldEditor[] colorFieldEditors) {
        LimyStoreColorProvider provider = LimyEclipsePlugin.getDefault().getColorProvider();
        VelocityCodeScanner scanner = new VelocityCodeScanner(provider);
        Map<String, Color> colors = getColorsFromProvider(provider);
        
        for (int i = 0; i < colorFieldEditors.length; i++) {
            LimyColorFieldEditor editor = colorFieldEditors[i];
            RGB color = editor.getColorSelector().getColorValue();
            String preferenceName = editor.getPreferenceName();
            
            addColor(colors, color, preferenceName);
            scanner.setRules(createRules(colors));
        }
        
        sourceViewer.unconfigure();
        sourceViewer.configure(new VelocitySourceViewerConfiguration(scanner));
        sourceViewer.refresh();
    }

    // original methods
    
    /**
     * �w�肵���F���Ń��[�����쐬���܂��B
     * @param colors �F���
     * @return �쐬���ꂽ���[��
     */
    private IRule[] createRules(Map<String, Color> colors) {

        List<IRule> rules = new ArrayList<IRule>();

        rules.add(new VelocityDollarRule(
                new TextAttribute(colors.get(LimyWebConstants.P_COLOR_PROPERTY))
        ));
        rules.add(new VelocitySharpRule(
                new TextAttribute(colors.get(LimyWebConstants.P_COLOR_COMMENT)),
                new TextAttribute(colors.get(LimyWebConstants.P_COLOR_KEYWORD)),
                new TextAttribute(colors.get(LimyWebConstants.P_COLOR_INNER)),
                new TextAttribute(colors.get(LimyWebConstants.P_COLOR_PROPERTY))
        ));
        rules.add(new HtmlTagRule(
                new TextAttribute(colors.get(LimyWebConstants.P_COLOR_TAG)),
                new TextAttribute(colors.get(LimyWebConstants.P_COLOR_KEYWORD)),
                new TextAttribute(colors.get(LimyWebConstants.P_COLOR_PROPERTY))
        ));
//        rules.add(new EndOfLineRule("function",
//                simpleToken(colors.get(LimyWebPreferencePage.P_COLOR_KEYWORD))));
        
        IRule[] result = new IRule[rules.size()];
        rules.toArray(result);
        return result;
    }

    /**
     * @param provider
     * @return �F���
     */
    private Map<String, Color> getColorsFromProvider(LimyStoreColorProvider provider) {
        Map<String, Color> colors = new LinkedHashMap<String, Color>();
        // �ȉ��͏��Ԃ��d�v�Ȃ̂ŕύX���Ȃ���
        addColorFromProvider(colors, provider, LimyWebConstants.P_COLOR_COMMENT);
        addColorFromProvider(colors, provider, LimyWebConstants.P_COLOR_KEYWORD);
        addColorFromProvider(colors, provider, LimyWebConstants.P_COLOR_INNER);
        addColorFromProvider(colors, provider, LimyWebConstants.P_COLOR_PROPERTY);
        addColorFromProvider(colors, provider, LimyWebConstants.P_COLOR_TAG);
        return colors;
    }

}
