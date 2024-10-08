/*
 * Copyright (c) 2020 Charlie Condorcet engineer student.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cl.ucn.disc.pdis.lab;

import cl.ucn.disc.pdis.lab.services.EngineImpl;
import cl.ucn.disc.pdis.lab.zeroice.model.Engine;
import com.zeroc.Ice.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementacion del server de articulos.
 *
 * @author Diego Urrutia-Astorga.
 */
public final class SystemServer {

    /**
     * The logger.
     */
    private static final Logger log = LoggerFactory.getLogger(SystemServer.class);

    /**
     * @param args to use.
     */
    public static void main(final String[] args) {

        log.debug("Starting the Server ..");

        // The communicator
        try (Communicator communicator = Util.initialize(getInitializationData(args))) {

            // The adapter
            final ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("EngineAdapter", "default -p 10000 -z");

            // The interface + implementation
            final Engine engine = new EngineImpl();

            // The unique identity
            final Identity identity = Util.stringToIdentity(Engine.class.getName());
            log.debug("Using name <{}> and category <{}> as identity.", identity.name, identity.category);

            // Register the API in the framework
            adapter.add(engine, identity);
            adapter.activate();

            log.debug("Waiting for connections ..");

            // .. waiting.
            communicator.waitForShutdown();
        }

        log.debug("Server ended!");

    }

    /**
     * @param args to use as source.
     * @return the {@link InitializationData}.
     */
    private static InitializationData getInitializationData(String[] args) {

        // Properties
        final Properties properties = Util.createProperties(args);
        properties.setProperty("Ice.Package.model", "cl.ucn.disc.pdis.lab.zeroice");

        // https://doc.zeroc.com/ice/latest/property-reference/ice-trace
        // properties.setProperty("Ice.Trace.Admin.Properties", "1");
        // properties.setProperty("Ice.Trace.Locator", "2");
        // properties.setProperty("Ice.Trace.Network", "3");
        // properties.setProperty("Ice.Trace.Protocol", "1");
        // properties.setProperty("Ice.Trace.Slicing", "1");
        // properties.setProperty("Ice.Trace.ThreadPool", "1");
        // properties.setProperty("Ice.Compression.Level", "9");
        // properties.setProperty("Ice.Plugin.Slf4jLogger.java", "cl.ucn.disc.pdis.lab.zeroice.Slf4jLoggerPluginFactory");

        InitializationData initializationData = new InitializationData();
        initializationData.properties = properties;

        return initializationData;
    }

}
