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

    public static void main(String[] args) {
        Common.setAppName("com.github.vatbub.safeAPIKeyStore.server");
        Options cliOptions = new Options();

        Option portOption = new Option("p", "port", true, "The port to run the server on");
        portOption.setRequired(false);
        portOption.setType(Integer.class);

        Option apiKeyFileOption = new Option("apiKeyFile", true, "The path to the file that contains all api keys to be served");
        apiKeyFileOption.setRequired(true);
        apiKeyFileOption.setType(String.class);

        cliOptions.addOption(portOption);
        cliOptions.addOption(apiKeyFileOption);

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine commandLine = parser.parse(cliOptions, args);
            if (commandLine.hasOption(portOption.getOpt()) || !commandLine.hasOption(apiKeyFileOption.getOpt())) {
                printHelpMessage(cliOptions);
            }
            int port = Integer.parseInt(commandLine.getOptionValue(portOption.getOpt(), "1650"));
            String apiKeyFile = commandLine.getOptionValue(apiKeyFileOption.getOpt());

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (server != null) {
                    server.stop();
                }
            }));

            server = new Server(port, apiKeyFile);
        } catch (ParseException e) {
            printHelpMessage(cliOptions);
        } catch (IOException e) {
            FOKLogger.log(Main.class.getName(), Level.SEVERE, "Could not launch the server", e);
        }
    }

    public static void printHelpMessage(Options cliOptions) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(Common.getPathAndNameOfCurrentJar(), cliOptions);
    }
}
