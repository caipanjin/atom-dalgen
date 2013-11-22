/*
 * Alipay.com Inc.
 * Copyright (c) 2004-2005 All Rights Reserved.
 */
package middlegen.plugins.iwallet.config;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * An configuration object represents a DAO method param.
 */
public class IWalletParamConfig {

    /** name of the param */
    private String name;

    /** java type of the param */
    private String javaType;

    /**
     * Constructor.
     */
    public IWalletParamConfig() {
        super();
    }

    /**
     * @return Returns the javaType.
     */
    public String getJavaType() {
        return javaType;
    }

    /**
     * @param javaType The javaType to set.
     */
    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
