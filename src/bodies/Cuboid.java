package bodies;

/**
 * A 3D-Object representing a Cuboid.
 */
public class Cuboid extends Body {

  /**
   * The Cuboid's length.
   */
  private final int length;

  /**
   * The Cuboid's width.
   */
  private final int width;

  /**
   * The Cuboid's height.
   */
  private final int height;

  /**
   * Creates a new {@code Cuboid}.
   * 
   * @param length non-negative length of the {@code Cuboid}
   * @param width non-negative width of the {@code Cuboid}
   * @param height non-negative height of the {@code Cuboid}
   */
  public Cuboid(int length, int width, int height) {
    this.length = length;
    this.width = width;
    this.height = height;
  }

  /**
   * Returns a textual representation of the {@code Cuboid}.
   */
  @Override
  public String toString() {
    return "Cuboid(" + length + "," + width + "," + height + ")";
  }

}
