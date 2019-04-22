
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author admin
 */
public class MainFrameClass extends JFrame{
 
   MainFrameClass(String title)
   {
       // constructor creating frame and calling methods to create menubar and panels
    super(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 700);
       JMenuBar bar = new JMenuBar();
        setJMenuBar(bar);
        bar.add(makeFileMenu());
        bar.add(makeEditMenu());
        bar.add(makeAnimationMenu());
        addTopPanel();
        addCenterPanel();
   
}
   public static void main(String[] args)
   {
       JFrame f1=new MainFrameClass("Bouncing balls");
       
       f1.setVisible(true);
       
   }
       JLabel sizelabel,colorlabel,hsl,vsl,sentence;
     JSlider hs,vs; //horizontal and vertical speeds
     java.util.Timer move; // for running thread for balls
    JButton addNewBall,colorbutton;
    JSpinner Size;
    static Color selectcolor,initialcolor=Color.red;
    int StoreSize,selectedball=0;
     private List<BouncingBall> Bouncingballs = new ArrayList<>();
     static JPanel centerPanel;
     BouncingBall storeTempBall; //used for cut and paste the ball
    public void addTopPanel() {
        //adding the controls for balls 
    JPanel top =new JPanel() ;
    top.setLayout(new BorderLayout());
    final JPanel left=new JPanel();
    left.setBorder(new TitledBorder("Ball Creation"));
    left.setLayout(new BorderLayout());
    Box bleft1=Box.createHorizontalBox();
    sentence=new JLabel("Balls are introduced into the game at the center point");bleft1.add(sentence);
    left.add(bleft1,BorderLayout.NORTH);   
    Box bleft2=Box.createHorizontalBox();
    sizelabel=new JLabel("Size:");bleft2.add(sizelabel);
     SpinnerNumberModel sizeModel = new SpinnerNumberModel(25, 5, 100, 1);
     Size=new JSpinner(sizeModel);bleft2.add(Size);
    colorlabel=new JLabel("Color:");bleft2.add(colorlabel);
    colorbutton=new JButton("Select");
    selectcolor=initialcolor;
    colorbutton.setBackground(initialcolor);
    colorbutton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            
selectcolor=JColorChooser.showDialog(left,"Select a color",initialcolor);  
colorbutton.setBackground(selectcolor);    
        }
    });bleft2.add(colorbutton);
    addNewBall=new JButton("Add New Ball");
    addNewBall.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) { 
       StoreSize=(int)Size.getValue();
       //creating a new ball by calling constructor
       BouncingBall ballshape=new BouncingBall(centerPanel.getWidth()/2,centerPanel.getHeight()/2,StoreSize,selectcolor,hs.getValue(),vs.getValue());
       for(BouncingBall bb: Bouncingballs){
           bb.selectedColor=false;
       }
       Bouncingballs.add(ballshape);
       selectedball=Bouncingballs.size()-1;
       Bouncingballs.get(selectedball).selectedColor=true;
       repaint();
        }
    });bleft2.add(addNewBall);
    left.add(bleft2,BorderLayout.CENTER);
    JPanel right=new JPanel();
    right.setBorder(new TitledBorder("Current Ball Control"));
  
    //right
    {//creating sliders
        right.setLayout(new BorderLayout());
        Box rightbox1=Box.createHorizontalBox();
        hs=new JSlider(0,30,15);
        hsl=new JLabel("Horizontal Speed");
        vsl=new JLabel("Vertical Speed    ");
        vs=new JSlider(0,30,15);
        hs.setMajorTickSpacing(5);vs.setMajorTickSpacing(5);
        hs.setMinorTickSpacing(1);vs.setMinorTickSpacing(1);
        rightbox1.add(hsl);
        rightbox1.add(hs);
        
        hs.addChangeListener(new ChangeListener() {
            //setting horizontal speed
            @Override
            public void stateChanged(ChangeEvent e) {
            int xp=hs.getValue();
            Bouncingballs.get(selectedball).dx=xp;
            }
        });
        Box rightbox2=Box.createHorizontalBox();
        rightbox2.add(vsl);
        rightbox2.add(vs);
        vs.addChangeListener(new ChangeListener() {
            //setting vertical speed
            @Override
            public void stateChanged(ChangeEvent e) {
            int yp=vs.getValue();
            Bouncingballs.get(selectedball).dy=yp;
            }
        });
         right.add(rightbox1,BorderLayout.NORTH);right.add(rightbox2,BorderLayout.CENTER);
        top.add(right);
        
    }
    Box total=Box.createHorizontalBox();
    total.add(left);total.add(right);
    add(total,BorderLayout.NORTH);
    }
public void Move()
   {//method called when the start animation menu item is clicked
        move=new java.util.Timer();
        move.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() { 
                //for all items the increment values of speeds are calculated
                        for(BouncingBall b:Bouncingballs)
            {
                b.IncrementXorY(50);
            }
            repaint();
            }
        }, 0, 50);
   }
    private void addCenterPanel() {
        
     centerPanel=new JPanel(){
          @Override
        
       public void paintComponent(Graphics g) {
              for (BouncingBall shape : Bouncingballs) {
         shape.draw(g);
      }
     }
  };
    
     centerPanel.setSize(300, 100);
add(centerPanel,BorderLayout.CENTER);
    }
