import java.net.*;
import java.io.*;
import java.applet.*;
import java.awt.*;
public class ChatClient extends Applet
{  private Socket socket              = null;
   private DataInputStream  console   = null;
   private DataOutputStream streamOut = null;
   private ChatClientThread client    = null;
   private TextArea  display = new TextArea();
   private TextField input   = new TextField();
   private Button    send    = new Button("Send"), connect = new Button("Connect"),
                     quit    = new Button("Bye");
   private String    serverName = "localhost";
   private int       serverPort = 4444;

   public void init()
   {  Panel keys = new Panel(); keys.setLayout(new GridLayout(1,2));
      keys.add(quit); keys.add(connect);
      Panel south = new Panel(); south.setLayout(new BorderLayout());
      south.add("West", keys); south.add("Center", input);  south.add("East", send);
      Label title = new Label("Simple Chat Client Applet", Label.CENTER);
      title.setFont(new Font("Helvetica", Font.BOLD, 14));
      setLayout(new BorderLayout());
      add("North", title); add("Center", display);  add("South",  south);
      quit.disable(); send.disable(); getParameters(); }
   public boolean action(Event e, Object o)
   {  if (e.target == quit)
      {  input.setText(".bye");
         send();  quit.disable(); send.disable(); connect.enable(); }
      else if (e.target == connect)
      {  connect(serverName, serverPort); }
      else if (e.target == send)
      {  send(); input.requestFocus(); }
      return true; }
   public void connect(String serverName, int serverPort)
   {  println("Establishing connection. Please wait ...");
      try
      {  socket = new Socket(serverName, serverPort);
         println("Connected: " + socket);
         open(); send.enable(); connect.disable(); quit.enable(); }
      catch(UnknownHostException uhe)
      {  println("Host unknown: " + uhe.getMessage()); }
      catch(IOException ioe)
      {  println("Unexpected exception: " + ioe.getMessage()); } }
   private void send()
   {  try
      {  streamOut.writeUTF(input.getText()); streamOut.flush(); input.setText(""); }
      catch(IOException ioe)
      {  println("Sending error: " + ioe.getMessage()); close(); } }
   public void handle(String msg)
   {  if (msg.equals(".bye"))
      {  println("Good bye. Press RETURN to exit ...");  close(); }
      else println(msg); }
   public void open()
   {  try
      {  streamOut = new DataOutputStream(socket.getOutputStream());
         client = new ChatClientThread(this, socket); }
      catch(IOException ioe)
      {  println("Error opening output stream: " + ioe); } }
   public void close()
   {  try
      {  if (streamOut != null)  streamOut.close();
         if (socket    != null)  socket.close(); }
      catch(IOException ioe)
      {  println("Error closing ..."); }
      client.close();  client.stop(); }
   private void println(String msg)
   {  display.appendText(msg + "\n"); }
   public void getParameters()
   {  serverName = getParameter("host");
      serverPort = Integer.parseInt(getParameter("port")); }
} 
