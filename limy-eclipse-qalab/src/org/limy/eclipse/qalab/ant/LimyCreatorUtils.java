/*
 * Created 2007/02/07
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
package org.limy.eclipse.qalab.ant;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.preference.IPreferenceStore;
import org.limy.eclipse.qalab.common.AntCreator;
import org.limy.eclipse.qalab.common.LimyQalabEnvironment;

/**
 * AntCreator�p�̃��[�e�B���e�B�N���X�ł��B
 * @author Naoki Iwami
 */
public final class LimyCreatorUtils {
    
    /** �SCreator�ꗗ */
    private static final Collection<AntCreator> ALL_CREATORS = new ArrayList<AntCreator>();
    
    static {
        ALL_CREATORS.add(new DefaultCreator());
        ALL_CREATORS.add(new CheckstyleCreator());
        ALL_CREATORS.add(new FindbugsCreator());
        ALL_CREATORS.add(new PmdCreator());
        ALL_CREATORS.add(new CoberturaCreator());
        ALL_CREATORS.add(new JavadocCreator());
        ALL_CREATORS.add(new JavancssCreator());
        ALL_CREATORS.add(new TodoCreator());
        ALL_CREATORS.add(new Java2HtmlCreator());
        ALL_CREATORS.add(new JDependCreator());
        ALL_CREATORS.add(new UmlgraphCreator());
    }
    
    /**
     * private constructor
     */
    private LimyCreatorUtils() { }

    /**
     * �L���ȃ^�[�Q�b�g��������𐶐����ĕԂ��܂��B
     * @param enableCreators �L����QA�c�[���ꗗ
     * @param suffix �e�^�[�Q�b�g�̌��ɕt����T�t�B�b�N�X����
     * @param targetGroups �ΏۃO���[�v
     * @return �^�[�Q�b�g��������
     */
    public static String createTargetString(
            AntCreator[] enableCreators, String suffix, int... targetGroups) {
        
        StringBuilder buff = new StringBuilder();
        for (AntCreator creator : enableCreators) {
            for (int group : targetGroups) {
                if (creator.getSummaryGroup() == group) {
                    if (buff.length() > 0) {
                        buff.append(',');
                    }
                    buff.append(creator.getTargetName());
                    if (suffix != null) {
                        buff.append(suffix);
                    }
                }
            }
        }
        return buff.toString();
    }

    /**
     * �L����Type������𐶐����ĕԂ��܂��B
     * @param enableCreators �L����QA�c�[���ꗗ
     * @param targetGroups �ΏۃO���[�v
     * @return Type������
     */
    public static String createTypeString(
            AntCreator[] enableCreators, int... targetGroups) {
        
        StringBuilder buff = new StringBuilder();
        for (AntCreator creator : enableCreators) {
            for (int targetGroup : targetGroups) {
                if (creator.getSummaryGroup() == targetGroup) {
                    for (String type : creator.getSummaryTypes()) {
                        if (buff.length() > 0) {
                            buff.append(',');
                        }
                        buff.append(type);
                    }
                }
            }
        }
        return buff.toString();
    }

    /**
     * �v���W�F�N�g�ŗL����Creator�ꗗ��Ԃ��܂��B
     * @param env 
     * @return �L����Creator�ꗗ
     */
    public static AntCreator[] decideCreators(LimyQalabEnvironment env) {
        Collection<AntCreator> results = new ArrayList<AntCreator>();
        IPreferenceStore store = env.getStore();
        for (AntCreator creator : ALL_CREATORS) {
            if (creator.isEnable(store)) {
                results.add(creator.newInstance());
            }
        }
        
//        results.add(CreatorFactory.createDefaultCraetor());
//        if (store.getBoolean(LimyQalabConstants.ENABLE_CHECKSTYLE)) {
//            results.add(CreatorFactory.createCheckstyleCreator());
//        }
//        if (store.getBoolean(LimyQalabConstants.ENABLE_FINDBUGS)) {
//            results.add(CreatorFactory.createFindbugsCreator());
//        }
//        if (store.getBoolean(LimyQalabConstants.ENABLE_PMD)) {
//            results.add(CreatorFactory.createPmdCreator());
//        }
//        if (store.getBoolean(LimyQalabConstants.ENABLE_JUNIT)) {
//            results.add(CreatorFactory.createCoberturaCreator());
//        }
//        results.add(CreatorFactory.createJavadocCreator());
//        if (store.getBoolean(LimyQalabConstants.ENABLE_NCSS)) {
//            results.add(CreatorFactory.createJavancssCreator());
//        }
//        if (store.getBoolean(LimyQalabConstants.ENABLE_TODO)) {
//            results.add(CreatorFactory.createTodoCreator());
//        }
//        results.add(CreatorFactory.createJava2HtmlCreator());
//        if (store.getBoolean(LimyQalabConstants.ENABLE_JDEPEND)) {
//            results.add(CreatorFactory.createJDependCreator());
//        }
//        if (store.getBoolean(LimyQalabConstants.ENABLE_UMLGRAPH)) {
//            results.add(CreatorFactory.createUmlgraphCreator());
//        }
        return results.toArray(new AntCreator[results.size()]);
    }

}
