package Packij.BetterGUI;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import static javax.swing.SwingUtilities.isLeftMouseButton;

public class MouseHandler implements MouseListener {


    MouseHandler(){
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (isLeftMouseButton(e)) {
            //if something is left-clicked
            if (e.getSource() instanceof PlayCard){
                //if the thing was a 'PlayCard' object, its selectCard() method is called
                ((PlayCard) e.getSource()).selectCard();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //System.out.println(e.getComponent());
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
