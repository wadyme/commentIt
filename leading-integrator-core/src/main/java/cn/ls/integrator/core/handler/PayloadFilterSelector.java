package cn.ls.integrator.core.handler;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.integration.handler.ExpressionEvaluatingMessageProcessor;

public class PayloadFilterSelector extends AbstractPayloadFilterSelector{
	
	private static final ExpressionParser expressionParser = new SpelExpressionParser(new SpelParserConfiguration(true, true));

	public PayloadFilterSelector(String expressionString) {
		super(new ExpressionEvaluatingMessageProcessor<Boolean>(expressionParser.parseExpression(expressionString), Boolean.class));
	}

	public PayloadFilterSelector(Expression expression) {
		super(new ExpressionEvaluatingMessageProcessor<Boolean>(expression, Boolean.class));
	}

}
