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

import javax.swing.*;
import java.util.*;
import middlegen.JSettingsPanel;
import middlegen.Plugin;
import middlegen.Column;
import middlegen.Table;

/**
 * A tabbed pane which displays settings for each active class type
 *
 * @author Aslak Helles
 * @created 11. april 2002
 */
public class JSettingsTabPane extends JTabbedPane {
   /**
    * @todo-javadoc Describe the column
    */
   private final Collection _settingsPanels = new ArrayList();


   /**
    * Describe what the JSettingsTabPane constructor does
    *
    * @todo-javadoc Write javadocs for constructor
    * @todo-javadoc Write javadocs for method parameter
    * @param plugins Describe what the parameter does
    */
   public JSettingsTabPane(Collection plugins) {
      Iterator pluginIterator = plugins.iterator();
      while (pluginIterator.hasNext()) {

         Plugin plugin = (Plugin)pluginIterator.next();
         JColumnSettingsPanel columnSettingsPanel = plugin.getColumnSettingsPanel();
         JTableSettingsPanel tableSettingsPanel = plugin.getTableSettingsPanel();
         if (columnSettingsPanel != null && tableSettingsPanel != null) {
            JSettingsPanel settingsPanel = new JSettingsPanel(plugin, columnSettingsPanel, tableSettingsPanel);
            _settingsPanels.add(settingsPanel);
            addTab(plugin.getDisplayName() + " (" + plugin.getName() + ")", settingsPanel);
         }
      }
   }


   /**
    * Sets the Columns attribute of the JSettingsTabPane object
    *
    * @param columns The new Columns value
    */
   public void setColumns(Column[] columns) {
      Iterator i = _settingsPanels.iterator();
      while (i.hasNext()) {
         JSettingsPanel settingsPanel = (JSettingsPanel)i.next();
         settingsPanel.setColumns(columns);
      }
   }


   /**
    * Sets the Table attribute of the JSettingsTabPane object
    *
    * @param table The new Table value
    */
   public void setTable(Table table) {
      Iterator i = _settingsPanels.iterator();
      while (i.hasNext()) {
         JSettingsPanel settingsPanel = (JSettingsPanel)i.next();
         settingsPanel.setTable(table);
      }
   }
}
