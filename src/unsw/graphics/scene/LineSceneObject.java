package unsw.graphics.scene;

import java.awt.Color;

import com.jogamp.opengl.GL3;

import unsw.graphics.CoordFrame2D;
import unsw.graphics.Matrix3;
import unsw.graphics.Shader;
import unsw.graphics.Vector3;
import unsw.graphics.geometry.Line2D;
import unsw.graphics.geometry.Point2D;

/**
 * A game object that has is a line.
 * 
 * This class extend SceneObject to draw lines.
 *
 * TODO: The methods you need to complete are at the bottom of the class
 *
 * @author malcolmr
 * @author Robert Clifton-Everest
 * @author Benny Hwang
 * 
 */
public class LineSceneObject extends SceneObject {
	
	private Color myLineColor;
	private Point2D p1;
	private Point2D p2;

	//Create a LineSceneObject from (0,0) to (1,0)
	public LineSceneObject(SceneObject parent, Color lineColor) {
		super(parent);
		myLineColor = lineColor;
		p1 = new Point2D(0,0);
		p2 = new Point2D(1,0);
	}

	//Create a LineSceneObject from (x1,y1) to (x2,y2)
	public LineSceneObject(SceneObject parent, float x0, float y0, float x1, float y1, Color lineColor) {
		super(parent);
		myLineColor = lineColor;
		p1 = new Point2D(x0,y0);
		p2 = new Point2D(x1,y1);
	}

	/**
	 * @return the lineColor
	 */
	public Color getLineColor() {
		return myLineColor;
	}

	/**
	 * @param myLineColor the myLineColor to set
	 */
	public void setLineColor(Color lineColor) {
		this.myLineColor = lineColor;
	}

	/**
	 * @return the p1
	 */
	public Point2D getP1() {
		return p1;
	}

	/**
	 * @param p1 the p1 to set
	 */
	public void setP1(Point2D p1) {
		this.p1 = p1;
	}

	/**
	 * @return the p2
	 */
	public Point2D getP2() {
		return p2;
	}

	/**
	 * @param p2 the p2 to set
	 */
	public void setP2(Point2D p2) {
		this.p2 = p2;
	}

	/**
	 * Draw a line
	 * 
	 * if the line color is non-null, draw the line with this color
	 * 
	 */
	@Override
	public void drawSelf(GL3 gl, CoordFrame2D frame) {
		if (getLineColor() != null) {
			Line2D lineToDraw = new Line2D(this.getP1(), this.getP2());
			Shader.setPenColor(gl, getLineColor());
			lineToDraw.draw(gl, frame);
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
		
		// convert the two end points to global using the similar method from SceneObject but change the vector
		Matrix3 translationMatrix, rotationMatrix, scaleMatrix, transformationMatrix;
    	Vector3 currentVector = new Vector3(this.getP1().getX(), this.getP1().getY(), 1);
    	SceneObject currentObject = this;
    	while (currentObject.getParent() != null) {
    		translationMatrix = Matrix3.translation(currentObject.getPosition());
    		rotationMatrix = Matrix3.rotation(currentObject.getRotation());
    		scaleMatrix = Matrix3.scale(currentObject.getScale(), currentObject.getScale());
    		transformationMatrix = translationMatrix.multiply(rotationMatrix).multiply(scaleMatrix);
    		currentVector = transformationMatrix.multiply(currentVector);
    		currentObject = currentObject.getParent();
    	}
    	
    	Point2D globalPoint1 = currentVector.asPoint2D();
    	
    	// reset to compute the second point
    	currentVector = new Vector3(this.getP2().getX(), this.getP2().getY(), 1);
    	currentObject = this;
    	translationMatrix = null;
    	rotationMatrix = null;
    	scaleMatrix = null;
    	transformationMatrix = null;
    	
    	while (currentObject.getParent() != null) {
    		translationMatrix = Matrix3.translation(currentObject.getPosition());
    		rotationMatrix = Matrix3.rotation(currentObject.getRotation());
    		scaleMatrix = Matrix3.scale(currentObject.getScale(), currentObject.getScale());
    		transformationMatrix = translationMatrix.multiply(rotationMatrix).multiply(scaleMatrix);
    		currentVector = transformationMatrix.multiply(currentVector);
    		currentObject = currentObject.getParent();
    	}
    	
    	Point2D globalPoint2 = currentVector.asPoint2D();
    	
    	// Math for if a point lies 
    	// Find the slope of the line to form the equation and sub p in. If p satisfies, then check if p is not out of bound
    	float slope = (globalPoint2.getY() - globalPoint1.getY())/(globalPoint2.getX() - globalPoint1.getX());
    	// y = mx+b, so b=y-mx
    	float b = globalPoint1.getY() - slope*globalPoint1.getX();
    	
    	// Check if the p satisfies, use an error acceptable range of 0.001?
    	if ((Math.abs((slope*p.getX() + b) - p.getY()) <= 0.001) && (Math.min(globalPoint1.getX(), globalPoint2.getX())<=p.getX())
    			&& (p.getX()<= Math.max(globalPoint1.getX(), globalPoint2.getX()))
    			&& (Math.min(globalPoint1.getY(), globalPoint2.getY())<=p.getY())
    			&& (p.getY()<= Math.max(globalPoint1.getY(), globalPoint2.getY()))){
    		return true;
    	}
		return false;
	}

	
}
