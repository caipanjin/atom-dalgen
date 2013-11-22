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
import java.util.Iterator;
import java.util.List;

import middlegen.plugins.iwallet.IWalletPlugin;
import middlegen.plugins.iwallet.IWalletSeq;
import middlegen.plugins.iwallet.config.IWalletConfig;
import middlegen.plugins.iwallet.config.IWalletConfigException;
import middlegen.plugins.iwallet.util.DalUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DynamicConfigurator;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

import com.atom.dalgen.ObjectFactory;
import com.atom.dalgen.utils.CfgUtils;
import com.atom.dalgen.utils.LogUtils;

/**
 * This Ant task is a thin wrapper around Middlegen that makes it possible to
 * fire up Middlegen (with or without gui) from Ant. The task also provides
 * configuration of Middlegen: What tables to read, what class types to generate
 * and so on.
 */
public class MiddlegenTask extends Task implements DynamicConfigurator {

    /** 生成器 */
    private Middlegen    middlegen;

    /**
     * @todo-javadoc Describe the field
     */
    private File         prefsDir = new File(System.getProperty("user.home") + File.separator
                                             + ".middlegen");

    /** We're storing the classpath for error reporting. */
    private final String classpath;

    /** 配置文件 */
    private File         configFile;

    /** 执行目标 */
    private String       tabs;

    /**
     * CTOR
     */
    public MiddlegenTask() {
        LogUtils.log("**********************************");
        LogUtils.log("* 开始启动dalgen任务 *");
        LogUtils.log("* " + this.getClass().getName() + " *");
        LogUtils.log("**********************************");

        this.middlegen = new Middlegen(this);

        try {
            this.classpath = ((AntClassLoader) getClass().getClassLoader()).getClasspath();
            LogUtils.log("dalgen类路径：" + this.classpath);
        } catch (ClassCastException e) {
            throw new BuildException(
                "middlegen.jar should not be on the system classpath when starting Ant. It should be on a path passed to the <taskdef> using classpath or classpathref.");
        }
    }

    /**
     * Sets the DynamicAttribute attribute of the MiddlegenTask object
     */
    public void setDynamicAttribute(String name, String value) {
        throw new BuildException("The <" + getTaskName() + "> task doesn't support the \"" + name
                                 + "\" attribute.");
    }

    /**
     * Called by Ant for each nested
     * <table>
     *   element after all attributes are set.
     */
    public void addConfiguredTable(TableElement tableElement) {
        this.middlegen.addTableElement(tableElement);
    }

    /**
     * Describe what the method does
     */
    public void execute() throws BuildException {
        LogUtils.log("开始执行dalgen任务：" + this.getClass().getName());

        // 2. 初始化DAL配置
        try {
            // 用来初始化dal-config.xml中的内容，_configFile就是dal-config.xml
            IWalletConfig.init(this.configFile);
        } catch (IWalletConfigException e) {
            e.printStackTrace();
            throw new BuildException(e);
        }

        try {
            Prefs.getInstance().init(this.prefsDir, CfgUtils.getAppName());
        } catch (MiddlegenException e) {
            throw new BuildException(e);
        } catch (NoClassDefFoundError e) {
            log(
                "It seems you're running a JDK inferior to 1.4. You should upgrade to JDK 1.4 if you want Middlegen to remember the settings.",
                Project.MSG_WARN);
        }

        try {
            MiddlegenPopulator populator = new MiddlegenPopulator(this.middlegen);

            // 用来验证dal-config.xml中的SEQ信息
            this.middlegen.validate();

            if (this.middlegen.getPlugin("iwallet") != null) {
                IWalletPlugin plugin = (IWalletPlugin) this.middlegen.getPlugin("iwallet");

                this.middlegen.getTableElements().clear();

                StringBuffer msgBuffer = new StringBuffer();

                int count = 0;

                // plugin.getAllTableNames()用来获得dal-config.xml中的所有表名信息
                for (Iterator<String> i = plugin.getAllTableNames().iterator(); i.hasNext();) {
                    String tableName = i.next();

                    TableElement tableElement = new TableElement();
                    tableElement.setName(tableName);

                    this.middlegen.addTableElement(tableElement);

                    if (DalUtil.inTabs(tableName)) { //要生成DAO的表名.xml
                        // 如果当前表在指定范围内,提示给用户
                        count++;
                        msgBuffer.append("(" + count + ")." + tableName + "\n");
                    }
                }

                if (StringUtils.equals(tabs, "seq")) {
                    List<IWalletSeq> seqs = plugin.getSequences();
                    int size = seqs == null ? 0 : seqs.size();

                    for (int i = 0; i < size; i++) {
                        count++;
                        IWalletSeq seq = (IWalletSeq) seqs.get(i);
                        msgBuffer.append("(" + count + ")." + seq.getName() + "\n");
                    }

                    if (msgBuffer.toString().length() == 0) {
                        log("没有找到Sequence记录.", Project.MSG_WARN);
                        return;
                    }
                    log("对以下Sequence重新生成DAL: \n" + msgBuffer.toString(), Project.MSG_INFO);
                } else {
                    if (msgBuffer.toString().length() == 0) {
                        // 如果指定的表名一个也没匹配,提示错误信息,结束操作
                        log("指定的表没有找到,请重新操作.", Project.MSG_WARN);
                        return;
                    }
                    log("对以下表重新生成DAL: \n" + msgBuffer.toString(), Project.MSG_INFO);
                }
            }

            if (this.middlegen.getTableElements().isEmpty()) {
                log(
                    "No <table> elements specified. Reading all tables. This might take a while...",
                    Project.MSG_WARN);
                populator.addRegularTableElements();
            }

            populator.populate(this.middlegen.getTableElements(), tabs);

            // Instantiate all plugins' decorators.
            this.middlegen.decorateAll();
            this.middlegen.writeSource();

            try {
                Prefs.getInstance().save();
            } catch (NoClassDefFoundError ignore) {
            }
        } catch (MiddlegenException e) {
            throw new BuildException(e);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new BuildException(e);
        }
    }

    /**
     * Describe what the method does
     */
    public Object createDynamicElement(String name) {
        LogUtils.log("任务创建动态节点：" + name);

        Class<?> pluginClass = this.middlegen.getPluginClass(name);
        if (pluginClass == null) {
            throw new BuildException("Nothing known about plugin <" + name + ">. ");
        }

        Plugin plugin = null;
        try {
            plugin = (Plugin) pluginClass.newInstance();
            plugin.setName(name);

            this.middlegen.addPlugin(plugin);
        } catch (Exception e) {
            throw new BuildException("Couldn't instantiate Plugin " + pluginClass.getName(), e);
        }

        return plugin;
    }

    // ~~~~~~~~~~~~ 设置属性 ~~~~~~~~~~~ //

    public void setTabs(String tabs) {
        this.tabs = tabs;
        ObjectFactory.setTabs(this.tabs);
    }

    public void setPrefsdir(File prefsDir) {
        this.prefsDir = prefsDir;
    }

    public void setConfigFile(String configFile) {
        this.configFile = new File(configFile);
    }

}
