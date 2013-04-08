package javarepl.commands;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.numbers.Numbers;
import javarepl.Evaluator;
import jline.console.completer.StringsCompleter;

import static com.googlecode.totallylazy.Pair.functions.values;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.replace;
import static com.googlecode.totallylazy.Strings.startsWith;
import static java.lang.Integer.parseInt;
import static javarepl.Evaluation.functions.expression;
import static javarepl.Utils.listValues;
import static javarepl.expressions.Expression.functions.source;

public class ShowHistory extends Command {
    private static final String COMMAND = ":hist";

    public ShowHistory() {
        super(COMMAND + " [num] - shows the history (optional 'num' is number of evaluations to show)",
                startsWith(COMMAND), new StringsCompleter(COMMAND));
    }

    public Void call(Evaluator evaluator, String expression) throws Exception {
        Sequence<String> splitLine = sequence(expression.split(" "));
        Integer limit = (splitLine.size() == 2) ? parseInt(splitLine.second()) : evaluator.evaluations().size();
        listValues("History", history(evaluator).reverse().take(limit).reverse());
        return null;
    }

    public static Sequence<String> history(Evaluator evaluator) {
        return Numbers.range(1)
                .zip(evaluator.evaluations().map(expression().then(source()).then(replace("\n", "\n   "))))
                .map(values().then(Sequences.toString("  ")));
    }
}
