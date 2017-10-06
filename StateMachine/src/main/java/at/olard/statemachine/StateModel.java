package at.olard.statemachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import at.olard.statemachine.eventarguments.StateChange;
import at.olard.statemachine.eventarguments.Update;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * The model for a state in the finite state machine.
 *
 * @param <S> the state type
 * @param <T> the trigger type
 * @param <D> the update-data type
 */
@RequiredArgsConstructor
public class StateModel<S, T, D> {

    private final List<Consumer<StateChange<S, T, D>>> entered = new ArrayList<>();
    private final List<Consumer<StateChange<S, T, D>>> exited = new ArrayList<>();
    private final List<Consumer<Update<S, T, D>>> updated = new ArrayList<>();

    @Getter private final S identifier;
    @Getter private final Map<S, Transition<S, T, D>> transitions = new HashMap<>();
    @Setter private boolean endState = false;
    @Getter @Setter private boolean clearStack = false;

    /**
     * Adds an enter state event handler.
     *
     * @param enter the enter state event handler
     */
    public void addEntered(Consumer<StateChange<S, T, D>> enter) {
        entered.add(enter);
    }

    /**
     * Tells the state that a transition towards it is happening.
     *
     * @param stateChange the event data for the state change
     */
    public void raiseEntered(StateChange<S, T, D> stateChange) {
        for (Consumer<StateChange<S, T, D>> enter : entered) {
            enter.accept(stateChange);
        }
    }

    /**
     * Adds an exit state event handler.
     *
     * @param exit the exit state event handler
     */
    public void addExtited(Consumer<StateChange<S, T, D>> exit) {
        exited.add(exit);
    }

    /**
     * Tells the state that a transition away from it is happening.
     *
     * @param stateChange the event data for the state change
     */
    public void raiseExited(StateChange<S, T, D> stateChange) {
        for (Consumer<StateChange<S, T, D>> exit : exited) {
            exit.accept(stateChange);
        }
    }

    /**
     * Adds an update event handler.
     *
     * @param update the update event handler
     */
    public void addUpdated(Consumer<Update<S, T, D>> update) {
        updated.add(update);
    }

    /**
     * Tells the state that it is updated.
     *
     * @param data the update-data
     */
    public void raiseUpdated(Update<S, T, D> data) {
        for (Consumer<Update<S, T, D>> update : updated) {
            update.accept(data);
        }
    }
}
