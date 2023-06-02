package com.deep_sea_dilemma.interfaces;

import java.util.List;

public interface ShipAI {
    void SetRandom(long seed);
    void SetGenerator();
    int[] AIMakeTurn(int difficulty);
    int[] AIMakeTurnEasy(List<int[]> allTurns);
    int[] AIMakeTurnNormal(List<int[]> allTurns);
    int[] AIMakeTurnHard(List<int[]> allTurns);
}
