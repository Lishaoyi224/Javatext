import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

public class FileUtil {
	// 创建文件
	public static boolean createFile(String destFileName) {
		File file = new File(destFileName);
		if(file.exists()) {
			System.out.println("创建失败，文件已存在!");
			return false;
		}
		else {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return false;
			}
		}
		return true;
	}
	
	// 创建文件夹
	public static boolean createDir(String destDirName) {
		File file = new File(destDirName);
		if(file.exists()) {
			System.out.println("创建失败，文件夹已存在!");
			return false;
		}
		else {
			file.mkdir();
			return true;
		}
	}
	
	// 删除文件
	public static boolean deleteFileOrDir(String path) {
        File file = new File(path);
        if (file.isFile() && file.exists()) {
            return file.delete();
        } 
        else {
        	String[] filenames = file.list();
            for (String f : filenames) {
            	deleteFileOrDir(f);
            }
            return file.delete();
        }
	}
	
	// 复制文件
	public static void copyFile(String sPath, String tPath) {	
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			in = new FileInputStream(sPath);
			out = new FileOutputStream(tPath);
			byte[] buf = new byte[8 * 1024];
			int len = 0;
			while ((len = in.read(buf)) != -1) {
				out.write(buf, 0, len);
				out.flush();
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try{
				if(in != null && out != null){
					in.close();
					out.close();
				}
			}
			catch(Exception e){
			}
		}
	}
	
	// 复制文件夹
	public static void copyDirectiory(String sDir, String tDir) {
		File sFile = new File(sDir);
		File[] sFiles = sFile.listFiles();
		
		for(File file : sFiles) {
			String sourcePath = sDir + File.separator + file.getName();
			File sourceFile = new File(sourcePath);
			if(sourceFile.isFile()) { //复制文件
				String targetPath = tDir + File.separator + file.getName();
				copyFile(sourcePath,targetPath);
			}
			if(file.isDirectory()) { //复制文件夹
				String targetPath = tDir + File.separator + file.getName();
				File tempfile = new File(targetPath);
				tempfile.mkdir();
				copyDirectiory(sourcePath,targetPath);
			}
		}
	}
	
	// 文件重命名
	public static void renameFile(String path, String oldname, String newname) {	
		String oldPath = path + oldname;
		String newPath = path + newname;
		File file = new File(oldPath);
		file.renameTo(new File(newPath));
		System.out.println(oldname + " -> " + newname);
	}
	
	//得到文件列表
	public static Vector<String> fileList(String path) {
		Vector<String> vector = new Vector<String>();
		File file = new File(path);
		File[] fileList = file.listFiles(); //将目录下所有文件保存在File数组中
		for(int i = 0; i < fileList.length; i++) {
			if(fileList[i].isFile()) {//判断是否为文件
				vector.add(fileList[i].getName());
			}
			if(fileList[i].isDirectory()) {//判断是否为文件夹
				vector .add(fileList[i].getName()+"\\");
		   }
		}
		return vector;
	}
}
