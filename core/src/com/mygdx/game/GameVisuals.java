package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.awt.*;
import java.util.ArrayList;


/*
Break down into 3 sections:
    - Top bar containing relevant game information such as the ability to see deck and game state stats like gold and turn
    - Node map containing an image of all the nodes as well as the ability to click on nodes in order to traverse - button
    - Background
 */
public class GameVisuals extends ApplicationAdapter {
    private Stage mainStage; //stage for the majority of the game - pauses when encounter pop ups
    private GameController game; //actual game controller
    private Label.LabelStyle resourceStyle; //labelstyle for all labels
    private TextButton[] nodeButtons; //map of buttons - ID matches that of the game nodeList
    //top bar labels
    private Label turnLabel;
    private Label goldLabel;
    private Label supplyLabel;
    private Label scoreLabel;
    private TextButton.TextButtonStyle buttonStyle;
    private TextButton.TextButtonStyle clickStyle;
    private TextButton.TextButtonStyle curNodeStyle;

    @Override
    public void create () {
        //init variables
        SpriteBatch batch = new SpriteBatch();
        game = new GameController(5,2,"Alcuin","p1");
        mainStage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(mainStage); //allows the mainStage and only the mainStage to have inputs

        //scaling guides
        int GuideRows = 12;
        int GuideCols = 12;
        int row_height = Gdx.graphics.getHeight()/GuideRows;
        int col_width = Gdx.graphics.getWidth()/GuideCols;

        //creating button styles for different nodes (current, neighbor, neither
        //non-neighbor non-current
        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.fontColor = Color.RED;
        buttonStyle.downFontColor = Color.PINK;
        buttonStyle.font = new BitmapFont();

        //neighbor button
        clickStyle = new TextButton.TextButtonStyle();
        clickStyle.fontColor = Color.GREEN;
        clickStyle.downFontColor = Color.YELLOW;
        clickStyle.font = new BitmapFont();

        //current button
        curNodeStyle = new TextButton.TextButtonStyle();
        curNodeStyle.fontColor = Color.CYAN;
        curNodeStyle.downFontColor = Color.BLUE;
        curNodeStyle.font = new BitmapFont();

        //initiliazation of the basic ui elements of the game
        addBackgroundGrid(GuideCols,GuideRows); //adds the background grid with a repeating texture
        createTopLabels(row_height, col_width); //creates the set of top labels
        createNodeMap(3,2); //creates the nodeMap with x,y coordinates scaled to match window

    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mainStage.act();
        mainStage.draw();
    }

    @Override
    public void dispose () {
        mainStage.dispose();
    }

    //used for sizing judgements
    public void addBackgroundGrid(int cols, int rows) {
        //sets background texture and makes it repeating
        Texture texture = new Texture(Gdx.files.internal("space.png"));
        texture.setWrap(Texture.TextureWrap.MirroredRepeat,Texture.TextureWrap.MirroredRepeat);

        TextureRegion textureRegion = new TextureRegion(texture);
        textureRegion.setRegion(0,0,texture.getWidth()*cols,texture.getHeight()*rows);
        Image background = new Image(textureRegion);
        background.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        background.setPosition(0,0);
        mainStage.addActor(background);


    }

    public void createNodeMap(int xScale, int yScale){
        //variable init
        float nodeSize = 10;
        int nodeListLength = game.getNodeList().length;
        int x;
        int y;
        TextButton.TextButtonStyle aStyle;
        Node[] nodeList = game.getNodeList();

        //iterates through all nodes creating buttons and connecting lines
        nodeButtons = new TextButton[nodeListLength];
        for (int i=0;i < nodeListLength; i++){
            //scaled x and y values
            x = nodeList[i].getX() * xScale;
            y = nodeList[i].getY() * yScale;

            //node textButton creation
            //if the node is a neighbor node to the current node it has a different style as defined above
            if (game.getNeighborNodes().contains(i)){
                aStyle = clickStyle;
            } else if (game.getCurrentNode() != i) {
                aStyle = buttonStyle;
            } else {
                aStyle = curNodeStyle;
            }
            nodeButtons[i] = new TextButton(String.valueOf(i),aStyle);
            nodeButtons[i].setText("0");
            nodeButtons[i].setSize(nodeSize,nodeSize);
            nodeButtons[i].setPosition(x,y);
            nodeButtons[i].setZIndex(i);
            //listener is what allows the button to be clicked
            nodeButtons[i].addListener(new ClickListener()
            {
                //on left click up - can only follow after a left click down
                @Override
                public void touchUp(InputEvent event,float x, float y, int pointer, int button){
                    
                }
            });
            mainStage.addActor(nodeButtons[i]);

            //lines not currently working
            /*
            int iterConnect = nodeList[i].getConnectnodes().size();
            Node[] connected = new Node[iterConnect];
            ShapeRenderer shapeRenderer = new ShapeRenderer();
            for (int j=0; j < iterConnect; j++){
                connected[j] = nodeList[nodeList[i].getConnectnodes().get(j)];
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                shapeRenderer.setColor(1,0,0,1);
                shapeRenderer.rectLine(nodeList[i].getX()*3,nodeList[i].getY()*2,connected[j].getX()*3,connected[j].getY()*2,5);
                shapeRenderer.end();
            }
            */

        }
    }

    public void createTopLabels(int row_height, int col_width){
        //initilizes standard labelstyle for the top labels to follow
        resourceStyle = new Label.LabelStyle();
        resourceStyle.fontColor = Color.RED;
        resourceStyle.font = new BitmapFont();

        //all labels follow this basic layout
        turnLabel = new Label("turn",resourceStyle);
        turnLabel.setText("Current Turn: " + String.valueOf(game.getCurrentTurn()));
        turnLabel.setSize(col_width,row_height);
        turnLabel.setPosition(Gdx.graphics.getWidth()-3*col_width,Gdx.graphics.getHeight()-row_height);
        turnLabel.setAlignment(Align.topLeft);
        mainStage.addActor(turnLabel);

        goldLabel = new Label("gold",resourceStyle);
        goldLabel.setText("Current Gold: " + String.valueOf(game.currentGold));
        goldLabel.setSize(col_width,row_height);
        goldLabel.setPosition(Gdx.graphics.getWidth()- 6*col_width,Gdx.graphics.getHeight()-row_height);
        goldLabel.setAlignment(Align.topLeft);
        mainStage.addActor(goldLabel);

        supplyLabel = new Label("supplies",resourceStyle);
        supplyLabel.setText("Current Supplies: " + String.valueOf(game.getCurrentSupplies()));
        supplyLabel.setSize(col_width,row_height);
        supplyLabel.setPosition(Gdx.graphics.getWidth()-9*col_width,Gdx.graphics.getHeight()-row_height);
        supplyLabel.setAlignment(Align.topLeft);
        mainStage.addActor(supplyLabel);

        scoreLabel = new Label("score",resourceStyle);
        scoreLabel.setText("Current Score: " + String.valueOf(game.getScore()));
        scoreLabel.setSize(col_width, row_height);
        scoreLabel.setPosition(Gdx.graphics.getWidth() - 12*col_width, Gdx.graphics.getHeight()-row_height);
        scoreLabel.setAlignment(Align.topLeft);
        mainStage.addActor(scoreLabel);
    }

}
