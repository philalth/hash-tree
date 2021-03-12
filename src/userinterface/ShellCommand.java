package userinterface;

/**
 * Contains all possible commands for the Shell for HashTrees that are currently implemented.
 * Furthermore all associated parameter numbers, the mode in which the command is possible and
 * a help text are stored.
 */
enum ShellCommand {
  NEW("new", 2, 0,
      "new <capacity>: Creates a new HashTree."),
  PUSH("push", 2, 1,
      "push <element>: Inserts a new element at the next possible leaf."),
  NEW_CHECK("new_check", 3, 0,
      "new_check <capacity> <root hash>: Creates a new HashTree and sets the root hash."),
  SET_VAL("set_val", 3, 2,
      "set_val <leafindex> <element>: Sets the value at a given index."),
  SET_HASH("set_hash", 3, 2,
      "set_hash <index> <hash>: Sets the hash at a given index."),
  READY("ready?", 1, 2,
      "ready?: Returns READY! if a check can be done, if not returns the missing indices."),
  CHECK("check", 1, 2,
      "check: Returns ACK if the tree is consistent, otherwise REJ."),
  CLEAR("clear", 1, 3,
      "clear: Deletes all values in this tree."),
  DEBUG("debug", 1, 3,
      "debug: Returns a textual representation of the current tree."),
  HELP("help", 1, 0,
      "help: Prints this help text."),
  QUIT("quit", 1, 0,
      "quit: Exits this programm."),
  // for wrong user input
  WRONG_PARAMETER("wrong", 0, -1, ""),
  // for wrong commands
  UNKNOWN("unknown", 0, -1, "");

  private final String command;

  // the expected length of a String[] containing the correct number of parameters for a command
  private final int numberOfExpectedParameters;

  // 0 - default (merkle), 1 - build, 2 - check, 3 - available in build and check
  private final int mode;

  // help text for the current command
  private final String helpText;

  private ShellCommand(String command, int numberOfExpectedParameters, int mode,
      String helpText) {
    this.command = command;
    this.numberOfExpectedParameters = numberOfExpectedParameters;
    this.mode = mode;
    this.helpText = helpText;
  }

  int getParameterNumber() {
    return numberOfExpectedParameters;
  }

  String getCommandAsString() {
    return command;
  }

  int getMode() {
    return mode;
  }

  String getHelpText() {
    return helpText;
  }

}
