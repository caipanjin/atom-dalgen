/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.atom.dalgen.test;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;

/**
 * Mcenter任务
 * 
 * @author shizihu
 * @version $Id: McenterMain.java, v 0.1 2012-8-24 上午09:55:00 shizihu Exp $
 */
public final class McenterMain {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Project project = new Project();
        project.init();
        
        DefaultLogger logger = new DefaultLogger();
        logger.setErrorPrintStream(System.err);
        logger.setOutputPrintStream(System.out);
        logger.setMessageOutputLevel(Project.MSG_DEBUG);
        project.addBuildListener(logger);

        String path = new File(".").getAbsolutePath();
        String file = FilenameUtils.normalize(path + "/resources/howto/build.xml");

        ProjectHelper helper = ProjectHelper.getProjectHelper();
        helper.parse(project, new File(file));
        
        project.executeTarget(project.getDefaultTarget());
    }

}
