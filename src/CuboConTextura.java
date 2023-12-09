
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;        
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.GL;
import static com.jogamp.opengl.GL.*;  
import static com.jogamp.opengl.GL2.*; 
import static com.jogamp.opengl.GL2ES3.GL_QUADS;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;
import com.jogamp.opengl.GLProfile;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


@SuppressWarnings("serial")
public class CuboConTextura extends GLJPanel implements GLEventListener, KeyListener {
   
   private static String TITLE = "Cubo"; 
   private static final int CANVAS_WIDTH = 440;  
   private static final int CANVAS_HEIGHT = 440; 
   private static final int FPS = 60; 
   private static final float factInc = 5.0f; 
   float fovy=45.0f;
   int   eje=0;
   float rotX=0.0f;
   float rotY=0.0f;
   float rotZ=0.0f;
   
   float posCamX = 0.0f;
   float posCamY = 0.0f;
   float posCamZ = 0.0f;
   
   Texture textura1;
 
  
   public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            GLJPanel canvas = new CuboConTextura();
            canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

            final FPSAnimator animator = new FPSAnimator(canvas, FPS, true);
 
          
            final JFrame frame = new JFrame();
            
            
            BorderLayout fl = new BorderLayout();
            frame.setLayout(fl);
                        
            frame.getContentPane().add(canvas, BorderLayout.CENTER);
            
            frame.addKeyListener((KeyListener)canvas);
            frame.addWindowListener(new WindowAdapter() {
               @Override
               public void windowClosing(WindowEvent e) {
                  new Thread() {
                     @Override
                     public void run() {
                        if (animator.isStarted()) animator.stop();
                        System.exit(0);
                     }
                  }.start();
               }
            });
                        
