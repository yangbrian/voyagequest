package voyagequest;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import voyagequest.special.LoadAnimations;

/**
 * Global resources, like animations, sprites, etc...
 * @author Brian Yang
 */
public class Res {
    
    /** Sprite sheet for Sebastian */
    public static StreamXMLPackedSheet sebastian; 
    public static StreamXMLPackedSheet njeri; 
    public static StreamXMLPackedSheet soldier; 
    public static StreamXMLPackedSheet lifei; 
    public static StreamXMLPackedSheet lifeisprite; 
    public static StreamXMLPackedSheet young;
    public static StreamXMLPackedSheet anika;
    
    static {
        try {
            sebastian = new StreamXMLPackedSheet("res/sebastian.png", "res/sebastian.xml");
            njeri = new StreamXMLPackedSheet("res/njeri.png", "res/njeri.xml");
            soldier = new StreamXMLPackedSheet("res/soldier_npc01_walk.png", "res/soldier.xml");
            lifei = new StreamXMLPackedSheet("res/lifei.png", "res/lifei.xml");
            lifeisprite = new StreamXMLPackedSheet("res/lifeifront.png", "res/lifeisprite.xml");
            young = new StreamXMLPackedSheet("res/test/surprisetiles.png","res/young.xml");
            anika = new StreamXMLPackedSheet("res/anika.png","res/anika.xml");
            System.out.println("XML Loaded");
        } catch (SlickException ex) {}
    }
    
    /** Sebastian animation, data mapped by JSON */
    public static LinkedList<LoadAnimations> animationData = new LinkedList<>();
    
    /** Every image */
    //public static HashMap<String, Image> images = new HashMap<>();
    
    /** The actual animations, mapped by ID strings */
    public static HashMap<String, Animation> animations = new HashMap<>();
    
    /** Main background music */
    public static Music mainmusic;
    static {
        try {
            URL is = Res.class.getClassLoader().getResource("res/sounds/main.ogg");
            mainmusic = new Music(is);
        } catch (SlickException e) {}
    }
    
    /** Teleport music - credit: Wizet (Maplestory) */
    public static Music teleport;
    static {
        try {
            URL is = Res.class.getClassLoader().getResource("res/sounds/teleport.ogg");
            teleport = new Music(is);
        } catch (SlickException e) {}
    }
    
    /**
     * Initialize all the remaining resources. 
     * Must be called <em>after</em> data is loaded from JSON
     */
    public static void init() {
        mainmusic.loop();
        
        ListIterator<LoadAnimations> animationIterator = animationData.listIterator();
        while (animationIterator.hasNext()) {
            LoadAnimations next = animationIterator.next();
            
            //For lack of better implementation, I will hardcode this now.
            //Here's the problem: before, it was sebastian.getSprite(frameIterator.next());
            //Basically, since you didn't write something that transferred the images from
            //the StreamXMLPackedSheets into a huge HashMap of Image objects, you went directly from
            //StreamXMLPackedSheet --> Animation with this method. Unfortunately, we end up with two
            //issues: 
            //     1. How do we load any images that don't belong with Animations?
            //     2. How do we tell between which StreamXMLPackedSheet to choose from? I decided to give
            //     a new JSON field called packedSheetID which solves the problem for now, but please
            //     later help me write something that creates a huge Hashmap<String, Image> please.
            
            StreamXMLPackedSheet currentPackedSheet = null;
            System.out.println(next.getPackedSheetID());
            if (next.getPackedSheetID().equals("njeri")) currentPackedSheet = njeri;
            if (next.getPackedSheetID().equals("sebastian")) currentPackedSheet = sebastian;
            if (next.getPackedSheetID().equals("soldier")) currentPackedSheet = soldier;
            if (next.getPackedSheetID().equals("lifei")) currentPackedSheet = lifei;
            if (next.getPackedSheetID().equals("lifeisprite")) currentPackedSheet = lifeisprite;
            if (next.getPackedSheetID().equals("young")) currentPackedSheet = young;
            if (next.getPackedSheetID().equals("anika")) currentPackedSheet = anika;
            
            Image[] frames = new Image[next.getImages().size()];
            int i = 0;
            ListIterator<String> frameIterator = next.getImages().listIterator();
            while (frameIterator.hasNext()) {
                frames[i] = currentPackedSheet.getSprite(frameIterator.next());
                i++;
            }
                
            animations.put(next.getName(), new Animation(frames, next.getDuration()));
        }
        
        
        
    }
    
}