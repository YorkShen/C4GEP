package ui.alginputdataprocess.controller;
/*
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;

import jxl.read.biff.BiffException;
import domain.core.outputmodel.GepConfiguration;
import domain.iservice.IgepAlgService;

import ui.algconfiguration.view.HostPanel;
import ui.algconfiguration.view.JPanelForFunction;
import ui.algconfiguration.view.GeneView;
import ui.algconfiguration.view.PopulationView;
import ui.algconfiguration.view.StopSettingView;
import ui.alginputdataprocess.model.UploadInterfaceModel;

public class UploadInterfaceController {

	private static String fileName;
	private static boolean available;
	private static String defaultFileSavePath = ".\\bin\\domain\\service\\alg\\userdefined\\";
	private static File file;
	private static File saveDir;
	static String packagePath;
	static StringBuffer buffer;

	
	
	
	
	
	
	
	public static int btnUploadController(
			UploadInterfaceModel uploadInterface) throws IOException {
		file = uploadInterface.getFile();
		saveDir = new File(defaultFileSavePath);
		try {
			buffer = ReadFile(file, saveDir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return WriteFile(buffer, file.getName());
	}

	public static StringBuffer ReadFile(File file, File saveDir)
			throws IOException {

		try {

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), "GBK"));
			String data = null;
			StringBuffer buffer = new StringBuffer();

			buffer.append("package domain.service.alg.userdefined;" + "\n");
			// 会报错

			int j = 1;// 当第一次读时替换package

			while ((data = reader.readLine()) != null) {
				if (j != 1 && j != 2) {
					buffer.append(data + "\n");
					fileName = file.getName().substring(0,
							file.getName().indexOf(".java"));// 得到类名
					char[] dataToChar = new char[data.length()];
					String dataStr = new String();
					for (int i = 0; i < data.length(); i++) {
						if (data.charAt(i) != ' ' && data.charAt(i) != '	') {
							dataToChar[i] = data.charAt(i);
							dataStr = dataStr + dataToChar[i];
						}

					}

				}
				j++;
			}
			reader.close();
			return buffer;
		} catch (IOException e) {
			throw new RuntimeException();
		}
	}

	public static <T> int WriteFile(StringBuffer buffer, String fileName)
			throws IOException {

		// 检查指定路径是否存在
		File newInterface = new File(defaultFileSavePath + file.getName());

		if (!saveDir.exists() && !saveDir.isDirectory()) {// 检查文件夹是否存在，不存在就生成
			newInterface.mkdirs();

		}
		File[] filesBeforeUpload = saveDir.listFiles();

		List<File> filesListBeforeUpload = new ArrayList<File>();
		for (int i = 0; i < filesBeforeUpload.length; i++) {
			filesListBeforeUpload.add(filesBeforeUpload[i]);
			if (file.getName().equals(filesBeforeUpload[i].getName())) {

				// 相同文件名的文件已存在

				System.out.println("文件已存在");
				newInterface.delete();
				return -1;// 文件已存在
			}
		}

		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(newInterface), "GBK"));
		writer.write(buffer.toString());
		writer.flush();
		writer.close();
		// 生成。class文件
		JavaCompiler complier = ToolProvider.getSystemJavaCompiler();
		// 通过TOOlProvider类的静态方法getSystemJavaCompiler来得到一个JAvaCompiler接口的实例,不能有效的控制输入输出
		StandardJavaFileManager fileMgr = complier.getStandardFileManager(null,
				null, null);
		//
		Iterable units = fileMgr.getJavaFileObjects(defaultFileSavePath
				+ file.getName());
		System.out.print(defaultFileSavePath + file.getName());
		CompilationTask t = complier.getTask(null, fileMgr, null, null, null,
				units);
		// writer out：用于输出错误的流，默认为System。err
		// javaFileManager fileManager：标准的文件管理
		// DiagnosticListener diagnosticListener:编译器的默认行为
		// iterable option:编译器选项
		// iterable class:参与编译的文件
		// iterable compiliationUnis不能为null,保存编译的java文件
		t.call();// 编译源程序
		fileMgr.close();

		try {
			fileName = file.getName().substring(0,
					file.getName().indexOf(".java"));// 得到类名

			File classfiles = new File(
					".\\bin\\domain\\service\\alg\\baseclass\\");
			System.out.println(classfiles.toString());
			// ---检查实现的接口

			Class<?> myClass = Class.forName("domain.service.alg.userdefined."
					+ fileName);
			for (String string : classfiles.list()) {
				available = myClass.isInstance(string);
			}
			File classfileForfunction = new File(
					".\\bin\\domain\\core\\algmodel\\genepiece\\function");
			available = myClass.isInstance(classfileForfunction.getName());

			if (available == false) {
				File[] filesAfterUpload = saveDir.listFiles();

				for (int i = 0; i < filesAfterUpload.length; i++) {
					if (!filesListBeforeUpload.contains(filesAfterUpload[i])) {
						filesAfterUpload[i].delete();
					}
				}

				return -2;// 没有实现接口

			}
			int i = 0;
			// 检查实现构造函数
			Constructor<?>[] constructorArray = myClass.getConstructors();
			for (Constructor<?> constructor : constructorArray) {
				if (constructor.getParameterTypes().length == 0) {
					break;
				} else {
					i++;
				}

			}
			if (i == constructorArray.length) {
				File[] filesAfterUpload = saveDir.listFiles();
				for (int j = 0; j < filesAfterUpload.length; i++) {
					if (!filesListBeforeUpload.contains(filesAfterUpload[j])) {
						filesAfterUpload[j].delete();
					}
				}
				return -3;// 没有默认构造函数

			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;// 可以上传
	}
}*/
