package edu.sudoku.serivce;

import java.util.*;

import static edu.sudoku.serivce.EventEnum.CLEAR_SPACE;

public class NotifierService {

  private final Map<EventEnum, List<EventListener>> listeners = new HashMap<>(){{
    put(CLEAR_SPACE, new ArrayList<>());
  }};

  public void subscribe(final EventEnum eventType, EventListener listener){
    var selectedListeners = listeners.get(eventType);
    selectedListeners.add(listener);
  }

  public void notify(final EventEnum eventType){
    listeners.get(eventType).forEach(l -> l.update(eventType));
  }
}
