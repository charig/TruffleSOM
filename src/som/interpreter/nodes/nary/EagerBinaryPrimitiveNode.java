package som.interpreter.nodes.nary;

import som.interpreter.TruffleCompiler;
import som.interpreter.nodes.ExpressionNode;
import som.interpreter.nodes.MessageSendNode;
import som.interpreter.nodes.MessageSendNode.GenericMessageSendNode;
import som.vmobjects.SSymbol;

import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrumentation.StandardTags.RootTag;


public class EagerBinaryPrimitiveNode extends BinaryExpressionNode {

  @Child private ExpressionNode receiver;
  @Child private ExpressionNode argument;
  @Child private BinaryExpressionNode primitive;

  private final SSymbol selector;

  public EagerBinaryPrimitiveNode(
      final SSymbol selector,
      final ExpressionNode receiver,
      final ExpressionNode argument,
      final BinaryExpressionNode primitive) {
    super(null);
    this.receiver  = receiver;
    this.argument  = argument;
    this.primitive = primitive;
    this.selector = selector;
  }

  @Override
  public ExpressionNode getReceiver(){
    return receiver;
  }

  @Override
  public Object executeGeneric(final VirtualFrame frame) {
    Object rcvr = receiver.executeGeneric(frame);
    Object arg  = argument.executeGeneric(frame);
    return executeEvaluated(frame, rcvr, arg);
  }

  @Override
  public Object executeEvaluated(final VirtualFrame frame,
    final Object receiver, final Object argument) {
    try {
      return primitive.executeEvaluated(frame, receiver, argument);
    } catch (UnsupportedSpecializationException e) {
      TruffleCompiler.transferToInterpreterAndInvalidate("Eager Primitive with unsupported specialization.");
      return makeGenericSend().doPreEvaluated(frame,
          new Object[] {receiver, argument});
    }
  }

  private GenericMessageSendNode makeGenericSend() {
    GenericMessageSendNode node = MessageSendNode.createGeneric(selector,
        new ExpressionNode[] {receiver, argument}, getSourceSection());
    return replace(node);
  }

  @Override
  public ExpressionNode getArgument() {
    return argument;
  }
  
  protected SSymbol getSelector(){
    return selector;
  }
  
  @Override
  protected boolean isTaggedWith(final Class<?> tag) {
    if (tag == RootTag.class) {
      return true;
    } else {
      return super.isTaggedWith(tag);
    }
  }
}
