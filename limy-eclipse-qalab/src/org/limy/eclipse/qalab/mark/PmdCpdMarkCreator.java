/*
 * Created 2007/01/07
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
package org.limy.eclipse.qalab.mark;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import net.sourceforge.pmd.cpd.CPD;
import net.sourceforge.pmd.cpd.JavaTokenizer;
import net.sourceforge.pmd.cpd.Language;
import net.sourceforge.pmd.cpd.LanguageFactory;
import net.sourceforge.pmd.cpd.Match;
import net.sourceforge.pmd.cpd.TokenEntry;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.limy.eclipse.common.LimyEclipsePluginUtils;
import org.limy.eclipse.common.resource.LimyMarkerUtils;
import org.limy.eclipse.common.resource.LimyResourceUtils;
import org.limy.eclipse.common.resource.ResourceWithBasedir;
import org.limy.eclipse.qalab.LimyQalabMarker;
import org.limy.eclipse.qalab.common.LimyQalabEnvironment;
import org.limy.eclipse.qalab.common.LimyQalabUtils;
import org.limy.eclipse.qalab.common.QalabResourceUtils;

/**
 * PMD CPD�p�̃}�[�J�[�쐬�N���X�ł��B
 * @author Naoki Iwami
 */
public final class PmdCpdMarkCreator implements MarkCreator {
    
    /** �B��̃C���X�^���X */
    private static PmdCpdMarkCreator instance = new PmdCpdMarkCreator();
    
    /**
     * private constructor
     */
    private PmdCpdMarkCreator() {
        // empty
    }
    
    /**
     * �B��̃C���X�^���X��Ԃ��܂��B
     * @return �B��̃C���X�^���X
     */
    public static PmdCpdMarkCreator getInstance() {
        return instance;
    }
    
    public void clearCache() {
        // do nothing
    }
    
    // ------------------------ Implement Methods

    public String getName() {
        return "pmd-cpd";
    }

    public boolean markJavaElement(LimyQalabEnvironment env,
            Collection<IJavaElement> elements, IProgressMonitor monitor) {
        
        return markResources(env, QalabResourceUtils.getResources(elements), monitor);
    }

    public boolean markResource(LimyQalabEnvironment env,
            IResource resource, IProgressMonitor monitor) {
        return true; // do nothing
    }

    public boolean markResourceTemporary(LimyQalabEnvironment env,
            IResource resource, IProgressMonitor monitor) {
        return true; // do nothing
    }

    public boolean markResources(LimyQalabEnvironment env,
            Collection<IResource> resources, IProgressMonitor monitor) {
        
        Properties p = new Properties();
        p.setProperty(JavaTokenizer.IGNORE_LITERALS, "true");
        //p.setProperty(JavaTokenizer.IGNORE_IDENTIFIERS, "true");
        Language language = new LanguageFactory().createLanguage("java", p);

        try {

            IProject project = env.getProject();
            CPD cpd = new CPD(50, language/*, project.getDefaultCharset()*/);
            
            IJavaProject javaProject = JavaCore.create(project);
            
            for (IResource resource : resources) {
                ResourceWithBasedir classResource = QalabResourceUtils.getResourceWithBasedir(
                        env, resource);
                
                String baseDir = LimyQalabUtils.createFullPath(
                        javaProject, classResource.getBaseDir());
                cpd.addRecursively(baseDir);
            }

            // TODO ���݂͎Q�ƃv���W�F�N�g�Ƃ̏d���`�F�b�N�����Ă��Ȃ��i����Ή��\��j
            // HTML���|�[�g�ł͊��ɎQ�ƃv���W�F�N�g�Ƃ̏d���`�F�b�N�ɑΉ���
            cpd.go();
            
            for (Iterator<Match> it = cpd.getMatches(); it.hasNext();) {
                Match match = it.next();
                markFiles(env, match);
            }
            return true;
        } catch (CoreException e) {
            LimyEclipsePluginUtils.log(e);
        } catch (IOException e) {
            LimyEclipsePluginUtils.log(e);
        }
        
        return false;
    }
    
    /**
     * �}�b�`���e�ɏ]���ďd���t�@�C���i�����j�Ƀ}�[�J�[��t���܂��B
     * @param env 
     * @param project �v���W�F�N�g
     * @param match �}�b�`���e
     * @throws CoreException �R�A��O
     */
    private void markFiles(LimyQalabEnvironment env, Match match) throws CoreException {
        
        TokenEntry first = match.getFirstMark();
        TokenEntry second = match.getSecondMark();
        
        String message = "���e���d�����Ă��܂��B" 
                + second.getTokenSrcID()
                + " �� " + second.getBeginLine() + " �s�ڈȍ~�i"
                + match.getLineCount() + " Lines, "
                + match.getTokenCount() + " Tokens�j�B";
        markFile(env, first, second, message);

        message = "���e���d�����Ă��܂��B" 
                + first.getTokenSrcID()
                + " �� " + first.getBeginLine() + " �s�ڈȍ~�i"
                + match.getLineCount() + " Lines, "
                + match.getTokenCount() + " Tokens�j�B";
        markFile(env, second, first, message);

    }

    /**
     * �d�����e�ɏ]���ăt�@�C���Ƀ}�[�J�[��t���܂��B
     * @param env 
     * @param project �v���W�F�N�g
     * @param mark �d�����e
     * @param otherMark �����Е��̏d�����e
     * @param message ���b�Z�[�W
     * @throws CoreException �R�A��O
     */
    private void markFile(LimyQalabEnvironment env, TokenEntry mark,
            TokenEntry otherMark,
            String message) throws CoreException {
        
        String token = mark.getTokenSrcID().replaceAll("\\\\", "/");
        for (IPath path : env.getSourcePaths(false)) {
            IResource folder = env.getProject().getWorkspace().getRoot().findMember(path);
            if (token.startsWith(folder.getLocation().toString())) {
                token = token.substring(folder.getLocation().toString().length());
                String className = token.substring(
                        1, token.length() - 5).replace('/', '.');
                IResource resource = QalabResourceUtils.getJavaResource(env, className, true);
                
                Map<String, Object> attrs = new HashMap<String, Object>();
                attrs.put(LimyQalabMarker.URL, getUrl(env, otherMark));

                LimyMarkerUtils.addMarker(LimyQalabMarker.PROBLEM_ID,
                        resource, mark.getBeginLine(), message, attrs);
                
                break;
            }
        }
        
        
    }

    /**
     * @param env 
     * @param mark
     * @return
     * @throws CoreException 
     */
    private String getUrl(LimyQalabEnvironment env, TokenEntry mark) throws CoreException {
        
        Collection<IPath> sourcePaths = env.getSourcePaths(true);
        
        // �\�[�X�p�XRoot����̑��΃p�X
//        String tokenPath = mark.getTokenPath();
        String tokenPath = mark.getTokenSrcID();
        
        for (IPath path : sourcePaths) {
            IPath fullPath = path.append(tokenPath);
            if (LimyResourceUtils.newFile(fullPath).exists()) {
                return fullPath.toString() + "#" + mark.getBeginLine();
            }
        }
        return null;
    }

}
