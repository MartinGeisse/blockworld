/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.server.command;

import com.google.inject.Inject;
import com.google.inject.Provider;
import name.martingeisse.blockworld.server.MinerServer;
import name.martingeisse.blockworld.server.MinerSession;
import name.martingeisse.blockworld.server.game.InventoryAccess;
import name.martingeisse.blockworld.server.game.ItemType;

/**
 * Default server-side command handler.
 */
public class DefaultServerCommandHandler implements ServerCommandHandler {

	/**
	 * the serverProvider
	 */
	private final Provider<MinerServer> serverProvider;

	/**
	 * Constructor.
	 * @param serverProvider the server provider
	 */
	@Inject
	public DefaultServerCommandHandler(Provider<MinerServer> serverProvider) {
		this.serverProvider = serverProvider;
	}

	// override
	@Override
	public void handleCommand(final MinerSession session, final String command, final String[] arguments) {
		final ServerCommandParser parser = new ServerCommandParser(session, arguments);
		final InventoryAccess inventoryAccess = new InventoryAccess(session.getCharacterId());
		final ItemType[] itemTypes = ItemType.values();
		try {
		
			if (command.equals("help")) {
				session.sendConsoleOutput("Available commands:");
				session.sendConsoleOutput("  help");
				session.sendConsoleOutput("  initworld");
				session.sendConsoleOutput("  itemtypes");
				session.sendConsoleOutput("  inventory");
				session.sendConsoleOutput("  wish <itemtype>");
				session.sendConsoleOutput("  equip <itemtype>");
				session.sendConsoleOutput("  equip inventory <index>");
				session.sendConsoleOutput("  unequip <itemtype>");
				session.sendConsoleOutput("  unequip inventory <index>");
				session.sendConsoleOutput("  trash <itemtype>");
				session.sendConsoleOutput("  trash inventory <index>");
				session.sendConsoleOutput("  give ...");

			} else if (command.equals("initworld")) {
				serverProvider.get().initializeWorldWithHeightField();
				session.sendConsoleOutput("world initialized");

			} else if (command.equals("itemtypes")) {
				session.sendConsoleOutput("item types: ");
				for (ItemType itemType : itemTypes) {
					session.sendConsoleOutput("  " + itemType.getDisplayName());
				}
				
			} else if (command.equals("inventory")) {
				session.sendConsoleOutput("backpack: ");
//				for (PlayerInventorySlot slot : inventoryAccess.listBackback()) {
//					session.sendConsoleOutput("  [" + slot.getIndex() + "]: " + slot.getQuantity() + " " + itemTypes[slot.getType()].getDisplayName());
//				}
				session.sendConsoleOutput("equipped: ");
//				for (PlayerInventorySlot slot : inventoryAccess.listEquipped()) {
//					session.sendConsoleOutput("  [" + slot.getIndex() + "]: " + slot.getQuantity() + " " + itemTypes[slot.getType()].getDisplayName());
//				}

			} else if (command.equals("wish")) {
				final ItemType type = parser.fetchItemType(false);
				if (type != null) {
					new InventoryAccess(session.getCharacterId()).add(type);
					session.sendConsoleOutput("wish granted");
				}
				
			} else if (command.equals("equip")) {
				int index = parser.fetchInventorySlot(inventoryAccess, false, false);
				if (index >= 0) {
					inventoryAccess.equip(index);
					session.sendConsoleOutput("equipped");
				}
				
			} else if (command.equals("unequip")) {
				int index = parser.fetchInventorySlot(inventoryAccess, true, false);
				if (index >= 0) {
					inventoryAccess.unequip(index);
					session.sendConsoleOutput("unequipped");
				}
				
			} else if (command.equals("trash")) {
				int index = parser.fetchInventorySlot(inventoryAccess, false, false);
				if (index >= 0) {
					inventoryAccess.deleteBackpackItemByIndex(index);
					session.sendConsoleOutput("trashed");
				}
				
			} else if (command.equals("give")) {
				int index = parser.fetchInventorySlot(inventoryAccess, false, false);
				if (index >= 0) {
					// TODO give item to another player
				}
				session.sendConsoleOutput("this command is not implemented yet");
	
			}

		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

}
