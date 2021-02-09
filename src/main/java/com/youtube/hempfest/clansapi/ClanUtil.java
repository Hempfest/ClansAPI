package com.youtube.hempfest.clansapi;

import com.youtube.hempfest.clans.construct.Clan;
import com.youtube.hempfest.clans.construct.actions.ClanAction;
import com.youtube.hempfest.clans.construct.api.ClansAPI;
import com.youtube.hempfest.clans.util.RankPriority;
import com.youtube.hempfest.clans.util.data.Config;
import com.youtube.hempfest.clans.util.data.DataManager;
import com.youtube.hempfest.clans.util.data.FileType;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;

public class ClanUtil {

	public static void setRank(String clanID, UUID target, RankPriority priority) {
		ClanAction clanAction = new ClanAction();
		String rank = "";
		switch (priority) {
			case NORMAL:
				rank = "members";
				break;
			case HIGH:
				rank = "moderators";
				break;
			case HIGHER:
				rank = "admins";
				break;
		}
		DataManager data = new DataManager(clanID);
		Config clan = data.getFile(FileType.CLAN_FILE);
		if (clan.getConfig().getStringList("members").contains(target.toString())) {
			if (priority != RankPriority.HIGHEST) {
				String currentRank = clanAction.getCurrentRank(clanAction.getRankPower(target));
				List<String> array = clan.getConfig().getStringList(rank);
				List<String> array2 = clan.getConfig().getStringList(currentRank);
				if (!currentRank.equals("members")) {
					array2.remove(target.toString());
				}
				array.add(target.toString());
				clan.getConfig().set(clanAction.getRankUpgrade(clanAction.getRankPower(target)), array);
				clan.getConfig().set(currentRank, array2);
				clan.saveConfig();
				Clan clanIndex = Clan.action.getClan(clanID);
				String format = String.format(ClansAPI.getData().getMessage("promotion"), Bukkit.getOfflinePlayer(target).getName(), Clan.action.getRankTag(Clan.action.getRank(target)));
				clanIndex.broadcast(format);
			}
		}
	}

}
