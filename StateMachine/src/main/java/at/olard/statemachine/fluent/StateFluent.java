package at.olard.statemachine.fluent;

import java.util.function.Consumer;

import at.olard.statemachine.eventarguments.StateChange;
import at.olard.statemachine.eventarguments.Update;

/**
 * Describes a state in the fluent API.
 *
 * @param <S> the state type
 * @param <T> the trigger type
 * @param <D> the update-data type
 */
public interface StateFluent<S, T, D> extends BuilderFluent<S, T, D> {
    /**
     * Creates a transition for the current state.
     *
     * @param state the state to be transitioned to
     * @return the next step in the fluent API
     */
    TransitionFluent<S, T, D> transitionTo(S state);

    /**
     * Adding a transition that, being triggered, will result in the last state on the stack being popped and set to be the current one.
     *
     * @return the next step in the fluent API
     */
    TransitionFluent<S, T, D> popTransition();

    /**
     * Adds a event handler for entering the current state
     *
     * @param enter the handler for the event
     * @return the next step in the fluent API
     */
    StateFluent<S, T, D> onEnter(Consumer<StateChange<S, T, D>> enter);

    /**
     * Adds a event handler for exiting the current state
     *
     * @param exit the handler for the event
     * @return the next step in the fluent API
     */
    StateFluent<S, T, D> onExit(Consumer<StateChange<S, T, D>> exit);

    /**
     * Adds a event handler for updating the current state
     *
     * @param update the handler for the event
     * @return the next step in the fluent API
     */
    StateFluent<S, T, D> update(Consumer<Update<S, T, D>> update);

    /**
     * Sets the current state as clearing the stack of the fsm.
     *
     * @return the next step in the fluent API
     */
    StateFluent<S, T, D> clearsStack();
}
