package unsw.graphics.scene;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL3;

import unsw.graphics.CoordFrame2D;
import unsw.graphics.Shader;
import unsw.graphics.geometry.Point2D;
import unsw.graphics.geometry.Polygon2D;

/**
 * A game object that has is a circle.
 * 
 * This class extend SceneObject to draw circle shapes.
 *
 * TODO: The methods you need to complete are at the bottom of the class
 *
 * @author malcolmr
 * @author Robert Clifton-Everest
 * @author Benny Hwang
 * 
 */
public class CircularSceneObject extends PolygonalSceneObject {
	
	private static final int POINTS = 32;
	private float myRadius;
	private Point2D myCentre;
    private Color myFillColor;
    private Color myLineColor;

	//Create a CircularSceneObject with centre 0,0 and radius 1
	public CircularSceneObject(SceneObject parent, Color fillColor, Color lineColor) {
		super(parent, new Polygon2D(getCirclePoints(new Point2D(0, 0), 1)), fillColor, lineColor); //
		myRadius = 1;
		myCentre = new Point2D(0, 0);
		myFillColor = fillColor;
		myLineColor = lineColor;
	}

	//Create a CircularSceneObject with centre 0,0 and a given radius
	public CircularSceneObject(SceneObject parent, float radius, Color fillColor, Color lineColor) {
		super(parent, new Polygon2D(getCirclePoints(new Point2D(0, 0), radius)), fillColor, lineColor);
		myRadius = radius;
		myCentre = new Point2D(0, 0);
		myFillColor = fillColor;
		myLineColor = lineColor;
	}

	/**
     * Get the radius
     * 
     * @return
     */	
	public float getRadius() {
		return myRadius;
	}

	/**
     * Set the radius of the circular object
     * 
     * @param float The new radius
     */
	public void setRadius(float r) {
		this.myRadius = r;
	}

	/**
     * Get the center
     * 
     * @return
     */
	public Point2D getCentre() {
		return myCentre;
	}

	/**
     * Set the center of the circular object
     * 
     * @param Point2D The new center coordinate
     */
	public void setCentre(Point2D centre) {
		this.myCentre = centre;
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
		this.myFillColor = fillColor;
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
		this.myLineColor = lineColor;
	}

	/**
	 * Draw the circle
	 * 
	 * if the fill color is non-null, draw the polygon filled with this color
     * if the line color is non-null, draw the outline with this color
     * 
	 */
	@Override
	public void drawSelf(GL3 gl, CoordFrame2D frame) {
		Polygon2D myCircle = new Polygon2D(getCirclePoints(getCentre(), getRadius()));
		Color colorToFill = getFillColor();
    	if (colorToFill != null) {
    		// use shader to set color
    		Shader.setPenColor(gl, colorToFill);
    		myCircle.draw(gl, frame);
    	}
    	
    	Color colorToEdge = getLineColor();
    	if (colorToEdge != null) {
    		Shader.setPenColor(gl, colorToEdge);
    		myCircle.drawOutline(gl, frame);
    	}

	}
	
	/**
	 * Compute the points for 32 side polygon to generate the circle using centre and radius
	 * 
	 * @param centre
	 * @param radius
	 * @return
	 */
	private static List<Point2D> getCirclePoints(Point2D centre, double radius){
		double currentAngle = 0; // in radians
		double angleInterval = (2*Math.PI)/POINTS;
		List<Point2D> circlePoints = new ArrayList<>();
		for (int i=0; i<POINTS; i++) {
			float x = centre.getX() + (float) (radius * Math.cos(currentAngle));
			float y = centre.getY() + (float) (radius * Math.sin(currentAngle));
			circlePoints.add(new Point2D(x, y));
			currentAngle += angleInterval;
		}
		return circlePoints;
	}
	
	/**
	 * Collision detection for Circles
	 * 
	 * @param point in world coordinate
	 * @return true if point inside circle
	 */
	@Override
	public boolean collision(Point2D p) {
		// Get the objects world coordinate and see if p is inside it
		Point2D globalCenter = this.getGlobalPosition();
		return (this.getRadius()*this.getGlobalScale()) >= Math.hypot(p.getX()-globalCenter.getX(), p.getY()-globalCenter.getY());
	}
	
}
