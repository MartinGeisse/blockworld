/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.server.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Database utility class to deal with characters' inventories.
 */
public final class InventoryAccess {

	/**
	 * the characterId
	 */
	private final String characterId;

	/**
	 * Constructor.
	 * @param characterId the ID of the character whose inventory shall be accessed
	 */
	public InventoryAccess(String characterId) {
		this.characterId = characterId;
	}

	/**
	 * Getter method for the characterId.
	 * @return the characterId
	 */
	public String getCharacterId() {
		return characterId;
	}
	
	/**
	 * Adds an item to the character's inventory. This is equivalent to add(type, 1).
	 * @param type the item type
	 */
	public void add(ItemType type) {
		add(type, 1);
	}
	
	/**
	 * Adds an item to the character's inventory.
	 * @param type the item type
	 * @param quantity the quantity of the item stack
	 */
	public void add(ItemType type, int quantity) {
		// TODO needs character database
//		QPlayerInventorySlot qpis = QPlayerInventorySlot.playerInventorySlot;
//		
//		final SQLQuery query = EntityConnectionManager.getConnection().createQuery().from(qpis);
//		int previousInventoryLength = (int)query.where(qpis.characterId.eq(characterId), qpis.equipped.isFalse()).count();
//		
//		SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(qpis);
//		insert.set(qpis.characterId, characterId);
//		insert.set(qpis.equipped, false);
//		insert.set(qpis.index, previousInventoryLength);
//		insert.set(qpis.type, type.ordinal());
//		insert.set(qpis.quantity, quantity);
//		insert.execute();
		
	}
	
	/**
	 * Lists all items in the player's inventory, both equipped and in the backpack.
	 * The returned list contains all backpack items first, then all equipped items.
	 * Both groups are sorted by index.
	 * 
	 * @return the list of items
	 */
	public <T> List<T> listAll() {
		// TODO needs player database
//		final QPlayerInventorySlot qpis = QPlayerInventorySlot.playerInventorySlot;
//		final SQLQuery q = EntityConnectionManager.getConnection().createQuery().from(qpis);
//		q.where(qpis.characterId.eq(characterId)).orderBy(qpis.equipped.asc(), qpis.index.asc());
//		return q.list(qpis);
		return new ArrayList<>();
	}
	
	/**
	 * Lists the items in the player's backpack, sorted by index.
	 * 
	 * @return the list of items
	 */
	public <T> List<T> listBackback() {
		// TODO needs player database
//		final QPlayerInventorySlot qpis = QPlayerInventorySlot.playerInventorySlot;
//		final SQLQuery q = EntityConnectionManager.getConnection().createQuery().from(qpis);
//		q.where(qpis.characterId.eq(characterId), qpis.equipped.isFalse()).orderBy(qpis.index.asc());
//		return q.list(qpis);
		return new ArrayList<>();
	}
	
	/**
	 * Lists the player's equipped items, sorted by index.
	 * 
	 * @return the list of items
	 */
	public <T> List<T> listEquipped() {
		// TODO needs player database
//		final QPlayerInventorySlot qpis = QPlayerInventorySlot.playerInventorySlot;
//		final SQLQuery q = EntityConnectionManager.getConnection().createQuery().from(qpis);
//		q.where(qpis.characterId.eq(characterId), qpis.equipped.isTrue()).orderBy(qpis.index.asc());
//		return q.list(qpis);
		return new ArrayList<>();
	}

	/**
	 * Deletes a backpack item, specified by index.
	 * @param index the item's index.
	 */
	public void deleteBackpackItemByIndex(int index) {
		// TODO needs player database
//		final QPlayerInventorySlot qpis = QPlayerInventorySlot.playerInventorySlot;
//		SQLDeleteClause delete = EntityConnectionManager.getConnection().createDelete(qpis);
//		delete.where(qpis.characterId.eq(characterId), qpis.equipped.isFalse(), qpis.index.eq(index));
//		delete.execute();
//		renumber(false);
	}
	
	/**
	 * Deletes an equipped item, specified by index.
	 * @param index the item's index.
	 */
	public void deleteEquippedItemByIndex(int index) {
		// TODO needs player database
//		final QPlayerInventorySlot qpis = QPlayerInventorySlot.playerInventorySlot;
//		SQLDeleteClause delete = EntityConnectionManager.getConnection().createDelete(qpis);
//		delete.where(qpis.characterId.eq(characterId), qpis.equipped.isTrue(), qpis.index.eq(index));
//		delete.execute();
//		renumber(true);
	}

	/**
	 * Equips the backpack item with the specified index.
	 * @param index the item's index
	 */
	public void equip(int index) {
		// TODO needs player database
//		final QPlayerInventorySlot qpis = QPlayerInventorySlot.playerInventorySlot;
//		SQLUpdateClause update = EntityConnectionManager.getConnection().createUpdate(qpis);
//		update.where(qpis.characterId.eq(characterId), qpis.equipped.isFalse(), qpis.index.eq(index));
//		update.set(qpis.equipped, true);
//		update.execute();
//		renumber(false);
//		renumber(true);
	}
	
	/**
	 * Unequips the equipped item with the specified index.
	 * @param index the item's index
	 */
	public void unequip(int index) {
		// TODO needs player database
//		final QPlayerInventorySlot qpis = QPlayerInventorySlot.playerInventorySlot;
//		SQLUpdateClause update = EntityConnectionManager.getConnection().createUpdate(qpis);
//		update.where(qpis.characterId.eq(characterId), qpis.equipped.isTrue(), qpis.index.eq(index));
//		update.set(qpis.equipped, false);
//		update.execute();
//		renumber(false);
//		renumber(true);
	}
	
	/**
	 * 
	 */
//	private void renumber(boolean equipped) {
//		try {
//			JdbcEntityDatabaseConnection entityConnection = (JdbcEntityDatabaseConnection)EntityConnectionManager.getConnection();
//			Connection connection = entityConnection.getJdbcConnection();
//			Statement statement = connection.createStatement();
//			statement.execute("SELECT (@a := -1);");
//			statement.execute("UPDATE `player_inventory_slot` SET `index` = (@a := @a + 1) WHERE `player_id` = " + characterId + " AND `equipped` = " + (equipped ? '1' : '0') + ";");
//		} catch (SQLException e) {
//			throw new RuntimeException(e);
//		}
//	}
	
	/**
	 * Finds an item in the player's inventory by item type.
	 * @param type the item type
	 * @param equipped whether to look for equipped items or backpack items
	 * @return the index, or -1 if not found
	 */
	public int findByType(ItemType type, boolean equipped) {
		// TODO needs player database
//		final QPlayerInventorySlot qpis = QPlayerInventorySlot.playerInventorySlot;
//		final SQLQuery q = EntityConnectionManager.getConnection().createQuery().from(qpis);
//		q.where(qpis.characterId.eq(characterId), qpis.equipped.eq(equipped), qpis.type.eq(type.ordinal()));
//		Integer result = q.singleResult(qpis.index);
//		return (result == null ? -1 : result);
		return -1;
	}
	
}
