package net.cabezudo.sofia.creator;

import net.cabezudo.html.nodes.Position;

import java.nio.file.Path;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2019.11.12
 */
public class Caller {

  private final String type;
  private final Path path;
  private final Position position;
  private final Caller caller;

  Caller(String type, Path path, Position position, Caller caller) {
    this.type = type;
    this.path = path;
    this.position = position;
    this.caller = caller;
  }

  public String getType() {
    return type;
  }

  public Path getPath() {
    return path;
  }

  public Position getPosition() {
    return position;
  }

  public Caller getCaller() {
    return caller;
  }

  @Override
  public String toString() {
    return getType() + " on " + getPath() + (getPosition() != null ? ":" + getPosition() : "");
  }
}
