/*
 * Copyright (c) 2001, Aslak Hellesøy, BEKK Consulting
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * - Neither the name of BEKK Consulting nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package middlegen.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import middlegen.DbTable;

import com.atom.dalgen.utils.LogUtils;

/**
 * @author <a href="mailto:aslak.hellesoy@bekk.no">Aslak Helles</a>
 * @created 3. oktober 2001
 * @todo use icons for * and 1
 * @todo make a nicer arrow
 * @todo display pk (on column) and fk icons on (arrow) (for info only)
 * @version $Id: JTablePanel.java,v 1.1 2005/10/25 14:59:22 lusu Exp $
 */
public class JTablePanel extends JPanel {

    /**
     * @todo-javadoc Describe the column
     */
    //	private JSettingsPanel _settingsPanel;

    /**
     * @todo-javadoc Describe the column
     */
    private final DbTable    _table;

    /**
     * @todo-javadoc Describe the column
     */
    private final JLabel     _title;
    /**
     * @todo-javadoc Describe the column
     */
    private final JTableList _jList;

    /**
     * @todo-javadoc Describe the column
     */
    private final int        _cellHeight;

    /**
     * @todo-javadoc Describe the column
     */
    private static Border    _unSelectedBorder = BorderFactory.createLineBorder(Color.black, 1);
    /**
     * @todo-javadoc Describe the column
     */
    private static Border    _selectedBorder   = BorderFactory.createLineBorder(Color.black, 2);

    /**
     * Creates new JDatabaseTable
     *
     * @todo-javadoc Write javadocs for method parameter
     * @todo-javadoc Write javadocs for method parameter
     * @todo-javadoc Write javadocs for method parameter
     * @param table Describe what the parameter does
     */
    public JTablePanel(DbTable table) {
        setLayout(new BorderLayout());
        _table = table;
        _title = new JLabel(" " + table.getName() + " ", SwingConstants.CENTER);
        add(_title, BorderLayout.NORTH);

        _jList = new JTableList(table);

        // Set the height of the table component
        Rectangle r = _jList.getCellBounds(0, 0);
        if (r == null) {
            r = new Rectangle();
            LogUtils.get().warn("Problem with table " + table.getName());
        }
        _cellHeight = r.height;
        add(_jList, BorderLayout.CENTER);
        setSelected(false);
    }

    /**
     * Sets the Selected attribute of the JTablePanel object
     *
     * @param selected The new Selected value
     */
    public void setSelected(boolean selected) {
        if (selected) {
            if (getBorder() == _unSelectedBorder) {
                setLocation(getLocation().x - 1, getLocation().y - 1);
            }
            setBorder(_selectedBorder);
        } else {
            if (getBorder() == _selectedBorder) {
                setLocation(getLocation().x + 1, getLocation().y + 1);
            }
            setBorder(_unSelectedBorder);
            _jList.clearSelection();
        }
    }

    /**
     * Gets the List attribute of the JTablePanel object
     *
     * @return The List value
     */
    public JList getList() {
        return _jList;
    }

    /**
     * Gets the Table attribute of the JTablePanel object
     *
     * @return The Table value
     */
    public DbTable getTable() {
        return _table;
    }

    /**
     * Gets the y coordinate of the given columnName
     *
     * @todo we should really throw an ex instead of logging an error. has to do
     *      with m:n relationships
     * @todo-javadoc Write javadocs for method parameter
     * @param columnName Describe what the parameter does
     * @return The ColumnY value
     */
    public int getColumnY(String columnName) {
        int h = getHeight();

        int rowCount = _jList.getModel().getSize();
        int index = _table.getIndex(columnName);
        if (index == -1) {
            // throw new IllegalStateException("There is no column named " + columnName + " in the table named " + _table.getSqlName() + middlegen.Middlegen.BUGREPORT);
            LogUtils.get().error("There is no column named " + columnName + " in the table named " + _table.getSqlName());
        }
        LogUtils.get().debug(_table.getSqlName() + "." + columnName + " " + index);
        int rowsUpFromBottom = rowCount - index;

        int y = h - (rowsUpFromBottom * _cellHeight) + (_cellHeight / 2);
        LogUtils.get().debug("y=" + y);
        return y;
    }
}
