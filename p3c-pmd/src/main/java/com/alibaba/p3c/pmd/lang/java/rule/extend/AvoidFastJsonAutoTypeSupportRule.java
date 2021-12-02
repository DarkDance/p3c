package com.alibaba.p3c.pmd.lang.java.rule.extend;

import com.alibaba.p3c.pmd.I18nResources;
import com.alibaba.p3c.pmd.lang.java.rule.AbstractAliRule;
import com.alibaba.p3c.pmd.lang.java.util.ViolationUtils;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTBooleanLiteral;
import net.sourceforge.pmd.lang.java.ast.ASTImportDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryExpression;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryPrefix;
import net.sourceforge.pmd.lang.java.ast.ASTPrimarySuffix;
import org.jaxen.JaxenException;

import java.util.List;

/**
 * @author wiiyaya
 * @date 2021/12/2.
 */
public class AvoidFastJsonAutoTypeSupportRule extends AbstractAliRule {
	private final String PARSER_CONFIG_IMPORT_NAME = "com.alibaba.fastjson.parser.ParserConfig";

	private final String SET_AUTO_TYPE_SUPPORT_NAME = "setAutoTypeSupport";

	private boolean hasImport = false;

	@Override
	public Object visit(ASTImportDeclaration node, Object data) {
		if (PARSER_CONFIG_IMPORT_NAME.equals(node.getImportedName())) {
			hasImport = true;
		}
		return super.visit(node, data);
	}

	@Override
	public Object visit(ASTPrimaryExpression node, Object data) {
		if (hasImport) {
			int size = node.jjtGetNumChildren();
			for (int i = 0; i < size; i++) {
				Node child = node.jjtGetChild(i);
				String imageName = null;
				if (child instanceof ASTPrimaryPrefix) {
					ASTPrimaryPrefix prefix = (ASTPrimaryPrefix) child;
					imageName = prefix.jjtGetChild(0).getImage();
				} else if (child instanceof ASTPrimarySuffix) {
					ASTPrimarySuffix suffix = (ASTPrimarySuffix) child;
					imageName = suffix.getImage();
				}
				if (imageName == null) {
					continue;
				} else if (imageName.endsWith(SET_AUTO_TYPE_SUPPORT_NAME)) {
					ASTPrimarySuffix argumentSuffix = (ASTPrimarySuffix) node.jjtGetChild(i + 1);
					try {
						List booleanArgs = argumentSuffix.findChildNodesWithXPath("//PrimaryPrefix/Literal/BooleanLiteral");
						if (booleanArgs.size() == 1) {
							ASTBooleanLiteral booleanLiteral = (ASTBooleanLiteral) booleanArgs.get(0);
							if (booleanLiteral.isTrue()) {
								ViolationUtils.addViolationWithPrecisePosition(this, argumentSuffix, data, I18nResources.getMessage("java.extend.AvoidFastJsonAutoTypeSupportRule.rule.msg"));

							}
						}
					} catch (JaxenException e) {
						e.printStackTrace();
					} finally {
						break;
					}
				}
			}
		}
		return super.visit(node, data);
	}
}
