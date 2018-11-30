package 银行家算法;

import java.util.Scanner;

public class BankerClass {

	// 系统总进程数
	int pro_num = TestBankerClass.pro;
	// 系统临界资源种数
	int os_num = TestBankerClass.os;

	// 定义可利用的3种资源量，分别为10,8,7
	int[] Available = new int[os_num];

	// 进程Pi请求Pj资源的最大量为Max[i][j]
	int[][] Max = new int[pro_num][os_num];

	// 进程Pi已经分配Pj资源Allocation[i][j]
	int[][] Allocation = new int[pro_num][os_num];

	// 进程Pi还需要请求Pj资源为Need[i][j]
	int[][] Need = new int[pro_num][os_num];

	// 本轮进程Pi请求Pj资源量为Request[i][j]
	int[][] Request = new int[pro_num][os_num];

	// 工作变量，记录可用资源
	int[] Work = new int[os_num];

	// 记录进程编号
	int num = 0;

	Scanner in = new Scanner(System.in);

	// Max={{6,3,2},{5,6,1},{2,3,2}};

	public BankerClass() {// 设置各初始系统变量，并判断是否处于安全状态。
		// 用户提示
		System.out.print("本系统拥有" + os_num + "类临界资源，数量分别为");
		Available = Ginput(os_num);
		for (int j = 0; j < os_num; j++) {
			System.out.print((char) ('A' + j) + ":" + Available[j] + ",");
		}
		System.out.println();
		setMax();
		setAllocation();
		printSystemVariable();// 输出
		if (!SecurityAlgorithm()) {// 安全性算法
			System.exit(0);
		}
	}

	public void setMax() {// 设置Max矩阵
		System.out.println("请设置进程对各资源的最大需求量：");
		for (int i = 0; i < pro_num; i++) {
			System.out.println("    请输入进程P" + i + "对各资源的最大资源需求量：");
			Max[i] = Ginput(os_num);
			for (int j = 0; j < os_num; j++) {
				if (Max[i][j] > Available[j]) {
					System.out.println("资源不足，请重新输入！");
					Max[i] = Ginput(os_num);
					j = 0;
				}
			}
		}
	}

	public void setAllocation() {// 设置已分配矩阵Allocation
		System.out.println("请设置已给各进程分配的资源量：");
		for (int i = 0; i < pro_num; i++) {
			System.out.println("    请输入进程P" + i + "的已分配资源量：");
			Allocation[i] = Ginput(os_num);
			for (int j = 0; j < os_num; j++) {
				if (Allocation[i][j] > Max[i][j]) {
					System.out.println("超出约定资源量，请重新输入！");
					Allocation[i] = Ginput(os_num);
					j = 0;
				}
			}
		}
		// 更新当前资源余量和需求矩阵
		// Available=Available-Allocation
		// Need=Max-Allocation

		for (int j = 0; j < os_num; j++) {// 设置Available矩阵
			for (int i = 0; i < pro_num; i++) {
				Available[j] = Available[j] - Allocation[i][j];
			}
		}
		for (int i = 0; i < pro_num; i++) {// 设置Need矩阵
			for (int j = 0; j < os_num; j++) {
				Need[i][j] = Max[i][j] - Allocation[i][j];
			}
		}
	}

	public void printSystemVariable() {// 输出函数
		System.out.println("此时资源分配量如下：");// 提示性文字
		// 表头
		System.out.println("进程\t\t" + "Max\t\t" + "Allocation\t\t" + "Need\t" + "Available");

		for (int i = 0; i < pro_num; i++) {
			System.out.print("P" + i + "\t\t");
			for (int j = 0; j < os_num; j++) {
				System.out.print(Max[i][j] + "  ");
			}
			System.out.print("\t\t|  ");
			for (int j = 0; j < os_num; j++) {
				System.out.print(Allocation[i][j] + "  ");
			}
			System.out.print("\t\t\t|  ");
			for (int j = 0; j < os_num; j++) {
				System.out.print(Need[i][j] + "  ");
			}
			System.out.print("\t\t|  ");

			if (i == 0) {// 只在第一行输出当前资源量即可
				for (int j = 0; j < os_num; j++) {
					System.out.print(Available[j] + "  ");
				}
			}
			System.out.println();
		}
	}

	public void setRequest() {// 设置请求资源量Request
		System.out.println("请输入请求资源的进程编号：");
		while (true) {
			num = Ginput(1)[0];// 设置全局变量进程编号num
			if (num >= pro_num)
				System.out.println("无此进程！");
			else
				break;
		}
		System.out.println("请输入P" + num + "请求各资源的数量：");
		Request[num]=Ginput(os_num);
		System.out.print("即进程P" + num + "对各资源的请求量为：(");
		for (int i = 0; i < os_num; i++) {
			System.out.print(Request[num][i] + ",");
		}
		System.out.println(").");
		// 调用银行家算法
		BankerAlgorithm();
	}

