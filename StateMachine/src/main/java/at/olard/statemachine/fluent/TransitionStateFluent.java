package at.olard.statemachine.fluent;

/**
 * The scope of the builder pattern after the transition is created and defined.
 *
 * @param <S> the state type
 * @param <T> the trigger type
 * @param <D> the update-data type
 */
public interface TransitionStateFluent<S, T, D> extends TransitionFluent<S, T, D>, StateFluent<S, T, D> {

}
