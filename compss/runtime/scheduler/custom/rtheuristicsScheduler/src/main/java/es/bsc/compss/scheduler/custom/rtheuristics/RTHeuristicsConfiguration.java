package es.bsc.compss.scheduler.custom.rtheuristics;

import es.bsc.compss.COMPSsConstants;

import es.bsc.compss.log.Loggers;
import es.bsc.compss.util.ErrorManager;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;


public class RTHeuristicsConfiguration {

    public static String INPUT_FILE = "/tmp/input.txt";

    public static String PROMETHEUS_ENDPOINT = "localhost:9091";

    public static Boolean PROMETHEUS_ACTIVE = false;

    public static String HEURISTICS_SELECTED = "lns";

    public static Boolean HEURISTIC_ACTIVATED = false;


    /**
     * Load the config files to select the heuristic to execute.
     */
    public static void load() {
        String configFile = System.getProperty(COMPSsConstants.SCHEDULER_CONFIG_FILE);
        if (configFile != null && !configFile.isEmpty()) {
            try {
                PropertiesConfiguration conf = new PropertiesConfiguration(configFile);
                if (conf.getString("paper.inputfile") != null) {
                    INPUT_FILE = conf.getString("paper.inputfile");
                }
                if (conf.getString("prometheus.endpoint") != null) {
                    PROMETHEUS_ENDPOINT = conf.getString("prometheus.endpoint");
                }
                if (conf.getString("prometheus.active") != null) { // getting as a string for comparison with null
                    PROMETHEUS_ACTIVE = conf.getBoolean("prometheus.active");
                }
                if (conf.getString("heuristics.heuristics") != null) {
                    HEURISTICS_SELECTED = conf.getString("heuristics.heuristics");
                    HEURISTIC_ACTIVATED = true;
                }
            } catch (ConfigurationException e) {
                ErrorManager.warn("Exception reading configuration. Continuing with default values.", e);
            }
        }
    }

}
