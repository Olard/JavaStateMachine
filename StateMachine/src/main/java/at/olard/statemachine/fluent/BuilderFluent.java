package at.olard.statemachine.fluent;

import at.olard.statemachine.Fsm;

/**
 * The most outer scope of the fluent builder API.
 *
 * @param <S> the state type
 * @param <T> the trigger type
 * @param <D> the update-data type
 */
public interface BuilderFluent<S, T, D> {
    /**
     * Enables the stack for the fsm.
     *
     * @return the builder for chaining
     */
    BuilderFluent<S, T, D> enableStack();

    /**
     * Defines a transition from any other state to the given one.
     *
     * @param state the state to transition to
     * @return the builder chain
     */
    GlobalTransitionFluent<S, T, D> globalTransitionTo(S state);

    /**
     * Defines the state with the given identifier.
     *
     * @param state the identifier
     * @return the {@link StateFluent} to chain further configurations
     */
    StateFluent<S, T, D> state(S state);

    /**
     * Finally build the fsm.
     *
     * @return the newly built finite state machine
     */
    Fsm<S, T, D> build();
}
