package ru.paingocry.holders.painholders;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commands implements CommandExecutor {
    public Commands() {
        Main.plugin.getCommand("painholders").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("painplaceholders.admin")) {
            return false;
        } else {
            if (args.length < 4) {
                sender.sendMessage("Недостаточно аргументов. Используйте /painplaceholders add/set/remove <игрок> <холдер> <значение>");
                return true;
            }

            if (args[0].equals("add")) {
                try {
                    Placeholder placeholder = Placeholder.getPlaceholderByName(args[2]);
                    placeholder.addValue(Integer.valueOf(args[3]), args[1]);
                    sender.sendMessage("Успешно!");
                } catch (Exception e) {
                    sender.sendMessage("Холдер не найден");
                }
            } else if (args[0].equals("remove")) {
                try {
                    Placeholder placeholder = Placeholder.getPlaceholderByName(args[2]);
                    placeholder.removeValue(Integer.valueOf(args[3]), args[1]);
                    sender.sendMessage("Успешно!");
                } catch (Exception e) {
                    sender.sendMessage("Холдер не найден");
                }
            } else if (args[0].equals("set")) {
                try {
                    Placeholder placeholder = Placeholder.getPlaceholderByName(args[2]);
                    placeholder.setValue(Integer.valueOf(args[3]), args[1]);
                    sender.sendMessage("Успешно!");
                } catch (Exception e) {
                    try {
                        PlaceholderText placeholder = PlaceholderText.getPlaceholderByName(args[2]);
                        placeholder.setValue(args[3], args[1]);
                        sender.sendMessage("Успешно!");
                    } catch (Exception ex) {
                        sender.sendMessage("Холдер не найден");
                    }
                }
            }
            return true;
        }
    }
}