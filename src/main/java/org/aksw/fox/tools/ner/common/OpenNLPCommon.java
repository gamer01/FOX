package org.aksw.fox.tools.ner.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.aksw.fox.data.Entity;
import org.aksw.fox.data.EntityClassMap;
import org.aksw.fox.tools.ner.AbstractNER;
import org.aksw.fox.utils.FoxTextUtil;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

public abstract class OpenNLPCommon extends AbstractNER {

    protected String[] modelPath;
    protected TokenNameFinderModel[] tokenNameFinderModels;

    public OpenNLPCommon(final String[] modelPath) {

        entityClasses.put("location", EntityClassMap.L);
        entityClasses.put("organization", EntityClassMap.O);
        entityClasses.put("person", EntityClassMap.P);

        this.modelPath = modelPath;

        tokenNameFinderModels = new TokenNameFinderModel[modelPath.length];
        final InputStream[] modelIn = new InputStream[modelPath.length];

        for (int i = 0; i < tokenNameFinderModels.length; i++) {
            try {
                modelIn[i] = new FileInputStream(modelPath[i]);
                if (modelIn[i] != null) {
                    tokenNameFinderModels[i] = new TokenNameFinderModel(modelIn[i]);
                }

            } catch (final IOException e) {
                LOG.error("\n", e);
            } finally {
                try {
                    if (modelIn[i] != null) {
                        modelIn[i].close();
                    }
                } catch (final IOException e) {
                    LOG.error("\n", e);
                }
            }
        }
    }

    // TODO: do parallel for each model
    @Override
    public List<Entity> retrieve(final String input) {
        LOG.info("retrieve ...");

        final List<Entity> list = new ArrayList<>();
        final String[] sentences = FoxTextUtil.getSentences(input);
        LOG.debug("sentences: " + sentences.length);

        for (TokenNameFinderModel tokenNameFinderModel : tokenNameFinderModels) {
            if (tokenNameFinderModel != null) {
                final NameFinderME nameFinder = new NameFinderME(tokenNameFinderModel);
                for (final String sentence : sentences) {
                    final String[] tokens = FoxTextUtil.getSentenceToken(sentence);
                    LOG.debug("tokens: " + tokens.length);
                    if ((tokens.length > 0) && tokens[tokens.length - 1].trim().isEmpty()) {
                        tokens[tokens.length - 1] = ".";
                    }

                    final Span[] nameSpans = nameFinder.find(tokens);
                    nameFinder.probs(nameSpans);
                    for (final Span span : nameSpans) {
                        StringBuilder word = new StringBuilder();
                        for (int j = 0; j < (span.getEnd() - span.getStart()); j++) {
                            word.append(tokens[span.getStart() + j]).append(" ");
                        }
                        word = new StringBuilder(word.toString().trim());

                        final float p = Entity.DEFAULT_RELEVANCE;
                        // if ((FoxCfg.get("openNLPDefaultRelevance") != null)
                        // && !Boolean.valueOf(FoxCfg.get("openNLPDefaultRelevance"))) {
                        // p = Double.valueOf(probs[ii]).floatValue();
                        // }
                        final String cl = mapTypeToSupportedType(span.getType());
                        if (!cl.equals(EntityClassMap.getNullCategory())) {
                            list.add(getEntity(word.toString(), cl, p, getToolName()));
                        }
                    }
                }
                nameFinder.clearAdaptiveData();
            }
        }

        // TRACE
        if (LOG.isTraceEnabled()) {
            LOG.trace(list);
        }

        return list;
    }
}
