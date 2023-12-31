package mlb;

/**
 * @author Roman Yasinovskyy
 */
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;

public class DatabaseReaderGUI extends javax.swing.JFrame {

        private final DatabaseReader db_reader = new DatabaseReader();
        private final DefaultListModel<String> listModelDivs = new DefaultListModel<String>();
        private final DefaultListModel<String> listModelTeams = new DefaultListModel<String>();
        private DefaultTableModel tableModelRoster = new DefaultTableModel();

        /**
         * Creates new form FileReaderGUI
         */
        public DatabaseReaderGUI() {
                initComponents();
                ArrayList<String> divisions = new ArrayList<>();
                long start = System.nanoTime();
                db_reader.getDivisions(divisions);
                long end = System.nanoTime();
                jLabelStatus.setText("Found " + divisions.size() + " divisions in " + (end - start) / 1000000 + " ms");
                for (String divStr : divisions) {
                        listModelDivs.addElement(divStr);
                }
                this.jListDivisions.setSelectedIndex(0);
        }

        /**
         * This method is called from within the constructor to initialize the form.
         * WARNING: Do NOT modify this code. The content of this method is always
         * regenerated by the Form Editor.
         */
        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated
        // Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                jLabelDivs = new javax.swing.JLabel();
                jScrollPaneDivisions = new javax.swing.JScrollPane();
                jListDivisions = new javax.swing.JList<String>();
                jLabelTeams = new javax.swing.JLabel();
                jScrollPaneTeams = new javax.swing.JScrollPane();
                jListTeams = new javax.swing.JList<String>();
                jLabelRoster = new javax.swing.JLabel();
                jScrollPaneRoster = new javax.swing.JScrollPane();
                jTableRoster = new javax.swing.JTable();
                jLabelInfo = new javax.swing.JLabel();
                jScrollPaneInfo = new javax.swing.JScrollPane();
                jTextAreaInfo = new javax.swing.JTextArea();
                jLabelLogo = new javax.swing.JLabel();
                jLabelStatus = new javax.swing.JLabel();

                setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
                setResizable(false);

                jLabelDivs.setText("Divisions");

