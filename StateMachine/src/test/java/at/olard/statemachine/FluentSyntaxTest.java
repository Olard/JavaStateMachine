package at.olard.statemachine;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import at.olard.statemachine.fluent.BuilderFluent;

@SuppressWarnings("javadoc")
public class FluentSyntaxTest {

    static class Spell {}

    static class Timer {
        public void start() {}

        public float value;

        public void stopAndReset() {}
    }

    static class Hero {
        public void doSpell(Spell spell) {}
    }

    static class Button {
        public enum ButtonKind {
            FLIPBACK
        }

        public enum ButtonState {
            IDLE, OVER, DOWN, REFRESHING
        }

        public ButtonState state = ButtonState.IDLE;
        public ButtonKind kind = ButtonKind.FLIPBACK;
        public Timer refreshTimer = new Timer();

        public Spell DoAssociatedSpell() {
            //Do something useful.
            return new Spell();
        }
    }

    private enum State {
        IDLE, OVER, PRESSED, REFRESHING
    };

    private enum Trigger {
        MOUSE_CLICKED, MOUSE_RELEASED, MOUSE_OVER, MOUSE_LEAVE
    };

    private final Map<Button, Fsm<State, Trigger, Float>> buttonMachines = new HashMap<>();

    @Test
    public void syntax() {
        Hero hero = new Hero();
        createMachineFor(Fsm.builder(State.IDLE), new Button(), hero);
        createMachineFor(Fsm.builder(State.IDLE), new Button(), hero);
    }

    private void createMachineFor(BuilderFluent<State, Trigger, Float> builder, Button button, Hero hero) {
        buttonMachines.put(button, builder.state(State.IDLE).transitionTo(State.OVER).on(Trigger.MOUSE_OVER).onEnter(t -> {
            button.state = Button.ButtonState.IDLE;
        }).state(State.OVER).transitionTo(State.IDLE).on(Trigger.MOUSE_LEAVE).transitionTo(State.PRESSED).on(Trigger.MOUSE_CLICKED)
                                          .onEnter(t -> {
                                              button.state = Button.ButtonState.OVER;
                                          }).state(State.PRESSED).transitionTo(State.IDLE).on(Trigger.MOUSE_LEAVE)
                                          .ifSatisfies(a -> button.kind == Button.ButtonKind.FLIPBACK).transitionTo(State.REFRESHING)
                                          .on(Trigger.MOUSE_RELEASED).onEnter(t -> {
                                              button.state = Button.ButtonState.DOWN;
                                          }).state(State.REFRESHING).onEnter(t -> {
                                              hero.doSpell(button.DoAssociatedSpell());
                                              button.refreshTimer.start();
                                              button.state = Button.ButtonState.REFRESHING;
                                          }).update(args -> {
                                              if (button.refreshTimer.value <= 0F) {
                                                  button.refreshTimer.stopAndReset();
                                                  args.getFsm().transitionTo(State.IDLE);
                                              }
                                          }).build());
    }
}
