
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.GL;
import static com.jogamp.opengl.GL.GL_DEPTH_TEST;
import static com.jogamp.opengl.GL.GL_LEQUAL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

import com.jogamp.opengl.awt.GLJPanel;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;
import com.jogamp.opengl.util.FPSAnimator;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

class Light extends GLJPanel implements GLEventListener, KeyListener {

    private static String TITLE = "Iluminacion Cono de Helado :P";  
    private static final int CANVAS_WIDTH = 840; 
    private static final int CANVAS_HEIGHT = 680; 
    private static final int FPS = 120; 
    private static final float factInc = 5.0f;
    private float fovy = 45.0f;

    //////////////// Variables /////////////////////////
    // Referencias de rotacion
    float rotX = 90.0f;
    float rotY = 0.0f;
    float rotZ = 0.0f;

    // Posicion de la luz.
    float lightX = 1f;
    float lightY = 1f;
    float lightZ = 1f;
    float dLight = 0.05f;

    // Posicion de la camara
    float camX = 2.0f;
    float camY = 2.0f;
    float camZ = 8.0f;

    final float ambient[] = {0.282f, 0.427f, 0.694f, 1.0f};
    final float position[] = {lightX, lightY, lightZ, 1.0f};

    //                                R    G    B    A
    final float[] colorBlack = {0.0f, 0.0f, 0.0f, 1.0f};
    final float[] colorWhite = {1.0f, 1.0f, 1.0f, 1.0f};
    final float[] colorGray = {0.4f, 0.4f, 0.4f, 1.0f};
    final float[] colorDarkGray = {0.2f, 0.2f, 0.2f, 1.0f};
    final float[] colorRed = {1.0f, 0.0f, 0.0f, 1.0f};
    final float[] colorGreen = {0.0f, 1.0f, 0.0f, 1.0f};
    final float[] colorBlue = {0.0f, 0.0f, 0.6f, 1.0f};
    final float[] colorYellow = {1.0f, 1.0f, 0.0f, 1.0f};
    final float[] colorLightYellow = {0.5f, 0.5f, 0.0f, 1.0f};
    final float[] colorPurple = {0.5f, 0.0f, 0.5f, 1.0f};
    final float[] colorTurquoise = {0.0f, 1.0f, 1.0f, 1.0f};
    final float[] colorPink = {1.0f, 0.5f, 0.5f, 1.0f};
    final float[] colorLightBlue = {0.5f, 0.5f, 1.0f, 1.0f};


        
    final float mat_diffuse[] = {0.6f, 0.6f, 0.6f, 1.0f};
    final float mat_specular[] = {1.0f, 1.0f, 1.0f, 1.0f};
    final float mat_shininess[] = {50.0f};
    private float aspect;

  
    public Light() {
        this.addGLEventListener(this);
        this.addKeyListener(this);
    }

