/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.launcher;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.PandaFactory;
import org.panda_lang.panda.PandaLoader;
import org.panda_lang.panda.framework.design.architecture.Application;
import org.panda_lang.panda.utilities.commons.io.FileUtils;

import java.io.File;
import java.util.Collection;

public class PandaLauncher {

    public static void main(String[] args) {
        PandaFactory pandaFactory = new PandaFactory();
        Panda panda = pandaFactory.createPanda();

        PandaLoader pandaLoader = panda.getPandaLoader();
        Collection<File> files = FileUtils.findFilesByExtension(System.getProperty("user.dir"), ".panda");

        for (File file : files) {
            Application application = pandaLoader.loadFiles(file);
            application.setApplicationArguments(args);
            application.launch();
        }
    }

}
