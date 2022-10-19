package om.self.ezftc.other;

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

    public void add(E unlinkedObj){
        unlinked.add(unlinkedObj);
    }

    public void load(LINK link){
        //set all unliked things
        for (E entry: unlinked) {
            linkFunction.accept(link, entry);
        }
        //clean up in case there is another call
        unlinked.clear();
    }
}
