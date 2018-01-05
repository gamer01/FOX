package org.aksw.fox.tools.ner.en;

import org.aksw.fox.tools.ner.common.OpenNLPCommon;
import org.aksw.fox.utils.FoxConst;

public class OpenNLPPT extends OpenNLPCommon {

    static final String[] modelPath = {"pt-ner-sigarra-iterations_170-cutoff_4.bin"};

    public OpenNLPEN() {
        super(modelPath);
    }

    public static void main(final String[] a) {
        LOG.info(new OpenNLPPT().retrieve(FoxConst.NER_PT_EXAMPLE_1));
    }
}
