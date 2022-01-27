package KitapListe;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.*;

public class Gui {
	public JFrame kitapEklemeForm;
	private JPanel KitapPanel;
	private JButton EkleButton;
	public JFrame aramaForm;
	public JFrame excelForm;
	JButton	AramaButton;
	JTextField AramaTextbox;
	JPanel AramaKutusu;
	String aranacakKelime;
	JButton	IleriButton;
	javaDb db;
	JButton	GeriButton;
	ArrayList<kitap> ktpList;
	private JTable KtpTablo;
	private DefaultTableModel ModelTablo;
	private JScrollPane TabloPanel;
	int sayfaSayisi;

	int SimdikiSayfa;
	public Gui(){
		JformCreate();
		aramaFormCrate();
		excelFormCreate();
		 
	}
	private void JformCreate() {
		kitapEklemeForm =new JFrame();
		kitapEklemeForm.setSize(800, 800);
		kitapEklemeForm.setLocationRelativeTo(null);
		kitapEklemeForm.setLayout(new BorderLayout());
		kitapEklemeForm.getContentPane().setBackground(new java.awt.Color(32, 32, 32));
		kitapEklemeForm.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		kitapEklemeForm.setVisible(false);
	}
	
	
	public void aramaFormCrate() {
		aramaForm=new JFrame();
		aramaForm.setSize(220, 140);
		aramaForm.setLayout(null);
		aramaForm.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		aramaForm.add(SearchBarCreate());
		aramaForm.setVisible(true);
		
	}
	
	public void excelFormCreate() {
		excelForm =new JFrame();
		excelForm.setSize(800, 700);
		excelForm.setLayout(null);
		excelPanelCreate();
	}
	
	
	
