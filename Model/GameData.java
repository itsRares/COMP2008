package com.bignerdranch.android.tradesexplorer20.Model;

import com.bignerdranch.android.tradesexplorer20.Model.Area;

import java.util.List;

public class GameData {
    private Area grid[][];
    private Player mPlayer;
    private GameData mGameData;

    public GameData(List<Area> map, int size) {
        this.grid = reCreateMap(map, size);
    }

    public GameData(int size) {
        this.grid = createMap(size);
    }

    /* ==== GETTERS ==== */

    public Area[][] getGrid() {
        return grid;
    }

    /* ==== SETTERS ==== */

    public void setGrid(Area[][] grid) {
        this.grid = grid;
    }

    public Area[][] createMap(int size) {
        Area[][] grid = new Area[size][size];
        for(int ii = 0; ii < size; ii++) {
            for(int jj = 0; jj < size; jj++) {
                grid[ii][jj] = new Area(ii, jj);
            }
        }
        return grid;
    }

    public Area[][] reCreateMap(List<Area> map, int size) {
        Area[][] grid = new Area[size][size];
        for (Area a : map) {
            for(int ii = 0; ii < size; ii++) {
                for(int jj = 0; jj < size; jj++) {
                    if (a.getyValue() == jj && a.getxValue() == ii) {
                        grid[ii][jj] = a;
                    }
                }
            }
        }
        return grid;
    }

}
