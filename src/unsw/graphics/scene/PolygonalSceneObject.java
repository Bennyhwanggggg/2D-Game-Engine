package unsw.graphics.scene;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL3;

import unsw.graphics.CoordFrame2D;
import unsw.graphics.Matrix3;
import unsw.graphics.Shader;
import unsw.graphics.Vector3;
import unsw.graphics.geometry.Point2D;
import unsw.graphics.geometry.Polygon2D;

/**
 * A game object that has a polygonal shape.
 * 
 * This class extend SceneObject to draw polygonal shapes.
 *
 * TODO: The methods you need to complete are at the bottom of the class
 *
 * @author malcolmr
 * @author Robert Clifton-Everest
 * @author Benny Hwang
 * 
 */
public class PolygonalSceneObject extends SceneObject {

    private Polygon2D myPolygon;
    private Color myFillColor;
    private Color myLineColor;

    /**
     * Create a polygonal scene object and add it to the scene tree
     * 
     * The line and fill colors can possibly be null, in which case that part of the object
     * should not be drawn.
     *
     * @param parent The parent in the scene tree
     * @param points A list of points defining the polygon
     * @param fillColor The fill color
     * @param lineColor The outline color
    */
    public PolygonalSceneObject(SceneObject parent, Polygon2D polygon,
            Color fillColor, Color lineColor) {
        super(parent);

        myPolygon = polygon;
        myFillColor = fillColor;
        myLineColor = lineColor;
    }

    /**
     * Get the fill color
     * 
     * @return
     */
    public Color getFillColor() {
        return myFillColor;
    }

    /**
     * Set the fill color.
     * 
     * Setting the color to null means the object should not be filled.
     * 
     * @param fillColor The fill color
     */
    public void setFillColor(Color fillColor) {
        myFillColor = fillColor;
    }

    /**
     * Get the outline color.
     * 
     * @return
     */
    public Color getLineColor() {
        return myLineColor;
    }

    /**
     * Set the outline color.
     * 
     * Setting the color to null means the outline should not be drawn
     * 
     * @param lineColor
     */
    public void setLineColor(Color lineColor) {
        myLineColor = lineColor;
    }

    // ===========================================
    // COMPLETE THE METHODS BELOW
    // ===========================================
    

    /**
     * Draw the polygon
     * 
     * if the fill color is non-null, draw the polygon filled with this color
     * if the line color is non-null, draw the outline with this color
     * 
     */
    @Override
    public void drawSelf(GL3 gl, CoordFrame2D frame) {

        // TODO: Write this method
    	Color colorToFill = getFillColor();
    	if (colorToFill != null) {
    		// use shader to set color
    		Shader.setPenColor(gl, colorToFill);
    		myPolygon.draw(gl, frame);
    	}
    	
    	Color colorToEdge = getLineColor();
    	if (colorToEdge != null) {
    		Shader.setPenColor(gl, colorToEdge);
    		myPolygon.drawOutline(gl, frame);
    	}

    }

    /**
	 * Collision detection for line
	 * 
	 * @param Point2D p
	 * @return true if a point lie on the line
	 */
	@Override
	public boolean collision(Point2D p) {
		// Use ray cast algorithm to detect if a point is inside a polygon. If a ray 
		// cross edges odd number of times, it indicate the point is inside the polygon.
		List<Point2D> polygonPoints = new ArrayList<Point2D>(this.myPolygon.getPoints());
		
		// Since there can be a large number of points, its more appropriate to do this in local coordinate system.
		// compute the inverse model view matrix
		SceneObject currentObj = this;
		Matrix3 translationMatrix, rotationMatrix, scaleMatrix, transformationMatrix = Matrix3.identity();
		translationMatrix = Matrix3.translation(-currentObj.getGlobalPosition().getX(), -currentObj.getGlobalPosition().getY());
    	rotationMatrix = Matrix3.rotation(-currentObj.getGlobalRotation());
    	scaleMatrix = Matrix3.scale(1f/currentObj.getGlobalScale(), 1f/currentObj.getGlobalScale());
    	transformationMatrix = scaleMatrix.multiply(rotationMatrix).multiply(translationMatrix).multiply(transformationMatrix);
        

        // To find the new local vector, we can now use the transformation matrix multiplied by the global vector 
        // Local = M^-1 * Global
        Vector3 globalVector = new Vector3(p.getX(), p.getY(), 1);
        p = transformationMatrix.multiply(globalVector).asPoint2D();

        // Adapted the solution provided in https://stackoverflow.com/questions/8721406/how-to-determine-if-a-point-is-inside-a-2d-convex-polygon
        // to determine if the point is inside polygon. 
        boolean collided = false;
        int nPoints = polygonPoints.size();
        int i, j;
        for (i=0, j=nPoints-1; i<nPoints; j=i++) {
        	if ((polygonPoints.get(i).getY() >= p.getY()) != (polygonPoints.get(j).getY() >= p.getY()) &&
        			(p.getX() <= ((polygonPoints.get(j).getX() - polygonPoints.get(i).getX()) 
        			* (p.getY() - polygonPoints.get(i).getY()))/(polygonPoints.get(j).getY()-polygonPoints.get(i).getY())
        			+ polygonPoints.get(i).getX())) {
        		collided = !collided;
        	}
        }
        
		return collided;
	}
    

}
