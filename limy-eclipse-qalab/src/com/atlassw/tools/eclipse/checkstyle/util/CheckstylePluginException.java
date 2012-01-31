/*
 * Created 2007/08/30
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
//============================================================================
//
// Copyright (C) 2002-2006  David Schneider, Lars K�dderitzsch
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
//============================================================================

package com.atlassw.tools.eclipse.checkstyle.util;

//=================================================
// Imports from java namespace
//=================================================

//=================================================
// Imports from javax namespace
//=================================================

//=================================================
// Imports from com namespace
//=================================================

//=================================================
// Imports from org namespace
//=================================================

/**
 * Exception used internal to Checkstyle.
 */
public class CheckstylePluginException extends Exception
{
    //
    // constants
    //

    /** safe serialization support across different versions. */
    private static final long serialVersionUID = 8173568340314023129L;

    //
    // constructors
    //

    /**
     * Exception for the Checkstyle plug-in.
     * 
     * @param msg Description of the error.
     */
    public CheckstylePluginException(String msg)
    {
        super(msg);
    }

    /**
     * Exception for the Checkstyle plug-in.
     * 
     * @param msg Description of the error.
     * @param cause the causing exception
     */
    private CheckstylePluginException(String msg, Throwable cause)
    {
        super(msg, cause);
    }

    //
    // methods
    //

    /**
     * Wraps an exception into a CheckstylePluginException.
     * 
     * @param t the exception
     * @param message an additional exception message
     * @throws CheckstylePluginException the wrapped exception
     */
    public static void rethrow(Throwable t, String message) throws CheckstylePluginException
    {
        if (t instanceof CheckstylePluginException)
        {
            throw (CheckstylePluginException) t;
        }
        else
        {
            throw new CheckstylePluginException(message, t);
        }
    }

    /**
     * Wraps an exception into a CheckstylePluginException.
     * 
     * @param t the exception
     * @throws CheckstylePluginException the wrapped exception
     */
    public static void rethrow(Throwable t) throws CheckstylePluginException
    {
        rethrow(t, t.getLocalizedMessage());
    }
}