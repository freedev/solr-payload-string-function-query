package it.damore.solr.payload;

import org.apache.lucene.index.Term;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.queries.function.valuesource.LiteralValueSource;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.util.BytesRefBuilder;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.parser.ParseException;
import org.apache.solr.schema.FieldType;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.StrField;
import org.apache.solr.schema.TextField;
import org.apache.solr.search.FunctionQParser;
import org.apache.solr.search.SyntaxError;
import org.apache.solr.search.ValueSourceParser;

public class PayloadStringValueSourceParser extends ValueSourceParser {
    public void init(NamedList namedList) {
    }

    private static TInfo parseTerm(FunctionQParser fp) throws SyntaxError {
        TInfo tinfo = new TInfo();

        tinfo.indexedField = tinfo.field = fp.parseArg();
        tinfo.val = fp.parseArg();
        tinfo.indexedBytes = new BytesRefBuilder();

        FieldType ft = fp.getReq().getSchema().getFieldTypeNoEx(tinfo.field);
        if (ft == null) ft = new StrField();

        if (ft instanceof TextField) {
            // need to do analysis on the term
            String indexedVal = tinfo.val;
            Query q = ft.getFieldQuery(fp, fp.getReq().getSchema().getFieldOrNull(tinfo.field), tinfo.val);
            if (q instanceof TermQuery) {
                Term term = ((TermQuery)q).getTerm();
                tinfo.indexedField = term.field();
                indexedVal = term.text();
            }
            tinfo.indexedBytes.copyChars(indexedVal);
        } else {
            ft.readableToIndexed(tinfo.val, tinfo.indexedBytes);
        }

        return tinfo;
    }

    @Override
    public ValueSource parse(FunctionQParser fp) throws SyntaxError {
        // spayload(field,value[,default])

        TInfo tinfo = parseTerm(fp); // would have made this parser a new separate class and registered it, but this handy method is private :/

        ValueSource defaultValueSource;
        if (fp.hasMoreArguments()) {
            defaultValueSource = fp.parseValueSource();
        } else {
            defaultValueSource = new LiteralValueSource("");
        }

        IndexSchema schema = fp.getReq().getCore().getLatestSchema();
        final FieldType fieldType = schema.getFieldType(tinfo.field);

        if (fieldType.getTypeName().equals("delimited_payloads_string")) {
            return new StringPayloadValueSource(
                    tinfo.field,
                    tinfo.val,
                    tinfo.indexedField,
                    tinfo.indexedBytes.get(),
                    defaultValueSource);
        }
        return new LiteralValueSource("");
   }

    private static class TInfo {
        String field;
        String val;
        String indexedField;
        BytesRefBuilder indexedBytes;
    }

}
