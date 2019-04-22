
import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

public class BouncingBall implements Serializable{
	
    int x, y;
    int dx, dy;
    boolean selectedColor;
    int r ;
    Color color;
//information required by the each ball

    public BouncingBall(int X, int Y, int Radius , Color storedColor, int dx, int dy) {
        //setting passed parameters
        x = X;
        y = Y;
    	r = Radius;
    	color = storedColor;
        selectedColor = false;
        this.dx = dx;
        this.dy = dy;
        
    	
    }
        
    public void draw(Graphics g) {
       //movement of the ball on the panel
 x+=dx;
            y+=dy;
    	if(x<0)
            dx=-dx;
            if(x> MainFrameClass.centerPanel.getWidth())
                dx=-dx;
            if(y<0)
                    dy=-dy;
                    if(y > MainFrameClass.centerPanel.getHeight())
                dy=-dy;          
      
        g.setColor(color.white);
        g.fillOval(x, y, r, r);
    	g.setColor(color);
        //drawing all the ovals except selected ball
        if (selectedColor) {

            g.fillOval(x, y, r, r);
        } else {
            g.drawOval(x, y, r, r);
        }
    }

    public void IncrementXorY(int delayTime) {
        //incrementing or decrementing according to the pixel values on the display
        double xMovement = dx * delayTime / 1000.0;
        double yMovement = dy * delayTime / 1000.0;
        
        x += xMovement;
        y += yMovement;
        

    }
    
    
}
