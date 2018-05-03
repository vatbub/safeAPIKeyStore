package com.github.vatbub.safeAPIKeyStore.server;

/*-
 * #%L
 * com.github.vatbub.server
 * %%
 * Copyright (C) 2016 - 2017 Frederik Kammel
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import com.github.vatbub.common.core.Common;
import com.github.vatbub.common.core.logging.FOKLogger;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.logging.Level;

public class Main {
    private static Server server;
    private static Options optionsCache;
    private static Option portOptionCache;
    private static Option apiKeyFileOptionCache;
    private static Option autoShutdownOptionCache;
    private static Option autoShutdownDurationOptionCache;
    private Main() {
        throw new IllegalStateException("Class may not be instantiated");
    }

    public static void main(String[] args) {
        Common.getInstance().setAppName("com.github.vatbub.safeAPIKeyStore.server");

        Options cliOptions = createCommandlineOptions();

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine commandLine = parser.parse(cliOptions, args);

            int port = Integer.parseInt(commandLine.getOptionValue(createPortOption().getOpt(), "1650"));
            String apiKeyFile = commandLine.getOptionValue(createApiKeyFileOption().getOpt());

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (server != null) {
                    server.stop();
                }
            }));

            server = new Server(port, apiKeyFile);

            if (commandLine.hasOption(createAutoShutdownOption().getOpt()))
                server.setAutoShutdownEnabled(true);
            if (commandLine.hasOption(createAutoShutdownDurationOption().getOpt()))
                server.setMinutesToIdleBeforeAutoShutdown(Long.parseLong(commandLine.getOptionValue(createAutoShutdownDurationOption().getOpt())));
            else
                server.setMinutesToIdleBeforeAutoShutdown(5);
        } catch (ParseException e) {
            printHelpMessage(cliOptions);
        } catch (IOException e) {
            FOKLogger.log(Main.class.getName(), Level.SEVERE, "Could not launch the server", e);
        }
    }

    public static Server getServer() {
        return server;
    }

    public static Options createCommandlineOptions() {
        if (optionsCache == null) {
            optionsCache = new Options();

            optionsCache.addOption(createPortOption());
            optionsCache.addOption(createApiKeyFileOption());
            optionsCache.addOption(createAutoShutdownOption());
            optionsCache.addOption(createAutoShutdownDurationOption());
        }
        return optionsCache;
    }

    public static Option createPortOption() {
        if (portOptionCache == null) {
            portOptionCache = new Option("p", "port", true, "The port to run the server on");
            portOptionCache.setRequired(false);
            portOptionCache.setType(Integer.class);
        }
        return portOptionCache;
    }

    public static Option createApiKeyFileOption() {
        if (apiKeyFileOptionCache == null) {
            apiKeyFileOptionCache = new Option("apiKeyFile", true, "The path to the file that contains all api keys to be served (required)");
            apiKeyFileOptionCache.setRequired(true);
            apiKeyFileOptionCache.setType(String.class);
        }
        return apiKeyFileOptionCache;
    }

    public static Option createAutoShutdownOption() {
        if (autoShutdownOptionCache == null) {
            autoShutdownOptionCache = new Option("autoShutdown", false, "If specified, the server will automatically shut down when idle for 5 minutes. Use --autoShutdownDuration to change that value.");
            autoShutdownOptionCache.setRequired(false);
        }
        return autoShutdownOptionCache;
    }

    public static Option createAutoShutdownDurationOption() {
        if (autoShutdownDurationOptionCache == null) {
            autoShutdownDurationOptionCache = new Option("autoShutdownDuration", true, "The server will automatically shut down when being idle for the specified amount of minutes. This option has no effect if --autoShutdown is not specified.");
            autoShutdownDurationOptionCache.setRequired(false);
            autoShutdownDurationOptionCache.setType(Double.class);
        }
        return autoShutdownDurationOptionCache;
    }

    public static void printHelpMessage(Options cliOptions) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(Common.getInstance().getPathAndNameOfCurrentJar(), cliOptions);
    }
}
