package om.self.ezftc.other.generator;

import com.qualcomm.robotcore.hardware.Gamepad;
import om.self.beans.core.Autowired;
import om.self.supplier.suppliers.LinkedSupplier;
import om.self.task.core.EventManager;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class Generator<T, E> {
    private T link;
    private final List<T> unlinked = new LinkedList<>();
    private final BiConsumer<T,E> linkFunction;

    public Generator(BiConsumer<T, E> linkFunction) {
        this.linkFunction = linkFunction;
    }

    public void construct(T link, EventManager eventManager){
        //set opMode
        this.link = link;
        //load controls on init
        eventManager.attachToEvent(EventManager.CommonTrigger.INIT, this::load);
    }

    private void load(){
        //set all gamepads
        for (Map.Entry<LinkedSupplier<Gamepad, ?>, Boolean> entry: unlinkedSuppliers) {
            if(entry.getValue()) entry.getKey().setInput(opMode.gamepad2);
            else entry.getKey().setInput(opMode.gamepad1);
        }
        //clean up in case there is another ControlGenerator
        unlinkedSuppliers.clear();
    }
}
