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

package org.panda_lang.panda;

import org.panda_lang.panda.design.architecture.PandaApplication;
import org.panda_lang.panda.design.architecture.PandaEnvironment;
import org.panda_lang.panda.design.interpreter.PandaInterpreter;
import org.panda_lang.panda.framework.design.interpreter.source.SourceProvider;
import org.panda_lang.panda.framework.design.interpreter.source.SourceSet;
import org.panda_lang.panda.framework.language.PandaFrameworkException;
import org.panda_lang.panda.framework.language.interpreter.source.providers.DirectorySourceProvider;
import org.panda_lang.panda.framework.language.interpreter.source.providers.FileSourceProvider;
import org.panda_lang.panda.utilities.commons.io.FileUtils;

import java.io.File;

public class PandaLoader {

    private final Panda panda;

    public PandaLoader(Panda panda) {
        this.panda = panda;
    }

    public PandaApplication loadFiles(String... paths) {
        return load(new FileSourceProvider(FileUtils.toFiles(paths)));
    }

    public PandaApplication loadFiles(File... files) {
        return load(new FileSourceProvider(files));
    }

    public PandaApplication loadDirectory(File directory) {
        return load(new DirectorySourceProvider(directory));
    }

    public PandaApplication load(SourceProvider provider) {
        SourceSet sourceSet = provider.toSourceSet();

        if (sourceSet.isEmpty()) {
            throw new PandaFrameworkException("Sources are not provided");
        }

        PandaEnvironment environment = new PandaEnvironment(panda);
        environment.initialize();

        PandaInterpreter interpreter = environment.getInterpreter();
        PandaApplication application = interpreter.interpret(sourceSet);

        if (application == null) {
            throw new RuntimeException("Application does not exist");
        }

        return application;
    }

}
