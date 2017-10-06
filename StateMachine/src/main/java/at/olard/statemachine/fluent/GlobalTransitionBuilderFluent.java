package at.olard.statemachine.fluent;

/**
 * The scope of the builder pattern where a global transition is currently created.
 *
 * @param <S> the state type
 * @param <T> the trigger type
 * @param <D> the update-data type
 */
public interface GlobalTransitionBuilderFluent<S, T, D> extends GlobalTransitionFluent<S, T, D>, BuilderFluent<S, T, D> {

}
