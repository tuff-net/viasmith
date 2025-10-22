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
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.screen.slot.Slot;
import com.ant.util.SmithingUtil;

public class ViaSmith implements ModInitializer {
	public static final String MOD_ID = "viasmith";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static boolean justgenerated = false;
	@Override
	public void onInitialize() {
		LOGGER.info("ViaSmith initialized!");
		UseBlockCallback.EVENT.register((entityplayer, world, hand, hitResult) -> {
			ServerPlayerEntity player = (ServerPlayerEntity) entityplayer;
			if (world.isClient()) return ActionResult.PASS;

			var pos = hitResult.getBlockPos();
			var block = world.getBlockState(pos).getBlock();
			// TODO: Add viaversion check thing
			if (block == Blocks.SMITHING_TABLE) {
				SimpleInventory tempInv = new SimpleInventory(9);
				SimpleGui gui = new SimpleGui(ScreenHandlerType.GENERIC_9X3, player, false) {
					@Override
					public void onClose() {
						for (int i = 0; i < tempInv.size(); i++) {
							ItemStack stack = tempInv.removeStack(i);
							if (!stack.isEmpty()) {
								if (!player.getInventory().insertStack(stack)) {
									player.dropItem(stack, false);
								}
							}
						}
					super.onClose();
					}

					@Override 
					public void onTick() {
						ServerWorld serverworld = player.getWorld();
						ItemStack stack = tempInv.getStack(3);
						if (justgenerated == true && stack.isEmpty()) {
							tempInv.removeStack(0, 1);
							tempInv.removeStack(1, 1);
							tempInv.removeStack(2, 1);
						}
						justgenerated = SmithingUtil.trySmithing(serverworld, tempInv);
						super.onTick();
					}

				};
				gui.setSlot(19, GuiElementBuilder.from(new ItemStack(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE))
					.setName(Text.literal("Smithing Template"))
					.build());

				gui.setSlot(21, GuiElementBuilder.from(new ItemStack(Items.DIAMOND_SWORD))
					.setName(Text.literal("Tool/Armor"))
					.build());

				gui.setSlot(23, GuiElementBuilder.from(new ItemStack(Items.NETHERITE_INGOT))
					.setName(Text.literal("Ingot"))
					.build());

				gui.setSlot(25, GuiElementBuilder.from(new ItemStack(Items.BARRIER))
					.setName(Text.literal("Result"))
					.build());
				for (int i = 0; i < gui.getSize(); i++) {
					if (gui.getSlot(i) == null) {
						gui.setSlot(i, GuiElementBuilder.from(new ItemStack(Items.GRAY_STAINED_GLASS_PANE))
							.setName(Text.literal(" "))
							.build());
					}
				}
				gui.setSlotRedirect(10, new Slot(tempInv, 0, 0, 0));
				gui.setSlotRedirect(12, new Slot(tempInv, 1, 0, 0));
				gui.setSlotRedirect(14, new Slot(tempInv, 2, 0, 0));
				gui.setSlotRedirect(16, new Slot(tempInv, 3, 0, 0) {
					@Override
					public boolean canInsert(ItemStack stack) {
						return false;
					}
				});
				gui.setTitle(Text.literal("Smithing Table"));
				gui.open();
				return ActionResult.SUCCESS;
			}
			return ActionResult.PASS;
		});
	}
}