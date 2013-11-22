/*
 * Copyright (c) 2001, Aslak Helles酶y, BEKK Consulting
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

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import middlegen.plugins.iwallet.IWalletPlugin;

import com.atom.dalgen.utils.LogUtils;

/**
 * This class implements the core engine that will initialise and invoke all the plugins.
 */
public class Middlegen {

    /** 数据库信息 */
    private DatabaseInfo                    databaseInfo;

    /** 生成任务 */
    private MiddlegenTask                   middlegenTask;

    /**
     * @todo-javadoc Describe the column
     */
    private final Map<String, TableElement> _tableElements = new HashMap<String, TableElement>();

    /**
     * @todo-javadoc Describe the column
     */
    private final Map<String, DbTable>      _tables        = new HashMap<String, DbTable>();

    /** All plugins */
    private final List<Plugin>              _plugins       = new LinkedList<Plugin>();

    /** Maps logical name to plugin class */
    private final Map<String, Class<?>>     _pluginClasses = new HashMap<String, Class<?>>();

    /**
     * @todo-javadoc Describe the column
     */
    public static final String              _NL            = System.getProperty("line.separator");

    /**
     * Creates a new Middlegen object
     */
    public Middlegen(MiddlegenTask middlegenTask) {
        this.middlegenTask = middlegenTask;

        LogUtils.log("系统启动，注册[IWalletPlugin]插件...");

        this.registerPlugin("iwallet", IWalletPlugin.class);
    }

    /**
     * Gets the DatabaseInfo attribute of the Middlegen object
     *
     * @return The DatabaseInfo value
     */
    public DatabaseInfo getDatabaseInfo() {
        return this.databaseInfo;
    }

    /**
     * Gets the TableElements attribute of the Middlegen object
     *
     * @return The TableElements value
     */
    public Map<String, TableElement> getTableElements() {
        return _tableElements;
    }

    /**
     * Gets the MiddlegenTask attribute of the Middlegen object
     *
     * @return The MiddlegenTask value
     */
    public MiddlegenTask getMiddlegenTask() {
        return this.middlegenTask;
    }

    /**
     * Gets all the registered tables
     *
     * @return A Collection of {@link DbTable} objects
     */
    public Collection<DbTable> getTables() {
        return _tables.values();
    }

    /**
     * Gets all the tables. The returned tables are decorated by the plugin.
     *
     * @param plugin Describe what the parameter does
     * @return A Collection of {@link TableDecorator} objects (of the class
     *      specified by the plugin's {@link Plugin#getTableDecoratorClass()}
     *      method)
     */
    public Collection getTables(Plugin plugin) {
        Collection result = new LinkedList();
        Iterator i = getTables().iterator();
        while (i.hasNext()) {
            result.add(plugin.decorate((DbTable) i.next()));
        }
        return result;
    }

    /**
     * Gets the Plugins attribute of the Middlegen object
     *
     * @return The Plugins value
     */
    public List<Plugin> getPlugins() {
        return _plugins;
    }

    /**
     * Gets the Plugin attribute of the Middlegen object
     */
    public Plugin getPlugin(String name) {
        // A plugin might change name after it's added. That's why
        // we don't store it in a map. just iterate.
        for (Plugin plugin : getPlugins()) {
            if (plugin.getName().equals(name)) {
                return plugin;
            }
        }

        return null;
    }

    /**
     * Gets the Table attribute of the Middlegen object
     *
     * @todo-javadoc Write javadocs for method parameter
     * @param tableSqlName Describe what the parameter does
     * @return The Table value
     * @throws MiddlegenException if no table can be found for the given
     *      tableSqlName
     */
    public DbTable getTable(String tableSqlName) {
        DbTable result = (DbTable) _tables.get(tableSqlName.toLowerCase());
        if (result == null) {
            throw new IllegalArgumentException(
                "Couldn't find any table named "
                        + tableSqlName
                        + ". Check the spelling and make sure it figures among the declared tables.");
        }
        return result;
    }

    /**
     * Gets the Class for a logical name. The name must match a name in one of
     * the middlegen-plugins.xml files inside one of the plugin jars on the
     * classpath
     */
    public Class<?> getPluginClass(String name) {
        return _pluginClasses.get(name);
    }

    /**
     * Returns true if Middlegen contains the table
     *
     * @param tableSqlName The sql name of the table
     * @return
     */
    public boolean containsTable(String tableSqlName) {
        return _tables.get(tableSqlName.toLowerCase()) != null;
    }

    /**
     * Adds a plugin
     *
     * @param plugin the one to add
     */
    public void addPlugin(Plugin plugin) {
        _plugins.add(plugin);
        plugin.setMiddlegen(this);
    }

    /**
     * Describe the method
     *
     * @todo-javadoc Describe the method
     * @todo-javadoc Describe the method parameter
     * @todo-javadoc Write javadocs for exception
     * @param tableElement Describe the method parameter
     */
    public void addTableElement(TableElement tableElement) {
        _tableElements.put(tableElement.getName(), tableElement);
    }

    /**
     * Describe what the method does
     *
     * @todo-javadoc Write javadocs for method
     */
    public void clear() {
        _tables.clear();
    }

    /**
     * Adds a table
     *
     * @param table The table to add
     */
    public void addTable(DbTable table) {
        _tables.put(table.getSqlName().toLowerCase(), table);
    }

    /**
     * Describe the method
     */
    public void registerPlugin(String name, Class<?> clazz) {
        LogUtils.log("注册插件： " + name + "->" + clazz.getName());
        _pluginClasses.put(name, clazz);
    }

    /**
     * Tells all file types to decorate all columns and tables. Called by ant
     * task before gui is shown and the generation begins.
     */
    public void decorateAll() {
        for (Plugin plugin : this.getPlugins()) {
            // passing ourself, so plugin can ask us for the stuff it wants to decorate.
            plugin.decorateAll(this.getTables());
        }
    }

    /**
     * Describe what the method does
     */
    public void validate() throws MiddlegenException {
        // verify that we don't already have a plugin with the same name
        Set<String> pluginNames = new HashSet<String>();

        for (Plugin plugin : getPlugins()) {
            if (pluginNames.contains(plugin.getName())) {
                String msg = "插件已存在[" + plugin.getName() + "]，请使用其它插件名！";
                LogUtils.log(msg);

                throw new MiddlegenException(msg);
            }

            pluginNames.add(plugin.getName());

            LogUtils.log("验证插件：" + plugin.getName());
            plugin.validate();
        }
    }

    /**
     * Generates source files for all registered file types
     * @todo move this method to FileProducer
     */
    public void writeSource() throws MiddlegenException {
        for (Plugin plugin : _plugins) {
            LogUtils.log("执行插件生成代码：" + plugin.getName());
            plugin.generate();
        }
    }

    /**
     * Sets the DatabaseInfo attribute of the Middlegen object
     *
     * @param databaseInfo The new DatabaseInfo value
     */
    void setDatabaseInfo(DatabaseInfo databaseInfo) {
        this.databaseInfo = databaseInfo;
    }
}
