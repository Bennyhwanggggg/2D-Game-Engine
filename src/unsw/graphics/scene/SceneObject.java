package unsw.graphics.scene;

import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL3;

import unsw.graphics.CoordFrame2D;
import unsw.graphics.Matrix3;
import unsw.graphics.Vector3;
import unsw.graphics.geometry.Point2D;

/**
 * A SceneObject is an object that can move around in the world.
 * 
 * SceneObjects form a scene tree.
 * 
 * Each SceneObject is offset from its parent by a translation, a rotation and a scale factor. 
 *
 * TODO: The methods you need to complete are at the bottom of the class
 *
 * @author malcolmr
 * @author Robert Clifton-Everest
 * @author Benny Hwang
 */
public class SceneObject {
    
	// List of all objects in Scene tree so we can use it for detection collision
	public final static List<SceneObject> ALL_SCENE_OBJECTS = new ArrayList<SceneObject>();
	
    // the links in the scene tree
    private SceneObject myParent;
    private List<SceneObject> myChildren;

    // the local transformation
    private Point2D myTranslation;
    private float myRotation; //normalised to the range [-180..180)
    private float myScale;
    
    // Is this part of the tree showing?
    private boolean amShowing;

    /**
     * Special constructor for creating the root node. Do not use otherwise.
     */
    public SceneObject() {
        myParent = null;
        myChildren = new ArrayList<SceneObject>();

        myRotation = 0;
        myScale = 1;
        myTranslation = new Point2D(0,0);

        amShowing = true;
        
        // Add the item to all object list
        ALL_SCENE_OBJECTS.add(this);
    }

    /**
     * Public constructor for creating SceneObjects, connected to a parent.
     *  
     * New objects are created at the same location, orientation and scale as the parent.
     *
     * @param parent
     */
    public SceneObject(SceneObject parent) {
        myParent = parent;
        myChildren = new ArrayList<SceneObject>();

        parent.myChildren.add(this);

        myRotation = 0;
        myScale = 1;
        myTranslation = new Point2D(0,0);

        // initially showing
        amShowing = true;
        
        // Add the item to all object list
        ALL_SCENE_OBJECTS.add(this);
    }

    /**
     * Remove an object and all its children from the scene tree.
     */
    public void destroy() {
	    List<SceneObject> childrenList = new ArrayList<SceneObject>(myChildren);
        for (SceneObject child : childrenList) {
            child.destroy();
        }
        if(myParent != null) {
                myParent.myChildren.remove(this);
        }
        
        ALL_SCENE_OBJECTS.remove(this);
    }

    /**
     * Get the parent of this scene object
     * 
     * @return
     */
    public SceneObject getParent() {
        return myParent;
    }

    /**
     * Get the children of this object
     * 
     * @return
     */
    public List<SceneObject> getChildren() {
        return myChildren;
    }

    /**
     * Get the local rotation (in degrees)
     * 
     * @return
     */
    public float getRotation() {
        return myRotation;
    }

    /**
     * Set the local rotation (in degrees)
     * 
     * @return
     */
    public void setRotation(float rotation) {
        myRotation = MathUtil.normaliseAngle(rotation);
    }

    /**
     * Rotate the object by the given angle (in degrees)
     * 
     * @param angle
     */
    public void rotate(float angle) {
        myRotation += angle;
        myRotation = MathUtil.normaliseAngle(myRotation);
    }

    /**
     * Get the local scale
     * 
     * @return
     */
    public float getScale() {
        return myScale;
    }

    /**
     * Set the local scale
     * 
     * @param scale
     */
    public void setScale(float scale) {
        myScale = scale;
    }

    /**
     * Multiply the scale of the object by the given factor
     * 
     * @param factor
     */
    public void scale(float factor) {
        myScale *= factor;
    }

    /**
     * Get the local position of the object 
     * 
     * @return
     */
    public Point2D getPosition() {
        return myTranslation;
    }

    /**
     * Set the local position of the object
     * 
     * @param x
     * @param y
     */
    public void setPosition(float x, float y) {
        setPosition(new Point2D(x,y));
    }

    /**
     * Set the local position of the object
     * 
     * @param x
     * @param y
     */
    public void setPosition(Point2D p) {
        myTranslation = p;
    }

    /**
     * Move the object by the specified offset in local coordinates
     * 
     * @param dx
     * @param dy
     */
    public void translate(float dx, float dy) {
        myTranslation = myTranslation.translate(dx, dy);
    }

    /**
     * Test if the object is visible
     * 
     * @return
     */
    public boolean isShowing() {
        return amShowing;
    }

    /**
     * Set the showing flag to make the object visible (true) or invisible (false).
     * This flag should also apply to all descendents of this object.
     * 
     * @param showing
     */
    public void show(boolean showing) {
        amShowing = showing;
    }

    /**
     * Update the object and all it's children. This method is called once per frame. 
     * 
     * @param dt The amount of time since the last update (in seconds)
     */
    public void update(float dt) {
        updateSelf(dt);
        
        // Make a copy of all the children to avoid concurrently modification issues if new objects
        // are added to the scene during the update.
        List<SceneObject> children = new ArrayList<SceneObject>(myChildren);
        for (SceneObject so : children) {
            so.update(dt);
        }
    }

    /** 
     * Update the object itself. Does nothing in the default case. Subclasses can override this
     * for animation or interactivity.
     * 
     * @param dt
     */
    public void updateSelf(float dt) {
        // Do nothing by default
    }

