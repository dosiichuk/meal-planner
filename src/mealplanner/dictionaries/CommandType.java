package mealplanner.dictionaries;

public enum CommandType {
    ADD("add"),
    SHOW("show"),
    PLAN("plan"),
    EXIT("exit");

    private String command;

    CommandType(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public static boolean isValidCommandType(String command) {
        for (CommandType commandType: CommandType.values()) {
            if (commandType.getCommand().equals(command)) {
                return true;
            }
        }
        return false;
    }

    public static CommandType getCommandTypeByTitle(String command) {
        for (CommandType commandType : CommandType.values()) {
            if (commandType.getCommand().equals(command)) {
                return commandType;
            }
        }
        return null;
    }
}
