package at.olard.statemachine;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import lombok.RequiredArgsConstructor;

/**
 * Tests the fluent API.
 */
@SuppressWarnings("javadoc")
public class FluentTest {
    private enum State {
        IDLE, OVER, PRESSED, REFRESHING
    }

    private enum Trigger {
        MOUSE_CLICKED, MOUSE_RELEASED, MOUSE_OVER, MOUSE_LEAVE
    }

    @RequiredArgsConstructor
    private class Button {
        private boolean isActivated = false;
        private State buttonState = State.IDLE;
        private State oldState = State.IDLE;
        private float refreshTimer = 1f;
        private int updateCounter;
    }

    @Test
    public void whenStateChangesOnEnterAndOnExitHooksShouldTrigger() {
        Button button = new Button();

        Fsm<State, Trigger, Float> fsm = // 
                Fsm.<State, Trigger, Float> builder(State.IDLE) //
                   //
                   .state(State.IDLE) //
                   .transitionTo(State.OVER).on(Trigger.MOUSE_OVER) //
                   .onEnter(t -> button.buttonState = State.IDLE) //
                   .onExit(t -> button.oldState = button.buttonState)
                   //
                   .state(State.OVER) //
                   .transitionTo(State.IDLE).on(Trigger.MOUSE_LEAVE) //
                   .transitionTo(State.PRESSED).on(Trigger.MOUSE_CLICKED) //
                   .onEnter(t -> button.buttonState = State.OVER) //
                   .onExit(t -> button.oldState = button.buttonState) //
                   .update(t -> button.updateCounter++)
                   //
                   .state(State.PRESSED) //
                   .transitionTo(State.IDLE).on(Trigger.MOUSE_LEAVE) //
                   .transitionTo(State.REFRESHING).on(Trigger.MOUSE_RELEASED).ifSatisfies(t -> button.isActivated) //
                   .onEnter(t -> button.buttonState = State.PRESSED) //
                   .onExit(t -> button.oldState = button.buttonState) //
                   //
                   .state(State.REFRESHING) //
                   .onEnter(t -> button.buttonState = State.REFRESHING) //
                   .onExit(t -> button.oldState = button.buttonState) //
                   .update(args -> {
                       button.refreshTimer -= args.getData();
                       if (button.refreshTimer <= 0.0f) {
                           button.refreshTimer = 0.0f;
                           args.getFsm().transitionTo(State.OVER);
                       }
                       button.updateCounter++;
                   })
                   //
                   .build();

        fsm.update(2f); // Should do nothing.
        assertThat(fsm.getCurrentState().getIdentifier()).isEqualTo(State.IDLE);
        fsm.trigger(Trigger.MOUSE_CLICKED);
        assertThat(fsm.getCurrentState().getIdentifier()).isEqualTo(State.IDLE);
        fsm.trigger(Trigger.MOUSE_LEAVE);
        assertThat(fsm.getCurrentState().getIdentifier()).isEqualTo(State.IDLE);
        fsm.trigger(Trigger.MOUSE_RELEASED);
        assertThat(fsm.getCurrentState().getIdentifier()).isEqualTo(State.IDLE);
        fsm.trigger(Trigger.MOUSE_OVER);
        assertThat(fsm.getCurrentState().getIdentifier()).isEqualTo(State.OVER);
        assertThat(button.buttonState).isEqualTo(State.OVER);
        assertThat(button.oldState).isEqualTo(State.IDLE);
        fsm.trigger(Trigger.MOUSE_CLICKED);
        assertThat(fsm.getCurrentState().getIdentifier()).isEqualTo(State.PRESSED);
        assertThat(button.buttonState).isEqualTo(State.PRESSED);
        assertThat(button.oldState).isEqualTo(State.OVER);
        fsm.trigger(Trigger.MOUSE_RELEASED); // Button is deactivated.
        assertThat(fsm.getCurrentState().getIdentifier()).isEqualTo(State.PRESSED);
        assertThat(button.buttonState).isEqualTo(State.PRESSED);
        assertThat(button.oldState).isEqualTo(State.OVER);
        button.isActivated = true;
        fsm.trigger(Trigger.MOUSE_RELEASED); // Now it's activated.
        assertThat(fsm.getCurrentState().getIdentifier()).isEqualTo(State.REFRESHING);
        assertThat(button.buttonState).isEqualTo(State.REFRESHING);
        assertThat(button.oldState).isEqualTo(State.PRESSED);
        fsm.update(.5F); // No transition yet...
        assertThat(fsm.getCurrentState().getIdentifier()).isEqualTo(State.REFRESHING);
        assertThat(button.buttonState).isEqualTo(State.REFRESHING);
        assertThat(button.oldState).isEqualTo(State.PRESSED);
        fsm.update(.5F); // But now.
        assertThat(fsm.getCurrentState().getIdentifier()).isEqualTo(State.OVER);
        assertThat(button.buttonState).isEqualTo(State.OVER);
        assertThat(button.oldState).isEqualTo(State.REFRESHING);

        // update was triggered twice over all states.
        assertThat(button.updateCounter).isEqualTo(2);
    }
}
