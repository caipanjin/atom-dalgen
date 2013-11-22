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

import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractListModel;

import middlegen.Column;
import middlegen.Table;

/**
 * @author <a href="mailto:aslak.hellesoy@bekk.no">Aslak Helles</a>
 * @created 3. oktober 2001
 * @version $Id: TableListModel.java,v 1.1 2005/10/25 14:59:22 lusu Exp $
 */
public class TableListModel extends AbstractListModel {

    /**
     * @todo-javadoc Describe the column
     */
    private final Table _table;

    /**
     * Creates new TableTableModel
     *
     * @todo-javadoc Write javadocs for method parameter
     * @param table Describe what the parameter does
     */
    public TableListModel(Table table) {
        _table = table;
    }

    /**
     * Gets the RowCount attribute of the TableTableModel object
     *
     * @return The RowCount value
     */
    public int getSize() {
        return _table.getColumns().size();
    }

    /**
     * Gets the ValueAt attribute of the TableTableModel object
     *
     * @todo-javadoc Write javadocs for method parameter
     * @todo-javadoc Write javadocs for method parameter
     * @param row Describe what the parameter does
     * @return The ValueAt value
     */
    public Object getElementAt(int row) {
        List list = Arrays.asList(_table.getColumns().toArray());
        //		Column column = (Column)CollectionUtils.index(_table.getColumns(), row);
        Column column = (Column) list.get(row);
        return column;
    }
}
