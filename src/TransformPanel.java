/*************************************************************
 * Creator: Nicholas Dower                                   *
 * Date:    Spring 2005                                      *
 * Contact: NicholasDower@gmail.com                          *
 *************************************************************/
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

public class TransformPanel extends JPanel
{
  private Selectable theSelectable;

  private OOPSButton    theDeleteButton;
  private OOPSButton    theSubtractButton;
  private OOPSButton    theIntersectButton;
  private OOPSButton    theCombineButton;
  private OOPSButton    theRedrawButton;
  private OOPSButton    theGroupButton;
  private OOPSButton    theDeGroupButton;
  private OOPSButton    theMoveUpButton;
  private OOPSButton    theMoveBackButton;
  private OOPSButton    theMoveToFrontButton;
  private OOPSButton    theMoveToBackButton;
  private OOPSButton    theMoveUpALayerButton;
  private OOPSButton    theMoveBackALayerButton;
  private OOPSButton    theFlipHorizontalButton;
  private OOPSButton    theFlipVerticalButton;

  public TransformPanel( Selectable aSelectable )
  {
    theSelectable = aSelectable;

    theDeleteButton      = new OOPSButton("BigDeleteShape.png","Delete");
    theSubtractButton    = new OOPSButton("BigSubtract.png","Subtract");
    theIntersectButton   = new OOPSButton("BigIntersect.png","Intersect");
    theCombineButton     = new OOPSButton("BigCombine.png","Combine");
    theRedrawButton      = new OOPSButton("BigRedraw.png","Re-Stroke");
    theGroupButton       = new OOPSButton("BigGroup.png","Group");
    theDeGroupButton     = new OOPSButton("BigDegroup.png","DeGroup");
    theMoveUpButton         = new OOPSButton("BigMoveUp.png","Move Up");
    theMoveBackButton       = new OOPSButton("BigMoveDown.png","Move Back");
    theMoveToFrontButton    = new OOPSButton("BigMoveToFront.png","Move To Front");
    theMoveToBackButton     = new OOPSButton("BigMoveToBack.png","Move To Back");
    theMoveBackALayerButton = new OOPSButton("BigMoveDownALayer.png","Move Back A Layer");
    theMoveUpALayerButton   = new OOPSButton("BigMoveUpALayer.png","Move Up A Layer");
    theFlipHorizontalButton = new OOPSButton("BigFlipHorizontal.png","Flip Horizontal");
    theFlipVerticalButton   = new OOPSButton("BigFlipVertical.png","Flip Vertical");
 
    ActionListener al = new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        Object o = e.getSource();
        if( o.equals(theDeleteButton) )
          theSelectable.delete();
        else if( o.equals(theSubtractButton) )
          theSelectable.subtract();
        else if( o.equals(theIntersectButton) )
          theSelectable.intersect();
        else if( o.equals(theCombineButton) )
          theSelectable.combine();
        else if( o.equals(theRedrawButton) )
          theSelectable.redraw();
        else if( o.equals(theGroupButton) )
          theSelectable.group();
        else if( o.equals(theDeGroupButton) )
          theSelectable.deGroup();
        else if( o.equals(theMoveUpButton) )
          theSelectable.moveUp();
        else if( o.equals(theMoveBackButton) )
          theSelectable.moveBack();
        else if( o.equals(theMoveToFrontButton) )
          theSelectable.moveToFront();
        else if( o.equals(theMoveToBackButton) )
          theSelectable.moveToBack();
        else if( o.equals(theMoveBackALayerButton) )
          theSelectable.moveBackALayer();
        else if( o.equals(theMoveUpALayerButton) )
          theSelectable.moveUpALayer();
        else if( o.equals(theFlipHorizontalButton) )
          theSelectable.flipHorizontal();
        else if( o.equals(theFlipVerticalButton) )
          theSelectable.flipVertical();
      }
    };
    theDeleteButton.addActionListener(al);
    theSubtractButton.addActionListener(al);
    theIntersectButton.addActionListener(al);
    theCombineButton.addActionListener(al);
    theRedrawButton.addActionListener(al);
    theGroupButton.addActionListener(al);
    theDeGroupButton.addActionListener(al);
    theMoveUpButton.addActionListener(al);
    theMoveBackButton.addActionListener(al);
    theMoveToFrontButton.addActionListener(al);
    theMoveToBackButton.addActionListener(al);
    theMoveBackALayerButton.addActionListener(al);
    theMoveUpALayerButton.addActionListener(al);
    theFlipHorizontalButton.addActionListener(al);
    theFlipVerticalButton.addActionListener(al);

    GridBagLayout gridBagLayout = new GridBagLayout();
    this.setLayout(gridBagLayout);

    GridBagConstraints gbc;


    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.BOTH;
    gridBagLayout.setConstraints(theCombineButton, gbc);
    this.add(theCombineButton);

    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.BOTH;
    gridBagLayout.setConstraints(theRedrawButton, gbc);
    this.add(theRedrawButton);

    gbc = new GridBagConstraints();
    gbc.gridx = 3;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.BOTH;
    gridBagLayout.setConstraints(theDeleteButton, gbc);
    this.add(theDeleteButton);





    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.BOTH;
    gridBagLayout.setConstraints(theSubtractButton, gbc);
    this.add(theSubtractButton);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.BOTH;
    gridBagLayout.setConstraints(theFlipHorizontalButton, gbc);
    this.add(theFlipHorizontalButton);

    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 1;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.BOTH;
    gridBagLayout.setConstraints(theGroupButton, gbc);
    this.add(theGroupButton);

    gbc = new GridBagConstraints();
    gbc.gridx = 3;
    gbc.gridy = 1;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.BOTH;
    gridBagLayout.setConstraints(theMoveUpButton, gbc);
    this.add(theMoveUpButton);

    gbc = new GridBagConstraints();
    gbc.gridx = 4;
    gbc.gridy = 1;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.BOTH;
    gridBagLayout.setConstraints(theMoveToFrontButton, gbc);
    this.add(theMoveToFrontButton);

    gbc = new GridBagConstraints();
    gbc.gridx = 5;
    gbc.gridy = 1;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.BOTH;
    gridBagLayout.setConstraints(theMoveUpALayerButton, gbc);
    this.add(theMoveUpALayerButton);



    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.BOTH;
    gridBagLayout.setConstraints(theIntersectButton, gbc);
    this.add(theIntersectButton);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.BOTH;
    gridBagLayout.setConstraints(theFlipVerticalButton, gbc);
    this.add(theFlipVerticalButton);

    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 2;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.BOTH;
    gridBagLayout.setConstraints(theDeGroupButton, gbc);
    this.add(theDeGroupButton);

    gbc = new GridBagConstraints();
    gbc.gridx = 3;
    gbc.gridy = 2;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.BOTH;
    gridBagLayout.setConstraints(theMoveBackButton, gbc);
    this.add(theMoveBackButton);

    gbc = new GridBagConstraints();
    gbc.gridx = 4;
    gbc.gridy = 2;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.BOTH;
    gridBagLayout.setConstraints(theMoveToBackButton, gbc);
    this.add(theMoveToBackButton);

    gbc = new GridBagConstraints();
    gbc.gridx = 5;
    gbc.gridy = 2;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new Insets(1,1,1,1);
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.BOTH;
    gridBagLayout.setConstraints(theMoveBackALayerButton, gbc);
    this.add(theMoveBackALayerButton);
  }
}