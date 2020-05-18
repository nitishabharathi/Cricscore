package scorereminder;

import java.io.IOException;
import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.util.concurrent.TimeUnit;
import javax.swing.*;





public class ScoreReminder{
    
    
     static void callSystemTray(final String matchTitle, final String score) throws AWTException, InterruptedException, IOException{
        if(System.getProperty("os.name").toLowerCase().contains("win")){
            if(SystemTray.isSupported()){
                SystemTray tray = SystemTray.getSystemTray();
                Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
                TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
                trayIcon.setImageAutoSize(true);
                trayIcon.setToolTip("System tray icon demo");
                tray.add(trayIcon);
                trayIcon.displayMessage(matchTitle, score, MessageType.INFO);                                
            }
            else{
                System.out.println("Your System does not support toolbar notification. :(");
                JOptionPane.showMessageDialog(null,"Your System does not support toolbar notification. :(");
            }
        }
        
    
         }
    

    public static void main(String[] args) throws Exception {
        
        String apikey = "9DwaDZv2cWSWk4XFe1kR3Ff9l9n2";
        UserInterface ca = new UserInterface(apikey);
        ca.setVisible(true);
        TimeUnit.SECONDS.sleep(12);
        ca.getMatchDetails();
        ca.getLiveScore();
        String matchTitle = ca.getMatchDetails();
        StringBuilder sb = new StringBuilder();        
        for(int i=0; i<matchTitle.indexOf("at"); i++){
            sb.append(matchTitle.charAt(i));
        }
        matchTitle = sb.toString();
        if(!ca.getUniqueId().equals("will generate 1-2 days before match")){       
            while(!ca.getUniqueId().equals("will generate 1-2 days before match")){       
                StringBuilder sb2 = new StringBuilder();
                String score = ca.getLiveScore();
                for(int i=score.indexOf("score")+8; i<score.indexOf("\"",score.indexOf("score")+9); i++){
                    sb2.append(score.charAt(i));
                }score = sb2.toString();score = score.replace("*", "");
                System.out.println(score);
                callSystemTray(matchTitle,score);
                TimeUnit.SECONDS.sleep(180); 
                }
        }else{
            callSystemTray("NEXT CRICKET MATCH", ca.getMatchDetails());
 
        }

    }
    
}
