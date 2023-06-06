package com.deep_sea_dilemma.objects;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Arrow extends Region {
    private final GridPane parentGridPane;
    private boolean isShown = false;
    private final double cellSize;
    private final double arrowBodySize;

    private final Image arrowBodyImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("arrowBody.png")));
    private final Image arrowHeadImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("arrowHead.png")));
    private List<ImageView> objects = new ArrayList<>();


    public Arrow(GridPane gridPane, double cellSize) {
        parentGridPane = gridPane;
        this.cellSize = cellSize;
        int bodyDivision = 4;
        arrowBodySize = cellSize / bodyDivision;
    }

    public void Clear() {
        isShown = false;
        for (ImageView i : objects){
            parentGridPane.getChildren().remove(i);
        }
        objects = new ArrayList<>();
    }

    public boolean IsShown(){
        return isShown;
    }

    public void Draw(int startX, int startY, int endX, int endY, String orientation) {
        if (startX == endX && startY == endY){
            return;
        }
        isShown = true;


        int lineType = 0;
        if (startY == endY) {
            lineType = 1; //only horizontal
        }
        if (startX == endX) {
            lineType = 2; //only vertical
        }

        switch (orientation){
            case "Top" -> {
                //Triangle pointing down
                ImageView arrowHead = new ImageView(arrowHeadImage);
                arrowHead.setFitWidth(cellSize);
                arrowHead.setFitHeight(cellSize);
                arrowHead.getTransforms().add(new Rotate(180, cellSize / 2, cellSize / 2));
                arrowHead.setMouseTransparent(true);
                objects.add(arrowHead);
                parentGridPane.add(arrowHead, endX, endY);
                switch (lineType){
                    case 0 -> CreateArrowBottomTop(startX, startY, endX, endY, true);
                    case 2 -> {
                        for (int y = startY + 1; y <= endY-1; y++){
                            ImageView arrowBody = new ImageView(arrowBodyImage);
                            arrowBody.setFitWidth(arrowBodySize);
                            arrowBody.setFitHeight(cellSize);
                            arrowBody.setMouseTransparent(true);
                            arrowBody.setTranslateX(arrowBody.getTranslateX() + cellSize / 3);
                            objects.add(arrowBody);
                            parentGridPane.add(arrowBody, startX, y);
                        }
                        ImageView arrowBody = new ImageView(arrowBodyImage);
                        arrowBody.setFitWidth(arrowBodySize);
                        arrowBody.setFitHeight(cellSize / 2);
                        arrowBody.setMouseTransparent(true);
                        arrowBody.setTranslateY(arrowBody.getTranslateY() + arrowBodySize);
                        arrowBody.setTranslateX(arrowBody.getTranslateX() + cellSize / 3);
                        objects.add(arrowBody);
                        parentGridPane.add(arrowBody, startX, startY);
                    }
                }

            }
            case "Right" -> {
                //Triangle pointing left
                ImageView arrowHead = new ImageView(arrowHeadImage);
                arrowHead.setFitWidth(cellSize);
                arrowHead.setFitHeight(cellSize);
                arrowHead.getTransforms().add(new Rotate(270, cellSize / 2, cellSize / 2));
                arrowHead.setMouseTransparent(true);
                objects.add(arrowHead);
                parentGridPane.add(arrowHead, endX, endY);

                switch (lineType){
                    case 0 -> CreateArrowLeftRight(startX, startY, endX, endY, false);
                    case 1 -> {
                        for (int x = endX + 1; x <= startX-1; x++){
                            ImageView arrowBody = new ImageView(arrowBodyImage);
                            arrowBody.setFitWidth(cellSize);
                            arrowBody.setFitHeight(arrowBodySize);
                            arrowBody.setMouseTransparent(true);
                            objects.add(arrowBody);
                            parentGridPane.add(arrowBody, x, startY);
                        }
                        ImageView arrowBody = new ImageView(arrowBodyImage);
                        arrowBody.setFitWidth(cellSize / 2);
                        arrowBody.setFitHeight(arrowBodySize);
                        arrowBody.setMouseTransparent(true);
                        objects.add(arrowBody);
                        parentGridPane.add(arrowBody, startX, startY);
                    }
                }
            }
            case "Bottom" -> {
                //Triangle pointing up
                ImageView arrowHead = new ImageView(arrowHeadImage);
                arrowHead.setFitWidth(cellSize);
                arrowHead.setFitHeight(cellSize);
                arrowHead.setMouseTransparent(true);
                objects.add(arrowHead);
                parentGridPane.add(arrowHead, endX, endY);

                switch (lineType){
                    case 0 -> CreateArrowBottomTop(startX, startY, endX, endY, false);
                    case 2 -> {
                        for (int y = endY + 1; y <= startY-1; y++){
                            ImageView arrowBody = new ImageView(arrowBodyImage);
                            arrowBody.setFitWidth(arrowBodySize);
                            arrowBody.setFitHeight(cellSize);
                            arrowBody.setMouseTransparent(true);
                            arrowBody.setTranslateX(arrowBody.getTranslateX() + cellSize / 3);
                            objects.add(arrowBody);
                            parentGridPane.add(arrowBody, startX, y);
                        }
                        ImageView arrowBody = new ImageView(arrowBodyImage);
                        arrowBody.setFitWidth(arrowBodySize);
                        arrowBody.setFitHeight(cellSize / 2);
                        arrowBody.setMouseTransparent(true);
                        arrowBody.setTranslateY(arrowBody.getTranslateY() - arrowBodySize);
                        arrowBody.setTranslateX(arrowBody.getTranslateX() + cellSize / 3);
                        objects.add(arrowBody);
                        parentGridPane.add(arrowBody, startX, startY);
                    }
                }
            }
            case "Left" -> {
                ImageView arrowHead = new ImageView(arrowHeadImage);
                arrowHead.setFitWidth(cellSize);
                arrowHead.setFitHeight(cellSize);
                arrowHead.getTransforms().add(new Rotate(90, cellSize / 2, cellSize / 2));
                arrowHead.setMouseTransparent(true);
                objects.add(arrowHead);
                parentGridPane.add(arrowHead, endX, endY);

                switch (lineType){
                    case 0 -> CreateArrowLeftRight(startX, startY, endX, endY, true);
                    case 1 -> {
                        for (int x = startX + 1; x <= endX-1; x++){
                            ImageView arrowBody = new ImageView(arrowBodyImage);
                            arrowBody.setFitWidth(cellSize);
                            arrowBody.setFitHeight(arrowBodySize);
                            arrowBody.setMouseTransparent(true);
                            objects.add(arrowBody);
                            parentGridPane.add(arrowBody, x, startY);
                        }
                        ImageView arrowBody = new ImageView(arrowBodyImage);
                        arrowBody.setFitWidth(cellSize / 2);
                        arrowBody.setFitHeight(arrowBodySize);
                        arrowBody.setMouseTransparent(true);
                        arrowBody.setTranslateX(arrowBody.getTranslateX() + cellSize / 2);
                        objects.add(arrowBody);
                        parentGridPane.add(arrowBody, startX, startY);
                    }
                }
            }
        }
    }

    private void CreateArrowBottomTop (int startX, int startY, int endX, int endY, boolean isTop){
        boolean toRight = false;
        int tempSX, tempSY, tempEX, tempEY;

        if (startX < endX){
            tempSX = startX;
            tempEX = endX;
            toRight = true; //Going Right
        }else {
            tempSX = endX;
            tempEX = startX;
        }

        if (startY < endY){
            tempSY = startY;
            tempEY = endY;
        }else {
            tempSY = endY;
            tempEY = startY;
        }

        for (int y = tempSY + 1; y <= tempEY-1; y++){
            ImageView arrowBody = new ImageView(arrowBodyImage);
            arrowBody.setFitWidth(arrowBodySize);
            arrowBody.setFitHeight(cellSize);
            arrowBody.setMouseTransparent(true);
            arrowBody.setTranslateX(arrowBody.getTranslateX() + cellSize / 3);
            objects.add(arrowBody);
            parentGridPane.add(arrowBody, endX, y);
        }

        for (int x = tempSX + 1; x <= tempEX-1; x++){
            ImageView arrowBody = new ImageView(arrowBodyImage);
            arrowBody.setFitWidth(cellSize);
            arrowBody.setFitHeight(arrowBodySize);
            arrowBody.setMouseTransparent(true);
            objects.add(arrowBody);
            parentGridPane.add(arrowBody, x, startY);
        }

        ImageView arrowBody = new ImageView(arrowBodyImage);
        arrowBody.setFitWidth(arrowBodySize);
        arrowBody.setFitHeight(cellSize / 2);
        arrowBody.setMouseTransparent(true);
        arrowBody.setTranslateX(arrowBody.getTranslateX() + cellSize / 3);
        if (isTop){
            arrowBody.setTranslateY(arrowBody.getTranslateY() + cellSize / 4);
        } else {
            arrowBody.setTranslateY(arrowBody.getTranslateY() - cellSize / 4);
        }
        objects.add(arrowBody);
        parentGridPane.add(arrowBody, endX, startY);

        arrowBody = new ImageView(arrowBodyImage);
        arrowBody.setFitWidth(cellSize / 1.5);
        arrowBody.setFitHeight(arrowBodySize);
        arrowBody.setMouseTransparent(true);
        if (!toRight){
            arrowBody.setTranslateX(arrowBody.getTranslateX() + cellSize / 2.5);
        }
        objects.add(arrowBody);
        parentGridPane.add(arrowBody, endX, startY);

        arrowBody = new ImageView(arrowBodyImage);
        arrowBody.setFitWidth(cellSize / 2);
        arrowBody.setFitHeight(arrowBodySize);
        arrowBody.setMouseTransparent(true);
        if (toRight){
            arrowBody.setTranslateX(arrowBody.getTranslateX() + cellSize / 2);
        }
        objects.add(arrowBody);
        parentGridPane.add(arrowBody, startX, startY);
    }

    private void CreateArrowLeftRight (int startX, int startY, int endX, int endY, boolean isLeft){
        boolean toTop = false;
        int tempSX, tempSY, tempEX, tempEY;

        if (startX < endX){
            tempSX = startX;
            tempEX = endX;
        }else {
            tempSX = endX;
            tempEX = startX;
        }

        if (startY < endY){

            tempSY = startY;
            tempEY = endY;
        }else {
            toTop = true; //Going Top
            tempSY = endY;
            tempEY = startY;
        }

        for (int y = tempSY + 1; y <= tempEY-1; y++){
            ImageView arrowBody = new ImageView(arrowBodyImage);
            arrowBody.setFitWidth(arrowBodySize);
            arrowBody.setFitHeight(cellSize);
            arrowBody.setMouseTransparent(true);
            arrowBody.setTranslateX(arrowBody.getTranslateX() + cellSize / 3);
            objects.add(arrowBody);
            parentGridPane.add(arrowBody, startX, y);
        }

        for (int x = tempSX + 1; x <= tempEX-1; x++){
            ImageView arrowBody = new ImageView(arrowBodyImage);
            arrowBody.setFitWidth(cellSize);
            arrowBody.setFitHeight(arrowBodySize);
            arrowBody.setMouseTransparent(true);
            objects.add(arrowBody);
            parentGridPane.add(arrowBody, x, endY);
        }

        ImageView arrowBody = new ImageView(arrowBodyImage);
        arrowBody.setFitWidth(arrowBodySize);
        arrowBody.setFitHeight(cellSize / 2);
        arrowBody.setMouseTransparent(true);
        arrowBody.setTranslateX(arrowBody.getTranslateX() + cellSize / 3);
        if (toTop){
            arrowBody.setTranslateY(arrowBody.getTranslateY() + cellSize / 4);
        } else {
            arrowBody.setTranslateY(arrowBody.getTranslateY() - cellSize / 4);
        }
        objects.add(arrowBody);
        parentGridPane.add(arrowBody, startX, endY);

        arrowBody = new ImageView(arrowBodyImage);
        arrowBody.setFitWidth(cellSize / 1.5);
        arrowBody.setFitHeight(arrowBodySize);
        arrowBody.setMouseTransparent(true);
        if (isLeft){
            arrowBody.setTranslateX(arrowBody.getTranslateX() + cellSize / 2.5);
        }
        objects.add(arrowBody);
        parentGridPane.add(arrowBody, startX, endY);

        arrowBody = new ImageView(arrowBodyImage);
        arrowBody.setFitWidth(arrowBodySize);
        arrowBody.setFitHeight(cellSize / 2);
        arrowBody.setMouseTransparent(true);
        if (toTop){
            arrowBody.setTranslateY(arrowBody.getTranslateY() - arrowBodySize);
        } else {
            arrowBody.setTranslateY(arrowBody.getTranslateY() + arrowBodySize);
        }
        arrowBody.setTranslateX(arrowBody.getTranslateX() + cellSize / 3);
        objects.add(arrowBody);
        parentGridPane.add(arrowBody, startX, startY);
    }
}