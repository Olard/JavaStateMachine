package at.olard.statemachine.eventarguments;

import javax.annotation.Nullable;

import at.olard.statemachine.Fsm;
import at.olard.statemachine.State;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Event arguments that are used when one state transitions into another
 *
 * @param <S> the state type
 * @param <T> the trigger type
 * @param <D> the update-data type
 */
@RequiredArgsConstructor
@Getter
public class StateChange<S, T, D> {
    private final Fsm<S, T, D> fsm;
    private final State<S, T, D> from;
    private final State<S, T, D> to;
    @Nullable private final T trigger;
}
