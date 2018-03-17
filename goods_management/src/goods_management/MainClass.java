package goods_management;

import java.io.IOException;
import java.util.*;


public class MainClass {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		FileProcess fp1=new FileProcess();
		ArrayList<String> inv=new ArrayList<String>();
		fp1.readTextLineByLine(inv,"Inventory.dat");
		System.out.println(inv);
		ArrayList<String> tra=new ArrayList<String>();
		fp1.readTextLineByLine(tra,"Transactions.dat");
		System.out.println(tra);
		GoodsProcess gp=new GoodsProcess(inv,tra);
		gp.process();
		//GoodsProcess.InventoryType a=new GoodsProcess.InventoryType();
		fp1.writeTextLineByLine(gp.newInventory, "NewInventory.dat");
		fp1.writeTextLineByLine(gp.errors, "Errors.dat");
	}

}

