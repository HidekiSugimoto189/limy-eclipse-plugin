/*
 * Created 2007/01/05
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
package org.limy.eclipse.qalab;

/**
 * �}�[�J�[�p�̒萔�N���X�ł��B
 * @author Naoki Iwami
 */
public final class LimyQalabMarker {

    /** �S�}�[�J�[�̐e�}�[�J�[ID */
    public static final String DEFAULT_ID = LimyQalabPlugin.PLUGIN_ID + ".LimyQalabMarker";

    /** Problem�}�[�J�[ID */
    public static final String PROBLEM_ID = LimyQalabPlugin.PLUGIN_ID + ".LimyQalabProblemMarker";

    /** �e�X�g�֘A�e�}�[�J�[ID */
    public static final String TEST_ID = LimyQalabPlugin.PLUGIN_ID + ".LimyQalabTestMarker";
    

    /** Non�J�o���b�W���ʃ}�[�J�[ID */
    public static final String NO_COVERAGE_ID = LimyQalabPlugin.PLUGIN_ID
            + ".LimyQalabNoCoverageMarker";

    /** �J�o���b�W���ʃ}�[�J�[ID */
    public static final String COVERAGE_ID = LimyQalabPlugin.PLUGIN_ID
            + ".LimyQalabCoverageMarker";

    /** �e�X�g���s���ʃ}�[�J�[ID */
    public static final String FAILURE_ID = LimyQalabPlugin.PLUGIN_ID + ".LimyQalabFailureMarker";

    /** �e�X�g�G���[���ʃ}�[�J�[ID */
    public static final String ERROR_ID = LimyQalabPlugin.PLUGIN_ID + ".LimyQalabErrorMarker";

    /** �e�X�g�������ʃ}�[�J�[ID */
    public static final String SUCCESS_ID = LimyQalabPlugin.PLUGIN_ID + ".LimyQalabSuccessMarker";

    /** �J�o���b�W���ʁinon-visible�j�}�[�J�[ID */
    public static final String COVERAGE_RESULT = LimyQalabPlugin.PLUGIN_ID
            + ".LimyQalabCoverageResult";
    
    
    /** ���� : �e�X�g�� */
    public static final String TEST_NAME = "testName";
    
    /** ���� : �SLine�� */
    public static final String ALL_LINE_NUMBER = "allLineNumber";

    /** ���� : �J�o���b�W���ꂽLine�� */
    public static final String COVERAGE_LINE = "coverageLineNumber";

    /** ���� : �SBranch�� */
    public static final String ALL_BRANCH_NUMBER = "allBranchNumber";

    /** ���� : �J�o���b�W���ꂽBranch�� */
    public static final String COVERAGE_BRANCH = "coverageBranchNumber";

    /** ���� : complexity�l�i���T�|�[�g�j */
    public static final String COMPLEXITY = "complexity";

    /** ���� : URL */
    public static final String URL = "url";

    /**
     * private constructor
     */
    private LimyQalabMarker() { }

}