JFileChooser fc;File file;String fileName;ObjectInputStream readObject;
    private JMenu makeFileMenu() {
         JMenu fileMenu = new JMenu("File");
        JMenuItem Readballshapesfromfile = new JMenuItem("Read ball shapes from file");
        fileMenu.add(Readballshapesfromfile);
        //reading from the file 
        Readballshapesfromfile.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 fc=new JFileChooser();
                            int result= fc.showOpenDialog(MainFrameClass.this);
if (result == JFileChooser.APPROVE_OPTION  ) {
file =fc.getSelectedFile();

fileName=file.getAbsolutePath();

				try {
					// checking whether file exists and readable
					FileInputStream in = new FileInputStream(fileName);
					readObject=new ObjectInputStream(in);
                                        //converting into BouncingBall class objects
                                        Bouncingballs=(ArrayList<BouncingBall>)readObject.readObject();

				} catch (Exception eh) {
					eh.printStackTrace();
				}
                                
             }
repaint();
             }
         });
        //Writing to a file
        JMenuItem Writeballshapestofile = new JMenuItem("Write ball shapes to file");
        fileMenu.add(Writeballshapestofile);
        Writeballshapestofile.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 fc=new JFileChooser();
                            int result= fc.showSaveDialog(MainFrameClass.this);
if (result == JFileChooser.APPROVE_OPTION  ) {
file =fc.getSelectedFile();
try{
FileOutputStream writefile = new FileOutputStream(file);
//output all the objects into the file 
				ObjectOutputStream outputobject = new ObjectOutputStream(writefile);
				outputobject.writeObject(Bouncingballs);

}
catch (Exception eh) {
					eh.printStackTrace();
				}
}

             }
         });
        JMenuItem exit = new JMenuItem("Exit");
        fileMenu.add(exit);
        exit.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 System.exit(0);
             }
         });
        
        return fileMenu;  
    }

    public JMenu makeEditMenu() {
       
    JMenu EditMenu =new JMenu("Edit");
    JMenuItem next=new JMenuItem("next");
    EditMenu.add(next);
    next.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if((selectedball<Bouncingballs.size()-1))
            {
                if(selectedball>=0)
                Bouncingballs.get(selectedball).selectedColor=false;
        
                selectedball++;
            Bouncingballs.get(selectedball).selectedColor=true;
            repaint();
            }
        }
    });
     JMenuItem previous=new JMenuItem("previous");
    EditMenu.add(previous);
    previous.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if((selectedball<=Bouncingballs.size()-1)&&(selectedball >= 0))
            {
                Bouncingballs.get(selectedball).selectedColor=false;
            selectedball--;
            if(selectedball >= 0)
            Bouncingballs.get(selectedball).selectedColor=true;
            repaint();
        }
        }
    });
     JMenuItem cut=new JMenuItem("cut");
     JMenuItem paste=new JMenuItem("paste");paste.setEnabled(false);
    EditMenu.add(cut);
    cut.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            //storing all the information of the cut ball
            storeTempBall= new BouncingBall(Bouncingballs.get(selectedball).x
                    ,Bouncingballs.get(selectedball).y,
                    Bouncingballs.get(selectedball).r,
                    Bouncingballs.get(selectedball).color,
                    Bouncingballs.get(selectedball).dx,Bouncingballs.get(selectedball).dy);
            Bouncingballs.remove(selectedball);
            selectedball--;
            if(selectedball>=0)
            Bouncingballs.get(selectedball).selectedColor=true;repaint();
            paste.setEnabled(true);
        }
    });
     
    EditMenu.add(paste);
    paste.addActionListener(new ActionListener() {
        //pasting the ball using storeTempBall object
        @Override
        public void actionPerformed(ActionEvent e) {
            if(storeTempBall != null){
                if(selectedball>=0){
            Bouncingballs.get(selectedball).selectedColor=false;
        }
                selectedball++;
            Bouncingballs.add(selectedball,storeTempBall);
            Bouncingballs.get(selectedball).selectedColor=true;repaint();
            storeTempBall=null;
            paste.setEnabled(false);
        }
        }
    });
    return EditMenu;
    }

    public JMenu makeAnimationMenu() { 
    JMenu AnimationMenu =new JMenu("Animation");
     JMenuItem start=new JMenuItem("start");
     JMenuItem stop=new JMenuItem("stop");stop.setEnabled(false);
    AnimationMenu.add(start);
    start.addActionListener(new ActionListener() {
        //starting the animation
        @Override
        public void actionPerformed(ActionEvent e) {
       Move();
       stop.setEnabled(true);
       start.setEnabled(false);
        }
    });
     
    AnimationMenu.add(stop);
     stop.addActionListener(new ActionListener() {
         //stopping the animation
        @Override
        public void actionPerformed(ActionEvent e) {
        move.cancel();start.setEnabled(true);stop.setEnabled(false);
        }
    });
    return AnimationMenu;
    }


}