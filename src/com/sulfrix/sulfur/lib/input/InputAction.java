package com.sulfrix.sulfur.lib.input;

import java.util.ArrayList;

public class InputAction {
    public String name;
    public ArrayList<Binding> bindings = new ArrayList<>();

    public InputAction(String name) {
        this.name = name;
    }

    public InputAction(String name, Binding defaultBinding) {
        this(name);
        addBinding(defaultBinding);
    }

    public InputAction addBinding(Binding binding) {
        bindings.add(binding);
        return this;
    }

    public boolean isPressed() {
        for (Binding b : bindings) {
            if (b.pressed()) {
                return true;
            }
        }
        return false;
    }

    public interface Binding {
        public boolean pressed();
    }
}
