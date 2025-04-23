package edu.sudoku.model;

import java.util.Collection;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Board {

  private final List<List<Space>> spaces;

  public Board(List<List<Space>> spaces) {
    this.spaces = spaces;
  }

  // fazer o objeto de cada posição informar sobre seu proprio status
  public GameStatusEnum getStatus() {
    // flatMap transforma cada elemento de um stream em outro stream
    // achantando cada sublista em uma nova stream
    // noneMatch - se nenhum valor vai dar match com a condição
    // verificamos se a posição não é fixa e se o atual não é nulo
    if (spaces.stream().flatMap(Collection::stream).noneMatch(s -> !s.isFixed() && nonNull(s.getActual()))) {
      return GameStatusEnum.NON_STARTED;
    }

    // agora verificamos se tem pelo menos um correspondente,
    // se tem algum espaço atual não preenchido
    return spaces.stream().flatMap(Collection::stream).anyMatch(s -> isNull(s.getActual()))
            ? GameStatusEnum.INCOMPLETE
            : GameStatusEnum.COMPLETE;
  }

  // verifica se tem erros
  public boolean hasErrors() {
    if (getStatus() == GameStatusEnum.NON_STARTED) {
      return false;
    }

    // procuramos pelo atual que não esteja nulo e
    // que o valor não seja igual ao esperado, pois se tiver tem erro
    return spaces.stream().flatMap(Collection::stream)
            .anyMatch(s -> nonNull(s.getActual()) && !s.getActual().equals(s.getExpected()));
  }

  // mostrar o encapsulamento do acesso a posições da lista
  public boolean changeValue(final int col, final int row, final Integer value) {
    var space = spaces.get(col).get(row);
    if (space.isFixed()) {
      return false;
    }

    space.setActual(value);
    return true;
  }

  public boolean clearValue(final int col, final int row) {
    var space = spaces.get(col).get(row);
    if (space.isFixed()) {
      return false;
    }

    space.clearSpace();
    return true;
  }

  public void reset() {
    spaces.forEach(c -> c.forEach(Space::clearSpace));
  }

  public boolean gameIsFinish() {
    return !hasErrors() && getStatus().equals(GameStatusEnum.COMPLETE);
  }

  public List<List<Space>> getSpaces() {
    return spaces;
  }
}
