package com.youtube.hempfest.clansapi;

import com.youtube.hempfest.clans.construct.Clan;
import com.youtube.hempfest.clans.construct.api.ClansAPI;
import com.youtube.hempfest.clans.util.RankPriority;
import java.util.Map;
import java.util.UUID;

public abstract class ClanBuilder {

	protected abstract Map<UUID, RankPriority> getMemberList();

	protected abstract UUID getLeader();

	protected abstract String getClanName();

	protected abstract String getPassword();

	/**
	 * Create the clan with the retained values from the blueprint object
	 * and apply all member rank priorities. NOTE: If any one of the members including
	 * the clan leader is within a clan during this time period they will be
	 * forcefully removed.
	 */
	public void create() {
		if (ClansAPI.getInstance().isInClan(getLeader())) {
			Clan.action.removePlayer(getLeader());
		}
		Clan.action.create(getLeader(), getClanName(), !getPassword().equals("none") ? getPassword() : null);
		for (Map.Entry<UUID, RankPriority> entry : getMemberList().entrySet()) {
			if (ClansAPI.getInstance().isInClan(entry.getKey())) {
				Clan.action.removePlayer(entry.getKey());
			}
			Clan.action.joinClan(entry.getKey(), getClanName(), getPassword());
			ClanUtil.setRank(getClan().getClanID(), entry.getKey(), entry.getValue());
		}
	}

	/**
	 * NOTE: Only use after ( ClanBuilder#create() ) has been initialized
	 * Get the clan object created from the builder.
	 * @return The clan object from creation.
	 */
	public Clan getClan() {
		return Clan.action.getClan(Clan.action.getClan(getLeader()));
	}

	/**
	 * Apply a desired amount of power to the clan by default
	 * @param amount The amount to give.
	 */
	public void givePower(double amount) {
		getClan().givePower(amount);
	}

	/**
	 * Apply a desired amount of claims to the clan by default
	 * @param amount The amount to give.
	 */
	public void giveClaims(int amount) {
		getClan().giveClaim(amount);
	}

	/**
	 * Set back a desired amount of power the clan has by default
	 * @param amount The amount to take.
	 */
	public void takePower(double amount) {
		getClan().takePower(amount);
	}

	/**
	 * Set back a desired amount of claims the clan has by default
	 * @param amount The amount to take.
	 */
	public void takeClaims(int amount) {
		getClan().takeClaim(amount);
	}

	/**
	 * Set the clans default description
	 * @param context The information to update the description with
	 */
	public void setDescription(String context) {
		getClan().setDescription(context);
	}

	/**
	 * Change the clans friendly status
	 * NOTE: Setting this to false will make the clan switch
	 * to 'War' mode aswell as turn friendly-fire on.
	 * @param friendly The peace status of the clan
	 */
	public void setIsFriendly(boolean friendly) {
		getClan().setFriendlyFire(!friendly);
		getClan().setPeaceful(friendly);
	}

}
