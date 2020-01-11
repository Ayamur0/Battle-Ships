package com.battleships.logic.AI;

import org.joml.Vector2i;

public interface Pattern {

    Vector2i nextIndex();

    Vector2i firstIndex();
}
