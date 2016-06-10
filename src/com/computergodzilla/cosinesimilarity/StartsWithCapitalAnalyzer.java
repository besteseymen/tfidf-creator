package com.computergodzilla.cosinesimilarity;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents;
import org.apache.lucene.analysis.standard.StandardTokenizer;

public class StartsWithCapitalAnalyzer extends Analyzer {
    @Override
    protected TokenStreamComponents createComponents(String field, Reader reader) {
        Tokenizer tokenizer = new StandardTokenizer(reader);
        TokenStream filter = new StartsWithCapitalTokenFilter(tokenizer);

        // chain any other filters you want in here, like so:
        //filter = new LowerCaseFilter(filter);

        return new TokenStreamComponents(tokenizer, filter);
    }
}
