package userinterface;

import bodies.Body;
import bodies.Cuboid;
import bodies.Cylinder;
import hashtrees.HashTree;
import hashtrees.MerkleTreeBuilder;
import hashtrees.MutableMerkleTree;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Shell to interact with Hash-Trees.
 */
public class Shell {

  // common error messages
  private static final String NO_VALID_INPUT_MESSAGE = "Error! No valid input: ";
  private static final String NO_VALID_INDEX_MESSAGE = "Error! No valid index: ";
  private static final String COMMAND_DOESNT_EXIST_MESSAGE =
      "Error! This command does not exist in this mode.";

  // all currently possible modes for this shell
  private static final int DEFAULT_MODE = 0;
  private static final int BUILD_MODE = 1;
  private static final int CHECK_MODE = 2;
  private static final int BUILD_AND_CHECK_MODE = 3;

  private Shell() {
    // Generating objects of this class is not intended.
  }

  /**
   * Starts a new shell that waits for user input.
   * 
   * @param args command line arguments
   * @throws IOException if a problem with the InputStream occurs
   */
  public static void main(String[] args) throws IOException {
    final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
    runDefaultMode(stdin);
  }

  /**
   * Helper method to run the default (merkle) mode.
   * 
   * @throws IOException if a problem with the InputStream occurs
   */
  private static void runDefaultMode(final BufferedReader stdin) throws IOException {
    boolean run = true;
    while (run) {
      System.out.print("merkle> ");
      final String input = stdin.readLine();
      if (input == null) {
        break;
      }
      final String[] tokens = input.trim().split("\\s+");
      final String firstInput = tokens[0];
      final ShellCommand command = identifyCommand(firstInput, tokens);

      switch (command) {
        case NEW:
          if (checkInput(tokens)) {
            runBuildMode(tokens, stdin);
            run = false;
          }
          break;
        case NEW_CHECK:
          if (checkInput(tokens)) {
            runCheckMode(tokens, stdin);
            run = false;
          }
          break;
        case HELP:
          helpPrinter(DEFAULT_MODE);
          break;
        case QUIT:
          run = false;
          break;
        default:
          System.out.println(COMMAND_DOESNT_EXIST_MESSAGE);
          break;
      }
    }
  }

  /**
   * Helper method to run the build mode.
   * 
   * @throws IOException if a problem with the InputStream occurs
   */
  private static void runBuildMode(final String[] parameters, final BufferedReader stdin)
      throws IOException {
    if (Integer.parseInt(parameters[1]) < 2) {
      System.out.println("Error! Minimum size is 2.");
      runDefaultMode(stdin);
      return;
    }
    boolean run = true;
    final MerkleTreeBuilder<Body> passedData =
        new MerkleTreeBuilder<Body>(Integer.parseInt(parameters[1]));

    while (run) {
      System.out.print("build> ");
      final String input = stdin.readLine();
      if (input == null) {
        break;
      }
      final String[] tokens = input.trim().split("\\s+");
      final String firstInput = tokens[0];
      final ShellCommand command = identifyCommand(firstInput, tokens);

      switch (command) {
        case NEW:
          if (checkInput(tokens)) {
            runBuildMode(tokens, stdin);
            run = false;
          }
          break;
        case PUSH:
          pushHelper(passedData, tokens);
          break;
        case CLEAR:
          passedData.clear();
          break;
        case DEBUG:
          System.out.println(passedData.build().toString());
          break;
        case NEW_CHECK:
          if (checkInput(tokens)) {
            runCheckMode(tokens, stdin);
            run = false;
          }
          break;
        case HELP:
          helpPrinter(BUILD_MODE);
          break;
        case QUIT:
          run = false;
          break;
        default:
          System.out.println(COMMAND_DOESNT_EXIST_MESSAGE);
          break;
      }
    }
  }

  /** Helper method to run the check mode. */
  private static void runCheckMode(final String[] parameters, final BufferedReader stdin)
      throws IOException {
    if (Integer.parseInt(parameters[1]) < 2) {
      System.out.println("Error! Minimum size is 2.");
      runDefaultMode(stdin);
      return;
    }
    final MutableMerkleTree<Body> passedData =
        new MutableMerkleTree<Body>(Integer.parseInt(parameters[1]));
    passedData.setHash(0, Long.parseLong(parameters[2]));
    boolean run = true;

    while (run) {
      System.out.print("check> ");
      final String input = stdin.readLine();
      if (input == null) {
        break;
      }

      final String[] tokens = input.trim().split("\\s+");
      final String firstInput = tokens[0];
      final ShellCommand command = identifyCommand(firstInput, tokens);

      switch (command) {
        case NEW_CHECK:
          if (checkInput(tokens)) {
            runCheckMode(tokens, stdin);
            run = false;
          }
          break;
        case SET_VAL:
          setValHelper(passedData, tokens);
          break;
        case SET_HASH:
          setHashHelper(passedData, tokens);
          break;
        case READY:
          readyHelper(passedData);
          break;
        case CHECK:
          checkHelper(passedData);
          break;
        case NEW:
          runBuildMode(tokens, stdin);
          run = false;
          break;
        case CLEAR:
          passedData.clear();
          break;
        case DEBUG:
          System.out.println(passedData.toString());
          break;
        case HELP:
          helpPrinter(CHECK_MODE);
          break;
        case QUIT:
          run = false;
          break;
        default:
          System.out.println(COMMAND_DOESNT_EXIST_MESSAGE);
          break;
      }
    }
  }

  /** Helper method for the command "push". Handles all occuring exceptions. */
  private static void pushHelper(final MerkleTreeBuilder<Body> mtb, final String[] parameters) {
    if (createData(parameters[1]) != null) {
      mtb.push(createData(parameters[1]));
    }
  }

