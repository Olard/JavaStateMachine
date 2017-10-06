package at.olard.statemachine.eventarguments;

import at.olard.statemachine.Fsm;
import at.olard.statemachine.State;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Event argument that are used when a state is updated.
 *
 * @param <S> the state type
 * @param <T> the trigger type
 * @param <D> the update-data type
 */
@RequiredArgsConstructor
@Getter
public class Update<S, T, D> {
    private final Fsm<S, T, D> fsm;
    private final State<S, T, D> state;
    private final D data;
}
