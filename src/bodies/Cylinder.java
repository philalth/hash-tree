package bodies;

/**
 * A 3D-Object representing a Cylinder.
 */
public class Cylinder extends Body {

  /**
   * The cylinder's radius.
   */
  private final int radius;

  /**
   * The cylinder's height.
   */
  private final int height;

  /**
   * Creates a new {@code Cylinder}.
   * 
   * @param radius non-negative radius of the {@code Cylinder}
   * @param height non-negative height of the {@code Cylinder}
   */
  public Cylinder(int radius, int height) {
    this.radius = radius;
    this.height = height;
  }

  /**
   * Returns a textual representation of the {@code Cylinder}.
   */
  @Override
  public String toString() {
    return "Cylinder(" + radius + "," + height + ")";
  }

}
