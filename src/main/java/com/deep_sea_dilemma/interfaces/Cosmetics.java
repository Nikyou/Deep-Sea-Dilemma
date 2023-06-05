package com.deep_sea_dilemma.interfaces;

public class Cosmetics {

    private static final Game game = Game.Initialize();


    public static void InitializeCosmetic(){
        game.ship.SetImage(game.settings.imageShips[game.settings.selectedCosmetic[0]]);
        game.tile.SetImage(game.settings.imageTiles[game.settings.selectedCosmetic[1]]);
        game.goal.SetImage(game.settings.imageGoals[game.settings.selectedCosmetic[2]]);
        game.vortex.SetImage(game.settings.imageVortices[game.settings.selectedCosmetic[3]]);
        game.rock.SetImage(game.settings.imageRocks[game.settings.selectedCosmetic[4]]);
    }
    public static void SelectCosmetic(int x, int i) {
        game.settings.selectedCosmetic[x] = i;
        switch (x) {
            case 0 -> game.ship.SetImage(game.settings.imageShips[i]);
            case 1 -> game.tile.SetImage(game.settings.imageTiles[i]);
            case 2 -> game.goal.SetImage(game.settings.imageGoals[i]);
            case 3 -> game.vortex.SetImage(game.settings.imageVortices[i]);
            case 4 -> game.rock.SetImage(game.settings.imageRocks[i]);
        }
        game.Save();
    }
    public static boolean IsEnoughGold(int i) {
        return game.settings.gold >= game.settings.cosmeticsPrice[i];
    }
    public static void AddGold(int i){
        game.settings.gold = game.settings.gold + i;
    }
    public static void UnlockCosmetic(int x, int i) {
        game.settings.gold = game.settings.gold - game.settings.cosmeticsPrice[i];
        switch (x) {
            case 0 -> game.settings.unlockedShips[i] = true;
            case 1 -> game.settings.unlockedTiles[i] = true;
            case 2 -> game.settings.unlockedGoals[i] = true;
            case 3 -> game.settings.unlockedVortices[i] = true;
            case 4 -> game.settings.unlockedRocks[i] = true;
        }
        game.Save();
    }
}
