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
package middlegen;

import java.awt.*;
import javax.swing.*;
import middlegen.swing.*;

/**
 * This panel renders and modifies columns and tables
 *
 * @author Aslak Helles
 * @created 3. oktober 2001
 * @version
 */
public class JSettingsPanel extends JPanel {

   /**
    * @todo-javadoc Describe the column
    */
   private final Plugin _plugin;
   /**
    * @todo-javadoc Describe the column
    */
   private final JColumnSettingsPanel _columnSettingsPanel;
   /**
    * @todo-javadoc Describe the column
    */
   private final JTableSettingsPanel _tableSettingsPanel;

   /**
    * @todo-javadoc Describe the column
    */
   private final CardLayout _cards = new CardLayout();
   /**
    * @todo-javadoc Describe the column
    */
   private static final String NOTHING = "NOTHING";
   /**
    * @todo-javadoc Describe the column
    */
   private static final String TABLE = "TABLE";
   /**
    * @todo-javadoc Describe the column
    */
   private static final String FIELD = "FIELD";


   /**
    * Describe what the JSettingsPanel constructor does
    *
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for constructor
    * @param plugin Describe what the parameter does
    * @param columnSettingsPanel Describe what the parameter does
    * @param tableSettingsPanel Describe what the parameter does
    */
   public JSettingsPanel(Plugin plugin, JColumnSettingsPanel columnSettingsPanel, JTableSettingsPanel tableSettingsPanel) {
      super();
      _plugin = plugin;
      _columnSettingsPanel = columnSettingsPanel;
      _tableSettingsPanel = tableSettingsPanel;

      setLayout(_cards);
      add(new JPanel(), NOTHING);
      add(decorate(_columnSettingsPanel), FIELD);
      add(decorate(_tableSettingsPanel), TABLE);
   }


   /**
    * Sets the Table attribute of the JSettingsPanel object
    *
    * @param table The new Table value
    */
   public void setTable(Table table) {
      _cards.show(this, TABLE);
      _tableSettingsPanel.setTable(_plugin.decorate(table));
   }


   /**
    * Sets the Column attribute of the JSettingsPanel object
    *
    * @param columns The new Columns value
    */
   public void setColumns(Column[] columns) {
      _cards.show(this, FIELD);

      Column[] decoratedColumns = new Column[columns.length];
      for (int i = 0; i < columns.length; i++) {
         decoratedColumns[i] = _plugin.decorate(columns[i]);
      }

      _columnSettingsPanel.setColumns(decoratedColumns);
   }


   /**
    * Describe what the method does
    *
    * @todo-javadoc Write javadocs for method
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for return value
    * @param panel Describe what the parameter does
    * @return Describe the return value
    */
   private JPanel decorate(JPanel panel) {
      JPanel decorator = new JPanel();
      FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
      decorator.setLayout(flowLayout);
      decorator.add(panel);
      return decorator;
   }
}