    /**
     * Draw the object (but not any descendants)
     * 
     * This does nothing in the base SceneObject class. Override this in subclasses.
     * 
     * @param gl
     */
    public void drawSelf(GL3 gl, CoordFrame2D frame) {
        // Do nothing by default
    }

    
    // ===========================================
    // COMPLETE THE METHODS BELOW
    // ===========================================
    
    /**
     * Draw the object and all of its descendants recursively.
     * 
     * TODO: Complete this method
     * 
     * @param gl
     */
    public void draw(GL3 gl, CoordFrame2D frame) {
        
        // don't draw if it is not showing
        if (!amShowing) {
            return;
        }

        // TODO: Compute the coordinate frame for this object
        // draw the object (Call drawSelf() to draw the object itself) 
        // and all its children recursively
        CoordFrame2D objFrame = frame.translate(getPosition())
        		.rotate(getRotation())
        		.scale(getScale(), getScale());
        
        drawSelf(gl, objFrame);
        
        for(SceneObject child: myChildren) {
        	child.draw(gl, objFrame);
        }
        
    }

    /**
     * Compute the object's position in world coordinates
     * 
     * @return a point in world coordinates
     */
    public Point2D getGlobalPosition() {
        // TODO: Complete this
    	
    	// if already at the root of the tree
    	if (this.myParent == null) {
    		return this.getPosition();
    	}
    	
    	// otherwise find the translation vector from current node all the way up the scene tree's root
    	// Global = Transformation Matrix * Local
    	Matrix3 translationMatrix, rotationMatrix, scaleMatrix, transformationMatrix;
    	Vector3 currentVector = new Vector3(0, 0, 1);
    	SceneObject currentObject = this;
    	while (currentObject.myParent != null) {
    		translationMatrix = Matrix3.translation(currentObject.getPosition());
    		rotationMatrix = Matrix3.rotation(currentObject.getRotation());
    		scaleMatrix = Matrix3.scale(currentObject.getScale(), currentObject.getScale());
    		transformationMatrix = translationMatrix.multiply(rotationMatrix).multiply(scaleMatrix);
    		currentVector = transformationMatrix.multiply(currentVector);
    		currentObject = currentObject.getParent();
    	}
        return currentVector.asPoint2D();
    }

    /**
     * Compute the object's rotation in the global coordinate frame
     * 
     * @return the global rotation of the object (in degrees) and 
     * normalized to the range (-180, 180) degrees. 
     */
    public float getGlobalRotation() {
        // TODO: Complete this
    	
    	// if already at the root of the tree
    	if (this.myParent == null) {
    		return this.getRotation();
    	}
    	
    	SceneObject currentObject = this;
    	float rotation = 0;
    	while (currentObject.myParent != null) {
    		rotation += currentObject.getRotation();
    		currentObject = currentObject.getParent();
    	}
    	
        return MathUtil.normaliseAngle(rotation);
    }

    /**
     * Compute the object's scale in global terms
     * 
     * @return the global scale of the object 
     */
    public float getGlobalScale() {
        // TODO: Complete this
    	if (this.myParent == null) {
    		return this.getScale();
    	}
    	
    	SceneObject currentObject = this;
    	float scale = 1;
    	while (currentObject.myParent != null) {
    		scale *= currentObject.getScale();
    		currentObject = currentObject.getParent();
    	}
    	
        return scale;
    }

    /**
     * Change the parent of a scene object.
     * 
     * @param parent
     */
    public void setParent(SceneObject parent) {
        // TODO: add code so that the object does not change its global position, rotation or scale
        // when it is reparented. You may need to add code before and/or after 
        // the fragment of code that has been provided - depending on your approach
    	
    	// Preserve the global position, rotation and scale first
    	Point2D globalPosition = getGlobalPosition();
    	float globalRotation = getGlobalRotation();
    	float globalScale = getGlobalScale();
    	
        // Change parents
        myParent.myChildren.remove(this);
        myParent = parent;
        myParent.myChildren.add(this);
        
        // Convert the preserved to global to the new local using the transformation matrix from the new parents
        // We need to compute the inverse transformation matrix
        Matrix3 translationMatrix, rotationMatrix, scaleMatrix, transformationMatrix = Matrix3.identity();
        while (parent != null) {
        	translationMatrix = Matrix3.translation(-parent.getPosition().getX(), -parent.getPosition().getY());
        	rotationMatrix = Matrix3.rotation(-parent.getRotation());
        	scaleMatrix = Matrix3.scale(1f/parent.getScale(), 1f/parent.getScale());
        	transformationMatrix = scaleMatrix.multiply(rotationMatrix).multiply(translationMatrix).multiply(transformationMatrix);
        	parent = parent.getParent();
        }
        
        // To find the new local vector, we can now use the transformation matrix multiplied by the global vector 
        // Local = M^-1 * Global
        Vector3 globalVector = new Vector3(globalPosition.getX(), globalPosition.getY(), 1);
        Vector3 localVector = transformationMatrix.multiply(globalVector);
        
        // set new local location
        setPosition(localVector.asPoint2D());
        
        // Compute the local rotation which is global - parent
        setRotation(globalRotation-myParent.getGlobalRotation());
        
        // Set new scale which is the global scale we computed divided by parent scale
        setScale(globalScale/myParent.getGlobalScale());
    }
    
    /**
     * Collision detection
     * 
     * This does nothing in the base SceneObject class. Override this in subclasses.
     * 
     * @param Point2D
     * @return true if point is inside the object
     */
    public boolean collision(Point2D p) {
    	return true; // return false by default
    }

}
