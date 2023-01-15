import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;  
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;  
import javax.swing.JTextArea;  
import javax.swing.JTextField;
@SuppressWarnings("serial")
public class TestFrame extends JFrame implements ActionListener { 
    JTextField myTitle;
	JTextArea txt; 
    Readable savetxt;
    private JPanel contents;
    private File fileToOpen, fileToCreate, fileToSave;
	public TestFrame () {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//		int width = (int) screenSize.getWidth();
//		int height = (int) screenSize.getHeight();
//		setSize(800, 400);
		setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);  
        setTitle("TestFrame");  
//        JPanel topPanel = new JPanel(); //не знаю зачем я создал её, но без нее текстовое поле выводится сразу, а не потом
        contents = new JPanel();
		txt = new JTextArea(40, 80);
//        topPanel.setLayout(new BorderLayout());
        contents.add(new JScrollPane(txt));
        getContentPane().add(contents);
//        getContentPane().add(topPanel);
        
//        add(txt);

        setJMenuBar(menuBar()); //устанавливаем в качетсве меню бара то, что возвращает метод menuBar()
        // Добавление вкладки во фрейм
//        jtabbedPane = new JTabbedPane();  
//        jtabbedPane.addTab("Options", options);  
//        topPanel.add(jtabbedPane, BorderLayout.CENTER);
//        jtabbedPane = new JTabbedPane();  
//        jtabbedPane.addTab("Test", test);  
//        topPanel.add(jtabbedPane, BorderLayout.EAST);
        pack();
        setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}
	
	public JMenuBar menuBar () { //метод menuBar, который возвращает объект класса JMenuBar
        JMenuBar menuBar = new JMenuBar(); //Создали переменную класса MenuBar
        setJMenuBar(menuBar); //Установили переменную menuBar в качестве меню бара
        JMenu fileMenu = new JMenu("File"); // Создали вкладку для меню бара с названием File 
        menuBar.add(fileMenu); //Добавили вкладку File в меню бар
        JMenu editMenu = new JMenu ("Edit");
        
        menuBar.add(editMenu);
        JMenuItem newFile = new JMenuItem("New File"); //Создали опцию для выпадающего меню File
        fileMenu.add(newFile); //Добавили опцию в выпадающее меню File
        JMenuItem openFile = new JMenuItem("Open File");
        fileMenu.add(openFile);
        JMenuItem saveFile = new JMenuItem("Save File");
        fileMenu.add(saveFile);
        newFile.addActionListener(new ActionListener () { //создаем новый файл
        	public void actionPerformed (ActionEvent e) {
        		JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
        		int result = fileChooser.showSaveDialog(null);
        		if (result == JFileChooser.APPROVE_OPTION ) fileToCreate = fileChooser.getSelectedFile(); //добавляем название введенного файла в переменную, чтобы её потом сохранить
//        		try { //здесь writer лишний, мы же еще ничего не записываем
//					FileWriter writer = new FileWriter(fileToCreate);
//					writer.close();
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				};
        		fileToSave = fileToCreate; //меняем содержимое переменной для сохранения
//        		txt = new JTextArea(40, 80); //выбираем дефолтные рамки, чтобы можно было вывести текст //создавать новый контейнер лишнее 
        		txt.setText(null);
        		txt.setVisible(true);
//        		contents.add(new JScrollPane(txt)); //добавляем поле ввода текста в скроллер
        		add(contents);
        		contents.setVisible(true);
        		pack(); //подгоняем рамки фрейма под поле текста
        	}
        });
        saveFile.addActionListener(new ActionListener () { //сохраняем файл
        	public void actionPerformed (ActionEvent e) {
        		try {
					FileWriter writer = new FileWriter(fileToSave);
					txt.write(writer);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
        		
        	}
        });
        openFile.addActionListener(new ActionListener() {
        	public void actionPerformed (ActionEvent e) { //Создаем ActionListener, который выводит текст из файла
//        		Scanner scan; 
//        		String fileName;
        		JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
//        		JFileChooserTest chooseFile = new JFileChooserTest();
				int result = fileChooser.showOpenDialog(null); //создаем и открываем FileChooser
				if (result == JFileChooser.APPROVE_OPTION )  //если нажата кнопка выбрать - передает файл в переменную fileToOpen
				                          fileToOpen = fileChooser.getSelectedFile();
				fileToSave = fileToOpen;
//        		fileName = JOptionPane.showInputDialog(null,"Введите название файла", "Открытие файла", JOptionPane.INFORMATION_MESSAGE); //конструкция рабочая, но можно выбирать файлы через FileChooser
//				try {
//					scan = new Scanner (fileToOpen);
//	        		StringBuilder sb = new StringBuilder();
//	        		while (scan.hasNext()) {
//	        		    sb.append(scan.next());
//	        		}
//	        		JTextArea txt = new JTextArea();
//	        		add(txt);
//	        		txt.setText(sb.toString());
//	        		txt.setVisible(true);
//	        		pack();
//				} catch (FileNotFoundException ex) {
//					ex.printStackTrace();
//				}
				// Код ниже работает намного лучше, нужно разобраться в сути написанного
				try {
					final DataInputStream dis = new DataInputStream(new FileInputStream(fileToOpen));//создали переменную, которая получает в качестве вводного потока данных информацию из файла
					final byte[] bytes = new byte[dis.available()]; //создаем массив байт, размер которого равен размеру файла
					dis.readFully(bytes); //записывает все байты из файла в массив bytes
					dis.close();
					final String fileContents = new String(bytes, 0, bytes.length); //собираем строку из байтового массива и добавляем ее в fileContents
//	        		txt = new JTextArea(40, 80); //создавать новый контейнер лишнее, нужно обновить уже имеющийся       		
	        		txt.setText(fileContents); //добавляем строку в текстовое поле
	        		txt.setVisible(true);
//	        		contents.add(new JScrollPane(txt)); //добавляем строку в контейнер скроллер (у JTextField нет своего скролла)
	        		add(contents); //добавляем контейнер скроллер на основной фрейм
	        		contents.setVisible(true); //делаем его видимым

	        		pack(); //подгоняем размеры
					} catch (FileNotFoundException ex) {
						ex.printStackTrace();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
        	}
        });
        
        
		return menuBar;
		
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
}