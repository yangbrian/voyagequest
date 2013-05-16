package voyagequest;

import map.*;
import org.newdawn.slick.*;
import java.util.ArrayList;
import org.newdawn.slick.geom.Rectangle;
import gui.special.*;

/**
 * Voyage Quest RPG
 * Copyright (c) 2013 Team Coding Voyage.
 * 
 * @author Edmund Qiu
 * @author Brian Yang
 */

public class VoyageQuest extends BasicGame {
    /** x resolution */
    public static float X_RESOLUTION = 1024;
    /** y resolution */
    public static float Y_RESOLUTION = 768;
    /** full screen mode */
    public static boolean FULLSCREEN = false;
    
    public static float MAP_WIDTH = VoyageQuest.X_RESOLUTION * 5;
    public static float MAP_HEIGHT = VoyageQuest.Y_RESOLUTION * 5;
    public static int ENTITY_TEST_COUNT = 200;
    public static Rectangle screen = new Rectangle(0.0f, 0.0f, MAP_WIDTH, MAP_HEIGHT);
    
    public static Camera camera;
    public static ArrayList<Entity> entities;
    public static QuadTree partitionTree;
    
    int index = 0;
    int whichDraw = -1;
    int deltaCounter = 0;
    int removeCounter = 0;
    
    public DialogBox dialog;
    
    /**
     * Construct a new game
     */
    public VoyageQuest() {
       super("Voyage Quest Pre-Alpha");
    }

    /**
     * Initialize the game container and start the scripting engine
     * @param gc the new game container
     * @throws SlickException something went wrong with the Slick engine
     */
    @Override
    public void init(GameContainer gc) throws SlickException {
        // Get rid of the default FPS count so we can use our own font
        gc.setShowFPS(false);
        
        // Set the minimum and maximum update intervals please
        gc.setMinimumLogicUpdateInterval(20);
        gc.setMaximumLogicUpdateInterval(20);
        
        camera = new Camera();
        partitionTree = new QuadTree(15, 10,
                new Rectangle(0, 0, MAP_WIDTH, MAP_HEIGHT));
        entities = new ArrayList<>();
        
        for (int i = 0; i < ENTITY_TEST_COUNT; i++)
        {
            float randX = Util.rand(0.0f, MAP_WIDTH - 50.0f);
            float randY = Util.rand(0.0f, MAP_HEIGHT - 50.0f);
            float randWidth = Util.rand(50.0f, 50.0f);
            float randHeight = Util.rand(50.0f, 50.0f);
            float randXVelocity = (float)Util.rand(50.0f, 50.0f);
            float randYVelocity = (float)Util.rand(50.0f, 50.0f);
            
            Entity e = new Entity(new Rectangle(randX, randY, randWidth, randHeight));
            e.vx = randXVelocity;
            e.vy = randYVelocity;
            
            entities.add(e);
            partitionTree.addEntity(e);
        }
        
        Color start = new Color(166, 250, 252, 75); // Color: #A6FAFC with alpha 75%
        Color end = new Color(205, 255, 145, 75); // Color #CDFF91 with alpha 75%
        
        String lorem = "The dialog box system is currently under construction. This is merely a test of automatic line breaks. Do not modify the gui.* packages.";
        dialog = new DialogBox(250, 550, 600, 150, lorem, start, end);
    }


    /**
     * Update the screen
     * @param gc the game container
     * @param delta time interval
     * @throws SlickException something went horribly wrong with Slick
     */
    @Override
    public void update(GameContainer gc, int delta) throws SlickException {
        
        Input input = gc.getInput();
        
        for (int i = 0; i < entities.size(); i++)
        {
           Entity e = entities.get(i);
           if (e != null)
           {
               e.act(delta);
           }
        }
        
        
        
        float step = delta * 2;
        /* tilt and move to the left */
        if (input.isKeyDown(Input.KEY_LEFT)) {
            camera.attemptMove(-step, 0);
        }
        
        if(input.isKeyDown(Input.KEY_RIGHT)) {
            camera.attemptMove(step, 0);
        }
        
        if(input.isKeyDown(Input.KEY_UP)) {
            camera.attemptMove(0, -step);
        }
        
        if(input.isKeyDown(Input.KEY_DOWN)) {
            camera.attemptMove(0, step);
        }
        
        if(input.isKeyDown(Input.KEY_ENTER))
        {
            if (partitionTree.getSize() > 0)
            {
                for (int i = 0; i < entities.size(); i++)
                {
                    Entity toBeRemoved = entities.get(i);
                    if (toBeRemoved != null)
                    partitionTree.removeEntity(toBeRemoved);
                }
            }
        }
        
        deltaCounter += delta;
        removeCounter += delta;
        
        if (deltaCounter > 1000)
        {
            deltaCounter = 0;
            whichDraw *= -1;
            if (whichDraw == 1)
            {
                Util.p("We will now traverse the tree to draw");
            }
            else 
            {
                Util.p("We will now draw the entity list");
            }
            
            Util.p("Total number of entities in Tree: " + partitionTree.getSize());
            Util.p("Total number of partitions in Tree: " + partitionTree.getPartitionCount());
            
            
        }
        
        if (removeCounter > 19)
        {
            removeCounter = 0;
            //now let's remove one.
            if (index < ENTITY_TEST_COUNT) {
                Entity toBeRemoved = entities.get(index);
                partitionTree.removeEntity(toBeRemoved);
                index++;
            }
        }
    }

    /**
     * Render the game window
     * @param gc the game container
     * @param g the graphics engine
     * @throws SlickException something went horribly wrong with Slick
     */
    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException
    {
        //if there isn't a full screen GUI...
        camera.display(g);
        //gui.display
        dialog.start();
        //if the settings say we're doing FPS displaying...
        Util.FONT.drawString(10, 10, "FPS: " + gc.getFPS());
    }

    /**
     * The main method<br/>
     * Create the game window and start the game!
     * @param args command line arguments
     * @throws SlickException something went horribly wrong with Slick
     */
    public static void main(String[] args) throws SlickException {
        // Start new game window
        AppGameContainer app = new AppGameContainer(new VoyageQuest());
        
        app.setDisplayMode((int)X_RESOLUTION, (int)Y_RESOLUTION, FULLSCREEN);
        app.setAlwaysRender(true);
        app.setTargetFrameRate(60);

        app.start();
        
    }
}
