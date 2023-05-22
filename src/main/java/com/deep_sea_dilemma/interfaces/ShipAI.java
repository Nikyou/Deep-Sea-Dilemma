package com.deep_sea_dilemma.interfaces;

public interface ShipAI {
    default void AIMakeTurn(int difficulty) {
        switch (difficulty) {
            case 1 -> AIMakeTurnEasy();
            case 2 -> AIMakeTurnNormal();
            case 3 -> AIMakeTurnHard();
        }
    }

    void AIMakeTurnEasy();
    void AIMakeTurnNormal();
    void AIMakeTurnHard();
}
