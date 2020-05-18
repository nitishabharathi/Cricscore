/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scorereminder;

import java.io.IOException;
import org.jsoup.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author Admin
 */
class UserInterface extends JFrame {

    String apikey;
    String uniqueId;
    String countryName;
    String[] countries = {"Australia", "England", "New Zealand", "India", "Pakistan", "Sri Lanka", "Bangladesh", "West Indies", "South Africa"};

    public UserInterface(String apikey) {
        this.apikey = apikey;
        initialize();
    }

    private void initialize() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final JLabel selCountry = new JLabel();
        final JLabel label_img = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("crichere.png")));
        final JLabel title = new JLabel();
        selCountry.setLocation(27, 20);
        selCountry.setText(" Select Country ");
        title.setFont(new java.awt.Font("Sans Serif", 1, 18));
        title.setText("                     CricHere");
        title.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        JComboBox comboBox = new JComboBox();
        comboBox.setEditable(false);
        comboBox.setLocation(50, 20);

        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(142, 142, 142)
                                                .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(86, 86, 86)
                                                .addComponent(selCountry, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(comboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(61, 61, 61)
                                                .addComponent(label_img, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(68, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(comboBox)
                                        .addComponent(selCountry, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(30, 30, 30)
                                .addComponent(label_img, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        for (String x : countries) {
            comboBox.addItem(x);
        }
        comboBox.addActionListener(new ActionListener() {

            //@Override
            public void actionPerformed(ActionEvent e) {
                JComboBox comboBox = (JComboBox) e.getSource();
                Object selectedCountry = comboBox.getSelectedItem();
                String x = selectedCountry.toString();
                countryName = x;
                comboBox.setEnabled(false);

            }
        });

        getContentPane().add(comboBox);
        getContentPane().add(selCountry);

    }

    public void setApiKey(String apikey) {
        this.apikey = apikey;
    }

    public int getMin(int x, int y) {
        if (x > y) {
            return y;
        } else {
            return x;
        }
    }

    public void setCountryName(String x) {
        countryName = x;
    }

    String getCountryName() {
        return countryName;
    }

    public String getMatchDetails() throws IOException {

        String doc = Jsoup.connect("http://cricapi.com/api/matchCalendar?apikey=9DwaDZv2cWSWk4XFe1kR3Ff9l9n2").header("Accept", "text/javascript").ignoreContentType(true).execute().body();
        String countryName1 = countryName + " v"; 
        String countryName2 = "v " + countryName;       
        String matchdetail;
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        int cindex; 
        int cindex1; 
        int cindex2; 
        int tempIndex;
        if (doc.toLowerCase().contains(countryName1.toLowerCase())) {
            cindex1 = doc.indexOf(countryName1);

        } else {
            cindex1 = -10000;
        }
        if (doc.toLowerCase().contains(countryName2.toLowerCase())) {
            cindex2 = doc.indexOf(countryName2);
            //System.out.println(cindex2);

        } else {
            cindex2 = -10000;
        }
        if (cindex1 == -10000 && cindex2 != -10000) {
            cindex = cindex2;
            tempIndex = doc.indexOf("name",cindex-25);
            tempIndex += 7;
            cindex = tempIndex;
        } else if (cindex2 == -10000 && cindex1 != -10000) {
            cindex = cindex1;
            tempIndex = doc.indexOf("name",cindex -25);
            tempIndex += 7;
            cindex = tempIndex;

        } else {
            cindex = getMin(cindex1, cindex2);
            tempIndex = doc.indexOf("name", cindex - 25);
            //System.out.println("TEMP INDEX: " + tempIndex);
            tempIndex += 7;
            cindex = tempIndex;
        }
        for (int i = cindex; i < doc.indexOf("}", cindex); i++) {
            sb.append(doc.charAt(i));
        }

        matchdetail = sb.toString();
        System.out.println(matchdetail);
        matchdetail = matchdetail.replaceAll("\"", "");
        cindex = cindex - 70;
        for (int i = doc.indexOf("unique_id", cindex) + 11; i < doc.indexOf("name", cindex); i++) {
            sb2.append(doc.charAt(i));
        }
        uniqueId = sb2.toString();
        uniqueId = uniqueId.replace("\"", "");
        uniqueId = uniqueId.replace(",", "");
        return matchdetail;
    }

    public String getLiveScore() throws IOException {
        String stat;

        if (uniqueId.equalsIgnoreCase("will generate 1-2 days before match")) {
            stat = "SORRY MATCH HAS NOT STARTED YET";
        } else {
            String result = Jsoup.connect("http://cricapi.com/api/cricketScore?apikey=" + apikey + "&unique_id=" + uniqueId).header("Accept", "text/javascript").ignoreContentType(true).execute().body();
            stat = result;
        }
        return stat;
    }

    public String getUniqueId() {
        return uniqueId;
    }
}
