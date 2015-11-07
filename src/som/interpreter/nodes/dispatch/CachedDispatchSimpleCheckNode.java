package som.interpreter.nodes.dispatch;

import som.interpreter.SArguments;
import som.interpreter.nodes.dispatch.AbstractDispatchNode.AbstractCachedDispatchNode;
import som.vm.constants.ExecutionLevel;
import som.vmobjects.SMateEnvironment;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.frame.VirtualFrame;


public final class CachedDispatchSimpleCheckNode extends AbstractCachedDispatchNode {

  private final Class<?> expectedClass;

  public CachedDispatchSimpleCheckNode(final Class<?> rcvrClass,
      final CallTarget callTarget, final AbstractDispatchNode nextInCache) {
    super(callTarget, nextInCache);
    this.expectedClass = rcvrClass;
  }

  @Override
  public Object executeDispatch(final VirtualFrame frame, final SMateEnvironment environment, final ExecutionLevel exLevel,
      final Object[] arguments) {
    Object rcvr = arguments[0];
    if (rcvr.getClass() == expectedClass) {
      return cachedMethod.call(frame, SArguments.createSArguments(environment, exLevel, arguments));
    } else {
      return nextInCache.executeDispatch(frame, environment, exLevel, arguments);
    }
  }

  public static final class CachedDispatchTrueCheckNode
      extends AbstractCachedDispatchNode {
    public CachedDispatchTrueCheckNode(final CallTarget callTarget,
        final AbstractDispatchNode nextInCache) {
      super(callTarget, nextInCache);
    }

    @Override
    public Object executeDispatch(final VirtualFrame frame, final SMateEnvironment environment, final ExecutionLevel exLevel,
        final Object[] arguments) {
      if (arguments[0] == Boolean.TRUE) {
        return cachedMethod.call(frame, SArguments.createSArguments(environment, exLevel, arguments));
      } else {
        return nextInCache.executeDispatch(frame, environment, exLevel, arguments);
      }
    }
  }

  public static final class CachedDispatchFalseCheckNode
      extends AbstractCachedDispatchNode {
    public CachedDispatchFalseCheckNode(final CallTarget callTarget,
        final AbstractDispatchNode nextInCache) {
      super(callTarget, nextInCache);
    }

    @Override
    public Object executeDispatch(final VirtualFrame frame, final SMateEnvironment environment, final ExecutionLevel exLevel,
        final Object[] arguments) {
      if (arguments[0] == Boolean.FALSE) {
        return cachedMethod.call(frame, SArguments.createSArguments(environment, exLevel, arguments));
      } else {
        return nextInCache.executeDispatch(frame, environment, exLevel, arguments);
      }
    }
  }
}
