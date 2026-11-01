package net.ludwici.tooltipfixmodern;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.FormattedCharSink;

import java.util.ArrayList;
import java.util.List;

public final class OrderedTextToTextVisitor implements FormattedCharSink {

    private final List<Component> lines = new ArrayList<>();
    private MutableComponent currentLine = Component.empty();
    private final StringBuilder segment = new StringBuilder();
    private Style currentStyle;
    private Style slashStyle;
    private boolean pendingSlash;

    @Override
    public boolean accept(int index, Style style, int codePoint) {
        if (pendingSlash) {
            pendingSlash = false;
            if (codePoint == 'n') {
                finishLine();
                return true;
            }
            appendCharacter('\\', slashStyle);
        }

        if (codePoint == '\\') {
            pendingSlash = true;
            slashStyle = style;
        } else if (codePoint == '\n') {
            finishLine();
        } else if (codePoint != '\r') {
            appendCharacter(codePoint, style);
        }
        return true;
    }

    private void appendCharacter(int codePoint, Style style) {
        if (currentStyle != null && !currentStyle.equals(style)) {
            finishSegment();
        }
        currentStyle = style;
        segment.appendCodePoint(codePoint);
    }

    private void finishSegment() {
        if (!segment.isEmpty()) {
            currentLine.append(Component.literal(segment.toString()).setStyle(currentStyle));
            segment.setLength(0);
        }
    }

    private void finishLine() {
        finishSegment();
        lines.add(currentLine);
        currentLine = Component.empty();
        currentStyle = null;
    }

    public static List<Component> getLines(FormattedCharSequence text) {
        OrderedTextToTextVisitor visitor = new OrderedTextToTextVisitor();
        text.accept(visitor);
        if (visitor.pendingSlash) {
            visitor.appendCharacter('\\', visitor.slashStyle);
        }
        visitor.finishLine();
        return visitor.lines;
    }
}
