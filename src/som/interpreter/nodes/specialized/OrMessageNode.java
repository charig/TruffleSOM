package som.interpreter.nodes.specialized;

import som.interpreter.SArguments;
import som.interpreter.nodes.nary.BinaryExpressionNode;
import som.vm.constants.ExecutionLevel;
import som.vmobjects.SBlock;
import som.vmobjects.SInvokable;
import tools.dym.Tags.ControlFlowCondition;
import tools.dym.Tags.OpComparison;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.DirectCallNode;
import com.oracle.truffle.api.object.DynamicObject;
import com.oracle.truffle.api.source.SourceSection;


public abstract class OrMessageNode extends BinaryExpressionNode {
  private final DynamicObject blockMethod;
  @Child private DirectCallNode blockValueSend;

  public OrMessageNode(final SBlock arg, final SourceSection source, ExecutionLevel level) {
    super(source);
    blockMethod = arg.getMethod();
    blockValueSend = Truffle.getRuntime().createDirectCallNode(
        SInvokable.getCallTarget(blockMethod, level));
  }

  public OrMessageNode(final OrMessageNode copy) {
    super(copy.getSourceSection());
    blockMethod = copy.blockMethod;
    blockValueSend = copy.blockValueSend;
  }

  protected final boolean isSameBlock(final SBlock argument) {
    return argument.getMethod() == blockMethod;
  }

  @Specialization(guards = "isSameBlock(argument)")
  public final boolean doOr(final VirtualFrame frame, final boolean receiver,
      final SBlock argument) {
    if (receiver) {
      return true;
    } else {
      return (boolean) blockValueSend.call(frame, new Object[] {SArguments.getEnvironment(frame), SArguments.getExecutionLevel(frame), argument});
    }
  }

  public abstract static class OrBoolMessageNode extends BinaryExpressionNode {
    public OrBoolMessageNode(final SourceSection source) { super(source); }
    @Specialization
    public final boolean doOr(final VirtualFrame frame, final boolean receiver,
        final boolean argument) {
      return receiver || argument;
    }
  }
  
  @Override
  protected boolean isTaggedWith(final Class<?> tag) {
    if (tag == ControlFlowCondition.class) {
      return true;
    } else if (tag == OpComparison.class) {
      return true;
    } else {
      return super.isTaggedWith(tag);
    }
  }
}
