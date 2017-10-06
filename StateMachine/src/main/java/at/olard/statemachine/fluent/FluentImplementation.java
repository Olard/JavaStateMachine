package at.olard.statemachine.fluent;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.Nullable;

import at.olard.statemachine.Fsm;
import at.olard.statemachine.FsmModel;
import at.olard.statemachine.State;
import at.olard.statemachine.StateModel;
import at.olard.statemachine.Transition;
import at.olard.statemachine.TransitionModel;
import at.olard.statemachine.eventarguments.Condition;
import at.olard.statemachine.eventarguments.StateChange;
import at.olard.statemachine.eventarguments.Update;
import lombok.RequiredArgsConstructor;

/**
 * A nice API to build a fsm.
 *
 * @param <S> the state type
 * @param <T> the trigger type
 * @param <D> the update-data type
 */
@RequiredArgsConstructor
public class FluentImplementation<S, T, D> implements GlobalTransitionBuilderFluent<S, T, D>, TransitionStateFluent<S, T, D> {
    private final FsmModel<S, T, D> model = new FsmModel<>();
    private final S startState;

    @Nullable private S currentState;
    @Nullable private TransitionKey currentTransition;
    @Nullable private S currentGlobalTransition;

    private final Map<S, StateModel<S, T, D>> stateModels = new HashMap<>();
    private final Map<TransitionKey, TransitionModel<S, T>> transitionModels = new HashMap<>();
    private final Map<S, TransitionModel<S, T>> globalTransitionModels = new HashMap<>();

    @Override
    public GlobalTransitionBuilderFluent<S, T, D> onGlobal(T trigger) {
        globalTransitionModels.get(currentGlobalTransition).getTriggers().add(trigger);
        return this;
    }

    @Override
    public GlobalTransitionBuilderFluent<S, T, D> ifGlobal(Function<Condition<S, T>, Boolean> condition) {
        globalTransitionModels.get(currentGlobalTransition).getConditions().add(condition);
        return this;
    }

    @Override
    public BuilderFluent<S, T, D> enableStack() {
        model.setStackEnabled(true);
        return this;
    }

    @Override
    public GlobalTransitionFluent<S, T, D> globalTransitionTo(S state) {
        currentGlobalTransition = state;
        if (globalTransitionModels.get(state) == null) {
            TransitionModel<S, T> transition = new TransitionModel<>(state);
            globalTransitionModels.put(state, transition);
            model.getGlobalTransitions().put(state, new Transition<>(transition));
        }
        return this;
    }

    @Override
    public StateFluent<S, T, D> state(S state) {
        currentState = state;
        if (model.getStates().get(currentState) == null) {
            StateModel<S, T, D> stateModel = new StateModel<>(state);
            stateModels.put(state, stateModel);
            model.getStates().put(state, new State<>(stateModel));
        }
        return this;
    }

    @Override
    public Fsm<S, T, D> build() {
        State<S, T, D> start = model.getStates().get(startState);
        if (start == null) {
            throw new IllegalStateException("Start state has not been configured.");
        }
        model.setCurrentState(start);
        return new Fsm<>(model);
    }

    @Override
    public TransitionStateFluent<S, T, D> on(T trigger) {
        transitionModels.get(currentTransition).getTriggers().add(trigger);
        return this;
    }

    @Override
    public TransitionStateFluent<S, T, D> ifSatisfies(Function<Condition<S, T>, Boolean> condition) {
        transitionModels.get(currentTransition).getConditions().add(condition);
        return this;
    }

    @Override
    public TransitionFluent<S, T, D> transitionTo(S state) {
        currentTransition = transitionKey(currentState, state);
        if (transitionModels.get(currentTransition) == null) {
            TransitionModel<S, T> transitionModel = new TransitionModel<>(state);
            transitionModels.put(currentTransition, transitionModel);
            stateModels.get(currentState).getTransitions().put(state, new Transition<>(transitionModel));
        }
        return this;
    }

    @Override
    public TransitionFluent<S, T, D> popTransition() {
        transitionTo(startState);
        transitionModels.get(currentTransition).setPop(true);
        return this;
    }

    @Override
    public StateFluent<S, T, D> onEnter(Consumer<StateChange<S, T, D>> enter) {
        stateModels.get(currentState).addEntered(enter);
        return this;
    }

    @Override
    public StateFluent<S, T, D> onExit(Consumer<StateChange<S, T, D>> exit) {
        stateModels.get(currentState).addExtited(exit);
        return this;
    }

    @Override
    public StateFluent<S, T, D> update(Consumer<Update<S, T, D>> update) {
        stateModels.get(currentState).addUpdated(update);
        return this;
    }

    @Override
    public StateFluent<S, T, D> clearsStack() {
        stateModels.get(currentState).setClearStack(true);
        return this;
    }

    private TransitionKey transitionKey(@Nullable S from, S to) {
        return new TransitionKey(from, to);
    }

    private class TransitionKey extends SimpleImmutableEntry<S, S> {
        private static final long serialVersionUID = -1185059071410823631L;

        private TransitionKey(@Nullable S key, S value) {
            super(key, value);
        }
    }
}
