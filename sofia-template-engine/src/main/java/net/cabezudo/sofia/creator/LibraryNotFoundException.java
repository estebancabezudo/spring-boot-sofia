package net.cabezudo.sofia.creator;

import net.cabezudo.html.nodes.FilePosition;

public class LibraryNotFoundException extends SiteCreationException {
  public LibraryNotFoundException(String message, FilePosition position) {
    super(message, position);
  }
}