	public JPanel SearchBarCreate() {
		AramaKutusu = new JPanel();
		AramaTextbox=new JTextField(10); 
		AramaTextbox.addKeyListener(new KeyListener() {
	       

	        @Override
	        public void keyPressed(KeyEvent e) {
	        	// TODO Auto-generated method stub
	        	
	        }

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				 if (AramaTextbox.getText().isBlank()&&AramaTextbox.getText().isEmpty()) {
		            	AramaButton.setEnabled(false);
					}
		            else {
		            	AramaButton.setEnabled(true);
					}
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

	        
	    });
		AramaButton=new JButton("Ara");  
        AramaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            aranacakKelime=	AramaTextbox.getText();
            SimdikiSayfa=1;
            IleriButton.setEnabled(false);
            if (!kitapEklemeForm.isVisible()) {
            	kitapEklemeForm.setVisible(true);
			}
            arama();
            if (SimdikiSayfa<=sayfaSayisi&&sayfaSayisi>1) {
        		GeriButton.setEnabled(true);
        	}
            else {
            	GeriButton.setEnabled(false);
			}
            
            }
        });
        AramaButton.setEnabled(false);
        IleriButton=new JButton("<<");  
        IleriButton.setEnabled(false);
		 IleriButton.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent event) {
	            
	            SimdikiSayfa--;
	            arama();
	            if (SimdikiSayfa<2) {
					IleriButton.setEnabled(false);
				}
	            if (SimdikiSayfa>=sayfaSayisi&&GeriButton.isEnabled()) {
	        		GeriButton.setEnabled(true);
	        	}
	            }
	        });
	
	
	GeriButton=new JButton(">>");  
	GeriButton.setEnabled(false);
	GeriButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
      SimdikiSayfa++;
      arama();
      if (SimdikiSayfa>1) {
			IleriButton.setEnabled(true);
		}
      if (SimdikiSayfa>=sayfaSayisi) {
		GeriButton.setEnabled(false);
	}
      }
  });
	JButton b4=new JButton("Tabloyu Göster");  
	b4.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent event) {
	        excelForm.setVisible(true);
	          }
	      });
	
        AramaKutusu.add(AramaTextbox);
        AramaKutusu.add(AramaButton);
        AramaKutusu.add(IleriButton);
        AramaKutusu.add(GeriButton);
        AramaKutusu.add(b4);
        AramaKutusu.setSize(200,300);
        return AramaKutusu;
        
	}

	public JPanel btnPanel(kitap ktp,int i) 
	{
		KitapPanel = new JPanel();
		KitapPanel.setBackground(new java.awt.Color(250, 250, 250));
			EkleButton = new JButton("Ekle");
			EkleButton.setSize(50, 50);
			EkleButton.setBackground(Color.GRAY);
			EkleButton.setForeground(Color.WHITE);
			EkleButton.setActionCommand(String.valueOf(i));
			EkleButton.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent event) {
	            	kitap ktp=ktpList.get(Integer.parseInt(event.getActionCommand()));
	            	JOptionPane.showMessageDialog(null, ktp.kitapAdi+" adlý kitap listeye eklendi","Kitap Eklenedi", JOptionPane.INFORMATION_MESSAGE);
	            db.insertKitap(ktp);
	            Vector<String> r  = new Vector<String>();
	            r.addElement(ktp.isbnNo);
	            r.addElement(ktp.kitapAdi);
	            r.addElement(ktp.yazarAdi);
	            r.addElement(ktp.yayineviAdi);
	            r.addElement(ktp.sayfaSayisi);
	            ModelTablo.addRow(r);
	            }
	        });

		    Image image = null;
		    URL url = null;
		    try {
		        url = new URL(ktp.resimLink);
		        image = ImageIO.read(url);
		        image=image.getScaledInstance(100, 120, Image.SCALE_DEFAULT);
		 
		    } catch (MalformedURLException ex) {
		        System.out.println("Malformed URL");
		    } catch (IOException iox) {
		        System.out.println("Can not load file");
		    }
		    JLabel label = new JLabel(new ImageIcon(image));
		    label.setBounds(0, 0, 60, 80);
			JLabel lblNewLabel = new JLabel("<html><div style='text-align: center;'>" + ktp.kitapAdi + "</div></html>");
			lblNewLabel.setBounds(0, 0,400, 80);
			JLabel lbl2 = new JLabel("<html><div style='text-align: center;'>" + ktp.yazarAdi + "</div></html>");
			lbl2.setBounds(0, 0,400, 80);
			JLabel lbl3 = new JLabel("<html><div style='text-align: center;'>" +ktp.yayineviAdi  + "</div></html>");
			lbl3.setBounds(0, 0,400, 80);
			JLabel lbl4 = new JLabel("<html><div style='text-align: center;'>" +ktp.sayfaSayisi  + "</div></html>");
			lbl3.setBounds(0, 0,400, 80);
			KitapPanel.add(label);
			KitapPanel.add(lblNewLabel);
			KitapPanel.add(lbl2);
			KitapPanel.add(lbl3);
			KitapPanel.add(lbl4);
			EkleButton.setBounds(5, 5, 50, 50);
			KitapPanel.add(EkleButton);
		KitapPanel.setLayout(new GridLayout(1,5,10,10));
		return KitapPanel;
	}
	
	
	public void excelPanelCreate() {
		db=new javaDb();
		ArrayList<kitap> kitaplar=db.selectKitap();
		KtpTablo = new JTable();
	    ModelTablo = new DefaultTableModel();
	    ModelTablo.addColumn("Isbn No");
	    ModelTablo.addColumn("Kitap Adi");
	    ModelTablo.addColumn("Yazar Adý");
	    ModelTablo.addColumn("Kitapevi Adý");
	    ModelTablo.addColumn("Sayfa Sayýsý");
	    KtpTablo.setModel(ModelTablo);

	    for (kitap ktp : kitaplar) {
	    	Vector<String> r  = new Vector<String>();
            r.addElement(ktp.isbnNo);
            r.addElement(ktp.kitapAdi);
            r.addElement(ktp.yazarAdi);
            r.addElement(ktp.yayineviAdi);
            r.addElement(ktp.sayfaSayisi);
            ModelTablo.addRow(r);
		}

	    TabloPanel = new JScrollPane(KtpTablo); 
	    TabloPanel.setBounds(15, 10, 750, 600);
	    TabloPanel.setVisible(true);
	    JButton btn=new JButton("Excele Kaydet");
	    btn.setBounds(15, 620,750, 20);
	    btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            excelOlustur();
            }
        });
	    excelForm.getContentPane().add(TabloPanel);
	    excelForm.add(btn);
	}
	
	public void setVipe(ArrayList<kitap> kitaplar) {
		kitapEklemeForm.getContentPane().removeAll();
		JPanel storyEditor = new JPanel();
		storyEditor.setLayout(new BoxLayout(storyEditor, 1));
		for (int i = 0; i < kitaplar.size(); i++) {
			storyEditor.add(btnPanel(kitaplar.get(i), i)) ;
		}
		storyEditor.setPreferredSize(new Dimension(750,(kitaplar.size()*150)));
		JScrollPane scroller = new JScrollPane(storyEditor);
		kitapEklemeForm.getContentPane().add(scroller);
		kitapEklemeForm.validate();

	}
	
	public void arama() {
		Crawler crw=new Crawler();
		ktpList= crw.kitapAra(SimdikiSayfa, aranacakKelime);
		sayfaSayisi=crw.sayfaSayiBul();
		setVipe(ktpList);
		
	}
	public void excelOlustur() {
		String filename="";
		String dir="";
		JFileChooser c = new JFileChooser();
	      int rVal = c.showSaveDialog(excelForm);
	      if (rVal == JFileChooser.APPROVE_OPTION) {
	        filename=c.getSelectedFile().getName();
	        dir=c.getCurrentDirectory().toString();
	        String fileLocation=dir+"\\"+filename+".xls";
	        WritableWorkbook workbook;
			try {
				workbook = Workbook.createWorkbook(new File(fileLocation));
				WritableSheet sheet = workbook.createSheet("Sheet 1", 0);

	            WritableCellFormat headerFormat = new WritableCellFormat();
	            WritableFont font = new WritableFont(WritableFont.ARIAL, 11, WritableFont.BOLD);
	            headerFormat.setFont(font);
	            
	            int number = ModelTablo.getColumnCount();
	            for(int i=0; i<number; i++) {
		            Label headerLabel = new Label(i, 0, ModelTablo.getColumnName(i), headerFormat);
		            sheet.setColumnView(0, 60);
		            sheet.addCell(headerLabel);
	            }
	            WritableCellFormat cellFormat = new WritableCellFormat();
	            for (int i = 0; i < ModelTablo.getRowCount(); i++) {
	            	for (int j = 0; j < ModelTablo.getColumnCount(); j++) {
	            		Label cellLabel = new Label(j, i+1, (String) ModelTablo.getValueAt(i, j), cellFormat);
	            		sheet.addCell(cellLabel);
					}
					
				}
	            workbook.write();
	            workbook.close();
			} catch (IOException | WriteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
	      }
	
	}


}