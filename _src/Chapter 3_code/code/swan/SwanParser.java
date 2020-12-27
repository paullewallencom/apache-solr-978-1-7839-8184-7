package com.plugin.swan;

import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanOrQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.index.Term;
import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.Parboiled;
import org.parboiled.parserunners.RecoveringParseRunner;
import org.parboiled.support.ParsingResult;

public class SwanParser extends BaseParser<SpanQuery> {

  public Rule Query() {
		return Sequence(OrExpression(),EOI);
  }
  
  public Rule OrExpression() {
    return Sequence(
      SameExpression(),
      ZeroOrMore(
        Sequence(
          OR(),
          SameExpression(),
            push(new SpanOrQuery(pop(1), pop()))
        )
      )
    );
  }
  
  public Rule SameExpression() {
    return Sequence(
      WithExpression(),
      ZeroOrMore(
        Sequence(
          SAME(),
          WithExpression(), push(SwanQueries.SAME(pop(1), pop()))
        )
      )
    );
  }
  
  public Rule WithExpression() {
    return Sequence(
      AdjNearExpression(),
      ZeroOrMore(
        Sequence(
          WITH(),
          AdjNearExpression(), push(SwanQueries.WITH(pop(1), pop()))
        )    
      )
    );
  }
  
  public Rule AdjNearExpression() {
    return Sequence(
      Term(),
      ZeroOrMore(FirstOf(
        Sequence(
          NEAR(),
          Term(), push(SwanQueries.NEAR(pop(1), pop()))
         ),
        Sequence(
          ADJ(),
          Term(), push(SwanQueries.ADJ(pop(1), pop()))
        )
      ))
    );
  }
  
  public Rule Term() {
    return Sequence(
      OneOrMore(Char()),
        push(new SpanTermQuery(new Term(match())))
    );
  }
  
  public Rule Char() {
    return AnyOf("0123456789" +
      "abcdefghijklmnopqrstuvwxyz" +
      "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
      "-_"
    );
  }
  
  public Rule WhiteSpace() {
    return OneOrMore(AnyOf(" \t\f"));
  }

  //////////////////////////////////////////////////
  public Rule OR() {
    return Sequence(IgnoreCase("OR"), WhiteSpace());
  }
  public Rule SAME() {
    return Sequence(IgnoreCase("SAME"), WhiteSpace());
  }
  public Rule WITH() {
    return Sequence(IgnoreCase("WITH"), WhiteSpace());
  }
  public Rule NEAR() {
    return Sequence(IgnoreCase("NEAR"), WhiteSpace());
  }
  public Rule ADJ() {
    return Sequence(IgnoreCase("ADJ"), WhiteSpace());
  }
 
  
  public static void main(String[] args) {
    SwanParser parser = Parboiled.createParser(SwanParser.class);
		
    String input = "apple AND banana NEAR coconut";

    ParsingResult<?> result = new RecoveringParseRunner<SpanQuery>(parser.Query()).run(input);

    SpanQuery node = (SpanQuery) result.parseTreeRoot.getValue();
  }
}