package es.bsc.compss.scheduler.custom.heuristics;

import es.bsc.compss.COMPSsConstants;
import es.bsc.compss.log.Loggers;
import es.bsc.compss.util.ErrorManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class HeuristicsConfiguration {

    public static String HEURISTICS_FILE = "";

    public static String HEURISTICS_SELECTED = "spt";

    // Logger
    protected static final Logger LOGGER = LogManager.getLogger(Loggers.TS_COMP);


    /**
     * Loads the scheduler config file.
     */
    public static void load(Heuristics heuristics) {
        String configFile = System.getProperty(COMPSsConstants.SCHEDULER_CONFIG_FILE);
        if (configFile != null && !configFile.isEmpty()) {
            try {
                PropertiesConfiguration conf = new PropertiesConfiguration(configFile);
                if (conf.getString("heuristics.inputfile") != null) {
                    HEURISTICS_FILE = conf.getString("heuristics.inputfile");
                }
                if (conf.getString("heuristics.heuristics") != null) {
                    HEURISTICS_SELECTED = conf.getString("heuristics.heuristics");
                }
            } catch (ConfigurationException e) {
                ErrorManager.warn("Exception reading configuration. Continuing with default values.", e);
            }
        }
        readInput(heuristics);
    }

    /**
     * Reads the input file input_ilp.dat.
     */
    public static void readInput(Heuristics heuristics) {
        try (BufferedReader br = new BufferedReader(new FileReader(new File(HEURISTICS_FILE)))) {
            String lines;

            // Set number of workers
            lines = br.readLine();
            String[] st = lines.trim().split("\\s+");

            int m;

            m = Integer.parseInt(st[2].substring(0, st[2].indexOf(';')));

            lines = br.readLine();
            st = lines.trim().split("\\s+");
            // Set number of tasks
            final int n = Integer.parseInt(st[2].substring(0, st[2].indexOf(';')));

            lines = br.readLine();
            st = lines.trim().split("\\s+");
            final int numberWarmUpTasks = Integer.parseInt(st[2].substring(0, st[2].indexOf(';')));

            // Reading nSucc list, only used in LNS
            int[] nSucc = new int[n];
            lines = br.readLine();
            st = lines.trim().split("\\s+");

            int index = 0;
            for (int j = 2; j < st.length - 1; j++) {
                if (j == 2) {
                    int pos = st[j].indexOf('[');
                    nSucc[index] = Integer.parseInt(st[j].substring(pos + 1));
                } else {
                    nSucc[index] = Integer.parseInt(st[j]);
                }
                index += 1;
            }

            br.readLine();

            // Reading all succ values
            boolean[][] succ = new boolean[n][n];
            for (int i = 0; i < n; ++i) {
                lines = br.readLine();
                st = lines.trim().split("\\s+");
                for (int j = 0; j < n; ++j) {
                    if (j == 0) {
                        int pos = st[0].indexOf('[');
                        st[0] = st[0].substring(pos + 1, pos + 2);
                    }
                    succ[i][j] = Integer.parseInt(st[j]) == 1;
                }
            }
            br.readLine();
            br.readLine();
            float[][] z = new float[n][n];
            for (int i = 0; i < n; ++i) {
                lines = br.readLine();
                st = lines.trim().split("\\s+");
                for (int j = 0; j < n; ++j) {
                    if (j == 0) {
                        int pos = st[0].indexOf('[');
                        st[0] = st[0].substring(pos + 1);
                    }
                    z[i][j] = Float.parseFloat(st[j]);
                }
            }

            br.readLine();

            // Set cMargin (per one)
            lines = br.readLine();
            st = lines.trim().split("\\s+");
            final float cMargin = Float.parseFloat(st[2].substring(0, st[2].indexOf(';')));

            br.readLine();

            int totalSyncTasks = 0;
            boolean[] syncTaskIds = new boolean[n];

            // Set C matrix adding the cMargin specified in the document
            float[][] c = new float[n][m];
            for (int i = 0; i < n; ++i) {
                float sum = 0.0f;
                lines = br.readLine();
                st = lines.trim().split("\\s+");
                for (int j = 0; j < m; ++j) {
                    if (j == 0) {
                        int pos = st[0].indexOf('[');
                        st[0] = st[0].substring(pos + 1);
                    }
                    float auxSum = Float.parseFloat(st[j]);
                    c[i][j] = auxSum < 0.0f ? auxSum : auxSum * (1 + cMargin);
                    sum += c[i][j];
                }
                syncTaskIds[i] = sum < 0.0f; // Check if syncNode
                totalSyncTasks += syncTaskIds[i] ? 1 : 0; // Increment totalSyncTasks if syncNode
            }

            br.readLine();
            br.readLine();

            float[][] ibw = new float[m][m];
            for (int i = 0; i < m; ++i) {
                lines = br.readLine();
                st = lines.trim().split("\\s+");
                for (int j = 0; j < m; ++j) {
                    if (j == 0) {
                        int pos = st[0].indexOf('[');
                        st[0] = st[0].substring(pos + 1);
                    }
                    ibw[i][j] = Float.parseFloat(st[j]);
                }
            }

            br.readLine();

            // Set MaxBandwidth
            lines = br.readLine();
            st = lines.trim().split("\\s+");
            final float maxBw = Float.parseFloat(st[2].substring(0, st[2].indexOf(';')));

            // Set mfs
            lines = br.readLine();
            st = lines.trim().split("\\s+");
            final float mfs = Float.parseFloat(st[2].substring(0, st[2].indexOf(';')));

            // Set H
            lines = br.readLine();
            st = lines.trim().split("\\s+");
            final float h = Float.parseFloat(st[2].substring(0, st[2].indexOf(';')));

            // Set network margin to the theoric value of ibw
            lines = br.readLine();
            st = lines.trim().split("\\s+");
            final float networkMargin = Float.parseFloat(st[2].substring(0, st[2].indexOf(';')));

            ArrayList<Integer> computingUnits = new ArrayList<>(m);
            lines = br.readLine();
            st = lines.trim().split("\\s+");
            int totalReaded = 0;
            int valueReaded;

            for (int j = 2; j < st.length - 1; j++) {
                if (j == 2) {
                    int pos = st[j].indexOf('[');
                    valueReaded = Integer.parseInt(st[j].substring(pos + 1));
                    computingUnits.add(valueReaded);
                } else {
                    valueReaded = Integer.parseInt(st[j]);
                    computingUnits.add(valueReaded);
                }
                totalReaded += valueReaded;
            }

            heuristics.setLastScheduledEndTime(new float[totalReaded]); // set offset for scheduling

            ArrayList<String> workersOrder = new ArrayList<>(m);
            lines = br.readLine();
            st = lines.trim().split("\\s+");
            for (int j = 2; j < st.length - 1; j++) {
                if (j == 2) {
                    int pos = st[j].indexOf('[');
                    workersOrder.add(st[j].substring(pos + 1));
                } else {
                    workersOrder.add(st[j]);
                }
            }

            Map<String, float[]> mapC = new HashMap<>();
            Map<String, Integer> compUnitsPerWorker = new HashMap<>();
            for (int j = 0; j < m; ++j) {
                float[] times = new float[n];
                for (int i = 0; i < n; ++i) {
                    times[i] = c[i][j];
                }
                mapC.put(workersOrder.get(j), times);
                compUnitsPerWorker.put(workersOrder.get(j), computingUnits.get(j));
            }

            boolean[] warmUpTaskIds = new boolean[n];
            if (numberWarmUpTasks == totalReaded) {
                for (int i = 1; i <= numberWarmUpTasks; i++) {
                    warmUpTaskIds[i] = true;
                }
            } else if (numberWarmUpTasks < totalReaded) {
                if (numberWarmUpTasks == m) {
                    int accumulated = 1;
                    for (int i = 0; i < numberWarmUpTasks; i++) {
                        warmUpTaskIds[accumulated] = true;
                        if (i > 0) {
                            accumulated += compUnitsPerWorker.get(workersOrder.get(i - 1));
                        }
                    }
                } else if (numberWarmUpTasks > 0) {
                    throw new RuntimeException(
                        "numberWarmUpTasks is neither equal" + "to totalComputingUnits nor to number of resources.");
                }
            }

            Map<String, Map<String, Float>> ibwMap = new HashMap<>();
            for (int j = 0; j < m; ++j) {
                Map<String, Float> ibws = new HashMap<>();
                for (int i = 0; i < m; i++) {
                    ibws.put(workersOrder.get(i), ibw[i][j]);
                }
                ibwMap.put(workersOrder.get(j), ibws);
            }

            lines = br.readLine();
            st = lines.trim().split("\\s+"); // contains isCloud
            ArrayList<Boolean> isCloud = new ArrayList<>(m);
            for (int j = 2; j < st.length - 1; j++) {
                if (j == 2) {
                    int pos = st[j].indexOf('[');
                    isCloud.add(st[j].substring(pos + 1).charAt(0) == '1');
                } else {
                    isCloud.add(st[j].charAt(0) == '1');
                }
            }

            Resources resources = new Resources(ibwMap, compUnitsPerWorker, mfs, h, m);
            resources.setSlotUnits(totalReaded); // Set the total SlotUnits for the computation
            resources.setResourceNames(workersOrder);
            for (int j = 0; j < isCloud.size(); j++) {
                if (isCloud.get(j)) {
                    resources.removeResource(workersOrder.get(j));
                    m--;
                }
            }

            // Set the resources object
            heuristics.setResources(resources);
            // Set the DAG object
            heuristics.setDag(
                new DAG(mapC, z, succ, n, isCloud, workersOrder, nSucc, totalSyncTasks, syncTaskIds, warmUpTaskIds));
        } catch (IOException | StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException
            | NullPointerException e) {
            StringWriter error = new StringWriter();
            e.printStackTrace(new PrintWriter(error));
            LOGGER.error("ERROR PARSING INPUT FILE:\n" + error);
            ErrorManager.fatal("ERROR WHILE PARSING INPUT FILE. FILE FORMAT NOT CORRECT");
        } catch (RuntimeException e) {
            StringWriter error = new StringWriter();
            e.printStackTrace(new PrintWriter(error));
            LOGGER.error("ERROR PARSING INPUT FILE:\n" + error);
            ErrorManager.fatal("ERROR WHILE PARSING INPUT FILE. FILE FORMAT NOT CORRECT");
        }
    }

}