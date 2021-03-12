package userinterface;

/**
 * Contains all possible types of data that can currently be stored in a {@code HashTree}. The
 * number of expected parameters associated with the specific type of data is also stored.
 */
enum HashTreeData {
  CUBOID("Cuboid", 4),
  CYLINDER("Cylinder", 3),
  WRONG_PARAMETER("wrong", 0),
  UNKNOWN("unknown", 0);

  private final String dataAsString;

  private final int numberOfExpectedParameters;

  private HashTreeData(String command, int numberOfExpectedParameters) {
    this.dataAsString = command;
    this.numberOfExpectedParameters = numberOfExpectedParameters;
  }

  int getParameterNumber() {
    return numberOfExpectedParameters;
  }

  String getDataAsString() {
    return dataAsString;
  }

}
