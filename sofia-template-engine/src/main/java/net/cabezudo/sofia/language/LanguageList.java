package net.cabezudo.sofia.language;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

@Getter
@AllArgsConstructor
public class LanguageList implements Iterable<Language> {
  private final List<Language> list;

  public LanguageList() {
    list = new ArrayList();
  }

  public void add(Language language) {
    list.add(language);
  }

  @NotNull
  @Override
  public Iterator<Language> iterator() {
    return list.iterator();
  }

  @Override
  public void forEach(Consumer<? super Language> action) {
    Iterable.super.forEach(action);
  }
}
