import javax.swing.*;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class virt_to_phys extends JFrame
{
	public Map< String, String > TLB = new HashMap< String, String >();
	public long a = 0L;
	public String last_cr3 = "";
	public virt_to_phys()
	{
		final int Interv = 10;
		Container panel = getContentPane();
		panel.setLayout(null);
		panel.getInsets();
		Insets insets = panel.getInsets();
		setTitle("Memory Addressing");
//		Translate a virtual address to a physical address
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 600);
	    JTextField virtAddr = new JTextField("", 10);
	    virtAddr.setEditable(true);
	    virtAddr.setHorizontalAlignment(JTextField.CENTER);
	    JLabel VALabel = new JLabel("Virtual Address");
	    Rectangle r = new Rectangle(25 + insets.left, 25 + insets.top, 
	    	VALabel.getPreferredSize().width, VALabel.getPreferredSize().height + 5);
        VALabel.setBounds(r);
	    panel.add(VALabel);
	    r.x += r.width + Interv;
	    r.width = virtAddr.getPreferredSize().width;
	    virtAddr.setBounds(r);
	    panel.add(virtAddr);
		JButton generateVirt = new JButton("Generate");
		r.x += r.width + Interv;
		r.width = generateVirt.getPreferredSize().width;
		generateVirt.setBounds(r);
		panel.add(generateVirt);
		JTextField linearAddr = new JTextField("", 7);
		linearAddr.setEditable(false);
		linearAddr.setHorizontalAlignment(JTextField.CENTER);
		JButton Trans = new JButton("Translate");
		r.x += r.width + Interv;
		r.width = Trans.getPreferredSize().width;
		Trans.setBounds(r);
		panel.add(Trans);
		JLabel LALabel = new JLabel("Linear Address");
		r.x = 25 + insets.left;
		r.y += r.height + 2 * Interv;
		r.width = LALabel.getPreferredSize().width;
		LALabel.setBounds(r);
		panel.add(LALabel);
		r.x += r.width + Interv;
		r.width = linearAddr.getPreferredSize().width;
		linearAddr.setBounds(r);
		panel.add(linearAddr);
		JLabel cr3lab = new JLabel("cr3");
		r.x += r.width + 2 * Interv; 
		r.width = cr3lab.getPreferredSize().width;
		cr3lab.setBounds(r);
		panel.add(cr3lab);
		JTextField cr3value = new JTextField("0x 00000 000", 7);
		r.x += r.width + Interv / 2;
		r.width = cr3value.getPreferredSize().width;
		cr3value.setBounds(r);
		panel.add(cr3value);
		JButton Change = new JButton("Change cr3");
		r.x += r.width + Interv;
		r.width = Change.getPreferredSize().width;
		Change.setBounds(r);
		panel.add(Change);
		//PDE
		JLabel pd = new JLabel("Page Directory               "
				+ "Page Entry");
		r.x = 25 + insets.left;
		r.y += r.height + 2 * Interv;
		r.width = pd.getPreferredSize().width;
		pd.setBounds(r);
		panel.add(pd);
		JTextField PD = new JTextField("", 7);
		PD.setEditable(false);
		PD.setHorizontalAlignment(JTextField.CENTER);
		r.x = 25 + insets.left;
		r.y += r.height + Interv;
		r.width = PD.getPreferredSize().width;
		PD.setBounds(r);
		panel.add(PD);
		JTextField DE = new JTextField("", 8);
		DE.setEditable(false);
		DE.setHorizontalAlignment(JTextField.CENTER);
		r.x += r.width + 4 * Interv;
		r.width = DE.getPreferredSize().width;
		DE.setBounds(r);
		panel.add(DE);
		//添加PTE
		JLabel pt = new JLabel("  Page Table                    "
				+ "Table Entry");
		r.x = 25 + insets.left + 8 * Interv;
		r.y += r.height + 2 * Interv;
		r.width = pt.getPreferredSize().width;
		pt.setBounds(r);
		panel.add(pt);
		JTextField PT = new JTextField("", 7);
		PT.setEditable(false);
		PT.setHorizontalAlignment(JTextField.CENTER);
		r.y += r.height + Interv;
		r.width = PT.getPreferredSize().width;
		PT.setBounds(r);
		panel.add(PT);
		JTextField TE = new JTextField("", 8);
		TE.setEditable(false);
		TE.setHorizontalAlignment(JTextField.CENTER);
		r.x += r.width + 4 * Interv;
		r.width = TE.getPreferredSize().width;
		TE.setBounds(r);
		panel.add(TE);
		//physical addr
		JLabel Offset = new JLabel("Offset");
		r.x = 25 + insets.left + 23 * Interv;
		r.y += r.height + 2 * Interv;
		Offset.setBounds(r);
		panel.add(Offset);
		JLabel pa = new JLabel("Physical Address");
		r.x = 25 + insets.left + 5 * Interv;
		r.y += r.height;
		r.width = pa.getPreferredSize().width;
		pa.setBounds(r);
		panel.add(pa);
		JTextField PA = new JTextField("", 5);
		PA.setEditable(false);
		PA.setHorizontalAlignment(JTextField.CENTER);
		r.x += r.width + Interv;
		r.width = PA.getPreferredSize().width;
		PA.setBounds(r);
		panel.add(PA);
		JLabel link = new JLabel(" - ");
		r.x += r.width;
		r.width = link.getPreferredSize().width;
		link.setBounds(r);
		panel.add(link);
		JTextField os = new JTextField("", 3);
		os.setEditable(false);
		os.setHorizontalAlignment(JTextField.CENTER);
		r.x += r.width;
		r.width = os.getPreferredSize().width;
		os.setBounds(r);
		panel.add(os);
		
		//TLB
		JLabel tlb = new JLabel("TLB");
		r.x = 25 + insets.left + 20 * Interv;
		r.y += r.height + 2 * Interv;
		r.width = tlb.getPreferredSize().width;
		tlb.setBounds(r);
		panel.add(tlb);
		JTextArea textArea = new JTextArea(10, 27);
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		r.x = 25 + insets.left + 6 * Interv;
		r.y += r.height;
		r.width = textArea.getPreferredSize().width;
		r.height = textArea.getPreferredSize().height;
		textArea.setText("   Virtual Address" + "\t" + 
				"Physical Address" + "\n");
		JScrollPane scrollPane = new JScrollPane(textArea,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(r);
		add(scrollPane);
		setVisible(true);
		this.setLocationRelativeTo(null);
		generateVirt.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{//generate a virtual address
				a = (long)(Math.random() * Math.pow(2, 32));
				String s = Long.toHexString(a);
				while(s.length() < 8)
					s = '0' + s;
				virtAddr.setText("0x 0000 " + s);
			}
		});
		Trans.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{//generate a virtual address
				if(!virtAddr.getText().isEmpty())
				{
					linearAddr.setText("0x " + virtAddr.getText().substring(8));
					PD.setText(cr3value.getText());
					if(false == TLB.containsKey(virtAddr.getText()) || last_cr3.compareTo(cr3value.getText()) != 0)
					{
						String s1 = Long.toBinaryString(a);
						while(s1.length() < 32)
							s1 = "0" + s1;
						DE.setText("B " + s1.substring(0, 10));
						int t = (int)(Math.random() * Math.pow(2, 20));
						String s = Long.toHexString(t);
						while(s.length() < 5)
							s = '0' + s;
						PT.setText("0x " + s + " 000");
						TE.setText("B " + s1.substring(10, 20));
						t = (int)(Math.random() * Math.pow(2, 20));
						s = Long.toHexString(t);
						while(s.length() < 5)
							s = '0' + s;
						PA.setText("0x " + s);
						os.setText(virtAddr.getText().substring(13));
					}
					else
					{//通过TLB查找
						PD.setText("");
						DE.setText("");
						PT.setText("");
						TE.setText("");
						PA.setText( TLB.get( virtAddr.getText()).substring(0, 8) );
						os.setText( TLB.get( virtAddr.getText()).substring(8) );
					}
					last_cr3 = cr3value.getText();
					if(false == TLB.containsKey(virtAddr.getText()))
						textArea.append("   " + virtAddr.getText() + "\t" + 
								PA.getText() + os.getText() + "\n");
					TLB.put(virtAddr.getText(), PA.getText() + os.getText());
				}
			}
		});
		Change.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				String s = Long.toHexString((long)(Math.random() * Math.pow(2, 20)));
				while(s.length() < 5)
					s = '0' + s;
				cr3value.setText("0x " + s + " 000");
				//修改后续结果
				TLB.clear();
				textArea.setText("   Virtual Address" + "\t" + 
						"Physical Address" + "\n");
			}
		});
	}
	public static void main(String[] args)
	{
		new virt_to_phys();
	}
}

