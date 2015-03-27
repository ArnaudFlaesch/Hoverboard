/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windows;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author Cavoleau
 */
public class AnswerCreator extends JPanel implements ActionListener{
    EditableText name;
    JButton delAnswer = new JButton("Suppr.");
    public AnswerCreator(String answer){
        this.setLayout(new BorderLayout());
        this.name=new EditableText(answer);
        this.add(delAnswer, BorderLayout.EAST);
        delAnswer.addActionListener(this);
        this.add(name, BorderLayout.WEST);
    }
    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == delAnswer) {
            Container parent =this.getParent();
            parent.remove(this);//retire la réponse
            parent.revalidate();
        }
    }
    
}
