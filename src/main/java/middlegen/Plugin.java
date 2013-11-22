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

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import middlegen.plugins.iwallet.config.IWalletConfigException;
import middlegen.plugins.iwallet.util.DalUtil;
import middlegen.swing.JColumnSettingsPanel;
import middlegen.swing.JTableSettingsPanel;

import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;

import com.atom.dalgen.utils.CfgUtils;

/**
 * This is the baseclass for plugins. It can be subclassed to add additional
 * functionality, or it can be used "as-is".
 *
 * @author <a href="mailto:aslak.hellesoy@bekk.no">Aslak Helles</a>
 * @created 3. april 2002
 */
public class Plugin {

    /**
     * @todo-javadoc Describe the field
     */
    private Middlegen                            _middlegen;

    /**
     * @todo-javadoc Describe the column
     */
    private final Map<DbColumn, ColumnDecorator> _columnDecorators               = new HashMap<DbColumn, ColumnDecorator>();
    /**
     * @todo-javadoc Describe the column
     */
    private final Map<String, TableDecorator>    _tableDecorators                = new HashMap<String, TableDecorator>();
    /**
     * @todo-javadoc Describe the column
     */
    private final Class[]                        _columnDecoratorConstructorArgs = new Class[] { Column.class };
    /**
     * @todo-javadoc Describe the column
     */
    private final Class<?>[]                     _tableDecoratorConstructorArgs  = new Class<?>[] { Table.class };

    /**
     * @todo-javadoc Describe the column
     */
    private File                                 _destinationDir;

    /** The name of the plugin */
    private String                               _name;

    /**
     * @todo-javadoc Describe the field
     */
    private String                               _mergedir;

    /**
     * @todo-javadoc Describe the field
     */
    private Map<String, FileProducer>            _fileProducers                  = new HashMap<String, FileProducer>();
    /**
     * @todo-javadoc Describe the field
     */
    private String                               _displayName;

    /** Whether or not to use the schema prefix in generated code. */
    private boolean                              _useSchemaPrefix                = false;

    /** Constructor */
    public Plugin() {
    }

    /**
     * Describe what the setUseSchemaPrefix constructor does
     *
     * @todo-javadoc Write javadocs for constructor
     * @todo-javadoc Write javadocs for method parameter
     * @param flag Describe what the parameter does
     */
    public void setUseSchemaPrefix(boolean flag) {
        _useSchemaPrefix = flag;
    }

    /**
     * Sets the Mergedir attribute of the Entity20Plugin object
     *
     * @param md The new Mergedir value
     */
    public void setMergedir(String md) {
        _mergedir = md;
    }

    /**
     * The root folder where the sources will be written. This value overrides
     * the destination attribute specified on the Ant task level.
     *
     * @param dir The new Destination value
     */
    public void setDestination(File dir) {
        _destinationDir = dir;
    }

    /**
     * Sets the logical plugin name. Not intended to be called from Ant.
     */
    public final void setName(String name) {
        _name = name;
    }

    /**
     * Gets the UseSchemaPrefix attribute of the Plugin object
     *
     * @return The UseSchemaPrefix value
     */
    public boolean isUseSchemaPrefix() {
        return _useSchemaPrefix;
    }

    /**
     * Returns the name to be used in the relations. Can be overridden in
     * subclasses
     *
     * @todo-javadoc Write javadocs for method parameter
     * @param table Describe what the parameter does
     * @return The RelationName value
     */
    public String getRelationName(Table table) {
        return table.getSqlName() + "-" + getName();
    }

    /**
     * Gets the Middlegen attribute of the Plugin object
     *
     * @return The Middlegen value
     */
    public Middlegen getMiddlegen() {
        return _middlegen;
    }

    /**
     * Gets the DestinationDir attribute of the Plugin object
     *
     * @return The DestinationDir value
     */
    public File getDestinationDir() {
        return _destinationDir;
    }

    /**
     * Gets the ColumnSettingsPanel attribute of the ClassType object
     *
     * @return The ColumnSettingsPanel value
     */
    public JColumnSettingsPanel getColumnSettingsPanel() {
        return null;
    }

