/*
 * SonarQube Python Plugin
 * Copyright (C) 2011-2020 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.python.checks;

import org.sonar.check.Rule;
import org.sonar.plugins.python.api.PythonSubscriptionCheck;
import org.sonar.plugins.python.api.tree.AssignmentStatement;
import org.sonar.plugins.python.api.tree.Token;
import org.sonar.plugins.python.api.tree.Tree;
import org.sonar.plugins.python.api.tree.UnaryExpression;

@Rule(key = "S2757")
public class WrongAssignmentOperatorCheck extends PythonSubscriptionCheck {

    private static final String MESSAGE = "Was %s= meant instead?";

    @Override
    public void initialize(Context context) {
        System.out.println("\n====>进入了initialize...");
        context.registerSyntaxNodeConsumer(Tree.Kind.ASSIGNMENT_STMT, ctx -> {
            System.out.println("\n====>进入了initialize方法体");
            AssignmentStatement assignment = (AssignmentStatement) ctx.syntaxNode();
            System.out.println("assignment.assignedValue()=" + assignment.assignedValue());
            if (assignment.assignedValue().is(Tree.Kind.UNARY_PLUS) || assignment.assignedValue().is(Tree.Kind.UNARY_MINUS)) {
                System.out.println("assignment.equalTokens().size() is: " + assignment.equalTokens().size());
                if (assignment.equalTokens().size() > 1) {
                    return;
                }
                UnaryExpression unaryExpression = (UnaryExpression) assignment.assignedValue();
                Token equalToken = assignment.equalTokens().get(0);
                System.out.println("equalToken:value=" + equalToken.value() + ",line=" + equalToken.line() + ",type=" + equalToken.type());
                Token unaryOperator = unaryExpression.operator();
                Token variableLastToken = assignment.lhsExpressions().get(0).lastToken();
                if (noSpacingBetween(variableLastToken, equalToken)
                        && noSpacingBetween(unaryOperator, unaryExpression.expression().firstToken())) {
                    return;
                }
                if (noSpacingBetween(equalToken, unaryOperator)) {
                    ctx.addIssue(equalToken, unaryOperator, String.format(MESSAGE, unaryOperator.value()));
                }
            }
            System.out.println("\n====>退出了initialize方法体");
        });
        System.out.println("\n====>结束了initialize...");
    }

    private static boolean noSpacingBetween(Token first, Token second) {
        return first.line() == second.line()
                && first.column() + first.value().length() == second.column();
    }
}
