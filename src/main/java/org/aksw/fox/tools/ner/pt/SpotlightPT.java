package org.aksw.fox.tools.ner.it;

import java.io.IOException;
import java.util.Locale;

import org.aksw.fox.tools.ner.common.SpotlightCommon;
import org.aksw.fox.utils.FoxConst;

public class SpotlightPT extends SpotlightCommon {
  public SpotlightPT() {
    super(Locale.ITALIAN);
  }

  public static void main(final String[] a) throws IOException {
    LOG.info(new SpotlightPT().retrieve(FoxConst.NER_PT_EXAMPLE_1));
  }
}
