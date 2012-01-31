/*
 * Created 2007/08/21
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
package org.limy.eclipse.code.di;

import java.io.IOException;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.limy.eclipse.common.resource.LimyResourceUtils;

/**
 * DI(Dipendency Injection)�֘A�̃��[�e�B���e�B�N���X�ł��B
 * @author Naoki Iwami
 */
public final class LimyDIUtils {
    
    /**
     * private constructor
     */
    private LimyDIUtils() { }
    

    /**
     * Java�����N���X�ɑΉ�����C���^�[�t�F�C�X�̃��\�[�X��Ԃ��܂��B
     * @param type Java�����N���X
     * @return �C���^�[�t�F�C�X�̃��\�[�X
     * @throws IOException I/O��O
     * @throws CoreException �R�A��O
     */
    public static IResource getInterfaceResource(IType type) throws IOException, CoreException {
        
        IPath path = type.getPath();
        String fileName = path.removeFileExtension().lastSegment();
        
        IResource targetResource = null;
        if (fileName.endsWith("Impl")) {
            // �N���X���̍ŌオImpl��������A��������������O�̃��\�[�X��T��
            path = path.removeLastSegments(1);
            path = path.append(fileName.substring(0, fileName.length() - 4) + ".java");
            
            targetResource = LimyResourceUtils.newFile(path);
        }
        if (targetResource == null || !targetResource.exists()) {
            // implements�߂��烊�\�[�X��T��
            String[] names = type.getSuperInterfaceNames();
            if (names.length > 0) {
                SearchPattern pattern = SearchPattern.createPattern("*" + names[0],
                        IJavaSearchConstants.TYPE,
                        IJavaSearchConstants.DECLARATIONS,
                        SearchPattern.R_PATTERN_MATCH | SearchPattern.R_CASE_SENSITIVE);
                
                IJavaSearchScope scope = SearchEngine.createHierarchyScope(type);
                
                SearchEngine searchEngine = new SearchEngine();
                LimySearchRequestor requestor = new LimySearchRequestor();
                searchEngine.search(pattern,
                        new SearchParticipant[] {SearchEngine.getDefaultSearchParticipant() },
                        scope, requestor, null);
                
                if (requestor.getMatch() != null) {
                    targetResource = requestor.getMatch().getResource();
                }
            }
        }
        
        return targetResource.exists() ? targetResource : null;
    }

}