  /** Helper method for the command "set_val". Handles all possibly occuring exceptions. */
  private static void setValHelper(final HashTree<Body> tr, final String[] parameters) {
    try {
      if (createData(parameters[2]) != null) {
        final int index = Integer.parseInt(parameters[1]);
        tr.setValue(index, createData(parameters[2]));
      }
    } catch (IndexOutOfBoundsException e) {
      System.out.println(NO_VALID_INDEX_MESSAGE + parameters[1] + ".");
    }
  }

  /** Helper method for the command "set_hash". Handles all possibly occuring exceptions. */
  private static void setHashHelper(final HashTree<Body> tr, final String[] parameters) {
    if (!checkInput(parameters)) {
      return;
    }
    final int index = Integer.parseInt(parameters[1]);
    if (index == 0) {
      System.out.println("Error! Root hash cannot be changed.");
      return;
    }
    final long hash = Long.parseLong(parameters[2]);
    try {
      tr.setHash(index, hash);
    } catch (IndexOutOfBoundsException e) {
      System.out.println(NO_VALID_INDEX_MESSAGE + parameters[1] + ".");
    } catch (IllegalArgumentException e) {
      System.out.println("Error! This node has a value, thus the hash cannot be changed.");
    }
  }

  /** Helper method for the command "ready?". */
  private static void readyHelper(final HashTree<Body> tr) {
    if (tr.getMissing().isEmpty()) {
      System.out.println("READY!");
    } else {
      // as Martin wanted no whitespace in the output it is removed
      System.out.println(tr.getMissing().toString().replaceAll("\\s+", ""));
    }
  }

  /** Helper method for the command "check". Handles all possibly occuring exceptions. */
  private static void checkHelper(HashTree<Body> tr) {
    if (!tr.getMissing().isEmpty()) {
      System.out.println("Error! Check is currently not available.");
    } else if (tr.isConsistent()) {
      System.out.println("ACK");
    } else {
      System.out.println("REJ");
    }
  }

  /** Helper method identify a given command. */
  private static ShellCommand identifyCommand(String possibleCommand, final String[] parameters) {
    possibleCommand = possibleCommand.toLowerCase();
    ShellCommand identifiedCommand = ShellCommand.UNKNOWN;

    if (possibleCommand.trim().isEmpty()) {
      // catch empty input
      return ShellCommand.UNKNOWN;
    }
    for (ShellCommand cmd : ShellCommand.values()) {
      if (cmd.getCommandAsString().equals(possibleCommand)) {
        identifiedCommand = cmd;
      }
    }
    if (identifiedCommand == ShellCommand.UNKNOWN) {
      // command does not exist
      return ShellCommand.UNKNOWN;
    } else if (identifiedCommand.getParameterNumber() == parameters.length) {
      // command is correct
      return identifiedCommand;
    } else {
      // command exists but was used with wrong number of parameters
      return ShellCommand.WRONG_PARAMETER;
    }
  }

  /** Helper method to identify the data to be inserted. */
  private static HashTreeData identifyData(String[] parameters) {
    if (!checkInput(parameters)) {
      return HashTreeData.WRONG_PARAMETER;
    }
    HashTreeData identifiedData = HashTreeData.UNKNOWN;

    for (HashTreeData data : HashTreeData.values()) {
      if (data.getDataAsString().equals(parameters[0])) {
        identifiedData = data;
      }
    }
    if (identifiedData == HashTreeData.UNKNOWN) {
      // data type does not exist
      return HashTreeData.UNKNOWN;
    } else if (identifiedData.getParameterNumber() == parameters.length) {
      // data type exists
      return identifiedData;
    } else {
      // data type exists but was used with wrong number of parameters
      return HashTreeData.WRONG_PARAMETER;
    }
  }

  /** Helper method to create the data to be inserted identified by a given String. */
  private static Body createData(String possibleData) {
    final String[] parameters = possibleData.split("[\\( \\) ,]");
    final HashTreeData data = identifyData(parameters);
    switch (data) {
      case CYLINDER:
        final int radius = Integer.parseInt(parameters[1]);
        int height = Integer.parseInt(parameters[2]);
        return new Cylinder(radius, height);
      case CUBOID:
        final int length = Integer.parseInt(parameters[1]);
        final int width = Integer.parseInt(parameters[2]);
        height = Integer.parseInt(parameters[3]);
        return new Cuboid(length, width, height);
      case WRONG_PARAMETER:
        System.out.println("Error! Invalid input, wrong parameters: " + possibleData + ".");
        return null;
      default:
        System.out.println("Error! Inserting this type of object is not allowed.");
        return null;
    }
  }

  /** Helper method to print the help texts for commands possible for the given mode. */
  private static void helpPrinter(int mode) {
    System.out.println("All possible commands in current mode:\n");
    if (mode == DEFAULT_MODE) {
      for (ShellCommand cmd : ShellCommand.values()) {
        if (cmd.getMode() == mode) {
          System.out.println(cmd.getHelpText() + "\n");
        }
      }
    } else {
      for (ShellCommand cmd : ShellCommand.values()) {
        if (cmd.getMode() == mode || cmd.getMode() == BUILD_AND_CHECK_MODE
            || cmd.getMode() == DEFAULT_MODE) {
          System.out.println(cmd.getHelpText() + "\n");
        }
      }
    }
  }

  private static boolean checkInput(String[] input) {
    for (int i = 1; i < input.length; i++) {
      try {
        if (Long.parseLong(input[i]) < 0) {
          System.out.println(NO_VALID_INPUT_MESSAGE + " Negative numbers are not allowed.");
          return false;
        }
      } catch (NumberFormatException e) {
        System.out.println("Error! Invalid input. That was no number.");
        return false;
      }
    }
    return true;
  }

}