            frame.setTitle(TITLE);
            frame.pack();
            frame.setVisible(true);
            animator.start(); 
         }
      });
   }
   
   Texture cargarTextura(String imageFile){
       Texture text1 = null;
       try {
            BufferedImage buffImage = ImageIO.read(new File(imageFile));           
            text1 = AWTTextureIO.newTexture(GLProfile.getDefault(),buffImage,false);
       } catch (IOException ioe){
           System.out.println("Problema al cargar el archivo "+imageFile);
       }  
       return text1;
   }
  
 
   private GLU glu;  
   private GLUT glut;
 
   public CuboConTextura() {
      this.addGLEventListener(this);
      this.addKeyListener(this);
   }
 

   @Override
   public void init(GLAutoDrawable drawable) {
      GL2 gl = drawable.getGL().getGL2();      
      glu = new GLU();                       
      glut = new GLUT();
      gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
      gl.glClearDepth(1.0f);     
      gl.glEnable(GL_DEPTH_TEST); 
      gl.glDepthFunc(GL_LEQUAL);  
      gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
      
      float[] whiteMaterial={1.0f, 1.0f, 1.0f};
      gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, whiteMaterial,0);
      gl.glShadeModel(GL_SMOOTH); 
 
    
      float[] ambientLight = { 0.5f, .5f, .5f,0.5f };  // weak RED ambient 
      gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambientLight, 0); 
      float[] diffuseLight = { .8f,.8f,.8f,0f };  // multicolor diffuse 
      gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuseLight, 0); 
      float[] specularLight = { 1f,1f,1f,0f };  // specular 
      gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, specularLight, 0);       
              
      gl.glEnable(GL2.GL_LIGHTING);      
      gl.glEnable(GL2.GL_LIGHT0);
      
      //this.textura1 = this.cargarTextura("imagenes/ojobonitom.jpg");
      //this.textura1 = this.cargarTextura("imagenes/wha.jpg");
      this.textura1 = this.cargarTextura("imagenes/speed.jpg");
      //this.textura1 = this.cargarTextura("imagenes/ye.jpg");
      
    
      gl.glEnable(GL2.GL_TEXTURE_2D);
      gl.glEnable(GL2.GL_BLEND);

     
      gl.glBlendFunc(GL2.GL_ONE, GL2.GL_ONE_MINUS_SRC_ALPHA);       
   }
 
   
   @Override
   public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
      GL2 gl = drawable.getGL().getGL2(); 
 
      if (height == 0) height = 1;  
      float aspect = (float)width / height;
 
      
      gl.glViewport(0, 0, width, height);
 
     
      gl.glMatrixMode(GL_PROJECTION);  
      gl.glLoadIdentity();            
      glu.gluPerspective(fovy, aspect, 0.1, 50.0); 
 
      
      gl.glMatrixMode(GL_MODELVIEW);
      gl.glLoadIdentity(); // reset
   }
 
   
   @Override
   public void display(GLAutoDrawable drawable) {
       
        float aspect = (float)this.getWidth() / this.getHeight();
        GL2 gl = drawable.getGL().getGL2(); 
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); 
        float[] lightPos = { 0.0f,5.0f,10.0f,1 };
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION,lightPos, 0);

        gl.glLoadIdentity();  
        gl.glEnable(GL.GL_LINE_SMOOTH);
        gl.glEnable(GL.GL_BLEND);


        gl.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_DONT_CARE);

        glu.gluLookAt(0.0, 0.0, 5.0, this.posCamX, this.posCamY, this.posCamZ, 0.0, 1.0, 0.0);
        
        if (rotX<0) rotX=360-factInc;
        if (rotY<0) rotY=360-factInc;
        if (rotZ<0) rotZ=360-factInc;

        if (rotX>=360) rotX=0;
        if (rotY>=360) rotY=0;
        if (rotZ>=360) rotZ=0;

       
        gl.glRotatef(rotX, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(rotY, 0.0f, 1.0f, 0.0f);
        gl.glRotatef(rotZ, 0.0f, 0.0f, 1.0f);
      
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        
        float no_mat[] = { 0.0f, 0.0f, 0.0f, 1.0f };
        float mat_ambient[] = { 1.0f, 1.0f, 1.0f, 1.0f };
        float mat_ambient_color[] = { 1.0f, 1.0f, 1.0f, 1.0f };
        float mat_diffuse[] = { 1.0f, 1.0f, 1.0f, 1.0f };
        float mat_specular[] = { 1.0f, 1.0f, 1.0f, 1.0f };
        float no_shininess[] = { 0.0f };
        float low_shininess[] = { 5.0f };
        float high_shininess[] = { 100.0f };
        float mat_emission[] = { 0.5f, 0.5f, 0.5f, 0.0f };

        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, no_mat, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, mat_diffuse, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, no_mat, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SHININESS, high_shininess, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_EMISSION, mat_emission, 0);

        // Asociar la textura con el canvas
        this.textura1.bind(gl);
        this.textura1.enable(gl); 
       
        this.drawCube(gl);
        
        this.textura1.disable(gl);
        
        
      
        gl.glFlush();
        
        this.rotY += 0.5f;
      
   }

    void drawCube(GL2 gl){     

        gl.glBegin(GL2.GL_QUADS);
        // Cara Frontal
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f,  1.0f,  1.0f);	// Bottom Left
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, -1.0f,  1.0f);	// Top Left
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( 1.0f, -1.0f,  1.0f);	// Top Right
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( 1.0f,  1.0f,  1.0f);	// Bottom Right
        
         // Cara trasera
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, -1.0f, -1.0f);    // Bottom Right
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f,  1.0f, -1.0f);    // Top Right
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f( 1.0f,  1.0f, -1.0f);    // Top Left
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f( 1.0f, -1.0f, -1.0f);    // Bottom Left
        
        // Cara superior
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f,  1.0f, -1.0f);	// Top Left
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f,  1.0f,  1.0f);	// Bottom Left
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f( 1.0f,  1.0f,  1.0f);	// Bottom Right
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f( 1.0f,  1.0f, -1.0f);	// Top Right
        
        // Cara inferior
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f, -1.0f);	// Top Right
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( 1.0f, -1.0f, -1.0f);	// Top Left
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f( 1.0f, -1.0f,  1.0f);	// Bottom Left
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f,  1.0f);	// Bottom Right
     
        // Cara derecha
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( 1.0f, -1.0f, -1.0f);	// Bottom Right
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( 1.0f,  1.0f, -1.0f);	// Top Right
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f( 1.0f,  1.0f,  1.0f);	// Top Left
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f( 1.0f, -1.0f,  1.0f);	// Bottom Left
  
        // Cara Izquierda
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, -1.0f, -1.0f);	// Bottom Left
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, -1.0f,  1.0f);	// Bottom Right
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f,  1.0f,  1.0f);	// Top Right
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f,  1.0f, -1.0f);	// Top Left
        
        
       
        gl.glEnd();
    }

   
    public static void drawCubeUVWmapped(GL2 gl) {
        gl.glBegin(GL2.GL_QUADS);
        
        gl.glTexCoord2f(0.0f, .33f); gl.glVertex3f(-1.0f, -1.0f, -1.0f);	// Bottom Left
        gl.glTexCoord2f(.33f, .33f); gl.glVertex3f(-1.0f, -1.0f,  1.0f);	// Bottom Right
        gl.glTexCoord2f(.33f, .66f); gl.glVertex3f(-1.0f,  1.0f,  1.0f);	// Top Right
        gl.glTexCoord2f(0.0f, .66f); gl.glVertex3f(-1.0f,  1.0f, -1.0f);	// Top Left
        
        gl.glTexCoord2f(.33f, .33f); gl.glVertex3f(-1.0f, -1.0f,  1.0f);	// Bottom Left
        gl.glTexCoord2f(.66f, .33f); gl.glVertex3f( 1.0f, -1.0f,  1.0f);	// Bottom Right
        gl.glTexCoord2f(.66f, .66f); gl.glVertex3f( 1.0f,  1.0f,  1.0f);	// Top Right
        gl.glTexCoord2f(.33f, .66f); gl.glVertex3f(-1.0f,  1.0f,  1.0f);	// Top Left
        
        gl.glTexCoord2f(.66f, .33f); gl.glVertex3f( 1.0f, -1.0f,  1.0f);	// Bottom Left
        gl.glTexCoord2f(1.0f, .33f); gl.glVertex3f( 1.0f, -1.0f, -1.0f);	// Bottom Right
        gl.glTexCoord2f(1.0f, .66f); gl.glVertex3f( 1.0f,  1.0f, -1.0f);	// Top Right
        gl.glTexCoord2f(.66f, .66f); gl.glVertex3f( 1.0f,  1.0f,  1.0f);	// Top Left
        
        gl.glTexCoord2f(.33f, .66f); gl.glVertex3f(-1.0f,  1.0f,  1.0f);	// Bottom Left
        gl.glTexCoord2f(.66f, .66f); gl.glVertex3f( 1.0f,  1.0f,  1.0f);	// Bottom Right
        gl.glTexCoord2f(.66f, 1.0f); gl.glVertex3f( 1.0f,  1.0f, -1.0f);	// Top Right
        gl.glTexCoord2f(.33f, 1.0f); gl.glVertex3f(-1.0f,  1.0f, -1.0f);	// Top Left
        
        gl.glTexCoord2f(.66f, .33f); gl.glVertex3f( 1.0f, -1.0f,  1.0f);	// Bottom Left
        gl.glTexCoord2f(.33f, .33f); gl.glVertex3f(-1.0f, -1.0f,  1.0f);	// Bottom Right
        gl.glTexCoord2f(.33f, 0.0f); gl.glVertex3f(-1.0f, -1.0f, -1.0f);	// Top Right
        gl.glTexCoord2f(.66f, 0.0f); gl.glVertex3f( 1.0f, -1.0f, -1.0f);	// Top Left
        
        gl.glTexCoord2f(.66f, 0.0f); gl.glVertex3f( 1.0f, -1.0f, -1.0f);	// Bottom Left
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f, -1.0f);	// Bottom Right
        gl.glTexCoord2f(1.0f, .33f); gl.glVertex3f(-1.0f,  1.0f, -1.0f);	// Top Right
        gl.glTexCoord2f(.66f, .33f); gl.glVertex3f( 1.0f,  1.0f, -1.0f);	// Top Left
        gl.glEnd();
    }
   
    void drawPyramid(float x,float y,float z,float w,float h,GL2 gl){
      float mw=w/2;
      float mh=h/2;
      float xi,xd,ys,yi,zf,zp;
      
      xi=x-mw;
      xd=x+mw;
              
      yi=y-mh;
      ys=y+mh;
              
      zf=z+mw;
      zp=z-mw;              
                   
      gl.glBegin(GL_TRIANGLES);
         gl.glColor3f(1.0f,0,0);
         gl.glVertex3f(xi, yi, zf);
         gl.glColor3f(0,1.0f,0);
         gl.glVertex3f(x, ys, z);                 
         gl.glColor3f(0,0,1.0f);
         gl.glVertex3f(xd, yi, zf);
      gl.glEnd();
      
      gl.glBegin(GL_TRIANGLES); 
         gl.glColor3f(0.0f,0,1.0f);
         gl.glVertex3f(xd,yi,zf);
         gl.glColor3f(0,1.0f,0);
         gl.glVertex3f(x, ys, z);
         gl.glColor3f(1.0f,0,0);
         gl.glVertex3f(xd, yi,zp);
      gl.glEnd();
      gl.glBegin(GL_TRIANGLES); 
         gl.glColor3f(1.0f,0.0f,0.0f);
         gl.glVertex3f(xd, yi, zp);
         gl.glColor3f(0,1.0f,0);
         gl.glVertex3f(x, ys,z);
         gl.glColor3f(0.0f,0,1.0f);
         gl.glVertex3f(xi, yi, zp);
      gl.glEnd();
      gl.glBegin(GL_TRIANGLES); 
         gl.glColor3f(0.0f,0,1.0f);
         gl.glVertex3f(xi,yi, zp);
         gl.glColor3f(0,1.0f,0);
         gl.glVertex3f(x, ys, z);
         gl.glColor3f(1.0f,0,0.0f);
         gl.glVertex3f(xi, yi, zf);
      gl.glEnd();
      gl.glBegin(GL_QUADS ); 
         gl.glColor3f(1.0f,0,0);
         gl.glVertex3f(xi, yi, zf);
         gl.glColor3f(0,0.0f,1.0f);
         gl.glVertex3f(xd, yi, zf);
         gl.glColor3f(1.0f,0,0.0f);
         gl.glVertex3f(xd, yi, zp);
         gl.glColor3f(0.0f,0.0f,1.0f);
         gl.glVertex3f(xi, yi, zp);
      gl.glEnd();          
   }

   
   @Override
   public void dispose(GLAutoDrawable drawable) { }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int cod = e.getKeyCode();
        switch(cod){
            case KeyEvent.VK_UP:
                posCamY +=0.5f;
                break;
            case KeyEvent.VK_DOWN:    
                posCamY -=0.5f;
                break;                
            case KeyEvent.VK_LEFT:
                posCamX -=0.5f;
                break;                
            case KeyEvent.VK_RIGHT:                
                posCamX +=0.5f;                
                break;                
            case KeyEvent.VK_F1:
                    fovy+=factInc; break;
            case KeyEvent.VK_F2:
                    fovy-=factInc; break;   
            case KeyEvent.VK_3:
                switch(eje){
                    case 1:
                        rotX+=factInc; break;
                    case 2:
                        rotY+=factInc; break;
                    case 3:
                        rotZ+=factInc; break;                        
                }
                break;
            case KeyEvent.VK_4:
                switch(eje){
                    case 1:
                        rotX-=factInc; break;
                    case 2:
                        rotY-=factInc; break;
                    case 3:
                        rotZ-=factInc; break;                        
                } 
                break;

            case KeyEvent.VK_X:
                    eje=1;
                    break;                               
            case KeyEvent.VK_Y:
                    eje=2;
                    break;                   
            case KeyEvent.VK_Z:
                    eje=3;
                    break;                                   
        }
                        
        System.out.println("Typed="+e.getKeyCode()+", fovy="+fovy+", eje="+eje+" ,rotX="+rotX+" ,rotY="+rotY+" ,rotZ="+rotZ);
    }

    @Override
    public void keyReleased(KeyEvent e) {
       
    }
}