package net.ludwici.tooltipfixmodern.mixin;

import net.ludwici.tooltipfixmodern.Helper;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(GuiGraphicsExtractor.class)
public abstract class FixToolTipMixin {

    @Shadow
    public abstract int guiWidth();

    @ModifyVariable(method = "tooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;Lnet/minecraft/resources/Identifier;)V", at = @At("HEAD"), argsOnly = true, index = 2)
    private List<ClientTooltipComponent> tooltipfix$prepareTooltip(List<ClientTooltipComponent> components, Font font, List<ClientTooltipComponent> original, int x, int y, ClientTooltipPositioner positioner, Identifier style) {
        return Helper.prepareTooltip(components, font, x, guiWidth());
    }

    @ModifyVariable(method = "setTooltipForNextFrameInternal(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;Lnet/minecraft/resources/Identifier;Z)V", at = @At("HEAD"), argsOnly = true, index = 2)
    private List<ClientTooltipComponent> tooltipfix$prepareDeferredTooltip(List<ClientTooltipComponent> components, Font font, List<ClientTooltipComponent> original, int x, int y, ClientTooltipPositioner positioner, Identifier style, boolean replaceExisting) {
        return Helper.prepareTooltip(components, font, x, guiWidth());
    }
}
