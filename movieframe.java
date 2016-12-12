package test1;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class movieframe extends JFrame implements ActionListener {
  
   private static final long serialVersionUID = 1L;
   JLabel idLabel, pwLabel;
   JTextField idField;
   JPasswordField pwf;
   JPanel gridPanel1, gridPanel2, gridPanel3, gridPanel4, panel;
   JButton loginBt, nonmemBt;
   static String[] array = new String[4];
   static String ins1,ins2,ins3,ins4;
   
   public Connection dbConnect() throws SQLException {
         Connection con = DriverManager.getConnection("jdbc:mysql://45.76.204.252", "root", "-----");
         return con;
      }
      
      public boolean idCheck(String id, Connection con) throws SQLException {
         Statement stat = con.createStatement();
         ResultSet rs = stat.executeQuery("USE reservation");
         rs = stat.executeQuery("select member_id from member");
         while (rs.next()) {
            String dbId = rs.getString("member_id");
            if (dbId.equals(id)) {
               return true;
            }
         }
         return false;
      }

      public boolean pwCheck(char[] memberpw, Connection con) throws SQLException {
         String pw = "";
         for(int i = 0; i<memberpw.length; i++) {
            pw += memberpw[i];
         }
         Statement stat = con.createStatement();
         ResultSet rs = stat.executeQuery("USE reservation");
         rs = stat.executeQuery("select member_password from member");
         while (rs.next()) {
            String dbPw = rs.getString("member_password");
            if (dbPw.equals(pw)) {
               return true;
            }
         }
         return false;
      }

      public void login() throws SQLException {
         JFrame f = new JFrame();
         f.setTitle("Movie Reservation");
         gridPanel1 = new JPanel();
         gridPanel1.setLayout(new GridLayout(2, 1));
         gridPanel2 = new JPanel();
         gridPanel2.setLayout(new GridLayout(2, 1));
         gridPanel3 = new JPanel();
         gridPanel3.setLayout(new GridLayout(1, 2));
         gridPanel4 = new JPanel();
         gridPanel4.setLayout(new GridLayout(1, 2));

         panel = new JPanel();
         idLabel = new JLabel("ID");
         pwLabel = new JLabel("PW");
         idField = new JTextField();
         pwf = new JPasswordField(10);
         pwf.setEchoChar('*');
         
         Connection con = dbConnect();

         loginBt = new JButton("로그인");
         loginBt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               String memberid = idField.getText();
               @SuppressWarnings("deprecation")
               char[] memberpw = pwf.getPassword();

               try {
                  Connection con = dbConnect();
                  if (idCheck(memberid, con) && pwCheck(memberpw, con)) {
                     f.dispose();
                     Statement stat = con.createStatement();
                     ResultSet rs = stat.executeQuery("select member_number from member where member_id = '" + memberid + "'");
                     while (rs.next()) {
                        String number = rs.getString("member_number");
                        loginok(number);
                     }
                  } else {
                     JOptionPane.showMessageDialog(null, "로그인 정보가 틀립니다.", "", JOptionPane.CLOSED_OPTION);
                  }
               } catch (SQLException e1) {
                  // TODO Auto-generated catch block
                  e1.printStackTrace();
               }
            }
         });
         nonmemBt = new JButton("비회원 예매");
         nonmemBt.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
               // TODO Auto-generated method stub
               nonmember();
            }
         });
      gridPanel1.add(idLabel);
      gridPanel1.add(pwLabel);

      gridPanel2.add(idField);
      gridPanel2.add(pwf);

      gridPanel3.add(gridPanel1);
      gridPanel3.add(gridPanel2);

      gridPanel4.add(loginBt);
      gridPanel4.add(nonmemBt);

      panel.add(gridPanel3, BorderLayout.NORTH);
      panel.add(gridPanel4, BorderLayout.SOUTH);
      
      JPanel pan = new JPanel();
      pan.setLayout(new GridLayout(2, 1));
      pan.add(panel);
      
      f.add(pan);
      f.setSize(300, 200);
      f.setDefaultCloseOperation(EXIT_ON_CLOSE);
      f.setVisible(true);
   }
   class ComboAction extends JFrame{
     
   String [] com = array;
      Container c = getContentPane();
      JButton seat = new JButton("좌석선택");
      JComboBox combo,combo2;
      JLabel label;
      
      ComboAction(){
         
         c.setLayout(new BorderLayout(70,70));
         combo = new JComboBox(com);
         c.add(combo,BorderLayout.WEST);
         
         label = new JLabel("영화관 선택");
         c.add(label,BorderLayout.NORTH);
        // JComboBox combo3 = new JComboBox();
         
         //c.add(combo3);
         combo.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
               
               JComboBox cb = (JComboBox)e.getSource();
               int index = cb.getSelectedIndex();
               ins2 = array[index];
               System.out.println("cinema_area:"+ins2);
               String i = "'"+array[index]+"'";
               String sq = "select cinema_name from cinema where cinema_area=";
               System.out.println(sq+i);
               String sql = sq+i;
               
              
               
               Connect(sql);
               String [] com2 = array;
               combo2 = new JComboBox(com2);
               combo2.setLocation(110, 100);
               c.add(combo2,BorderLayout.CENTER);
               
               JComboBox cb2 = (JComboBox)e.getSource();
               int index2 = cb2.getSelectedIndex();
               ins3 = array[index];
               //array = null;
               System.out.println("cinema_name:"+ins3);
              
               setSize(300,300);
               setVisible(true);
               
            }
         });
         seat.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                  movieseat("member");
               }
            });
         setSize(300,300);
         setVisible(true);
         c.add(seat,BorderLayout.SOUTH);
        
         
        
      }
   }
   class MyItemListener implements ItemListener{

   @Override
   public void itemStateChanged(ItemEvent e) {
      // TODO Auto-generated method stub
      String sql = "select cinema_area from cinema";
      if(e.getStateChange()==ItemEvent.SELECTED){
         if(e.getItem()==array[0])
            Connect(sql);
         else if(e.getItem()==array[1])
            Connect(sql);
         else if(e.getItem()==array[2])
            Connect(sql);
         else
            Connect(sql);
      }
   }
      
   }
 
   public void nonmember() {
         JFrame f = new JFrame();
         f.setLayout(new BoxLayout(f.getContentPane(), BoxLayout.Y_AXIS));
         f.setTitle("Movie Reservation");

         f.add(reservation());
         f.setSize(300, 300);
         f.setDefaultCloseOperation(EXIT_ON_CLOSE);
         f.setVisible(true);
      }
   public JPanel reservation() {
         JPanel pan2 = new JPanel();
         JPanel pan3 = new JPanel();
         pan2.setLayout(new GridLayout(4, 1));
         pan3.setLayout(new FlowLayout());

         JCheckBox[] title = new JCheckBox[4];
         String sql = "select movie_name from movie";
      
         Connect(sql);
         MyItemListener listener = new MyItemListener();
         
         for (int i = 0; i < title.length; i++) {
            title[i] = new JCheckBox(array[i]);
            pan2.add(title[i]);
            title[i].addItemListener(listener);
         }
         // new ComboAction();
         // String str = Connect();
         class MyItemListener implements ItemListener{

           @Override
           public void itemStateChanged(ItemEvent e) {
              // TODO Auto-generated method stub
              if(e.getStateChange()==ItemEvent.SELECTED){
                 ins1 = (String) e.getItem();
                 System.out.println("movie_name:"+ins1);
              }
              
           }
              
           }
         /*
          * JList strList = new JList(array); f.add(strList);
          */

         JButton rb = new JButton("예매하기");
         rb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               new ComboAction();
               // movieseat("member");
            }
         });
         pan3.add(rb);
         
         JPanel reservationMovie = new JPanel();
         reservationMovie.setLayout(new BoxLayout(reservationMovie, BoxLayout.Y_AXIS));
         reservationMovie.add(pan2);
         reservationMovie.add(pan3);
         
         return reservationMovie;
      }

   public void loginok(String number) {
      JFrame f = new JFrame();
      f.setLayout(new BoxLayout(f.getContentPane(), BoxLayout.Y_AXIS));
      f.setTitle("Movie Reservation");
      
      JLabel welcome1 = new JLabel(number + "회원님 환영합니다.");
      JLabel welcome2 = new JLabel("적립된 마일리지는 0점 입니다.");
      JPanel loginpan = new JPanel();
      loginpan.setLayout(new FlowLayout(FlowLayout.LEFT));
      loginpan.add(welcome1);
      loginpan.add(welcome2);
      f.add(loginpan);

      f.add(reservation());
      f.setSize(300, 300);
      f.setDefaultCloseOperation(EXIT_ON_CLOSE);
      f.setVisible(true);
   }

   
   

   public void movieseat(String check) {
      JFrame f = new JFrame();
      JPanel p1 = new JPanel();
      p1.setLayout(new GridLayout(1, 3));
      JPanel p2 = new JPanel();
      p2.setLayout(new GridLayout(1, 3));
      JPanel p3 = new JPanel();
      p3.setLayout(new GridLayout(1, 3));
      JPanel p4 = new JPanel();
      p4.setLayout(new GridLayout(1, 3));

      JButton submit = new JButton("확인");
      
      submit.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            
            JOptionPane.showMessageDialog(null, "예매 완료 되었습니다.",check, JOptionPane.CLOSED_OPTION);
            
            f.dispose();
                  

         } 
         });
       ButtonGroup g= new ButtonGroup();
       
       JRadioButton A1 = new JRadioButton("A1");
       JRadioButton B1 = new JRadioButton("B1");
       JRadioButton C1 = new JRadioButton("C1");
       
       JRadioButton A2 = new JRadioButton("A2");
       JRadioButton B2 = new JRadioButton("B2");
       JRadioButton C2 = new JRadioButton("C2");
       
       JRadioButton A3 = new JRadioButton("A3");
       JRadioButton B3 = new JRadioButton("B3");
       JRadioButton C3 = new JRadioButton("C3");
       
       f.setBackground(Color.WHITE);
       f.setLayout(new GridLayout(4,1));
       
       g.add(A1);   g.add(B1);   g.add(C1);
       g.add(A2);   g.add(B2);   g.add(C2);
       g.add(A3);   g.add(B3);   g.add(C3);
         
       p1.add(A1);   p1.add(B1);   p1.add(C1);
       p2.add(A2);   p2.add(B2);   p2.add(C2);
       p3.add(A3);   p3.add(B3);   p3.add(C3);     
       
       p4.add(new JPanel());
       p4.add(submit);
       p4.add(new JPanel());
       
       f.add(p1);
       f.add(p2);
       f.add(p3);
       f.add(p4);
       f.setSize(300, 250);
       f.setVisible(true);
   }

   protected void nonmem() {
      System.out.println("비회원예매");

   }

   protected void loginCheck() {
      System.out.println("로그인");

   }

   public static String Connect(String s){
      String str="";
      String command = s;
      //String[] array=null;
      try{
         Connection con = null;

         con = DriverManager.getConnection("jdbc:mysql://45.76.204.252",
               "root", "-----");
         
         java.sql.Statement st = null;
         ResultSet rs = null;
         
         st = con.createStatement();
         rs = st.executeQuery("USE reservation");
         String query = command;
         Statement stmt = (Statement) con.createStatement();
         ResultSet rs2 = stmt.executeQuery(query);
         //String[] array;
         
         int i=0;
         while(rs2.next()) {
            str = rs2.getString(1);
            array[i]=str;
            i++;
            }
         dumpArray(array);
         }
      
      catch(SQLException e){
         e.printStackTrace();
      }
      
   
      return null;
   }
   
   private static void dumpArray(String[] array) {
      // TODO Auto-generated method stub
      for (int i = 0; i < array.length; i++){
         System.out.println("array["+i+"]="+array[i]);
      }
   }

   public static void main(String[] args) throws SQLException {
      movieframe login = new movieframe();
      login.login();
      //Connect();
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      // TODO Auto-generated method stub
      
   }
}