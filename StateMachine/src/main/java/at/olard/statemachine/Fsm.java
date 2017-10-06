package at.olard.statemachine;

import static java.util.Objects.requireNonNull;

import javax.annotation.Nullable;

import at.olard.statemachine.eventarguments.StateChange;
import at.olard.statemachine.eventarguments.Update;
import at.olard.statemachine.fluent.BuilderFluent;
import at.olard.statemachine.fluent.FluentImplementation;
import lombok.AllArgsConstructor;

/**
 * A finite state machine.
 *
 * @param <S> the state type
 * @param <T> the trigger type
 * @param <D> the update-data type
 */
@AllArgsConstructor
public class Fsm<S, T, D> {

    protected FsmModel<S, T, D> model = new FsmModel<>();

    /**
     * Creates a builder to construct a new fsm with the fluent API.
     *
     * @param startState the start state identifier
     * @return the {@link BuilderFluent}
     */
    public static <S2, T2, D2> BuilderFluent<S2, T2, D2> builder(S2 startState) {
        return new FluentImplementation<>(startState);
    }

    @SuppressWarnings("javadoc")
    public State<S, T, D> getCurrentState() {
        return requireNonNull(model.getCurrentState(), "Model is not finished initializing: Current state is null.");
    }

    /**
     * Force a transition to the given state.
     *
     * @param state the state to transition to
     */
    public void transitionTo(S state) {
        transitionTo(state, false);
    }

    /**
     * Force a transition to the given state.
     *
     * @param state the state to transition to
     * @param isPop whether to perform a pop on the state stack
     */
    public void transitionTo(S state, boolean isPop) {
        State<S, T, D> wantedState = model.getStates().get(state);
        if (wantedState != null) {
            doTransition(state, null, isPop);
        }
    }

    private void doTransition(S state, @Nullable T input, boolean isPop) {
        State<S, T, D> old = getCurrentState();

        if (model.isStackEnabled() && isPop) {
            model.getStack().pop();
            model.setCurrentState(model.getStack().peek());
        } else {
            model.setCurrentState(model.getStates().get(state));
            if (model.isStackEnabled()) {
                model.getStack().push(getCurrentState());
            }
        }

        if (model.isStackEnabled() && getCurrentState().isClearStack()) {
            model.getStack().clear();
        }

        if (!getCurrentState().equals(old)) {
            StateChange<S, T, D> stateChange = new StateChange<>(this, old, getCurrentState(), input);
            old.raiseExited(stateChange);
            getCurrentState().raiseEntered(stateChange);
            model.raiseEntered(stateChange);
        }
    }

    /**
     * Triggers a transition via a trigger.
     *
     * @param input the trigger input
     */
    public void trigger(@Nullable T input) {
        for (Transition<S, T, D> globalTransition : model.getGlobalTransitions().values()) {
            if (globalTransition.process(getCurrentState(), input)) {
                doTransition(globalTransition.getTarget(), input, globalTransition.isPop());
            }
        }

        Transition<S, T, D> transition = getCurrentState().process(input);

        if (transition != null) {
            doTransition(transition.getTarget(), input, transition.isPop());
        }
    }

    /**
     * Updates the state machine by invoking the update event of the current state.
     *
     * @param updateData the update data
     */
    public void update(D updateData) {
        model.getCurrentState().raiseUpdated(new Update<>(this, getCurrentState(), updateData));
    }
}
