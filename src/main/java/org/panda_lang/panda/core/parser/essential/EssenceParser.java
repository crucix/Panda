package org.panda_lang.panda.core.parser.essential;

import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.Parser;
import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.NamedExecutable;

public class EssenceParser implements Parser {

    @Override
    public Essence parse(Atom atom) {
        String source = atom.getSourceCode();

        return null;
    }

}