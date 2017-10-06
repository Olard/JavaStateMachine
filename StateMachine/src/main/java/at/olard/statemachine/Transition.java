package at.olard.statemachine;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Nullable;

import at.olard.statemachine.eventarguments.Condition;
import lombok.RequiredArgsConstructor;

/**
 * A transition from one state to another.
 *
 * @param <S> the state type
 * @param <T> the trigger type
 * @param <D> the update-data type
 */
@RequiredArgsConstructor
public class Transition<S, T, D> {
    private final TransitionModel<S, T> model;

    @SuppressWarnings("javadoc")
    public S getTarget() {
        return model.getTarget();
    }

    @SuppressWarnings("javadoc")
    public boolean isPop() {
        return model.isPop();
    }

    @SuppressWarnings("javadoc")
    public Transition(Collection<T> triggers, @Nullable S source, S target) {
        model = new TransitionModel<>(target);
        model.setSource(source);
        model.getTriggers().addAll(triggers);
    }

    /**
     * Adds a trigger for this transition.
     *
     * @param trigger the trigger to add for this transition
     * @exception IllegalStateException if the trigger already exists
     */
    public void add(T trigger) {
        if (model.getTriggers().contains(trigger)) {
            throw new IllegalStateException("Trigger already exists");
        }
        model.getTriggers().add(trigger);
    }

    /**
     * Check if this transition is triggered with the given input.
     *
     * @param from the current state
     * @param input the trigger
     * @return true if the transition is being triggered; false otherwise
     */
    public boolean process(State<S, T, D> from, @Nullable T input) {
        return model.getTriggers().contains(input)
                && model.getConditions().parallelStream()
                        .allMatch(x -> x.apply(new Condition<>(from.getIdentifier(), model.getTarget(), input)));
    }

    @Override
    public String toString() {
        return Objects.toString(model.getSource()) + "-(" + model.getTriggers() + ")-" + Objects.toString(model.getTarget());
    }
}
