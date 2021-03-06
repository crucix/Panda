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

package org.panda_lang.panda.bootstrap;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.PandaFactory;
import org.panda_lang.panda.elements.PandaElements;
import org.panda_lang.panda.framework.design.interpreter.lexer.Syntax;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.registry.ParserPipelineRegistry;
import org.panda_lang.panda.language.structure.prototype.registry.ClassPrototypeModel;

import java.util.ArrayList;
import java.util.Collection;

public class PandaBootstrap {

    protected Syntax syntax;
    protected ParserPipelineRegistry registry;
    protected Collection<Collection<Class<? extends ClassPrototypeModel>>> modelsCollection = new ArrayList<>();
    protected GenerationInitializer generationInitializer;

    public PandaBootstrap syntax(Syntax syntax) {
        this.syntax = syntax;
        return this;
    }

    public PandaBootstrap mapModels(Collection<Class<? extends ClassPrototypeModel>> models) {
        this.modelsCollection.add(models);
        return this;
    }

    public PandaBootstrap addGenerationHandler(GenerationInitializer initializer) {
        this.generationInitializer = initializer;
        return this;
    }

    public Panda get() {
        PandaFactory factory = new PandaFactory();
        Panda panda = factory.createPanda();

        PandaElements elements = panda.getPandaElements();
        elements.setSyntax(syntax);
        elements.setMappings(modelsCollection);

        return panda;
    }

}