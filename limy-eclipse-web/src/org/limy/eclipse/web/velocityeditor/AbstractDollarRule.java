/*
 * Created 2007/01/15
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

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.limy.eclipse.common.jface.MultiTextAttribute;

/**
 *
 * @author Naoki Iwami
 */
public abstract class AbstractDollarRule implements IRule {

    // ------------------------ Fields

    /**
     * �X�L���i
     */
    private ICharacterScanner scanner;
    
    /**
     * $�L���̌��o���[��
     */
    private VelocityDollarRule dollarRule;

    /**
     * �X�L���i���J�E���g
     */
    private int count;

    // ------------------------ Constructors

    /**
     * AbstractDollarRule�C���X�^���X���\�z���܂��B
     * @param dollarRule $�L���̌��o���[��
     */
    public AbstractDollarRule(VelocityDollarRule dollarRule) {
        
        super();
        this.dollarRule = dollarRule;
    }
    
    // ------------------------ Protected Methods

    /**
     * �X�L���i����ꕶ���擾���܂��B
     * @param scanner 
     * @return �擾��������
     */
    protected int readScanner() {
        ++count;
        return scanner.read();
    }

    /**
     * �X�L���i����S�Ă̕�����߂��܂��B
     */
    protected void unreadAll() {
        for (int i = 0; i < getCount(); i++) {
            scanner.unread();
        }
    }

    /**
     * $�����ȍ~�̉�͂��s���܂��B
     * @param attr 
     */
    protected void doEvaluateDollar(MultiTextAttribute attr) {
        scanner.unread();
        IToken result = dollarRule.evaluate(scanner);
        if (result == Token.UNDEFINED) {
            // do nothing
            scanner.read();
        } else {
            attr.addMulti((MultiTextAttribute)result.getData(), 0);
            count += dollarRule.getCount() - 1;
        }

    }
    
    // ------------------------ Getter/Setter Methods

    /**
     * �X�L���i���擾���܂��B
     * @return �X�L���i
     */
    protected ICharacterScanner getScanner() {
        return scanner;
    }

    /**
     * �X�L���i��ݒ肵�܂��B
     * @param scanner �X�L���i
     */
    protected void setScanner(ICharacterScanner scanner) {
        this.scanner = scanner;
    }

    /**
     * �X�L���i���J�E���g���擾���܂��B
     * @return �X�L���i���J�E���g
     */
    protected int getCount() {
        return count;
    }

    /**
     * �X�L���i���J�E���g��ݒ肵�܂��B
     * @param count �X�L���i���J�E���g
     */
    protected void setCount(int count) {
        this.count = count;
    }
    
}
