package at.olard.statemachine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import javax.annotation.Nullable;

import at.olard.statemachine.eventarguments.Condition;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * The data model for transitions.
 *
 * @param <S> the state type
 * @param <T> the trigger type
 */
@Getter
@Setter
@RequiredArgsConstructor
public class TransitionModel<S, T> {

    private final Set<T> triggers = new HashSet<>();
    private final List<Function<Condition<S, T>, Boolean>> conditions = new ArrayList<>();
    private final S target;

    @Nullable private S source;
    private boolean pop = false;
}