	public void BankerAlgorithm() {// 银行家算法
		// 定义缓存数组，储存当安全算法判定失败的结果
		int[][] cache = new int[3][os_num];
		// 中间判断变量T
		boolean[] T = { true, true };// 判断银行家算法的两个条件是否成立
		// 1.判断Request是否小于Need
		for (int j = 0; j < os_num; j++) {
			if (Request[num][j] > Need[num][j]) {
				T[0] = false;
				j = os_num;
			}
		}
		// 2.判断Request是否小于Allocation
		for (int j = 0; j < os_num; j++) {
			if (Request[num][j] <= Available[j])
				;
			else {
				T[1] = false;
				j = os_num;
			}
		}
		if (T[0]) {
			if (T[1]) {
				// 试分配资源
				for (int j = 0; j < os_num; j++) {
					cache[0][j] = Available[j];
					cache[1][j] = Allocation[num][j];
					cache[2][j] = Need[num][j];

					Available[j] -= Request[num][j];
					Allocation[num][j] += Request[num][j];
					Need[num][j] -= Request[num][j];
				}
			} else {// 剩余资源不足
				System.out.println("当前没有足够的资源可分配，进程P" + num + "需等待。");
				System.out.println("......");
			}
		} else {// 请求超出给出的最大需求max
			System.out.println("进程P" + num + "请求已经超出最大需求量Need.");
			System.out.println("......");
		}

		if (T[0] && T[1]) {
			printSystemVariable();
			System.out.println("现在进入安全算法：");
			if (!SecurityAlgorithm()) {// 如果安全判定失败，返回上一步
				for (int j = 0; j < os_num; j++) {
					Available[j] = cache[0][j];
					Allocation[num][j] = cache[1][j];
					Need[num][j] = cache[2][j];
				}
			}
		} else// 请求失败，重新申请
			setRequest();
	}

	public boolean SecurityAlgorithm() {// 安全算法
		boolean[] Finish = new boolean[pro_num];// 当有足够资源分配给进程 Pi
												// 时，令finish[i]=true

		int count = 0;// 完成进程数
		int[] safe = new int[pro_num];// 安全序列

		for (int i = 0; i < pro_num; i++) {// 初始化Finish数组
			Finish[i] = false;
		}
		for (int j = 0; j < os_num; j++) {// 初始化工作向量
			Work[j] = Available[j];
		}
		System.out.println("进程\t\t" + "Work\t" + "Allocation\t\t" + "Need\t" + "Available");
		for (int i = 0; i < pro_num; i++) {
			boolean T = true;// 中间变量
			for (int j = 0; j < os_num; j++) {// 判断资源余量是否能完成进程Pi
				if (Need[i][j] > Work[j]) {
					T = false;
					j = os_num;
				}
			}
			if (Finish[i] == false && T) {
				System.out.print("P" + i + "\t\t");
				for (int j = 0; j < os_num; j++) {
					System.out.print(Work[j] + "  ");
				}
				System.out.print("\t\t|  ");
				for (int j = 0; j < os_num; j++) {
					System.out.print(Allocation[i][j] + "  ");
				}
				System.out.print("\t\t\t|  ");
				for (int j = 0; j < os_num; j++) {
					System.out.print(Need[i][j] + "  ");
				}
				System.out.print("\t\t|  ");

				for (int j = 0; j < os_num; j++) {
					Work[j] += Allocation[i][j];
				}
				for (int j = 0; j < os_num; j++) {
					System.out.print(Work[j] + "  ");
				}
				System.out.println();
				Finish[i] = true;// 当前进程能满足时
				safe[count] = i;// 保存当前进程序号
				if (i != 0)// 根据新work[]重新判断
					i = -1;
				count++;// 满足进程数加1
			}
		}
		if (count < pro_num) {// 判断完成进程数是否小于总进程数
			System.out.println("....... .......");
			System.out.println("当前系统不存在安全序列。返回预分配的各资源量");
			return false;// 安全判断失败，结束运行
		} else {
			System.out.print("此时至少存在一个安全序列：");
			for (int i = 0; i < pro_num; i++) {// 输出安全序列
				System.out.print("P" + safe[i] + "->");
			}
			System.out.println("故当前可分配！");
			return true;
		}
	}

	// 规范化输入的数据，识别形如1,2,3的直接字符串，或如违规输入P1
	// num 需要识别的数量
	public int[] Ginput(int num) {
		int[] putnum = new int[num];// 最终的输出数组
		char[] s = in.nextLine().toCharArray();// 将用户输入的字符串转换成字符数组
		int[] after = new int[s.length];// 对字符数组的临时储存
		int[] cache = { 0, 0 };// 定位到的数是连续几位；记录已经读取的数个数
		for (int i = 0; i < s.length; i++) {
			if (cache[1] >= num) {// 读取够长度后，后面的忽略
				break;
			}
			if ((int) s[i] >= 48 && (int) s[i] <= 57) {// ASCII码的个位数组48-57，判断是不是数字
				cache[0]++;
				after[i] = (int) s[i] - 48;// 临时储存这一位数
				if (i == s.length - 1) { // 假如是最后一位且为数字。直接进行合成操作
					for (int t = cache[0]; t > 0; t--) {
						putnum[cache[1]] += after[i + 1 - t] * (int) Math.pow(10, (t - 1));
					}
				}
			} else {
				for (int t = cache[0]; t > 0; t--) {// 已经不再是连续数字，将各位数合并
					putnum[cache[1]] += after[i - t] * (int) Math.pow(10, (t - 1));
				}
				if (cache[0] != 0)
					cache[1]++;// 第一次遇到非数字时已读取数+1
				cache[0] = 0; // 长度归零
			}
		}
		return putnum;// 返回提取的有效数组
	}

}