    private GLU glu;  
    private GLUT glut;

    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        // Establece un material por default.
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f); 
        
        gl.glClearDepth(1.0f);      
        gl.glEnable(GL_DEPTH_TEST); 
        gl.glDepthFunc(GL_LEQUAL);  
        gl.glShadeModel(GL_SMOOTH); 

        // Alguna luz de ambiente global.
        gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, this.ambient, 0);

        gl.glEnable(GL2.GL_LIGHTING);

        gl.glEnable(GL2.GL_LIGHT0);

        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, colorWhite, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, colorWhite, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, position, 0);

        this.initPosition(gl);

        glu = new GLU();                       
        glut = new GLUT();
    }

    public void initPosition(GL2 gl) {
        float posLight1[] = {lightX, lightY, lightZ, 1.0f};
        float spotDirection1[] = {0.0f, -1.f, 0.f};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, posLight1, 0);
    }

    public void moveLightX(boolean positivDirection) {
        lightX += positivDirection ? dLight : -dLight;
    }

    public void moveLightY(boolean positivDirection) {
        lightY += positivDirection ? dLight : -dLight;
    }

    public void moveLightZ(boolean positivDirection) {
        lightZ += positivDirection ? dLight : -dLight;
    }

    public void animate(GL2 gl, GLU glu, GLUT glut) {
        float posLight0[] = {lightX, lightY, lightZ, 1.f};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, posLight0, 0);
        drawLight(gl, glu, glut);
       
    }


    public void setLightSphereMaterial(GL2 gl, int face) {
        gl.glMaterialfv(face, GL2.GL_AMBIENT, colorPurple, 0);
        gl.glMaterialfv(face, GL2.GL_DIFFUSE, colorPurple, 0);
        gl.glMaterialfv(face, GL2.GL_SPECULAR, colorPurple, 0);
        gl.glMateriali(face, GL2.GL_SHININESS, 100);
        gl.glMaterialfv(face, GL2.GL_EMISSION, colorPurple, 0);
        
    }

    public void setSomeMaterial(GL2 gl, int face, float rgba[], int offset) {
        gl.glMaterialfv(face, GL2.GL_AMBIENT, rgba, offset);
        gl.glMaterialfv(face, GL2.GL_DIFFUSE, rgba, offset);
        gl.glMaterialfv(face, GL2.GL_SPECULAR, rgba, offset);
        gl.glMaterialfv(face, GL2.GL_SHININESS, rgba, offset);
        gl.glMateriali(face, GL2.GL_SHININESS, 4);
        gl.glMaterialfv(face, GL2.GL_EMISSION, colorBlack, 0);
    }

    public void setSomeMaterial(GL2 gl, int face) {
        gl.glMaterialfv(face, GL2.GL_DIFFUSE, mat_diffuse, 0);
        gl.glMaterialfv(face, GL2.GL_SPECULAR, mat_specular, 0);
        gl.glMaterialfv(face, GL2.GL_SHININESS, mat_shininess, 0);
    }

    public void setSomePinkMaterial(GL2 gl, int face) {
        gl.glMaterialfv(face, GL2.GL_AMBIENT, colorPink, 0);
        gl.glMaterialfv(face, GL2.GL_DIFFUSE, colorPink, 0);
        gl.glMaterialfv(face, GL2.GL_SPECULAR, colorPink, 0);
        gl.glMateriali(face, GL2.GL_SHININESS, 4);
        gl.glMaterialfv(face, GL2.GL_EMISSION, colorBlack, 0);
    }
    
    public void setSomeBrownMaterial(GL2 gl, int face) {
       float[] colorBrown = {0.6f, 0.3f, 0.0f, 1.0f}; // Valores RGB para el color café

       gl.glMaterialfv(face, GL2.GL_AMBIENT, colorBrown, 0);
       gl.glMaterialfv(face, GL2.GL_DIFFUSE, colorBrown, 0);
       gl.glMaterialfv(face, GL2.GL_SPECULAR, colorBrown, 0);
       gl.glMateriali(face, GL2.GL_SHININESS, 4);
       gl.glMaterialfv(face, GL2.GL_EMISSION, colorBlack, 0);
    }

    public void setSomeYellowMaterial(GL2 gl, int face) {
        gl.glMaterialfv(face, GL2.GL_AMBIENT, colorBlack, 0);
        gl.glMaterialfv(face, GL2.GL_DIFFUSE, colorLightYellow, 0);
        gl.glMaterialfv(face, GL2.GL_SPECULAR, colorYellow, 0);
        gl.glMateriali(face, GL2.GL_SHININESS, 5);
        gl.glMaterialfv(face, GL2.GL_EMISSION, colorBlack, 0);
    }
   
    public void drawLight(GL2 gl, GLU glu, GLUT glut) {
        setLightSphereMaterial(gl, GL.GL_FRONT);
        gl.glPushMatrix();
        {
            gl.glTranslatef(lightX, lightY, lightZ);
            glut.glutSolidSphere(0.1f, 20, 20);
        }
        gl.glPopMatrix();
    }
    
    

    public void drawFigure(GL2 gl, GLUT glut) {
    // Dibuja una esfera en lugar del cubo
    this.setSomePinkMaterial(gl, GL.GL_FRONT);
    gl.glTranslatef(0.0f, 0.0f, 0.0f);
    glut.glutSolidSphere(1.0, 20, 20); // Usa glutSolidSphere para dibujar una esfera
    
    this.setSomeBrownMaterial(gl, GL.GL_FRONT);
    gl.glTranslatef(0.0f, 1f, 0.0f);
    glut.glutSolidSphere(1.0, 20, 20); // Usa glutSolidSphere para dibujar una esfera
    // Dibuja un cono en lugar de la esfera
    this.setSomeYellowMaterial(gl, GL.GL_FRONT);
    gl.glTranslatef(0.0f, -1.5f, 0.0f);
    // Rotar el cono 90 grados alrededor del eje x para que esté en posición vertical
    gl.glRotatef(90.0f, 2.0f, 0.0f, 0.0f);
    glut.glutSolidCone(0.9, 3.5, 20, 20);
    
}

    

    @Override
    public void dispose(GLAutoDrawable glad) {

    }

    @Override
    public void display(GLAutoDrawable glad) {

        GL2 gl = glad.getGL().getGL2();  
        
        gl.glMatrixMode(GL_PROJECTION);  
        gl.glLoadIdentity();
        glu.gluPerspective(fovy, aspect, 0.1, 20.0);
        glu.gluLookAt(this.camX, this.camY, this.camZ, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);

        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity();  

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

      
        
        gl.glTranslatef(-2.0f, 0.0f, -2.0f);
        this.drawFigure(gl, glut);

        this.animate(gl, this.glu, this.glut);

        gl.glFlush();

    }

    @Override
    public void reshape(GLAutoDrawable glad, int x, int y, int width, int height) {
        GL2 gl = glad.getGL().getGL2(); 

        if (height == 0) {
            height = 1;   
        }
        aspect = (float) width / height;

        gl.glViewport(0, 0, width, height);

        gl.glMatrixMode(GL_PROJECTION);  
        gl.glLoadIdentity();             
        glu.gluPerspective(fovy, aspect, 0.1, 50.0); 

        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity(); 

    }

    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               
                GLJPanel canvas = new Light();
                canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
                
                final FPSAnimator animator = new FPSAnimator(canvas, FPS, true);

                final JFrame frame = new JFrame(); 

                FlowLayout fl = new FlowLayout();
                frame.setLayout(fl);
                
                frame.getContentPane().add(canvas);

                frame.addKeyListener((KeyListener) canvas);

                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        new Thread() {
                            @Override
                            public void run() {
                                if (animator.isStarted()) {
                                    animator.stop();
                                }
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

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int codigo = e.getKeyCode();
        System.out.println("codigo presionado = " + codigo);

        switch (codigo) {
            case KeyEvent.VK_DOWN:                             //Estas teclas nos dejaran cambiar la posicion de la luz asi dejandonos moverla en todo el entorno de las figuras de este escenario
                this.moveLightY(false);
                break;
            case KeyEvent.VK_UP:
                this.moveLightY(true);
                break;
            case KeyEvent.VK_RIGHT:
                this.moveLightX(true);
                break;
            case KeyEvent.VK_LEFT:
                this.moveLightX(false);
                break;
            case KeyEvent.VK_ENTER:                            //Este boton hara que la luz se dirija hacia atras dejando de ilimunar poco a poco parte delantera der las figuras 
                this.moveLightZ(false);
                break;
            case KeyEvent.VK_DELETE:                           //Este es el boton suprimir a la hora de presionarlo la luz se ira hacia el frente iluminando la parte delantera de las figuras
                this.moveLightZ(true);
                break;

            case KeyEvent.VK_NUMPAD8:                          //Esta tecla junto al igual que las demas del pad numerico haran rotar la figura sobre su eje asi dandonos la vista desde cualquier punto a 360°
                this.camY += 0.2f;
                break;
            case KeyEvent.VK_NUMPAD2:
                this.camY -= 0.2f;
                break;
            case KeyEvent.VK_NUMPAD6:
                this.camX += 0.2f;
                break;
            case KeyEvent.VK_NUMPAD4:
                this.camX -= 0.2f;
                break;
            case KeyEvent.VK_Z:                                //Las teclas A y Z hacen acercamiento y alejamiento a la figura asi dejandonos ver cada detalle en la figura               
                this.camZ += 0.2f;
                break;
            case KeyEvent.VK_A:                                
                this.camZ -= 0.2f;
                break;
        }
        System.out.println("rotX = " + rotX);
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
