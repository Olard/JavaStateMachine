package at.olard.statemachine.eventarguments;

import javax.annotation.Nullable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Event arguments for a condition that is checked when a transition should happen.
 *
 * @param <S> the state type
 * @param <T> the trigger type
 */
@RequiredArgsConstructor
@Getter
public class Condition<S, T> {

    private final S source;
    private final S target;
    @Nullable private final T trigger;
}
