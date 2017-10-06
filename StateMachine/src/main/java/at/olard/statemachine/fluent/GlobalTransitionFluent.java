package at.olard.statemachine.fluent;

import java.util.function.Function;

import at.olard.statemachine.eventarguments.Condition;

/**
 * Builder pattern after a global transition has been created.
 *
 * @param <S> the state type
 * @param <T> the trigger type
 * @param <D> the update-data type
 */
public interface GlobalTransitionFluent<S, T, D> {

    /**
     * Adds a trigger to the transition.
     *
     * @param trigger the trigger for the global transition
     * @return the next scope in the builder pattern
     */
    GlobalTransitionBuilderFluent<S, T, D> onGlobal(T trigger);

    /**
     * Adds a condition to the transition.
     *
     * @param condition the condition which is required to be true for the transition to be triggered
     * @return the next scope in the builder pattern
     */
    GlobalTransitionBuilderFluent<S, T, D> ifGlobal(Function<Condition<S, T>, Boolean> condition);
}
