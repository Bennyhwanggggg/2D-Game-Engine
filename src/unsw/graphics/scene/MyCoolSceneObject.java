package unsw.graphics.scene;

import java.awt.Color;

import unsw.graphics.geometry.Polygon2D;

/**
 * A scene object that is a dog/hippo
 *
 * #########NOTE#########
 * Use non black background as the object itself is black
 */
public class MyCoolSceneObject extends SceneObject {
	
	private PolygonalSceneObject leftFrontLeg;
	private PolygonalSceneObject rightFrontLeg;
	private PolygonalSceneObject leftBackLeg;
	private PolygonalSceneObject rightBackLeg;
	private PolygonalSceneObject leftFrontFeet;
	private PolygonalSceneObject leftBackFeet;
	private PolygonalSceneObject head;
	private PolygonalSceneObject body;
	private PolygonalSceneObject leftEar;
	private PolygonalSceneObject rightEar;
	private LineSceneObject tailBranch;
	private LineSceneObject tailTip;
	private CircularSceneObject mouse;
	private CircularSceneObject nose;
	
	private Color black = Color.BLACK;
    private Color white = Color.WHITE;
	
	

    public MyCoolSceneObject(SceneObject parent) {
        super(parent);
        
        Polygon2D bodyShape = new Polygon2D(0.5f,0.3f, 0.6f,0.2f, 0.65f,0.1f, 0.65f,-0.05f, 0.5f,-0.2f, -0.4f,-0.2f, -0.5f,-0.15f, -0.6f,0.1f, -0.5f,0.325f);
        body = new PolygonalSceneObject(this, bodyShape, black, black);
        body.scale(0.75f);
        
        // Four legs. Right leg is behind left leg from the 2D view.
        Polygon2D leg = new Polygon2D(0,0, -0.25f,0, -0.25f,-0.4f, 0,-0.4f);
        rightFrontLeg = new PolygonalSceneObject(body, leg, black, white);
        rightFrontLeg.translate(-0.1f,  -0.2f);
        rightFrontLeg.scale(0.55f);
        leftFrontLeg = new PolygonalSceneObject(body, leg, black, white);
        leftFrontLeg.translate(-0.2f, -0.2f);
        leftFrontLeg.scale(0.6f);
        rightBackLeg = new PolygonalSceneObject(body, leg, black, white);
        rightBackLeg.translate(0.4f,  -0.2f);
        rightBackLeg.scale(0.55f);
        leftBackLeg = new PolygonalSceneObject(body, leg, black, white);
        leftBackLeg.translate(0.3f, -0.2f);
        leftBackLeg.scale(0.6f);
        
        // Since both right leg is behind left, we don't draw the two right feet and assume its behind the two left legs
        Polygon2D feet = new Polygon2D(0,0, -0.05f,0, -0.15f,-0.05f, -0.25f,-0.1f, -0.25f,-0.2f, 0,-0.2f);
        leftFrontFeet = new PolygonalSceneObject(leftFrontLeg, feet, black, black);
        leftFrontFeet.scale(0.5f);
        leftFrontFeet.translate(-0.25f, -0.295f);
        leftBackFeet = new PolygonalSceneObject(leftBackLeg, feet, black, black);
        leftBackFeet.scale(0.5f);
        leftBackFeet.translate(-0.25f, -0.295f);
        
        // wriggly tail
        tailBranch = new LineSceneObject(body, 0,0, 0.15f,0.05f, black);
        tailBranch.translate(0.65f, 0);
        tailTip = new LineSceneObject(tailBranch, 0,0, 0.15f, 0.15f, black);
        tailTip.translate(0.15f, 0.05f);
        
        // head
        Polygon2D skull = new Polygon2D(-0.1f,0.25f, 0.2f,0.25f, 0.4f,0, 0.2f,-0.3f, -0.2f,-0.3f, -0.25f,0);
        head = new PolygonalSceneObject(body, skull, black, black);
        head.translate(-0.6f, 0.3f);
        head.scale(0.7f);
        mouse = new CircularSceneObject(head, 0.15f, black, black);
        mouse.translate(-0.225f, -0.15f);
        nose = new CircularSceneObject(mouse, 0.03f, black, white);
        nose.translate(-0.12286f, 0.08604f);
        Polygon2D ear = new Polygon2D(0.075f,0, -0.075f,0, 0,0.15f);
        leftEar = new PolygonalSceneObject(head, ear, black, black);
        leftEar.translate(0,  0.25f);
        rightEar = new PolygonalSceneObject(head, ear, black, black);
        rightEar.translate(0.1f, 0.25f);
        
    }

}