                jListDivisions.setModel(listModelDivs);
                jListDivisions.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
                        public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                                jListDivisionsValueChanged(evt);
                        }
                });
                jScrollPaneDivisions.setViewportView(jListDivisions);

                jLabelTeams.setText("Teams");

                jListTeams.setModel(listModelTeams);
                jListTeams.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
                        public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                                jListTeamsValueChanged(evt);
                        }
                });
                jScrollPaneTeams.setViewportView(jListTeams);

                jLabelRoster.setText("Roster");

                jTableRoster.setModel(new javax.swing.table.DefaultTableModel(
                                new Object[][] {

                                },
                                new String[] {
                                                "Name", "Position"
                                }) {
                        Class<String>[] types = new Class[] {
                                        java.lang.String.class, java.lang.String.class
                        };
                        boolean[] canEdit = new boolean[] {
                                        false, false
                        };

                        public Class<String> getColumnClass(int columnIndex) {
                                return types[columnIndex];
                        }

                        public boolean isCellEditable(int rowIndex, int columnIndex) {
                                return canEdit[columnIndex];
                        }
                });
                jTableRoster.setFocusable(false);
                jScrollPaneRoster.setViewportView(jTableRoster);

                jLabelInfo.setText("Information");

                jTextAreaInfo.setEditable(false);
                jTextAreaInfo.setColumns(20);
                jTextAreaInfo.setRows(5);
                jTextAreaInfo.setFocusable(false);
                jScrollPaneInfo.setViewportView(jTextAreaInfo);

                jLabelLogo.setMaximumSize(new java.awt.Dimension(79, 76));
                jLabelLogo.setMinimumSize(new java.awt.Dimension(79, 76));
                jLabelLogo.setPreferredSize(new java.awt.Dimension(79, 76));

                jLabelStatus.setText("Status");

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(layout.createSequentialGroup()
                                                                                                .addGroup(layout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                                                false)
                                                                                                                .addComponent(jLabelTeams)
                                                                                                                .addComponent(jLabelDivs)
                                                                                                                .addComponent(jScrollPaneDivisions,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                150,
                                                                                                                                Short.MAX_VALUE)
                                                                                                                .addComponent(jScrollPaneTeams,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                150,
                                                                                                                                Short.MAX_VALUE))
                                                                                                .addGap(18, 18, 18)
                                                                                                .addGroup(layout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                .addGroup(layout.createSequentialGroup()
                                                                                                                                .addComponent(jLabelRoster)
                                                                                                                                .addGap(0, 0, Short.MAX_VALUE))
                                                                                                                .addComponent(jScrollPaneRoster,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                326,
                                                                                                                                Short.MAX_VALUE))
                                                                                                .addGroup(layout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                .addGroup(layout.createSequentialGroup()
                                                                                                                                .addPreferredGap(
                                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                                                .addComponent(jLabelInfo))
                                                                                                                .addGroup(layout.createSequentialGroup()
                                                                                                                                .addGap(12, 12, 12)
                                                                                                                                .addGroup(layout.createParallelGroup(
                                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                                                .addComponent(jLabelLogo,
                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                                .addComponent(jScrollPaneInfo,
                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                                                                .addGap(18, 18, 18))
                                                                                .addGroup(layout.createSequentialGroup()
                                                                                                .addComponent(jLabelStatus)
                                                                                                .addContainerGap(
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                Short.MAX_VALUE)))));
                layout.setVerticalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout
                                                                .createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(jLabelDivs,
                                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                .addGroup(layout.createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                .addComponent(jLabelRoster)
                                                                                                .addComponent(jLabelInfo)))
                                                                .addGap(7, 7, 7)
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(layout.createSequentialGroup()
                                                                                                .addComponent(jScrollPaneInfo,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                199,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                .addComponent(jLabelLogo,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addGap(21, 21, 21))
                                                                                .addGroup(layout.createSequentialGroup()
                                                                                                .addGroup(layout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                .addGroup(layout.createSequentialGroup()
                                                                                                                                .addComponent(jScrollPaneDivisions,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                140,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                .addPreferredGap(
                                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                                Short.MAX_VALUE)
                                                                                                                                .addComponent(jLabelTeams)
                                                                                                                                .addPreferredGap(
                                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                .addComponent(jScrollPaneTeams,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                140,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                                .addComponent(jScrollPaneRoster,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                0,
                                                                                                                                Short.MAX_VALUE))
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                .addComponent(jLabelStatus)
                                                                                                .addGap(7, 7, 7)))));

                pack();
        }// </editor-fold>//GEN-END:initComponents

        private void jListDivisionsValueChanged(javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_jListDivisionsValueChanged
                String divStr = jListDivisions.getSelectedValue().toString();
                listModelTeams.clear();
                ArrayList<String> teams = new ArrayList<>();
                if (!evt.getValueIsAdjusting()) {
                        long start = System.nanoTime();
                        db_reader.getTeams(divStr, teams);
                        long end = System.nanoTime();
                        jLabelStatus.setText("Found " + teams.size() + " teams in " + (end - start) / 1000000 + " ms");
                }
                for (String team : teams) {
                        listModelTeams.addElement(team);
                }
                this.jListTeams.setSelectedIndex(0);
        }// GEN-LAST:event_jListDivisionsValueChanged

        private void jListTeamsValueChanged(javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_jListTeamsValueChanged
                this.jTextAreaInfo.setText("");
                this.jLabelLogo.setText("");
                this.jLabelLogo.setIcon(null);
                this.tableModelRoster.setRowCount(0);
                if (jListTeams.isSelectionEmpty()) {
                        return;
                }
                String teamName = jListTeams.getSelectedValue().toString();
                if (!evt.getValueIsAdjusting()) {
                        long start = System.nanoTime();
                        Team team = this.db_reader.getTeamInfo(teamName);
                        long end = System.nanoTime();
                        jLabelStatus.setText("Found team information in " + (end - start) / 1000000 + " ms");
                        /* Populate roster table */
                        ArrayList<Player> roster = team.getRoster();
                        for (Player player : roster) {
                                this.tableModelRoster = (DefaultTableModel) jTableRoster.getModel();
                                this.tableModelRoster.addRow(new Object[] { player.getName(), player.getPosition() });
                        }
                        /* Populate information text area */
                        jTextAreaInfo.setText(team.toString());
                        /* Set team logo */
                        byte[] logoBytes = team.getLogo();
                        Image img = Toolkit.getDefaultToolkit().createImage(logoBytes);
                        ImageIcon logo = new ImageIcon(img);
                        this.jLabelLogo.setIcon(logo);
                }
        }// GEN-LAST:event_jListTeamsValueChanged

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
                        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
                                        .getInstalledLookAndFeels()) {
                                if ("Metal".equals(info.getName())) {
                                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                                        break;
                                }
                        }
                } catch (ClassNotFoundException ex) {
                        java.util.logging.Logger.getLogger(DatabaseReaderGUI.class.getName()).log(
                                        java.util.logging.Level.SEVERE,
                                        null, ex);
                } catch (InstantiationException ex) {
                        java.util.logging.Logger.getLogger(DatabaseReaderGUI.class.getName()).log(
                                        java.util.logging.Level.SEVERE,
                                        null, ex);
                } catch (IllegalAccessException ex) {
                        java.util.logging.Logger.getLogger(DatabaseReaderGUI.class.getName()).log(
                                        java.util.logging.Level.SEVERE,
                                        null, ex);
                } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                        java.util.logging.Logger.getLogger(DatabaseReaderGUI.class.getName()).log(
                                        java.util.logging.Level.SEVERE,
                                        null, ex);
                }
                // </editor-fold>
                // </editor-fold>
                // </editor-fold>
                // </editor-fold>

                /* Create and display the form */
                java.awt.EventQueue.invokeLater(new Runnable() {
                        public void run() {
                                new DatabaseReaderGUI().setVisible(true);
                        }
                });
        }

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JLabel jLabelDivs;
        private javax.swing.JLabel jLabelInfo;
        private javax.swing.JLabel jLabelLogo;
        private javax.swing.JLabel jLabelRoster;
        private javax.swing.JLabel jLabelStatus;
        private javax.swing.JLabel jLabelTeams;
        private javax.swing.JList<String> jListDivisions;
        private javax.swing.JList<String> jListTeams;
        private javax.swing.JScrollPane jScrollPaneDivisions;
        private javax.swing.JScrollPane jScrollPaneInfo;
        private javax.swing.JScrollPane jScrollPaneRoster;
        private javax.swing.JScrollPane jScrollPaneTeams;
        private javax.swing.JTable jTableRoster;
        private javax.swing.JTextArea jTextAreaInfo;
        // End of variables declaration//GEN-END:variables
}
