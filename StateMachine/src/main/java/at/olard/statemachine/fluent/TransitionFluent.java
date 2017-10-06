package at.olard.statemachine.fluent;

import java.util.function.Function;

import at.olard.statemachine.eventarguments.Condition;

/**
 * The step in the builder after the transition is created.
 *
 * @param <S> the state type
 * @param <T> the trigger type
 * @param <D> the update-data type
 */
public interface TransitionFluent<S, T, D> {

    /**
     * Adds a trigger to the transition.
     *
     * @param trigger the trigger to be added
     * @return the next step in the builder
     */
    TransitionStateFluent<S, T, D> on(T trigger);

    /**
     * Adds a condition to the transition
     *
     * @param condition the condition to be added
     * @return the next step in the builder
     */
    TransitionStateFluent<S, T, D> ifSatisfies(Function<Condition<S, T>, Boolean> condition);
}
