package map;

import org.newdawn.slick.geom.Rectangle;
import java.util.LinkedList;

/**
 *
 * @author Edmund
 */
public class QuadTree {
    public final int MAX_LEVELS;
    public final int MAX_OBJECTS;
    private TreeNode treeRootNode;
    
    public QuadTree(int maxLevel, int maxObjects, Rectangle boundary)
    {
        MAX_LEVELS = maxLevel;
        MAX_OBJECTS = maxObjects;
        treeRootNode = new TreeNode(null, boundary, 0, this);
    }
    
    public void addEntity(Entity e)
    {
        treeRootNode.addEntity(e);
    }
    
    public void removeEntity(Entity e)
    {
        treeRootNode.removeEntity(e);
        treeRootNode.adjustPartitions(e);
    }
    
    public int getSize()
    {
        return treeRootNode.getSize();
    }
    
    public int getPartitionCount()
    {
        return getPartitions().size();
    }
    
    public LinkedList<TreeNode> getPartitions()
    {
        return treeRootNode.getPartitionBoxes();
    }
    
    public LinkedList<Entity> rectQuery(Rectangle queryRect)
    {
        return treeRootNode.rectQuery(queryRect);
    }
}
