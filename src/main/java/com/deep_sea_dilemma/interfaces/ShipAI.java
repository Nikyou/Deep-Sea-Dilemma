package com.deep_sea_dilemma.interfaces;

import java.util.List;

public interface ShipAI {
    void SetRandom(long seed);
    void SetGenerator();
    void AIMakeTurn(int difficulty);
    void AIMakeTurnEasy(List<int[]> allTurns);
    void AIMakeTurnNormal(List<int[]> allTurns);
    void AIMakeTurnHard(List<int[]> allTurns);
}
