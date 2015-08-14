
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BatchDimen extends JFrame {
    
    JPanel projectNamePanel = new JPanel();
    
    JPanel formerPathPanel;
    JTextField formerPathtf;
    JButton formerPathButton;
    
    JPanel desPathPanel;
    JTextField desPathtf;
    JButton desPathButton;
    
    JPanel formerDimenValuePanel;
    JLabel labelFormerDimenValue = new JLabel("请输入原始dimen 的 dp 值:");
    JTextField formerDimenValuetf;
    
    JPanel desDimenValuePanel;
    JLabel labelDesDimenValue = new JLabel("请输入目标 dp 值， 以逗号区分");
    JTextField desDimenValuetf;
    
    
    JPanel generatePanel;
    JButton generateButton;
    JLabel tipLabel;
    
    String formerPath;
    String desPath;
    
    String formerDimenValue;
    String desDimenValues;
    
    CoreManager mManager;
    
    public static void main(String[] args)
    {
        new BatchDimen().luanch();
    }
    
    
    public void luanch() {
        
        mManager = new CoreManager();
        
        setTitle(" -- BatchDimen -- ");
        
        setBounds(300, 250, 520, 240);
        setDefaultCloseOperation(3);
        GridLayout gl = new GridLayout(5, 1);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(gl);
        
        
        this.formerPathPanel = new JPanel();
        this.formerPathPanel.setLayout(new FlowLayout(0));
        this.formerPathtf = new JTextField(20);
        this.formerPathtf.setEditable(false);
        this.formerPathButton = new JButton("请选择原始dimen所在文件夹地址");
        
        this.formerPathButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser choser = new JFileChooser("C:\\");
                choser.setFileSelectionMode(1);
                int result = choser.showOpenDialog(BatchDimen.this);
                if (result == 0) {
                    File file = choser.getSelectedFile();
                    formerPath = file.getAbsolutePath();
                    formerPathtf.setText(formerPath);
                    
                    desPath = file.getParentFile().getAbsolutePath();
                    desPathtf.setText(desPath);
                    
                } else if (result == 1) {
                    formerPath = null;
                    formerPathtf.setText("你没有选取目录");
                }
            }
        });
        
        formerPathPanel.add(this.formerPathtf);
        formerPathPanel.add(this.formerPathButton);
        
        this.desPathPanel = new JPanel();
        this.desPathPanel.setLayout(new FlowLayout(0));
        this.desPathtf = new JTextField(20);
        this.desPathtf.setEditable(false);
        this.desPathButton = new JButton("请选择目标dimen所在文件夹地址");
        this.desPathButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser choser = new JFileChooser("C:\\");
                choser.setFileSelectionMode(1);
                int result = choser.showOpenDialog(BatchDimen.this);
                if (result == 0) {
                    File file = choser.getSelectedFile();
                    desPath = file.getAbsolutePath();
                    desPathtf.setText(desPath);
                } else if (result == 1) {
                    desPath = null;
                    desPathtf.setText("你没有选取目录");
                }
            }
        });
        
        desPathPanel.add(this.desPathtf);
        desPathPanel.add(this.desPathButton);
        
        this.formerDimenValuePanel = new JPanel();
        this.formerDimenValuePanel.setLayout(new FlowLayout(0));
        this.formerDimenValuetf = new JTextField(20);
        formerDimenValuePanel.add(formerDimenValuetf);
        formerDimenValuePanel.add(labelFormerDimenValue);
        
        
        this.desDimenValuePanel = new JPanel();
        this.desDimenValuePanel.setLayout(new FlowLayout(0));
        this.desDimenValuetf = new JTextField(20);
        desDimenValuePanel.add(desDimenValuetf);
        desDimenValuePanel.add(labelDesDimenValue);
        
        
        this.generatePanel = new JPanel();
        this.generatePanel.setLayout(new FlowLayout(0));
        this.generateButton = new JButton("生成");
        this.tipLabel = new JLabel();
        
        this.generateButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                formerPath = formerPathtf.getText().trim();
                desPath = desPathtf.getText().trim();
                formerDimenValue = formerDimenValuetf.getText().trim();
                desDimenValues = desDimenValuetf.getText().trim();
                
                if ((formerPath == null) || (formerPath.trim().equals(""))) {
                    tipLabel.setText("请选择正确的原始目录!");
                    return;
                } /**else if ((desPath == null) || (desPath.trim().equals(""))) {
                    tipLabel.setText("请选择正确的目标目录!");
                    return;
                } */else if ((formerDimenValue == null) || (formerDimenValue.trim().equals(""))) {
                    tipLabel.setText("请输入原始dimen 的 dp 值!");
                    return;
                } else if ((desDimenValues == null) || (desDimenValues.trim().equals(""))) {
                    tipLabel.setText("请输入目标 dp 值， 以逗号区分!");
                    return;
                }
                int tSrcDimen = Integer.parseInt(formerDimenValue);
                String tArrStr[] = desDimenValues.split(",");
                List<Integer> tArr = new ArrayList<Integer>();
                for(int i = 0;i<tArrStr.length;i++){
                    tArr.add(Integer.parseInt(tArrStr[i]));
                }
                if(desPath==null || "".equals(desPath.trim())){
                    mManager.startWork(formerPath, formerPath, tSrcDimen, tArr);
                }else{
                    mManager.startWork(formerPath, desPath, tSrcDimen, tArr);
                }
                
                tipLabel.setText("生成成功!!!");
//                
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e1) {
//                    e1.printStackTrace();
//                }
//                
//                System.exit(-1);
            }
        });
        
        this.generatePanel.add(this.generateButton);
        this.generatePanel.add(this.tipLabel);
        
       
        mainPanel.add(this.formerPathPanel);
        mainPanel.add(this.desPathPanel);
        
        mainPanel.add(this.formerDimenValuePanel);
        mainPanel.add(this.desDimenValuePanel);
        mainPanel.add(this.generatePanel);
        
        add(mainPanel, "Center");
        
        setResizable(false);

        setVisible(true);
        
    }
}
