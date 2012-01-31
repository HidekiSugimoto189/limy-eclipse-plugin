/*
 * Created 2006/08/19
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
package org.limy.eclipse.qalab.common;

/**
 * Limy Qalab�p�̒萔�N���X�ł��B
 * @author Naoki Iwami
 * @version 1.0.0
 */
public final class LimyQalabConstants {

    // ------------------------ Preference Names
    
    /**
     * Preference�L�[ : Checkstyle�̐ݒ�t�@�C�����
     */
    public static final String KEY_CHK_TYPE = "checkstyleConfigType";

    /**
     * Preference�L�[ : Checkstyle�̐ݒ�t�@�C��
     */
    public static final String KEY_CHK_CFG = "checkstyleConfig";

    /**
     * Preference�L�[ : PMD�̐ݒ�t�@�C�����
     */
    public static final String KEY_PMD_TYPE = "pmdConfigType";

    /**
     * Preference�L�[ : PMD�̐ݒ�t�@�C��
     */
    public static final String KEY_PMD_CFG = "pmdConfig";

    /**
     * �ݒ�t�@�C����ʒl : �f�t�H���g
     */
    public static final int FILE_TYPE_DEFAULT = 0;

    /**
     * �ݒ�t�@�C����ʒl : �v���W�F�N�g�����t�@�C��
     */
    public static final int FILE_TYPE_INTERNAL = 1;

    /**
     * �ݒ�t�@�C����ʒl : �O���t�@�C��
     */
    public static final int FILE_TYPE_EXTERNAL = 2;


    /**
     * Preference�L�[ : qalab.xml�i�[��
     */
    public static final String KEY_QALAB_XML = "qalabXml";

    /**
     * Preference�L�[ : �o�͐�f�B���N�g��
     */
    public static final String KEY_DEST_DIR = "destDir";

    /**
     * Preference�L�[ : build.xml�t�@�C����
     */
    public static final String KEY_BUILD_XML = "buildXml";

    /**
     * Preference�L�[ : build.properties�t�@�C����
     */
    public static final String KEY_BUILD_PROP = "buildProperties";

    /**
     * Preference�L�[ : JDepend��p�b�P�[�W��
     */
    public static final String KEY_JDEPEND_BASE = "jdependBasePackage";

    /**
     * Preference�L�[ : dot�o�C�i���p�X
     */
    public static final String KEY_DOT_EXE = "dotExe";

//    /**
//     * Preference�L�[ : QA�ΏۊO�\�[�X�f�B���N�g��
//     */
//    public static final String IGNORE_SOURCE = "ignoreSource";
//
//    /**
//     * Preference�L�[ : QA�ΏۊO�p�b�P�[�W��
//     */
//    public static final String IGNORE_PACKAGE = "ignorePackage";

    /**
     * Preference�L�[ : QA�ΏۊO���\�[�X�ꗗ
     */
    public static final String IGNORE_RESOURCES = "ignoreResources";

    /**
     * Preference�L�[ : QA�ΏۊO�p�b�P�[�W�ꗗ
     */
    public static final String IGNORE_PACKAGES = "ignorePackages";

    /**
     * Preference�L�[ : �L���Ƃ���T�u�v���W�F�N�g���ꗗ
     */
    public static final String SUB_PROJECT_NAMES = "subProjectNames";

    /**
     * Preference�L�[ : JDepend�ΏۊO���\�[�X�ꗗ
     */
    public static final String EXCLUDE_JDEPENDS = "excludeJdepends";

    /**
     * Preference�L�[ : �e�X�g�����ϐ��ꗗ
     */
    public static final String TEST_ENVS = "testEnvs";

    /**
     * Preference�L�[ : �e�X�g�����O�\�[�X�f�B���N�g���ꗗ
     */
    public static final String IGNORE_SOURCE_DIRS = "ignoreSourceDirs";

//    /**
//     * Preference�L�[ : �e�X�g�ΏۃN���X���S���薼
//     */
//    public static final String TEST_INCLUDE_NAME = "testIncludeName";
//
//    /**
//     * Preference�L�[ : �e�X�g���O�N���X���S���薼
//     */
//    public static final String TEST_EXCLUDE_NAME = "testExcludeName";

//    /**
//     * Preference�L�[ : Ant�o�[�W����
//     */
//    public static final String KEY_ANT_VERSION = "antVersion";

    /**
     * Ant�o�[�W���� : 1.6
     */
    public static final int ANT_VERSION_16 = 0;

    /**
     * Ant�o�[�W���� : 1.7
     */
    public static final int ANT_VERSION_17 = 1;

    /**
     * Preference�L�[ : Checkstyle�L���t���O
     */
    public static final String ENABLE_CHECKSTYLE = "enableCheckstyle";

    /**
     * Preference�L�[ : PMD�L���t���O
     */
    public static final String ENABLE_PMD = "enablePmd";

    /**
     * Preference�L�[ : Findbugs�L���t���O
     */
    public static final String ENABLE_FINDBUGS = "enableFindbugs";

    /**
     * Preference�L�[ : JUnit�L���t���O
     */
    public static final String ENABLE_JUNIT = "enableJUnit";

    /**
     * Preference�L�[ : Ncss�L���t���O
     */
    public static final String ENABLE_NCSS = "enableNcss";

    /**
     * Preference�L�[ : TO-DO���|�[�g�L���t���O
     */
    public static final String ENABLE_TODO = "enableTodo";

    /**
     * Preference�L�[ : JDepend���|�[�g�L���t���O
     */
    public static final String ENABLE_JDEPEND = "enableJdepend";

    /**
     * Preference�L�[ : UmlGraph���|�[�g�L���t���O
     */
    public static final String ENABLE_UMLGRAPH = "enableUmlgraph";

    /**
     * Preference�L�[ : �t�@�C����QALab���|�[�g�L���t���O
     */
    public static final String ENABLE_INDIVISUAL = "enableIndivisual";

    /**
     * Preference�L�[ : �e�X�g�p���C�u�����i�[�f�B���N�g��
     */
    public static final String TEST_LIBDIR = "testLibDir";

    /**
     * Preference�L�[ : �Q�ƃv���W�F�N�g�L���t���O
     */
    public static final String ENABLE_REFPROJECT = "enableRelatedProject";

    /**
     * Preference�L�[ : Nature�L���t���O
     */
    public static final String ENABLE_NATURE = "enableNature";
    
    /** QALab�G�f�B�^�p�X�e�[�^�XCategory�� : CC */
    public static final String QALAB_CATEGORY_CC = "qalabInfoCc";

    /** QALab�G�f�B�^�p�X�e�[�^�XCategory�� : Coverage */
    public static final String QALAB_CATEGORY_COVERAGE = "qalabInfoCoverage";

    /**
     * Preference�L�[ : UmlGraph�̃O���t�\������
     */
    public static final String UMLGRAPH_HORIZONTAL = "graphHorizontal";

    /**
     * Preference�L�[ : UmlGraph�Ńt�B�[���h���̘A������`����
     */
    public static final String UMLGRAPH_INFERREL = "graphInferrel";

    /**
     * Preference�L�[ : Javadoc�o�͂�UmlGraph���g�p���邩
     */
    public static final String UMLGRAPH_JAVADOC = "graphJavadoc";
    
    /**
     * Preference�L�[ : �|�b�v�A�b�v���̃C���[�W�X�P�[���𒲐����邩
     */
    public static final String ADJUST_SCALING = "adjustScaling";

    /**
     * private constructor
     */
    private LimyQalabConstants() { }
    
}
