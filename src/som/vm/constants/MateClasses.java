package som.vm.constants;

import som.vm.MateUniverse;
import som.vm.Universe;
import som.vmobjects.SClass;

public class MateClasses extends Classes {
  public static final SClass environmentMO;
  public static final SClass operationalSemanticsMO;
  public static final SClass messageMO;

  static {
    // Allocate the Metaclass classes
    environmentMO = MateUniverse.newSystemClass();
    operationalSemanticsMO = MateUniverse.newSystemClass();
    messageMO = MateUniverse.newSystemClass();
    //environment = Universe.newSystemClass();
  }

}
