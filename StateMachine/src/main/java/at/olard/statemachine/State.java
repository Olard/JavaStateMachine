package at.olard.statemachine;

import javax.annotation.Nullable;

import at.olard.statemachine.eventarguments.StateChange;
import at.olard.statemachine.eventarguments.Update;
import lombok.RequiredArgsConstructor;

/**
 * A state of the fsm.
 *
 * @param <S> the state type
 * @param <T> the trigger type
 * @param <D> the update-data type
 */
@RequiredArgsConstructor
public class State<S, T, D> {
    private final StateModel<S, T, D> model;

    @SuppressWarnings("javadoc")
    public S getIdentifier() {
        return model.getIdentifier();
    }

    /**
     * @see StateModel#isClearStack()
     * @return whether to clear the stack when transitioning to this state
     */
    public boolean isClearStack() {
        return model.isClearStack();
    }

    /**
     * Checks if the input triggers any transition and returns it if that's the case.
     *
     * @param input the trigger
     * @return null if no transition is triggered, the transition otherwise
     */
    @Nullable
    public Transition<S, T, D> process(@Nullable T input) {
        return model.getTransitions().values().stream().filter(t -> t.process(this, input)).findFirst().orElse(null);
    }

    @Override
    @Nullable
    public String toString() {
        return model.getIdentifier().toString();
    }

    /**
     * @see StateModel#raiseExited
     */
    public void raiseExited(@SuppressWarnings("javadoc") StateChange<S, T, D> stateChange) {
        model.raiseExited(stateChange);
    }

    /**
     * @see StateModel#raiseEntered
     */
    public void raiseEntered(@SuppressWarnings("javadoc") StateChange<S, T, D> stateChange) {
        model.raiseEntered(stateChange);
    }

    /**
     * @see StateModel#raiseUpdated
     */
    public void raiseUpdated(@SuppressWarnings("javadoc") Update<S, T, D> update) {
        model.raiseUpdated(update);
    }
}