    /**
     * Gets the TableSettingsPanel attribute of the ClassType object
     *
     * @todo return a TableConfigurator interface instead, to avoid dependence on
     *      swing packae
     * @return The TableSettingsPanel value
     */
    public JTableSettingsPanel getTableSettingsPanel() {
        return null;
    }

    /**
     * Gets the DisplayName attribute of the ClassType object
     *
     * @return The DisplayName value
     */
    public final String getDisplayName() {
        return _displayName;
    }

    /**
     * Returns the name of the plugin.
     *
     * @return The Name value
     */
    public final String getName() {
        return _name;
    }

    /**
     * Gets the ColumnDecoratorClass attribute of the Plugin object
     *
     * @return The ColumnDecoratorClass value
     */
    public Class<ColumnDecorator> getColumnDecoratorClass() {
        return ColumnDecorator.class;
    }

    /**
     * Gets the TableDecoratorClass attribute of the Plugin object
     *
     * @return The TableDecoratorClass value
     */
    public Class<TableDecorator> getTableDecoratorClass() {
        return TableDecorator.class;
    }

    /**
     * Gets the Tables attribute of the Plugin object
     *
     * @return The Tables value
     */
    public final Collection<TableDecorator> getTables() {
        return _tableDecorators.values();
    }

    /**
     * Gets the Table attribute of the Plugin object
     *
     * @todo-javadoc Write javadocs for method parameter
     * @param sqlName Describe what the parameter does
     * @return The Table value
     */
    public final TableDecorator getTable(String sqlName) {
        return (TableDecorator) _tableDecorators.get(sqlName);
    }

    /**
     * Gets the Mergedir attribute of the Entity20Plugin object
     *
     * @return The Mergedir value
     */
    public String getMergedir() {
        return _mergedir;
    }

    /**
     * Adds a file producer. If the file producer's file name contains the String
     * {0}, Middlegen will assume this is a per-table file producer, and one
     * instance for each table will be created. This method can be called from
     * Ant or from subclasses. <BR>
     *
     *
     * @param fileProducer the FileProducer to add.
     */
    public void addConfiguredFileproducer(FileProducer fileProducer) {
        fileProducer.validate();
        String id = fileProducer.getId();
        if (id == null) {
            // YUK. Magic id :-(
            fileProducer.setId("__custom_" + _fileProducers.size());
        }

        FileProducer customFileProducer = (FileProducer) _fileProducers.get(id);
        if (customFileProducer != null) {
            // A custom file producer has been specified in Ant. Override the destination.
            customFileProducer.copyPropsFrom(fileProducer);
        } else {
            // use the added file producer, but perform some sanity checks first.
            _fileProducers.put(fileProducer.getId(), fileProducer);
        }
    }

    /**
     * Creates and caches decorators for all Tables and Columns.
     */
    public final void decorateAll(Collection<DbTable> tables) {
        // loop over all tables
        for (DbTable table : tables) {
            // decorate table
            TableDecorator tableDecorator = createDecorator(table);
            tableDecorator.setPlugin(this);

            // cache it using subject as key. will be by clients as argument to decorate()
            _tableDecorators.put(table.getSqlName(), tableDecorator);

            // decorate columns and store refs in newly created TableDecorator
            DbColumn pkColumn = (DbColumn) table.getPkColumn();
            if (pkColumn != null) {
                ColumnDecorator pkColumnDecorator = createDecorator(pkColumn);
                pkColumnDecorator.setTableDecorator(tableDecorator);
                tableDecorator.setPkColumnDecorator(pkColumnDecorator);
                _columnDecorators.put(pkColumn, pkColumnDecorator);
            }

            Collection<ColumnDecorator> columnDecorators = new ArrayList<ColumnDecorator>(table
                .getColumns().size());
            for (Column coltmp : table.getColumns()) {
                DbColumn column = (DbColumn) coltmp;

                ColumnDecorator columnDecorator = createDecorator(column);
                columnDecorator.setPlugin(this);
                columnDecorator.setTableDecorator(tableDecorator);
                _columnDecorators.put(column, columnDecorator);
                columnDecorators.add(columnDecorator);
            }
            tableDecorator.setColumnDecorators(columnDecorators);
        }
        // now that everything is properly set up, call init on all decorators.
        for (TableDecorator tbdecorator : _tableDecorators.values()) {
            tbdecorator.init();
        }

        for (ColumnDecorator coldecorator : _columnDecorators.values()) {
            coldecorator.init();
        }
    }

