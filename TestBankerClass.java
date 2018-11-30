package 银行家算法;

import java.util.Scanner;

public class TestBankerClass {

	public static int pro = 0;
	public static int os = 0;

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		Scanner main_in = new Scanner(System.in);
		// 初始化系统资源
		System.out.print("配置系统资源种类数：");
		os = main_in.nextInt();
		System.out.print("配置系统进程数：");
		pro = main_in.nextInt();
		BankerClass T = new BankerClass();
		// 仅当用户选择退出系统时退出
		while (true) {
			T.setRequest();
			System.out.println("您是否还要进行请求：y/n ?");
			boolean flag=true;
			while ( flag) {
				String s=main_in.next();//临时变量
				if (s.equals("n")) {
					System.out.println("感谢使用！");
					System.exit(0);// 退出
				}else 
					if(s.equals("y"))
						flag=false;
				else{
					System.out.println("输入不规范！");
				}
			}
		}
	}
}