package com.ant;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Blocks;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.GuiHelpers;
import eu.pb4.sgui.api.elements.*;
import eu.pb4.sgui.api.gui.*;
import eu.pb4.sgui.api.gui.layered.Layer;
import eu.pb4.sgui.api.gui.layered.LayerView;
import eu.pb4.sgui.api.gui.layered.LayeredGui;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import com.ant.util.SmithingUtil;

public class ViaSmith implements ModInitializer {
	public static final String MOD_ID = "viasmith";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("ViaSmith initialized!");
		UseBlockCallback.EVENT.register((entityplayer, world, hand, hitResult) -> {
			ServerPlayerEntity player = (ServerPlayerEntity) entityplayer;
			if (world.isClient()) return ActionResult.PASS;

			var pos = hitResult.getBlockPos();
			var block = world.getBlockState(pos).getBlock();

			if (block == Blocks.SMITHING_TABLE) {
				SimpleGui gui = new SimpleGui(ScreenHandlerType.GENERIC_9X6, player, true);
				gui.setTitle(Text.literal("Smithing Table"));
				gui.open();
				return ActionResult.SUCCESS;
			}
			return ActionResult.PASS;
		});
	}
}