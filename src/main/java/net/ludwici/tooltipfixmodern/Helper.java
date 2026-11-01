package net.ludwici.tooltipfixmodern;

import net.ludwici.tooltipfixmodern.mixin.OrderedTextToolTipAccessor;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.ArrayList;
import java.util.List;

public final class Helper {

    private Helper() {
    }

    public static List<ClientTooltipComponent> prepareTooltip(List<ClientTooltipComponent> components, Font font, int mouseX, int screenWidth) {
        if (components.isEmpty()) {
            return components;
        }

        int forcedWidth = 0;
        for (ClientTooltipComponent component : components) {
            if (!(component instanceof ClientTextTooltip)) {
                forcedWidth = Math.max(forcedWidth, component.getWidth(font));
            }
        }

        int rightWidth = Math.max(1, screenWidth - mouseX - 20);
        int leftWidth = Math.max(1, mouseX - 28);
        int maxWidth = rightWidth;
        if (rightWidth < 100 || forcedWidth > rightWidth) {
            maxWidth = Math.max(rightWidth, leftWidth);
        }
        maxWidth = Math.clamp(maxWidth, 1, screenWidth - 28);

        List<ClientTooltipComponent> result = new ArrayList<>();
        for (ClientTooltipComponent component : components) {
            if (component instanceof ClientTextTooltip textTooltip) {
                FormattedCharSequence text = ((OrderedTextToolTipAccessor) textTooltip).tooltipfix$getText();
                for (Component line : OrderedTextToTextVisitor.getLines(text)) {
                    List<FormattedCharSequence> wrapped = font.split(line, maxWidth);
                    if (wrapped.isEmpty()) {
                        result.add(ClientTooltipComponent.create(FormattedCharSequence.EMPTY));
                    } else {
                        for (FormattedCharSequence wrappedLine : wrapped) {
                            result.add(ClientTooltipComponent.create(wrappedLine));
                        }
                    }
                }
            } else {
                result.add(component);
            }
        }
        return result;
    }
}
