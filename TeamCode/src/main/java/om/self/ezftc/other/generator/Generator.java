package om.self.ezftc.other.generator;

import om.self.task.core.EventManager;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

public class Generator<LINK, E> {
    private LINK link;
    private final List<E> unlinked = new LinkedList<>();
    private final BiConsumer<LINK,E> linkFunction;

    public Generator(BiConsumer<LINK, E> linkFunction) {
        this.linkFunction = linkFunction;
    }

    void add(E unlinkedObj){
        unlinked.add(unlinkedObj);
    }

    void init(LINK link, EventManager eventManager){
        //set opMode
        this.link = link;
        //load controls on init
        eventManager.attachToEvent(EventManager.CommonTrigger.INIT, this::load);
    }

    private void load(){
        //set all gamepads
        for (E entry: unlinked) {
            linkFunction.accept(link, entry);
        }
        //clean up in case there is another call
        unlinked.clear();
    }
}
