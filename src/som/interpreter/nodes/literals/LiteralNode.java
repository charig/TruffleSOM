/**
 * Copyright (c) 2013 Stefan Marr, stefan.marr@vub.ac.be
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package som.interpreter.nodes.literals;

import som.compiler.MethodGenerationContext;
import som.compiler.Variable.Local;
import som.interpreter.nodes.ExpressionNode;
import som.interpreter.nodes.PreevaluatedExpression;
import tools.highlight.Tags.LiteralTag;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.source.SourceSection;

@NodeInfo(cost = NodeCost.NONE)
public abstract class LiteralNode extends ExpressionNode
    implements PreevaluatedExpression {

  public LiteralNode(final SourceSection source) {
    super(source);
  }
  
  public Object[] evaluateArguments(final VirtualFrame frame){
    System.out.print("Error in evaluateArguments of literalNODE!!!!!!!!!!!!!!!!!!!!");
    return null;
  }

  @Override
  public final Object doPreEvaluated(final VirtualFrame frame,
      final Object[] arguments) {
    return executeGeneric(frame);
  }

  public ExpressionNode inline(final MethodGenerationContext mgenc,
      final Local... blockArguments) {
    return this;
  }
  
  @Override
  protected boolean isTaggedWith(final Class<?> tag) {
    if (tag == LiteralTag.class) {
      return true;
    } else {
      return super.isTaggedWith(tag);
    }
  }
}