    /**
     * Validates that the plugin is correctly configured
     *
     * @exception MiddlegenException if the state is invalid
     */
    public void validate() throws MiddlegenException {
        if (_destinationDir == null) {
            throw new MiddlegenException("destination must be specified in <" + getName() + ">");
        }
    }

    /**
     * Describe what the method does
     *
     * @todo-javadoc Write javadocs for method
     * @todo-javadoc Write javadocs for method parameter
     * @todo-javadoc Write javadocs for return value
     * @param mergeFile Describe what the parameter does
     * @return Describe the return value
     */
    public boolean mergeFileExists(String mergeFile) {
        return new File(getMergedir(), mergeFile).exists();
    }

    /**
     * Sets the DisplayName attribute of the Plugin object
     *
     * @param s The new DisplayName value
     */
    protected final void setDisplayName(String s) {
        _displayName = s;
    }

    /**
     * Describe what the method does
     *
     * @todo-javadoc Write javadocs for method
     */
    protected void registerFileProducers() {
    }

    /**
     * Describe what the method does
     */
    protected void generate() throws MiddlegenException {
        registerFileProducers();

        VelocityEngine velocityEngine = getEngine();

        doIt(velocityEngine);
    }

    /**
     * Sets the Middlegen attribute of the Plugin object
     *
     * @param middlegen The new Middlegen value
     */
    void setMiddlegen(Middlegen middlegen) {
        _middlegen = middlegen;
    }

    /**
     * Describe what the method does
     *
     * @todo-javadoc Write javadocs for method
     * @todo-javadoc Write javadocs for method parameter
     * @todo-javadoc Write javadocs for return value
     * @param column Describe what the parameter does
     * @return Describe the return value
     */
    final Column decorate(Column column) {
        if (column.getClass() != DbColumn.class) {
            throw new IllegalArgumentException("column must be of class "
                                               + DbColumn.class.getName());
        }
        if (column == null) {
            throw new IllegalArgumentException("column can't be null!");
        }
        ColumnDecorator result = (ColumnDecorator) _columnDecorators.get(column);
        if (result == null) {
            throw new IllegalArgumentException("result can't be null!");
        }
        return result;
    }

    /**
     * Describe what the method does
     *
     * @todo-javadoc Write javadocs for method
     * @todo-javadoc Write javadocs for method parameter
     * @todo-javadoc Write javadocs for return value
     * @param table Describe what the parameter does
     * @return Describe the return value
     */
    final Table decorate(Table table) {
        if (!table.getClass().equals(DbTable.class)) {
            throw new IllegalArgumentException("table must be of class " + DbTable.class.getName());
        }
        if (table == null) {
            throw new IllegalArgumentException("table can't be null!");
        }
        TableDecorator result = (TableDecorator) _tableDecorators.get(table.getSqlName());
        if (result == null) {
            throw new IllegalArgumentException("result can't be null!");
        }
        return result;
    }

    /**
     * Returns all the tabledecorators' file producers. Override this method if
     * you want different behaviour.
     *
     * @return The FileProducers value
     */
    private final Collection<FileProducer> getFileProducers() {
        return _fileProducers.values();
    }

