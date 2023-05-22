package com.deep_sea_dilemma.levels;

public interface LevelsStore {
    char[][][] levelMap = {
            // 0 level
            {
                    {'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T', 'S'},
                    {'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T'},
                    {'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T'},
                    {'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T'},
                    {'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T'},
                    {'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T'},
                    {'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T'},
                    {'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T'},
                    {'T', 'G', 'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T'},
                    {'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T'},
            },
            // 1 level
            {
                    {'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T', 'S'},
                    {'T', 'T', 'T', 'V', 'T', 'T', 'T', 'T', 'T', 'T'},
                    {'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T'},
                    {'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T'},
                    {'R', 'T', 'T', 'T', 'T', 'T', 'R', 'T', 'T', 'T'},
                    {'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T'},
                    {'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T', 'R'},
                    {'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T'},
                    {'T', 'G', 'T', 'V', 'T', 'T', 'T', 'T', 'T', 'T'},
                    {'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T'},
            },
    };
    int [][] levelMapSize = {
            // 0 level
            {10, 10},
            // 1 level
            {10, 10},
    };

    int [] levelObjectSize = {
            // 0 level
            80,
            // 1 level
            80,
    };
    int [] levelShipSpeed = {
            // 0 level
            3,
            // 1 level
            3,
    };
    long [] levelAISeed = {
            // 0 level
            0L,
            // 1 level
            1617962274894074042L,
            // 2 level
            302899975204109730L,
            // 3 level
            6874385687971440541L,
            // 4 level
            3544202141286109506L,
            // 5 level
            1175495985196389534L,
            // 6 level
            3070445099713772221L,
            // 7 level
            2139078146060529145L,
            // 8 level
            9040721391101870082L,
            // 9 level
            7111528997690110898L,
    };
}
