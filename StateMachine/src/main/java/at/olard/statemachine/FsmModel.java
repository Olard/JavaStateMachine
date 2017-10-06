package at.olard.statemachine;

import static at.olard.statemachine.NullUtils.promiseNonNull;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import at.olard.statemachine.eventarguments.StateChange;
import lombok.Getter;
import lombok.Setter;

/**
 * The data model for a fsm.
 *
 * @param <S> the state type
 * @param <T> the trigger type
 * @param <D> the update-data type
 */

public class FsmModel<S, T, D> {
    @Getter @Setter private State<S, T, D> currentState = promiseNonNull();
    @Getter @Setter private boolean stackEnabled;

    @Getter private final Deque<State<S, T, D>> stack = new LinkedList<>();

    @Getter private final Map<S, State<S, T, D>> states = new HashMap<>();
    @Getter private final Map<S, Transition<S, T, D>> globalTransitions = new HashMap<>();

    private final List<Consumer<StateChange<S, T, D>>> stateChangeHandlers = new ArrayList<>();

    /**
     * Adds a state change event handler.
     *
     * @param stateChangeHandler the state change event handler
     */
    public void addEntered(Consumer<StateChange<S, T, D>> stateChangeHandler) {
        stateChangeHandlers.add(stateChangeHandler);
    }

    /**
     * Tells the fsm that a state change is happening.
     *
     * @param stateChangeArgs the event data for the state change
     */
    public void raiseEntered(StateChange<S, T, D> stateChangeArgs) {
        for (Consumer<StateChange<S, T, D>> stateChangeHandler : stateChangeHandlers) {
            stateChangeHandler.accept(stateChangeArgs);
        }
    }
}
