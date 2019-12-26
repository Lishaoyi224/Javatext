import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class FileSystem {
	JFrame frame; //窗口
	Container container; //创中的容器对象
	JPanel jPanel; //创建面板
	JButton btn1,btn2,btn3,btn4,btn5,btn6,btn7; //创建按钮
	JList fileList; //列表框对象
	Vector<String> vector = new Vector<String>(); //列表框内容
	String currentPath = ".\\AD\\"; //当前显示路径
	String copyPath = null; //待拷贝路径

	public FileSystem() {
		frame = new JFrame("武永亮文件管理器");
		frame.setBounds(400, 300, 800, 600); //设置窗口大小和位置
		frame.setLayout(new BorderLayout());
		frame.setLocationRelativeTo(null); //窗体居中显示
		container = frame.getContentPane();
		jPanel = new JPanel(); //创建面板
		jPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		btn1 = new JButton("创建文件"); //创建按钮
		btn2 = new JButton("创建文件夹");
		btn3 = new JButton("复制");
		btn4 = new JButton("粘贴");
		btn5 = new JButton("删除");
		btn6 = new JButton("重命名");
		btn7 = new JButton("返回上一级目录");

		//添加创建文件事件
		btn1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("创建文件");
				String fileName = JOptionPane.showInputDialog("请输入文件名");
				if(fileName != null) {
					if(!FileUtil.createFile(currentPath + fileName)) { //文件创建失败
						JOptionPane.showMessageDialog(frame, "创建失败，文件已存在!");
					}
					else { //文件创建成功
						System.out.println("文件创建成功!");
					}
				}
				refreshFileList();
			}
		});
		
		//添加创建文件夹事件
		btn2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("创建文件夹");
				String dirName = JOptionPane.showInputDialog("请输入文件夹名");
				if(dirName != null) {
					if(!FileUtil.createDir(currentPath + dirName)) { //文件夹创建失败
						JOptionPane.showMessageDialog(frame, "创建失败，文件夹已存在!");
					}
					else { //文件创建成功
						System.out.println("文件夹创建成功!");
					}
				}
				refreshFileList();
			}
		});
		
		//添加复制事件
		btn3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("复制");
				if(fileList.getSelectedValue() != null) {
					String selectFile = fileList.getSelectedValue().toString();
					copyPath = currentPath + File.separator + selectFile; //待拷贝文件或文件夹路径
					System.out.println(copyPath);
				}
				else {
					JOptionPane.showMessageDialog(frame, "请选择需要复制的文件或文件夹!");
				}
				refreshFileList();
			}
		});
		
		//添加粘贴事件
		btn4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("粘贴");
				if(copyPath != null) {
					if(new File(copyPath).isFile()) { //粘贴文件
						String fileName = copyPath.substring(copyPath.lastIndexOf("\\"));
						String targetPath = currentPath + File.separator + fileName;
						if(new File(targetPath).exists()) {
							int isPaste = JOptionPane.showConfirmDialog(null, "文件已存在，是否替换？", "提示", JOptionPane.YES_NO_OPTION);
							if(isPaste == JOptionPane.YES_OPTION) {
								FileUtil.copyFile(copyPath, targetPath);
							}
						}
						else {
							FileUtil.copyFile(copyPath, targetPath);
						}
					}
					if(new File(copyPath).isDirectory()) { //粘贴文件夹
						String[] strs = copyPath.split("\\\\");
						String dirName = strs[strs.length-1];
						String targetPath = currentPath + File.separator + dirName;
						if(new File(targetPath).exists()) {
							int isPaste = JOptionPane.showConfirmDialog(null, "文件夹已存在，是否替换？", "提示", JOptionPane.YES_NO_OPTION);
							if(isPaste == JOptionPane.YES_OPTION) {
								FileUtil.copyDirectiory(copyPath, targetPath);
							}
						}
						else {
							File tempFile = new File(targetPath);
							tempFile.mkdir();
							FileUtil.copyDirectiory(copyPath, targetPath);
						}
					}
				}
				else {
					JOptionPane.showMessageDialog(frame, "请先复制文件或文件夹!");
				}
				refreshFileList();
			}
		});
		
		//添加删除事件
		btn5.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("删除");
				if(fileList.getSelectedValue() != null) {
					String fileName = fileList.getSelectedValue().toString(); //获取文件名或文件夹名
					if(!"".equals(fileName)) {
						String filePath = currentPath + File.separator + fileName;
						int isDel = JOptionPane.showConfirmDialog(null, "确定删除？", "提示", JOptionPane.YES_NO_OPTION);
						if(isDel == JOptionPane.YES_OPTION) {
							FileUtil.deleteFileOrDir(filePath);						
						}
					}
				}
				else {
					System.out.println("请选择需要删除的文件或文件夹!");
					JOptionPane.showMessageDialog(frame, "请选择需要删除的文件或文件夹!");
				}
				refreshFileList();
			}
		});
		
		//添加重命名事件
		btn6.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("重命名");
				if(fileList.getSelectedValue() != null) {
					String fileName = fileList.getSelectedValue().toString(); //获取文件名或文件夹名
					String newFileName = null;
					if(fileName.contains("\\")) { //重命名文件夹
						newFileName = JOptionPane.showInputDialog("请输入修改后的文件夹名");
					}
					else { //重命名文件
						newFileName = JOptionPane.showInputDialog("请输入修改后的文件名");
					}
					if(new File(currentPath + newFileName).exists()) {
						JOptionPane.showMessageDialog(frame, "该名称已存在!");
					}
					else {
						FileUtil.renameFile(currentPath, fileName, newFileName);
					}
				}
				else {
					System.out.println("请选择需要重命名的文件或文件夹!");
					JOptionPane.showMessageDialog(frame, "请选择需要重命名的文件或文件夹!");
				}
				refreshFileList();
			}
		});
		
		//添加返回上一级目录事件
		btn7.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("返回上一级目录");
				File file = new File(currentPath);
				currentPath = file.getParent(); //获取上一级目录
				refreshFileList();
			}
		});

		// 面板中添加按钮
		jPanel.add(btn1);
		jPanel.add(btn2);
		jPanel.add(btn3);
		jPanel.add(btn4);
		jPanel.add(btn5);
		jPanel.add(btn6);
		jPanel.add(btn7);
		
		container.add(jPanel, BorderLayout.NORTH);
		fileList = new JList(vector);
		fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		fileList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				if (e.getValueIsAdjusting()) {
					System.out.println("点选的内容是："+fileList.getSelectedValue());
				}
			}
		});
		fileList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					System.out.println("双击的内容是："+fileList.getSelectedValue());
					String newPath = currentPath + File.separator + fileList.getSelectedValue().toString();
					File file = new File(newPath);
					if(file.isFile()) {
						try {
							Desktop.getDesktop().open(file);
						} 
						catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					if(file.isDirectory()) {
						currentPath = newPath;
					}
					refreshFileList();
				}
			}
		});

		container.add(fileList, BorderLayout.CENTER);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				System.exit(1);
			}
		});
	}

	public void refreshFileList() {
		vector = FileUtil.fileList(currentPath); //获取目录下所有文件名
		fileList.setBorder(BorderFactory.createTitledBorder(currentPath+"文件列表:"));
		fileList.setListData(vector);
	}

	public void start() {
		refreshFileList();
		frame.setVisible(true);
	}
}

