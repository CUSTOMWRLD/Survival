package cc.minetale.survival.utils;

import dev.jorel.commandapi.arguments.CustomArgument;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Arguments {

    public static CustomArgument<OfflinePlayer> offlinePlayerArgument(String nodeName) {
        CustomArgument<OfflinePlayer> argument = new CustomArgument<>(nodeName, Bukkit::getOfflinePlayer);

        argument.overrideSuggestions(sender -> Bukkit.getServer().getOnlinePlayers().stream().map(Player::getName).toArray(String[]::new));

        return argument;
    }

}
