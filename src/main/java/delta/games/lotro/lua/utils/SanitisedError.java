package delta.games.lotro.lua.utils;

import java.util.HashSet;
import java.util.Set;

/**
 * Wraps {@link Throwable}, which attempts to sanitise error messages.
 * <p>
 * This is intended for logging errors where the message content is supplied from untrusted sources. This isn't a
 * perfect escaping mechanism, but ensures basic "unsafe" strings (i.e. ANSI escape sequences, long lines) are escaped.
 *
 * <h2>Example:</h2>
 * <pre>{@code
 * LOG.error("Some error occurred: {}", new TruncatedError(error));
 * }</pre>
 *
 */
public class SanitisedError extends Throwable {
  private static final long serialVersionUID = -5250215591874653608L;
  private static final int MAX_LENGTH = 200;
  private static final String CAUSED_BY = "Caused by: ";
  private static final String SUPPRESSED = "Suppressed: ";

  private Throwable error;
  
  public SanitisedError(Throwable throwable) {
    this.error = throwable;
  }
  
  @Override
  public String toString() {
    StringBuilder output = new StringBuilder();
    printStackTrace(output, this.error);
    return output.toString();
  }

  public static void printStackTrace(StringBuilder output, Throwable error) {
    appendMessage(output, error);
  
    StackTraceElement[] trace = error.getStackTrace();
    for (StackTraceElement traceElement : trace) output.append("\tat ").append(traceElement).append("\n");
  
    HashSet<Throwable> seen = new HashSet<Throwable>();
    seen.add(error);
  
    printAdditionalErrors(output, error, trace, "", seen);
  }

  private static void appendMessage(StringBuilder output, Throwable self) {
    String message = self.toString();
    int length = message.length();
    for (int i = 0, limit = Math.min(MAX_LENGTH, length); i < limit; i++) {
      char c = message.charAt(i);
      switch (c) {
        case '\\': output.append("\\\\"); break;
        case '\n': output.append("\\n"); break;
        case '\r': output.append("\\r"); break;
        case '\t': output.append("\\t"); break;
        default: {
          if (c >= ' ') {
            output.append(c);
          } else {
            output.append("\\u{").append(Integer.toHexString(c)).append("}");
          }
        }
      }
    }

    if (length > MAX_LENGTH) output.append("... (message truncated)");
    output.append("\n");
  }

  private static void printAdditionalErrors(
    StringBuilder output, Throwable self, StackTraceElement[] trace, String indent, Set<Throwable> seen
  ) {
    // Print suppressed exceptions, if any
    for (Throwable se : self.getSuppressed()) printAdditionalError(output, se, trace, SUPPRESSED, indent + "\t", seen);

    // Print cause, if any
    Throwable ourCause = self.getCause();
    if (ourCause != null) printAdditionalError(output, ourCause, trace, CAUSED_BY, indent, seen);
  }

  private static void printAdditionalError(
    StringBuilder output, Throwable self, StackTraceElement[] parent, String label, String indent, Set<Throwable> seen
  ) {
    if (!seen.add(self)) {
      output.append("[DUPLICATE ERROR: ");
      appendMessage(output, self);
      output.append("]\n");
      return;
    }

    output.append(indent).append(label);
    appendMessage(output, self);

    // Find a common prefix with the parent exception and just print that.
    StackTraceElement[] trace = self.getStackTrace();
    int traceIdx, parentIdx;
    for (
      traceIdx = trace.length - 1, parentIdx = parent.length - 1;
      traceIdx >= 0 && parentIdx >= 0;
      traceIdx--, parentIdx--
    ) {
      if (!trace[traceIdx].equals(parent[parentIdx])) break;
    }

    for (int i = 0; i <= traceIdx; i++) output.append(indent).append("\tat ").append(trace[traceIdx]).append("\n");

    int remaining = trace.length - traceIdx - 1;
    if (remaining >= 0) output.append(indent).append("\t... ").append(remaining).append(" more\n");

    printAdditionalErrors(output, self, trace, indent + "\t", seen);
  }
}