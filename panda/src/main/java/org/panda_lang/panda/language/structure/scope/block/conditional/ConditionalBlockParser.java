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

package org.panda_lang.panda.language.structure.scope.block.conditional;

import org.panda_lang.panda.framework.design.architecture.dynamic.Block;
import org.panda_lang.panda.design.interpreter.parser.pipeline.DefaultPipelines;
import org.panda_lang.panda.design.interpreter.parser.pipeline.registry.ParserRegistration;
import org.panda_lang.panda.design.interpreter.parser.util.Components;
import org.panda_lang.panda.design.interpreter.token.AbyssPatternAssistant;
import org.panda_lang.panda.design.interpreter.token.AbyssPatternBuilder;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.token.TokenUtils;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.interpreter.token.distributor.SourceStream;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.abyss.redactor.AbyssRedactor;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.language.structure.general.expression.ExpressionParser;
import org.panda_lang.panda.language.structure.scope.block.conditional.blocks.ConditionalBlock;
import org.panda_lang.panda.language.structure.scope.block.conditional.blocks.ElseBlock;
import org.panda_lang.panda.language.syntax.PandaSyntax;

@ParserRegistration(target = DefaultPipelines.BLOCK, parserClass = ConditionalBlockParser.class, handlerClass = ConditionalBlockParserHandler.class)
public class ConditionalBlockParser implements UnifiedParser {

    protected static final AbyssPattern PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "+* ( +* )")
            .build();

    @Override
    public void parse(ParserInfo info) {
        SourceStream stream = info.getComponent(Components.SOURCE_STREAM);
        ParserInfo parentInfo = info.getComponent(Components.PARENT_INFO);

        if (stream.getUnreadLength() == 1) {
            ElseBlock elseBlock = new ElseBlock();
            Block previousBlock = parentInfo.getComponent("previous-block");

            if (!(previousBlock instanceof ConditionalBlock)) {
                throw new PandaParserException("The Else-block without associated If-block at line " + TokenUtils.getLine(stream.toTokenizedSource()));
            }

            ConditionalBlock conditionalBlock = (ConditionalBlock) previousBlock;
            conditionalBlock.setElseBlock(elseBlock);

            info.setComponent("block", elseBlock);
            info.setComponent("block-unlisted", true);
            return;
        }

        AbyssRedactor redactor = AbyssPatternAssistant.traditionalMapping(PATTERN, info, "condition-type", "condition-expression");
        TokenizedSource conditionType = redactor.get("condition-type");
        TokenizedSource conditionExpression = redactor.get("condition-expression");

        ExpressionParser expressionParser = new ExpressionParser();
        Expression expression = expressionParser.parse(info, conditionExpression);

        ConditionalBlock conditionalBlock = new ConditionalBlock(expression);
        info.setComponent("block", conditionalBlock);

        switch (conditionType.asString()) {
            case "if":
                break;
            case "else if":
                Block previousBlock = parentInfo.getComponent("previous-block");

                if (!(previousBlock instanceof ConditionalBlock)) {
                    throw new PandaParserException("The If-Else-block without associated If-block at line " + TokenUtils.getLine(stream.toTokenizedSource()));
                }

                ConditionalBlock previousConditionalBlock = (ConditionalBlock) previousBlock;
                conditionalBlock.setElseBlock(previousConditionalBlock);
                info.setComponent("block-unlisted", true);
                break;
            default:
                throw new PandaParserException("Unrecognized condition type at line " + TokenUtils.getLine(conditionType));
        }
    }

}
