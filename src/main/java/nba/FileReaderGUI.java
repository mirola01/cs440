package nba;

/**
 *
 * @author Roman Yasinovskyy
 */

import javax.swing.DefaultListModel;

import java.util.ArrayList;

public class FileReaderGUI extends javax.swing.JFrame {
    League nba;
    private final DefaultListModel<String> listModelDivs = new DefaultListModel<String>();
    private final DefaultListModel<String> listModelTeams = new DefaultListModel<String>();

    /**
     * Creates new form FileReaderGUI
     */
    public FileReaderGUI() {
        initComponents();
        FileReader fr = new FileReader();

        long start = System.nanoTime();
        nba = fr.readFileTxt("data/nba/nba.txt");
        jRadioButtonTxt.setSelected(true);
        long end = System.nanoTime();
        jLabelStatus.setText("Loaded nba.txt in " + (end - start) / 1000000 + " ms");

        // nba = fr.readFileJson("teams.json");
        // nba = fr.readFileSqlite("teams.sqlite");
        ArrayList<String> divs = new ArrayList<>();

        for (Team t : nba.getTeamList()) {
            String div = t.getDivision();
            if (!divs.contains(div)) {
                divs.add(div);
                listModelDivs.addElement(div);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupSource = new javax.swing.ButtonGroup();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaTeams = new javax.swing.JTextArea();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListDivisions = new javax.swing.JList<String>();
        jScrollPane3 = new javax.swing.JScrollPane();
        jListTeams = new javax.swing.JList<String>();
        jLabelStatus = new javax.swing.JLabel();
        jRadioButtonTxt = new javax.swing.JRadioButton();
        jRadioButtonJson = new javax.swing.JRadioButton();
        jRadioButtonSqlite = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTextAreaTeams.setColumns(20);
        jTextAreaTeams.setRows(5);
        jScrollPane2.setViewportView(jTextAreaTeams);

        jListDivisions.setModel(listModelDivs);
        jListDivisions.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListDivisionsValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jListDivisions);

        jListTeams.setModel(listModelTeams);
        jListTeams.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListTeamsValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(jListTeams);

        jLabelStatus.setText("Status: ");

        buttonGroupSource.add(jRadioButtonTxt);
        jRadioButtonTxt.setText("txt");
        jRadioButtonTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonTxtActionPerformed(evt);
            }
        });

        buttonGroupSource.add(jRadioButtonJson);
        jRadioButtonJson.setText("json");
        jRadioButtonJson.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonJsonActionPerformed(evt);
            }
        });

        buttonGroupSource.add(jRadioButtonSqlite);
        jRadioButtonSqlite.setText("sqlite");
        jRadioButtonSqlite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonSqliteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout
                                                .createSequentialGroup()
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jScrollPane3,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, 270,
                                                                Short.MAX_VALUE)
                                                        .addComponent(jScrollPane2)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabelStatus)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jRadioButtonTxt)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(jRadioButtonJson)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(jRadioButtonSqlite)))
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jRadioButtonTxt)
                                        .addComponent(jRadioButtonJson)
                                        .addComponent(jRadioButtonSqlite))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0,
                                                        Short.MAX_VALUE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 119,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 239,
                                                Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabelStatus)
                                .addContainerGap()));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jListDivisionsValueChanged(javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_jListDivisionsValueChanged
        jTextAreaTeams.setText("");
        String div = jListDivisions.getSelectedValue().toString();
        listModelTeams.clear();
        for (Team t : nba.getTeamList()) {
            if (t.getDivision().equals(div)) {
                // jTextAreaTeams.append(t.getName() + "\n");
                listModelTeams.addElement(t.getName());
            }
        }
    }// GEN-LAST:event_jListDivisionsValueChanged

    private void jListTeamsValueChanged(javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_jListTeamsValueChanged
        jTextAreaTeams.setText("");
        if (jListTeams.isSelectionEmpty()) {
            return;
        }
        String teamName = jListTeams.getSelectedValue().toString();
        for (Team t : nba.getTeamList()) {
            if (t.getName().equals(teamName)) {
                jTextAreaTeams.append(
                        t.getConference() + " Conference\n" +
                                t.getArena() + "\n" +
                                t.getCity() + ", " +
                                t.getState() + "\n");
            }
        }
    }// GEN-LAST:event_jListTeamsValueChanged

    private void jRadioButtonTxtActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jRadioButtonTxtActionPerformed
        FileReader fr = new FileReader();
        long start = System.nanoTime();
        nba = fr.readFileTxt("data/nba/nba.txt");
        long end = System.nanoTime();
        jLabelStatus.setText("Loaded nba.txt in " + (end - start) / 1000000 + " ms");
    }// GEN-LAST:event_jRadioButtonTxtActionPerformed

    private void jRadioButtonJsonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jRadioButtonJsonActionPerformed
        FileReader fr = new FileReader();
        long start = System.nanoTime();
        nba = fr.readFileJson("data/nba/nba.json");
        long end = System.nanoTime();
        jLabelStatus.setText("Loaded nba.json in " + (end - start) / 1000000 + " ms");
    }// GEN-LAST:event_jRadioButtonJsonActionPerformed

    private void jRadioButtonSqliteActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jRadioButtonSqliteActionPerformed
        FileReader fr = new FileReader();
        long start = System.nanoTime();
        nba = fr.readFileSqlite("data/nba/nba.sqlite");
        long end = System.nanoTime();
        jLabelStatus.setText("Loaded nba.sqlite in " + (end - start) / 1000000 + " ms");
    }// GEN-LAST:event_jRadioButtonSqliteActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
        // (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default
         * look and feel.
         * For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Metal".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FileReaderGUI.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FileReaderGUI.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FileReaderGUI.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FileReaderGUI.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        }
        // </editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FileReaderGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupSource;
    private javax.swing.JLabel jLabelStatus;
    private javax.swing.JList<String> jListDivisions;
    private javax.swing.JList<String> jListTeams;
    private javax.swing.JRadioButton jRadioButtonJson;
    private javax.swing.JRadioButton jRadioButtonSqlite;
    private javax.swing.JRadioButton jRadioButtonTxt;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextAreaTeams;
    // End of variables declaration//GEN-END:variables
}
