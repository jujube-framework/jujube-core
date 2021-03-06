package org.jujubeframework.constant;

import org.jujubeframework.util.Beans;

/**
 * Spring Profiles
 *
 * @author John Li
 */
public class Profiles {

    /**
     * H2的驱动
     */
    public static final String H2_DRIVER_CLASS_NAME = "org.h2.Driver";

    private static final String SPRING_PROFILES_ACTIVE = "spring.profiles.active";

    private Profiles() {
    }

    public static void setSpringProfileAsSystemProperty(String profile) {
        System.setProperty(SPRING_PROFILES_ACTIVE, profile);
    }

    public static String getSpringProfileAsSystemProperty() {
        return System.getProperty(SPRING_PROFILES_ACTIVE);
    }

    public static final String ACTIVE_PROFILE = SPRING_PROFILES_ACTIVE;
    public static final String DEFAULT_PROFILE = "spring.profiles.default";
    public static final String PRODUCTION = "prod";
    public static final String DEVELOPMENT = "dev";
    public static final String UNIT_TEST = "test";
    public static final String FUNCTIONAL_TEST = "func";
}