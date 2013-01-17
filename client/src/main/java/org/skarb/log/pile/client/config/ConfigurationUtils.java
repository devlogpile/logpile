package org.skarb.log.pile.client.config;

/**
 * The list of the different of way of passing the configuration information.
 * User: skarb
 * Date: 17/01/13
 */
public enum ConfigurationUtils {
    XML(new XmlFile()),
    PROPERTIES(new PropertiesFile()),
    DEFAULT(new SystemProperty());

    /**
     * The type of the configuration retrievers.
     */
    private final Configuration configuration;

    /**
     * Constructor.
     * @param conf the bean.
     */
    private ConfigurationUtils(final Configuration conf){
         configuration = conf;
    }

    /**
     * Get the current configuration used by the project.
     * @return  the instance.
     */
    public static Configuration retrieveCurrent() {
       for (final ConfigurationUtils conf :  values()) {
            if(!conf.equals(DEFAULT) && conf.configuration.configured()){
                return conf.configuration;
            }
       }

       return DEFAULT.configuration;
    }
}