    /**
     * Gets the Engine attribute of the Middlegen object
     *
     * @todo-javadoc Write javadocs for method parameter
     * @todo-javadoc Write javadocs for exception
     * @return The Engine value
     * @throws Exception 
     */
    private VelocityEngine getEngine() throws MiddlegenException {
        VelocityEngine velocityEngine = new VelocityEngine();

        Properties props = new Properties();
        // �趨velocity�ַ�
        props.setProperty(Velocity.INPUT_ENCODING, CfgUtils.findValue(Velocity.INPUT_ENCODING,
            "GBK"));
        props.setProperty(Velocity.OUTPUT_ENCODING, CfgUtils.findValue(Velocity.OUTPUT_ENCODING,
            "GBK"));

        // only load templates from file we don't have access to the jar and use a workaround for that
        props.setProperty(RuntimeConstants.RESOURCE_LOADER, "file");
        // use a resource loader that won't throw an exception if a resource (file) isn't found
        props.setProperty("file.resource.loader.class", "middlegen.KindFileResourceLoader");
        // tell velocity where merge files are located
        if (getMergedir() != null) {
            props.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, getMergedir());
        }
        // use our own log system that doesn't close the appenders upon gc() (the velocity one does)
        props.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
            "middlegen.DontCloseLog4JLogSystem");
        try {
            velocityEngine.init(props);
            return velocityEngine;
        } catch (Exception e) {
            // Hmm, throwning Exception is bad API, Velocity guys ;-)
            e.printStackTrace();
            throw new MiddlegenException(e.getMessage());
        }
    }

    /**
     * Adds additional file producers. This method is called right before the
     * generation starts. Depending on the fileName and tableDecorators
     * parameters, several things can happen: <p>
     *
     * If fileName contains {0}, a copy of each of these file producers is
     * created, substituting the {0} with the table name, and the original one is
     * removed.
     */
    private void doIt(VelocityEngine engine) throws MiddlegenException {
        for (FileProducer fileProducer : getFileProducers()) {
            if (fileProducer.isGenerationPerTable()) {
                // explode this file producer in multiple instances, potentially
                // one for every table (or less, if the file producer knows exactly
                // what tables it cares about).
                for (TableDecorator tableDecorator : getTables()) {
                    // ȡ�õ�ǰ����
                    String tableName = tableDecorator.getName();

                    // Check if we should generate for the table.
                    try {
                        if (DalUtil.inTabs(tableName)
                            && tableDecorator.getTableElement().isGenerate()) {
                            // Check whether the file producer accepts this table
                            if (tableDecorator.isGenerate() && fileProducer.accept(tableDecorator)) {
                                fileProducer.getContextMap().put("plugin", this);
                                fileProducer.generateTableForSofa(engine, tableDecorator);
                            }
                        }
                    } catch (IWalletConfigException e) {
                        throw new MiddlegenException(e.getMessage());
                    }
                }
            } else {
                // This file producer will take a collection of table decorators in stead of
                // one single table. Let's see if it wants all or only a subset.
                List<TableDecorator> acceptedTableDecorators = new ArrayList<TableDecorator>();
                for (TableDecorator tableDecorator : getTables()) {
                    if (tableDecorator.getTableElement().isGenerate()) {
                        if (tableDecorator.isGenerate() && fileProducer.accept(tableDecorator)) {
                            acceptedTableDecorators.add(tableDecorator);
                        }
                    }
                }

                fileProducer.getContextMap().put("plugin", this);
                fileProducer.generateForTables(engine, acceptedTableDecorators);
            }
        }
    }

    /**
     * Describe what the method does
     *
     * @todo-javadoc Write javadocs for return value
     * @todo-javadoc Write javadocs for method
     * @todo-javadoc Write javadocs for method parameter
     * @param column Describe what the parameter does
     * @return Describe the return value
     */
    private final ColumnDecorator createDecorator(DbColumn column) {
        Object decorator = _columnDecorators.get(column);

        if (decorator == null) {
            decorator = createDecorator(column, getColumnDecoratorClass(),
                _columnDecoratorConstructorArgs);
        }

        return (ColumnDecorator) decorator;
    }

    /**
     * Describe what the method does
     */
    private final TableDecorator createDecorator(DbTable table) {
        Object decorator = _tableDecorators.get(table.getSqlName());

        if (decorator == null) {
            decorator = createDecorator(table, getTableDecoratorClass(),
                _tableDecoratorConstructorArgs);
        }

        return (TableDecorator) decorator;
    }

    /**
     * Describe what the method does
     */
    private final Object createDecorator(Object subject, Class<?> decoratorClass,
                                         Class<?>[] decoratorConstructorArgs) {
        Object decorator = null;
        String invokedConstructor = decoratorClass.getName() + "(" + subject.getClass().getName()
                                    + ")";
        try {
            Constructor<?> constructor = decoratorClass.getConstructor(decoratorConstructorArgs);
            decorator = constructor.newInstance(new Object[] { subject });
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Couldn't invoke constructor " + invokedConstructor);
        }

        return decorator;
    }
}
